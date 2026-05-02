package org.h2.command;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.h2.engine.GeneratedKeysMode;
import org.h2.engine.SessionRemote;
import org.h2.engine.SysProperties;
import org.h2.expression.ParameterInterface;
import org.h2.expression.ParameterRemote;
import org.h2.index.IndexSort;
import org.h2.message.DbException;
import org.h2.message.Trace;
import org.h2.result.BatchResult;
import org.h2.result.MergedResult;
import org.h2.result.ResultInterface;
import org.h2.result.ResultRemote;
import org.h2.result.ResultWithGeneratedKeys;
import org.h2.util.Utils;
import org.h2.value.Transfer;
import org.h2.value.Value;
import org.h2.value.ValueLob;
import org.h2.value.ValueNull;

/* loaded from: ijava.jar:org/h2/command/CommandRemote.class */
public class CommandRemote implements CommandInterface {
    private final ArrayList<Transfer> transferList;
    private final Trace trace;
    private final String sql;
    private final int fetchSize;
    private SessionRemote session;
    private int id;
    private boolean isQuery;
    private boolean readonly;
    private final int created;
    private int cmdType = 0;
    private final ArrayList<ParameterInterface> parameters = Utils.newSmallArrayList();

    public CommandRemote(SessionRemote sessionRemote, ArrayList<Transfer> arrayList, String str, int i) {
        this.transferList = arrayList;
        this.trace = sessionRemote.getTrace();
        this.sql = str;
        prepare(sessionRemote, true);
        this.session = sessionRemote;
        this.fetchSize = i;
        this.created = sessionRemote.getLastReconnect();
    }

    @Override // org.h2.command.CommandInterface
    public void stop(boolean z) {
    }

    private void prepare(SessionRemote sessionRemote, boolean z) {
        this.id = sessionRemote.getNextId();
        int i = 0;
        int i2 = 0;
        while (i < this.transferList.size()) {
            try {
                Transfer transfer = this.transferList.get(i);
                if (z) {
                    sessionRemote.traceOperation("SESSION_PREPARE_READ_PARAMS2", this.id);
                    transfer.writeInt(18).writeInt(this.id).writeString(this.sql);
                } else {
                    sessionRemote.traceOperation("SESSION_PREPARE", this.id);
                    transfer.writeInt(0).writeInt(this.id).writeString(this.sql);
                }
                sessionRemote.done(transfer);
                this.isQuery = transfer.readBoolean();
                this.readonly = transfer.readBoolean();
                this.cmdType = z ? transfer.readInt() : 0;
                int readInt = transfer.readInt();
                if (z) {
                    this.parameters.clear();
                    for (int i3 = 0; i3 < readInt; i3++) {
                        ParameterRemote parameterRemote = new ParameterRemote(i3);
                        parameterRemote.readMetaData(transfer);
                        this.parameters.add(parameterRemote);
                    }
                }
            } catch (IOException e) {
                int i4 = i;
                i--;
                i2++;
                sessionRemote.removeServer(e, i4, i2);
            }
            i++;
        }
    }

    @Override // org.h2.command.CommandInterface
    public boolean isQuery() {
        return this.isQuery;
    }

    @Override // org.h2.command.CommandInterface
    public ArrayList<ParameterInterface> getParameters() {
        return this.parameters;
    }

    private void prepareIfRequired() {
        if (this.session.getLastReconnect() != this.created) {
            this.id = Integer.MIN_VALUE;
        }
        this.session.checkClosed();
        if (this.id <= this.session.getCurrentId() - SysProperties.SERVER_CACHED_OBJECTS) {
            prepare(this.session, false);
        }
    }

    @Override // org.h2.command.CommandInterface
    public ResultInterface getMetaData() {
        SessionRemote sessionRemote = this.session;
        sessionRemote.lock();
        try {
            if (!this.isQuery) {
                return null;
            }
            int nextId = sessionRemote.getNextId();
            ResultRemote resultRemote = null;
            int i = 0;
            for (int i2 = 0; i2 < this.transferList.size(); i2 = (i2 - 1) + 1) {
                prepareIfRequired();
                Transfer transfer = this.transferList.get(i2);
                try {
                    sessionRemote.traceOperation("COMMAND_GET_META_DATA", this.id);
                    transfer.writeInt(10).writeInt(this.id).writeInt(nextId);
                    sessionRemote.done(transfer);
                    resultRemote = new ResultRemote(sessionRemote, transfer, nextId, transfer.readInt(), IndexSort.FULLY_SORTED);
                    break;
                } catch (IOException e) {
                    i++;
                    sessionRemote.removeServer(e, i2, i);
                }
            }
            sessionRemote.autoCommitIfCluster();
            ResultRemote resultRemote2 = resultRemote;
            sessionRemote.unlock();
            return resultRemote2;
        } finally {
            sessionRemote.unlock();
        }
    }

    @Override // org.h2.command.CommandInterface
    public ResultInterface executeQuery(long j, boolean z) {
        int i;
        checkParameters();
        SessionRemote sessionRemote = this.session;
        sessionRemote.lock();
        try {
            int nextId = sessionRemote.getNextId();
            ResultRemote resultRemote = null;
            int i2 = 0;
            int i3 = 0;
            while (i2 < this.transferList.size()) {
                prepareIfRequired();
                Transfer transfer = this.transferList.get(i2);
                try {
                    sessionRemote.traceOperation("COMMAND_EXECUTE_QUERY", this.id);
                    transfer.writeInt(2).writeInt(this.id).writeInt(nextId);
                    transfer.writeRowCount(j);
                    if (sessionRemote.isClustered() || z) {
                        i = Integer.MAX_VALUE;
                    } else {
                        i = this.fetchSize;
                    }
                    transfer.writeInt(i);
                    sendParameters(transfer);
                    sessionRemote.done(transfer);
                    int readInt = transfer.readInt();
                    if (resultRemote != null) {
                        resultRemote.close();
                    }
                    resultRemote = new ResultRemote(sessionRemote, transfer, nextId, readInt, i);
                } catch (IOException e) {
                    int i4 = i2;
                    i2--;
                    i3++;
                    sessionRemote.removeServer(e, i4, i3);
                }
                if (this.readonly) {
                    break;
                }
                i2++;
            }
            sessionRemote.autoCommitIfCluster();
            sessionRemote.readSessionState();
            ResultRemote resultRemote2 = resultRemote;
            sessionRemote.unlock();
            return resultRemote2;
        } catch (Throwable th) {
            sessionRemote.unlock();
            throw th;
        }
    }

    @Override // org.h2.command.CommandInterface
    public ResultWithGeneratedKeys executeUpdate(Object obj) {
        checkParameters();
        int valueOf = GeneratedKeysMode.valueOf(obj);
        boolean z = valueOf != 0;
        int nextId = z ? this.session.getNextId() : 0;
        SessionRemote sessionRemote = this.session;
        sessionRemote.lock();
        long j = 0;
        ResultRemote resultRemote = null;
        boolean z2 = false;
        int i = 0;
        int i2 = 0;
        while (i < this.transferList.size()) {
            try {
                prepareIfRequired();
                Transfer transfer = this.transferList.get(i);
                try {
                    sessionRemote.traceOperation("COMMAND_EXECUTE_UPDATE", this.id);
                    transfer.writeInt(3).writeInt(this.id);
                    sendParameters(transfer);
                    sendGeneratedKeysRequest(obj, valueOf, transfer);
                    sessionRemote.done(transfer);
                    j = transfer.readRowCount();
                    z2 = transfer.readBoolean();
                    if (z) {
                        int readInt = transfer.readInt();
                        if (resultRemote != null) {
                            resultRemote.close();
                        }
                        resultRemote = new ResultRemote(sessionRemote, transfer, nextId, readInt, IndexSort.FULLY_SORTED);
                    }
                } catch (IOException e) {
                    int i3 = i;
                    i--;
                    i2++;
                    sessionRemote.removeServer(e, i3, i2);
                }
                i++;
            } catch (Throwable th) {
                sessionRemote.unlock();
                throw th;
            }
        }
        sessionRemote.setAutoCommitFromServer(z2);
        sessionRemote.autoCommitIfCluster();
        sessionRemote.readSessionState();
        if (resultRemote != null) {
            ResultWithGeneratedKeys.WithKeys withKeys = new ResultWithGeneratedKeys.WithKeys(j, resultRemote);
            sessionRemote.unlock();
            return withKeys;
        }
        ResultWithGeneratedKeys of = ResultWithGeneratedKeys.of(j);
        sessionRemote.unlock();
        return of;
    }

    @Override // org.h2.command.CommandInterface
    public BatchResult executeBatchUpdate(ArrayList<Value[]> arrayList, Object obj) {
        int valueOf = GeneratedKeysMode.valueOf(obj);
        boolean z = valueOf != 0;
        int size = arrayList.size();
        int nextId = z ? this.session.getNextId() : 0;
        SessionRemote sessionRemote = this.session;
        sessionRemote.lock();
        try {
            long[] jArr = new long[size];
            MergedResult mergedResult = null;
            ArrayList arrayList2 = new ArrayList();
            boolean z2 = false;
            int i = 0;
            int i2 = 0;
            while (i < this.transferList.size()) {
                prepareIfRequired();
                Transfer transfer = this.transferList.get(i);
                MergedResult mergedResult2 = mergedResult;
                mergedResult = z ? new MergedResult() : null;
                ArrayList arrayList3 = arrayList2;
                arrayList2 = new ArrayList();
                try {
                    if (transfer.getVersion() >= 21) {
                        sessionRemote.traceOperation("COMMAND_EXECUTE_BATCH_UPDATE", this.id);
                        transfer.writeInt(20).writeInt(this.id);
                        transfer.writeInt(size);
                        Iterator<Value[]> it = arrayList.iterator();
                        while (it.hasNext()) {
                            Value[] next = it.next();
                            transfer.writeInt(next.length);
                            sendParameters(transfer, next);
                        }
                        sendGeneratedKeysRequest(obj, valueOf, transfer);
                        sessionRemote.done(transfer);
                        for (int i3 = 0; i3 < size; i3++) {
                            jArr[i3] = transfer.readRowCount();
                        }
                        if (z) {
                            ResultRemote resultRemote = new ResultRemote(sessionRemote, transfer, nextId, transfer.readInt(), IndexSort.FULLY_SORTED);
                            mergedResult.add(resultRemote);
                            resultRemote.close();
                        }
                        int readInt = transfer.readInt();
                        for (int i4 = 0; i4 < readInt; i4++) {
                            arrayList2.add(SessionRemote.readSQLException(transfer));
                        }
                        z2 = transfer.readBoolean();
                    } else {
                        for (int i5 = 0; i5 < size; i5++) {
                            sessionRemote.traceOperation("COMMAND_EXECUTE_UPDATE", this.id);
                            transfer.writeInt(3).writeInt(this.id);
                            Value[] valueArr = arrayList.get(i5);
                            transfer.writeInt(valueArr.length);
                            sendParameters(transfer, valueArr);
                            sendGeneratedKeysRequest(obj, valueOf, transfer);
                            try {
                                sessionRemote.done(transfer);
                                jArr[i5] = transfer.readRowCount();
                                z2 = transfer.readBoolean();
                                if (z) {
                                    ResultRemote resultRemote2 = new ResultRemote(sessionRemote, transfer, nextId, transfer.readInt(), IndexSort.FULLY_SORTED);
                                    mergedResult.add(resultRemote2);
                                    resultRemote2.close();
                                }
                            } catch (DbException e) {
                                jArr[i5] = -3;
                                arrayList2.add(DbException.toSQLException(e));
                            }
                        }
                    }
                } catch (IOException e2) {
                    int i6 = i;
                    i--;
                    i2++;
                    sessionRemote.removeServer(e2, i6, i2);
                    mergedResult = mergedResult2;
                    arrayList2 = arrayList3;
                }
                i++;
            }
            sessionRemote.setAutoCommitFromServer(z2);
            sessionRemote.autoCommitIfCluster();
            sessionRemote.readSessionState();
            BatchResult batchResult = new BatchResult(jArr, mergedResult != null ? mergedResult.getResult() : null, arrayList2);
            sessionRemote.unlock();
            return batchResult;
        } catch (Throwable th) {
            sessionRemote.unlock();
            throw th;
        }
    }

    private void checkParameters() {
        if (this.cmdType != 60) {
            Iterator<ParameterInterface> it = this.parameters.iterator();
            while (it.hasNext()) {
                it.next().checkSet();
            }
        }
    }

    private void sendParameters(Transfer transfer) throws IOException {
        transfer.writeInt(this.parameters.size());
        Iterator<ParameterInterface> it = this.parameters.iterator();
        while (it.hasNext()) {
            Value paramValue = it.next().getParamValue();
            if (paramValue == null && this.cmdType == 60) {
                paramValue = ValueNull.INSTANCE;
            }
            transfer.writeValue(paramValue);
        }
    }

    private void sendParameters(Transfer transfer, Value[] valueArr) throws IOException {
        for (Value value : valueArr) {
            if (value == null && this.cmdType == 60) {
                value = ValueNull.INSTANCE;
            }
            transfer.writeValue(value);
        }
    }

    private static void sendGeneratedKeysRequest(Object obj, int i, Transfer transfer) throws IOException {
        transfer.writeInt(i);
        switch (i) {
            case 2:
                int[] iArr = (int[]) obj;
                transfer.writeInt(iArr.length);
                for (int i2 : iArr) {
                    transfer.writeInt(i2);
                }
                return;
            case 3:
                String[] strArr = (String[]) obj;
                transfer.writeInt(strArr.length);
                for (String str : strArr) {
                    transfer.writeString(str);
                }
                return;
            default:
                return;
        }
    }

    @Override // org.h2.command.CommandInterface, java.lang.AutoCloseable
    public void close() {
        SessionRemote sessionRemote = this.session;
        if (sessionRemote == null || sessionRemote.isClosed()) {
            return;
        }
        sessionRemote.lock();
        try {
            sessionRemote.traceOperation("COMMAND_CLOSE", this.id);
            Iterator<Transfer> it = this.transferList.iterator();
            while (it.hasNext()) {
                try {
                    it.next().writeInt(4).writeInt(this.id);
                } catch (IOException e) {
                    this.trace.error(e, "close");
                }
            }
            this.session = null;
            try {
                Iterator<ParameterInterface> it2 = this.parameters.iterator();
                while (it2.hasNext()) {
                    Value paramValue = it2.next().getParamValue();
                    if (paramValue instanceof ValueLob) {
                        ((ValueLob) paramValue).remove();
                    }
                }
            } catch (DbException e2) {
                this.trace.error(e2, "close");
            }
            this.parameters.clear();
        } finally {
            sessionRemote.unlock();
        }
    }

    @Override // org.h2.command.CommandInterface
    public void cancel() {
        this.session.cancelStatement(this.id);
    }

    public String toString() {
        return this.sql + Trace.formatParams(getParameters());
    }

    @Override // org.h2.command.CommandInterface
    public int getCommandType() {
        return this.cmdType;
    }
}
