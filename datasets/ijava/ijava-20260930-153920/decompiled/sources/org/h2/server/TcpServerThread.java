package org.h2.server;

import java.io.ByteArrayInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import org.h2.api.ErrorCode;
import org.h2.command.Command;
import org.h2.engine.ConnectionInfo;
import org.h2.engine.Constants;
import org.h2.engine.Engine;
import org.h2.engine.Session;
import org.h2.engine.SessionLocal;
import org.h2.engine.SysProperties;
import org.h2.expression.Parameter;
import org.h2.expression.ParameterInterface;
import org.h2.expression.ParameterRemote;
import org.h2.jdbc.JdbcException;
import org.h2.jdbc.meta.DatabaseMetaServer;
import org.h2.message.DbException;
import org.h2.result.BatchResult;
import org.h2.result.ResultColumn;
import org.h2.result.ResultInterface;
import org.h2.result.ResultWithGeneratedKeys;
import org.h2.util.IOUtils;
import org.h2.util.NetUtils;
import org.h2.util.NetworkConnectionInfo;
import org.h2.util.SmallLRUCache;
import org.h2.util.SmallMap;
import org.h2.util.TimeZoneProvider;
import org.h2.value.Transfer;
import org.h2.value.Value;
import org.h2.value.ValueLob;

/* loaded from: ijava.jar:org/h2/server/TcpServerThread.class */
public class TcpServerThread implements Runnable {
    protected final Transfer transfer;
    private final TcpServer server;
    private SessionLocal session;
    private boolean stop;
    private Thread thread;
    private Command commit;
    private final SmallMap cache = new SmallMap(SysProperties.SERVER_CACHED_OBJECTS);
    private final SmallLRUCache<Long, CachedInputStream> lobs = SmallLRUCache.newInstance(Math.max(SysProperties.SERVER_CACHED_OBJECTS, SysProperties.SERVER_RESULT_SET_FETCH_SIZE * 5));
    private final int threadId;
    private int clientVersion;
    private String sessionId;
    private long lastRemoteSettingsId;

    /* JADX INFO: Access modifiers changed from: package-private */
    public TcpServerThread(Socket socket, TcpServer tcpServer, int i) {
        this.server = tcpServer;
        this.threadId = i;
        this.transfer = new Transfer(null, socket);
    }

    private void trace(String str) {
        this.server.trace(this + " " + str);
    }

    @Override // java.lang.Runnable
    public void run() {
        Socket socket;
        try {
            try {
                this.transfer.init();
                trace("Connect");
                try {
                    socket = this.transfer.getSocket();
                } catch (OutOfMemoryError e) {
                    this.server.traceError(e);
                    sendError(e, true);
                    this.stop = true;
                } catch (Throwable th) {
                    sendError(th, true);
                    this.stop = true;
                }
                if (socket == null) {
                    close();
                    return;
                }
                if (!this.server.allow(this.transfer.getSocket())) {
                    throw DbException.get(ErrorCode.REMOTE_CONNECTION_NOT_ALLOWED);
                }
                int readInt = this.transfer.readInt();
                if (readInt < 6) {
                    throw DbException.get(ErrorCode.DRIVER_VERSION_ERROR_2, Integer.toString(readInt), "17");
                }
                int readInt2 = this.transfer.readInt();
                if (readInt2 < 17) {
                    throw DbException.get(ErrorCode.DRIVER_VERSION_ERROR_2, Integer.toString(readInt2), "17");
                }
                if (readInt > 21) {
                    throw DbException.get(ErrorCode.DRIVER_VERSION_ERROR_2, Integer.toString(readInt), "21");
                }
                if (readInt2 >= 21) {
                    this.clientVersion = 21;
                } else {
                    this.clientVersion = readInt2;
                }
                this.transfer.setVersion(this.clientVersion);
                String readString = this.transfer.readString();
                String readString2 = this.transfer.readString();
                if (readString == null && readString2 == null) {
                    String readString3 = this.transfer.readString();
                    int readInt3 = this.transfer.readInt();
                    this.stop = true;
                    if (readInt3 == 13) {
                        this.server.cancelStatement(readString3, this.transfer.readInt());
                    } else if (readInt3 == 14) {
                        readString = this.server.checkKeyAndGetDatabaseName(readString3);
                        if (readString3.equals(readString)) {
                            this.transfer.writeInt(0);
                        } else {
                            this.transfer.writeInt(1);
                        }
                    }
                }
                String baseDir = this.server.getBaseDir();
                if (baseDir == null) {
                    baseDir = SysProperties.getBaseDir();
                }
                ConnectionInfo connectionInfo = new ConnectionInfo(this.server.checkKeyAndGetDatabaseName(readString));
                connectionInfo.setOriginalURL(readString2);
                connectionInfo.setUserName(this.transfer.readString());
                connectionInfo.setUserPasswordHash(this.transfer.readBytes());
                connectionInfo.setFilePasswordHash(this.transfer.readBytes());
                int readInt4 = this.transfer.readInt();
                for (int i = 0; i < readInt4; i++) {
                    connectionInfo.setProperty(this.transfer.readString(), this.transfer.readString());
                }
                if (baseDir != null) {
                    connectionInfo.setBaseDir(baseDir);
                }
                if (this.server.getIfExists()) {
                    connectionInfo.setProperty("FORBID_CREATION", Constants.CLUSTERING_ENABLED);
                }
                this.transfer.writeInt(1);
                this.transfer.writeInt(this.clientVersion);
                this.transfer.flush();
                if (connectionInfo.getFilePasswordHash() != null) {
                    connectionInfo.setFileEncryptionKey(this.transfer.readBytes());
                }
                connectionInfo.setNetworkConnectionInfo(new NetworkConnectionInfo(NetUtils.ipToShortForm(new StringBuilder(this.server.getSSL() ? "ssl://" : "tcp://"), socket.getLocalAddress().getAddress(), true).append(':').append(socket.getLocalPort()).toString(), socket.getInetAddress().getAddress(), socket.getPort(), new StringBuilder().append('P').append(this.clientVersion).toString()));
                if (this.clientVersion < 20) {
                    connectionInfo.setProperty("OLD_INFORMATION_SCHEMA", Constants.CLUSTERING_ENABLED);
                    connectionInfo.setProperty("NON_KEYWORDS", "VALUE");
                }
                this.session = Engine.createSession(connectionInfo);
                this.transfer.setSession(this.session);
                this.server.addConnection(this.threadId, readString2, connectionInfo.getUserName());
                trace("Connected");
                this.lastRemoteSettingsId = this.session.getDatabase().getRemoteSettingsId();
                while (!this.stop) {
                    try {
                        process();
                    } catch (Throwable th2) {
                        sendError(th2, true);
                    }
                }
                trace("Disconnect");
                close();
            } catch (Throwable th3) {
                this.server.traceError(th3);
                close();
            }
        } catch (Throwable th4) {
            close();
            throw th4;
        }
    }

    private void closeSession() {
        if (this.session != null) {
            RuntimeException runtimeException = null;
            try {
                this.session.close();
                this.server.removeConnection(this.threadId);
            } catch (RuntimeException e) {
                runtimeException = e;
                this.server.traceError(e);
            } catch (Exception e2) {
                this.server.traceError(e2);
            } finally {
                this.session = null;
            }
            if (runtimeException != null) {
                throw runtimeException;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void close() {
        try {
            this.stop = true;
            closeSession();
        } catch (Exception e) {
            this.server.traceError(e);
        } finally {
            this.transfer.close();
            trace("Close");
            this.server.remove(this);
        }
    }

    private void sendError(Throwable th, boolean z) {
        if (z) {
            try {
                this.transfer.writeInt(0);
            } catch (Exception e) {
                if (!this.transfer.isClosed()) {
                    this.server.traceError(e);
                }
                this.stop = true;
                return;
            }
        }
        sendSQLException(DbException.convert(th).getSQLException());
        this.transfer.flush();
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void sendSQLException(SQLException sQLException) throws IOException {
        String message;
        String str;
        StringWriter stringWriter = new StringWriter();
        sQLException.printStackTrace(new PrintWriter(stringWriter));
        String stringWriter2 = stringWriter.toString();
        if (sQLException instanceof JdbcException) {
            JdbcException jdbcException = (JdbcException) sQLException;
            message = jdbcException.getOriginalMessage();
            str = jdbcException.getSQL();
        } else {
            message = sQLException.getMessage();
            str = null;
        }
        this.transfer.writeString(sQLException.getSQLState()).writeString(message).writeString(str).writeInt(sQLException.getErrorCode()).writeString(stringWriter2);
    }

    private void setParameters(Command command) throws IOException {
        int readInt = this.transfer.readInt();
        ArrayList<? extends ParameterInterface> parameters = command.getParameters();
        for (int i = 0; i < readInt; i++) {
            ((Parameter) parameters.get(i)).setValue(this.transfer.readValue(null));
        }
    }

    private void process() throws IOException {
        int state;
        int state2;
        SessionLocal sessionLocal = this.session;
        int readInt = this.transfer.readInt();
        switch (readInt) {
            case 0:
            case 18:
                int readInt2 = this.transfer.readInt();
                String readString = this.transfer.readString();
                int modificationId = sessionLocal.getModificationId();
                Command prepareLocal = sessionLocal.prepareLocal(readString);
                boolean isReadOnly = prepareLocal.isReadOnly();
                this.cache.addObject(readInt2, prepareLocal);
                this.transfer.writeInt(getState(modificationId)).writeBoolean(prepareLocal.isQuery()).writeBoolean(isReadOnly);
                if (readInt != 0) {
                    this.transfer.writeInt(prepareLocal.getCommandType());
                }
                ArrayList<? extends ParameterInterface> parameters = prepareLocal.getParameters();
                this.transfer.writeInt(parameters.size());
                if (readInt != 0) {
                    Iterator<? extends ParameterInterface> it = parameters.iterator();
                    while (it.hasNext()) {
                        ParameterRemote.writeMetaData(this.transfer, it.next());
                    }
                }
                this.transfer.flush();
                return;
            case 1:
                this.stop = true;
                closeSession();
                this.transfer.writeInt(1).flush();
                close();
                return;
            case 2:
                int readInt3 = this.transfer.readInt();
                int readInt4 = this.transfer.readInt();
                long readRowCount = this.transfer.readRowCount();
                int readInt5 = this.transfer.readInt();
                Command command = (Command) this.cache.getObject(readInt3, false);
                setParameters(command);
                int modificationId2 = sessionLocal.getModificationId();
                sessionLocal.lock();
                try {
                    ResultInterface executeQuery = command.executeQuery(readRowCount, false);
                    sessionLocal.unlock();
                    this.cache.addObject(readInt4, executeQuery);
                    int visibleColumnCount = executeQuery.getVisibleColumnCount();
                    this.transfer.writeInt(getState(modificationId2)).writeInt(visibleColumnCount);
                    long rowCount = executeQuery.isLazy() ? -1L : executeQuery.getRowCount();
                    this.transfer.writeRowCount(rowCount);
                    for (int i = 0; i < visibleColumnCount; i++) {
                        ResultColumn.writeColumn(this.transfer, executeQuery, i);
                    }
                    sendRows(executeQuery, rowCount >= 0 ? Math.min(rowCount, readInt5) : readInt5);
                    this.transfer.flush();
                    return;
                } finally {
                }
            case 3:
                Command command2 = (Command) this.cache.getObject(this.transfer.readInt(), false);
                setParameters(command2);
                Object readGeneratedKeysRequest = readGeneratedKeysRequest();
                int modificationId3 = sessionLocal.getModificationId();
                sessionLocal.lock();
                try {
                    ResultWithGeneratedKeys executeUpdate = command2.executeUpdate(readGeneratedKeysRequest);
                    sessionLocal.unlock();
                    if (sessionLocal.isClosed()) {
                        state2 = 2;
                        this.stop = true;
                    } else {
                        state2 = getState(modificationId3);
                    }
                    this.transfer.writeInt(state2);
                    this.transfer.writeRowCount(executeUpdate.getUpdateCount());
                    this.transfer.writeBoolean(sessionLocal.getAutoCommit());
                    if (readGeneratedKeysRequest != Boolean.FALSE) {
                        sendGeneratedKeys(executeUpdate.getGeneratedKeys());
                    }
                    this.transfer.flush();
                    return;
                } finally {
                }
            case 4:
                int readInt6 = this.transfer.readInt();
                Command command3 = (Command) this.cache.getObject(readInt6, true);
                if (command3 != null) {
                    command3.close();
                    this.cache.freeObject(readInt6);
                    return;
                }
                return;
            case 5:
                int readInt7 = this.transfer.readInt();
                int readInt8 = this.transfer.readInt();
                ResultInterface resultInterface = (ResultInterface) this.cache.getObject(readInt7, false);
                this.transfer.writeInt(1);
                sendRows(resultInterface, readInt8);
                this.transfer.flush();
                return;
            case 6:
                ((ResultInterface) this.cache.getObject(this.transfer.readInt(), false)).reset();
                return;
            case 7:
                int readInt9 = this.transfer.readInt();
                ResultInterface resultInterface2 = (ResultInterface) this.cache.getObject(readInt9, true);
                if (resultInterface2 != null) {
                    resultInterface2.close();
                    this.cache.freeObject(readInt9);
                    return;
                }
                return;
            case 8:
                if (this.commit == null) {
                    this.commit = sessionLocal.prepareLocal("COMMIT");
                }
                int modificationId4 = sessionLocal.getModificationId();
                this.commit.executeUpdate(null);
                this.transfer.writeInt(getState(modificationId4)).flush();
                return;
            case 9:
                int readInt10 = this.transfer.readInt();
                int readInt11 = this.transfer.readInt();
                Object object = this.cache.getObject(readInt10, false);
                this.cache.freeObject(readInt10);
                this.cache.addObject(readInt11, object);
                return;
            case 10:
                int readInt12 = this.transfer.readInt();
                int readInt13 = this.transfer.readInt();
                ResultInterface metaData = ((Command) this.cache.getObject(readInt12, false)).getMetaData();
                this.cache.addObject(readInt13, metaData);
                int visibleColumnCount2 = metaData.getVisibleColumnCount();
                this.transfer.writeInt(1).writeInt(visibleColumnCount2).writeRowCount(0L);
                for (int i2 = 0; i2 < visibleColumnCount2; i2++) {
                    ResultColumn.writeColumn(this.transfer, metaData, i2);
                }
                this.transfer.flush();
                return;
            case 11:
            case 13:
            case 14:
            default:
                trace("Unknown operation: " + readInt);
                close();
                return;
            case 12:
                this.sessionId = this.transfer.readString();
                if (this.clientVersion >= 20) {
                    sessionLocal.setTimeZone(TimeZoneProvider.ofId(this.transfer.readString()));
                }
                this.transfer.writeInt(1).writeBoolean(sessionLocal.getAutoCommit()).flush();
                return;
            case 15:
                sessionLocal.setAutoCommit(this.transfer.readBoolean());
                this.transfer.writeInt(1).flush();
                return;
            case 16:
                this.transfer.writeInt(1).writeInt(sessionLocal.hasPendingTransaction() ? 1 : 0).flush();
                return;
            case 17:
                long readLong = this.transfer.readLong();
                byte[] readBytes = this.transfer.readBytes();
                long readLong2 = this.transfer.readLong();
                int readInt14 = this.transfer.readInt();
                this.transfer.verifyLobMac(readBytes, readLong);
                CachedInputStream cachedInputStream = this.lobs.get(Long.valueOf(readLong));
                if (cachedInputStream == null || cachedInputStream.getPos() != readLong2) {
                    InputStream inputStream = sessionLocal.getDataHandler().getLobStorage().getInputStream(readLong, -1L);
                    cachedInputStream = new CachedInputStream(inputStream);
                    this.lobs.put(Long.valueOf(readLong), cachedInputStream);
                    inputStream.skip(readLong2);
                }
                int min = Math.min(Constants.MAX_ARRAY_CARDINALITY, readInt14);
                byte[] bArr = new byte[min];
                int readFully = IOUtils.readFully(cachedInputStream, bArr, min);
                this.transfer.writeInt(1);
                this.transfer.writeInt(readFully);
                this.transfer.writeBytes(bArr, 0, readFully);
                this.transfer.flush();
                return;
            case 19:
                int readInt15 = this.transfer.readInt();
                int readInt16 = this.transfer.readInt();
                Value[] valueArr = new Value[readInt16];
                for (int i3 = 0; i3 < readInt16; i3++) {
                    valueArr[i3] = this.transfer.readValue(null);
                }
                int modificationId5 = sessionLocal.getModificationId();
                sessionLocal.lock();
                try {
                    ResultInterface process = DatabaseMetaServer.process(sessionLocal, readInt15, valueArr);
                    sessionLocal.unlock();
                    int visibleColumnCount3 = process.getVisibleColumnCount();
                    this.transfer.writeInt(getState(modificationId5)).writeInt(visibleColumnCount3);
                    long rowCount2 = process.getRowCount();
                    this.transfer.writeRowCount(rowCount2);
                    for (int i4 = 0; i4 < visibleColumnCount3; i4++) {
                        ResultColumn.writeColumn(this.transfer, process, i4);
                    }
                    sendRows(process, rowCount2);
                    this.transfer.flush();
                    return;
                } finally {
                    sessionLocal.unlock();
                }
            case 20:
                Command command4 = (Command) this.cache.getObject(this.transfer.readInt(), false);
                int readInt17 = this.transfer.readInt();
                ArrayList<Value[]> arrayList = new ArrayList<>(readInt17);
                for (int i5 = 0; i5 < readInt17; i5++) {
                    int readInt18 = this.transfer.readInt();
                    Value[] valueArr2 = new Value[readInt18];
                    for (int i6 = 0; i6 < readInt18; i6++) {
                        valueArr2[i6] = this.transfer.readValue(null);
                    }
                    arrayList.add(valueArr2);
                }
                Object readGeneratedKeysRequest2 = readGeneratedKeysRequest();
                int modificationId6 = sessionLocal.getModificationId();
                sessionLocal.lock();
                try {
                    BatchResult executeBatchUpdate = command4.executeBatchUpdate(arrayList, readGeneratedKeysRequest2);
                    sessionLocal.unlock();
                    if (sessionLocal.isClosed()) {
                        state = 2;
                        this.stop = true;
                    } else {
                        state = getState(modificationId6);
                    }
                    this.transfer.writeInt(state);
                    for (long j : executeBatchUpdate.getUpdateCounts()) {
                        this.transfer.writeLong(j);
                    }
                    if (readGeneratedKeysRequest2 != Boolean.FALSE) {
                        sendGeneratedKeys(executeBatchUpdate.getGeneratedKeys());
                    }
                    List<SQLException> exceptions = executeBatchUpdate.getExceptions();
                    this.transfer.writeInt(exceptions.size());
                    Iterator<SQLException> it2 = exceptions.iterator();
                    while (it2.hasNext()) {
                        sendSQLException(it2.next());
                    }
                    this.transfer.writeBoolean(sessionLocal.getAutoCommit());
                    this.transfer.flush();
                    return;
                } finally {
                    sessionLocal.unlock();
                }
        }
    }

    private Object readGeneratedKeysRequest() throws IOException {
        int readInt = this.transfer.readInt();
        switch (readInt) {
            case 0:
                return Boolean.FALSE;
            case 1:
                return Boolean.TRUE;
            case 2:
                int readInt2 = this.transfer.readInt();
                int[] iArr = new int[readInt2];
                for (int i = 0; i < readInt2; i++) {
                    iArr[i] = this.transfer.readInt();
                }
                return iArr;
            case 3:
                int readInt3 = this.transfer.readInt();
                String[] strArr = new String[readInt3];
                for (int i2 = 0; i2 < readInt3; i2++) {
                    strArr[i2] = this.transfer.readString();
                }
                return strArr;
            default:
                throw DbException.get(ErrorCode.CONNECTION_BROKEN_1, "Unsupported generated keys' mode " + readInt);
        }
    }

    private void sendGeneratedKeys(ResultInterface resultInterface) throws IOException {
        int visibleColumnCount = resultInterface.getVisibleColumnCount();
        this.transfer.writeInt(visibleColumnCount);
        long rowCount = resultInterface.getRowCount();
        this.transfer.writeRowCount(rowCount);
        for (int i = 0; i < visibleColumnCount; i++) {
            ResultColumn.writeColumn(this.transfer, resultInterface, i);
        }
        sendRows(resultInterface, rowCount);
        resultInterface.close();
    }

    private int getState(int i) {
        if (this.session == null) {
            return 2;
        }
        if (this.session.getModificationId() == i) {
            long remoteSettingsId = this.session.getDatabase().getRemoteSettingsId();
            if (this.lastRemoteSettingsId == remoteSettingsId) {
                return 1;
            }
            this.lastRemoteSettingsId = remoteSettingsId;
            return 3;
        }
        return 3;
    }

    private void sendRows(ResultInterface resultInterface, long j) throws IOException {
        ValueLob copyToResult;
        int visibleColumnCount = resultInterface.getVisibleColumnCount();
        boolean isLazy = resultInterface.isLazy();
        Session threadLocalSession = isLazy ? this.session.setThreadLocalSession() : null;
        while (true) {
            try {
                long j2 = j;
                j = j2 - 1;
                if (j2 <= 0) {
                    break;
                }
                try {
                    if (resultInterface.next()) {
                        this.transfer.writeByte((byte) 1);
                        Value[] currentRow = resultInterface.currentRow();
                        for (int i = 0; i < visibleColumnCount; i++) {
                            Value value = currentRow[i];
                            if (isLazy && (value instanceof ValueLob) && (copyToResult = ((ValueLob) value).copyToResult()) != value) {
                                value = this.session.addTemporaryLob(copyToResult);
                            }
                            this.transfer.writeValue(value);
                        }
                    } else {
                        this.transfer.writeByte((byte) 0);
                        break;
                    }
                } catch (Exception e) {
                    this.transfer.writeByte((byte) -1);
                    sendError(e, false);
                }
            } finally {
                if (isLazy) {
                    this.session.resetThreadLocalSession(threadLocalSession);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setThread(Thread thread) {
        this.thread = thread;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Thread getThread() {
        return this.thread;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void cancelStatement(String str, int i) {
        if (Objects.equals(str, this.sessionId)) {
            ((Command) this.cache.getObject(i, false)).cancel();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: ijava.jar:org/h2/server/TcpServerThread$CachedInputStream.class */
    public static class CachedInputStream extends FilterInputStream {
        private static final ByteArrayInputStream DUMMY = new ByteArrayInputStream(new byte[0]);
        private long pos;

        CachedInputStream(InputStream inputStream) {
            super(inputStream == null ? DUMMY : inputStream);
            if (inputStream == null) {
                this.pos = -1L;
            }
        }

        @Override // java.io.FilterInputStream, java.io.InputStream
        public int read(byte[] bArr, int i, int i2) throws IOException {
            int read = super.read(bArr, i, i2);
            if (read > 0) {
                this.pos += read;
            }
            return read;
        }

        @Override // java.io.FilterInputStream, java.io.InputStream
        public int read() throws IOException {
            int read = this.in.read();
            if (read >= 0) {
                this.pos++;
            }
            return read;
        }

        @Override // java.io.FilterInputStream, java.io.InputStream
        public long skip(long j) throws IOException {
            long skip = super.skip(j);
            if (skip > 0) {
                this.pos += skip;
            }
            return skip;
        }

        public long getPos() {
            return this.pos;
        }
    }
}
