package org.h2.value;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import org.h2.engine.CastDataProvider;
import org.h2.engine.SysProperties;
import org.h2.index.IndexSort;
import org.h2.message.DbException;
import org.h2.store.DataHandler;
import org.h2.store.FileStore;
import org.h2.store.FileStoreOutputStream;
import org.h2.store.RangeReader;
import org.h2.util.DateTimeUtils;
import org.h2.util.IOUtils;
import org.h2.util.MathUtils;
import org.h2.util.StringUtils;
import org.h2.value.lob.LobData;
import org.h2.value.lob.LobDataDatabase;
import org.h2.value.lob.LobDataFetchOnDemand;
import org.h2.value.lob.LobDataFile;
import org.h2.value.lob.LobDataInMemory;

/* loaded from: ijava.jar:org/h2/value/ValueClob.class */
public final class ValueClob extends ValueLob {
    public static ValueClob createSmall(byte[] bArr) {
        return new ValueClob(new LobDataInMemory(bArr), bArr.length, new String(bArr, StandardCharsets.UTF_8).length());
    }

    public static ValueClob createSmall(byte[] bArr, long j) {
        return new ValueClob(new LobDataInMemory(bArr), bArr.length, j);
    }

    public static ValueClob createSmall(String str) {
        return new ValueClob(new LobDataInMemory(str.getBytes(StandardCharsets.UTF_8)), r0.length, str.length());
    }

    public static ValueClob createTempClob(Reader reader, long j, DataHandler dataHandler) {
        BufferedReader bufferedReader;
        char[] cArr;
        int readFully;
        if (j >= 0) {
            try {
                reader = new RangeReader(reader, 0L, j);
            } catch (IOException e) {
                throw DbException.convert(e);
            }
        }
        if (reader instanceof BufferedReader) {
            bufferedReader = (BufferedReader) reader;
        } else {
            bufferedReader = new BufferedReader(reader, 4096);
        }
        long j2 = Long.MAX_VALUE;
        if (j >= 0 && j < Long.MAX_VALUE) {
            j2 = j;
        }
        try {
            int bufferSize = ValueLob.getBufferSize(dataHandler, j2);
            if (bufferSize >= Integer.MAX_VALUE) {
                cArr = IOUtils.readStringAndClose(bufferedReader, -1).toCharArray();
                readFully = cArr.length;
            } else {
                cArr = new char[bufferSize];
                bufferedReader.mark(bufferSize);
                readFully = IOUtils.readFully(bufferedReader, cArr, bufferSize);
            }
            if (readFully <= dataHandler.getMaxLengthInplaceLob()) {
                return createSmall(new String(cArr, 0, readFully));
            }
            bufferedReader.reset();
            return createTemporary(dataHandler, bufferedReader, j2);
        } catch (IOException e2) {
            throw DbException.convertIOException(e2, null);
        }
    }

    private static ValueClob createTemporary(DataHandler dataHandler, Reader reader, long j) throws IOException {
        String createTempLobFileName = ValueLob.createTempLobFileName(dataHandler);
        FileStore openFile = dataHandler.openFile(createTempLobFileName, "rw", false);
        openFile.autoDelete();
        long j2 = 0;
        long j3 = 0;
        FileStoreOutputStream fileStoreOutputStream = new FileStoreOutputStream(openFile, null);
        try {
            char[] cArr = new char[4096];
            while (true) {
                int readFully = IOUtils.readFully(reader, cArr, ValueLob.getBufferSize(dataHandler, j));
                if (readFully != 0) {
                    fileStoreOutputStream.write(new String(cArr, 0, readFully).getBytes(StandardCharsets.UTF_8));
                    j2 += r0.length;
                    j3 += readFully;
                } else {
                    fileStoreOutputStream.close();
                    return new ValueClob(new LobDataFile(dataHandler, createTempLobFileName, openFile), j2, j3);
                }
            }
        } catch (Throwable th) {
            try {
                fileStoreOutputStream.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    public ValueClob(LobData lobData, long j, long j2) {
        super(lobData, j, j2);
    }

    @Override // org.h2.value.Value
    public int getValueType() {
        return 3;
    }

    @Override // org.h2.value.Value
    public String getString() {
        if (this.charLength > DateTimeUtils.NANOS_PER_SECOND) {
            throw getStringTooLong(this.charLength);
        }
        if (this.lobData instanceof LobDataInMemory) {
            return new String(((LobDataInMemory) this.lobData).getSmall(), StandardCharsets.UTF_8);
        }
        return readString((int) this.charLength);
    }

    @Override // org.h2.value.ValueLob
    byte[] getBytesInternal() {
        long j = this.octetLength;
        if (j >= 0) {
            if (j > DateTimeUtils.NANOS_PER_SECOND) {
                throw getBinaryTooLong(j);
            }
            return readBytes((int) j);
        }
        if (this.octetLength > DateTimeUtils.NANOS_PER_SECOND) {
            throw getBinaryTooLong(octetLength());
        }
        byte[] readBytes = readBytes(IndexSort.FULLY_SORTED);
        long length = readBytes.length;
        this.octetLength = length;
        if (length > DateTimeUtils.NANOS_PER_SECOND) {
            throw getBinaryTooLong(length);
        }
        return readBytes;
    }

    @Override // org.h2.value.Value
    public InputStream getInputStream() {
        return this.lobData.getInputStream(-1L);
    }

    @Override // org.h2.value.Value
    public InputStream getInputStream(long j, long j2) {
        return rangeInputStream(this.lobData.getInputStream(-1L), j, j2, -1L);
    }

    @Override // org.h2.value.Value
    public Reader getReader(long j, long j2) {
        return rangeReader(getReader(), j, j2, this.charLength);
    }

    @Override // org.h2.value.Value
    public int compareTypeSafe(Value value, CompareMode compareMode, CastDataProvider castDataProvider) {
        if (value == this) {
            return 0;
        }
        ValueClob valueClob = (ValueClob) value;
        LobData lobData = this.lobData;
        LobData lobData2 = valueClob.lobData;
        if (lobData.getClass() == lobData2.getClass()) {
            if (lobData instanceof LobDataInMemory) {
                return Integer.signum(getString().compareTo(valueClob.getString()));
            }
            if (lobData instanceof LobDataDatabase) {
                if (((LobDataDatabase) lobData).getLobId() == ((LobDataDatabase) lobData2).getLobId()) {
                    return 0;
                }
            } else if ((lobData instanceof LobDataFetchOnDemand) && ((LobDataFetchOnDemand) lobData).getLobId() == ((LobDataFetchOnDemand) lobData2).getLobId()) {
                return 0;
            }
        }
        return compare(this, valueClob);
    }

    private static int compare(ValueClob valueClob, ValueClob valueClob2) {
        int read;
        int read2;
        try {
            Reader reader = valueClob.getReader();
            try {
                Reader reader2 = valueClob2.getReader();
                try {
                    char[] cArr = new char[512];
                    char[] cArr2 = new char[512];
                    for (long min = Math.min(valueClob.charLength, valueClob2.charLength); min >= 512; min -= 512) {
                        if (IOUtils.readFully(reader, cArr, 512) != 512 || IOUtils.readFully(reader2, cArr2, 512) != 512) {
                            throw DbException.getUnsupportedException("Invalid LOB");
                        }
                        int signum = Integer.signum(Arrays.compare(cArr, cArr2));
                        if (signum != 0) {
                            if (reader2 != null) {
                                reader2.close();
                            }
                            if (reader != null) {
                                reader.close();
                            }
                            return signum;
                        }
                    }
                    do {
                        read = reader.read();
                        read2 = reader2.read();
                        if (read < 0) {
                            int i = read2 < 0 ? 0 : -1;
                            if (reader2 != null) {
                                reader2.close();
                            }
                            if (reader != null) {
                                reader.close();
                            }
                            return i;
                        }
                        if (read2 < 0) {
                            if (reader2 != null) {
                                reader2.close();
                            }
                            if (reader != null) {
                                reader.close();
                            }
                            return 1;
                        }
                    } while (read == read2);
                    int i2 = read < read2 ? -1 : 1;
                    if (reader2 != null) {
                        reader2.close();
                    }
                    if (reader != null) {
                        reader.close();
                    }
                    return i2;
                } catch (Throwable th) {
                    if (reader2 != null) {
                        try {
                            reader2.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (Throwable th4) {
                        th3.addSuppressed(th4);
                    }
                }
                throw th3;
            }
        } catch (IOException e) {
            throw DbException.convert(e);
        }
    }

    @Override // org.h2.util.HasSQL
    public StringBuilder getSQL(StringBuilder sb, int i) {
        if ((i & 2) != 0 && (!(this.lobData instanceof LobDataInMemory) || this.charLength > SysProperties.MAX_TRACE_DATA_LENGTH)) {
            sb.append("SPACE(").append(this.charLength);
            formatLobDataComment(sb);
        } else if ((i & 6) == 0) {
            StringUtils.quoteStringSQL(sb.append("CAST("), getString()).append(" AS CHARACTER LARGE OBJECT(").append(this.charLength).append("))");
        } else {
            StringUtils.quoteStringSQL(sb, getString());
        }
        return sb;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ValueClob convertPrecision(long j) {
        ValueClob createSmall;
        if (this.charLength <= j) {
            return this;
        }
        DataHandler dataHandler = this.lobData.getDataHandler();
        if (dataHandler != null) {
            createSmall = createTempClob(getReader(), j, dataHandler);
        } else {
            try {
                createSmall = createSmall(IOUtils.readStringAndClose(getReader(), MathUtils.convertLongToInt(j)));
            } catch (IOException e) {
                throw DbException.convertIOException(e, null);
            }
        }
        return createSmall;
    }

    @Override // org.h2.value.ValueLob
    public ValueLob copy(DataHandler dataHandler, int i) {
        if (this.lobData instanceof LobDataInMemory) {
            if (((LobDataInMemory) this.lobData).getSmall().length > dataHandler.getMaxLengthInplaceLob()) {
                ValueClob createClob = dataHandler.getLobStorage().createClob(getReader(), this.charLength);
                ValueLob copy = createClob.copy(dataHandler, i);
                createClob.remove();
                return copy;
            }
            return this;
        }
        if (this.lobData instanceof LobDataDatabase) {
            return dataHandler.getLobStorage().copyLob(this, i);
        }
        throw new UnsupportedOperationException();
    }

    @Override // org.h2.value.Value
    public long charLength() {
        return this.charLength;
    }

    @Override // org.h2.value.Value
    public long octetLength() {
        long j = this.octetLength;
        if (j < 0) {
            if (this.lobData instanceof LobDataInMemory) {
                j = ((LobDataInMemory) this.lobData).getSmall().length;
            } else {
                try {
                    InputStream inputStream = getInputStream();
                    long j2 = 0;
                    while (true) {
                        try {
                            j = j2 + inputStream.skip(Long.MAX_VALUE);
                            if (inputStream.read() < 0) {
                                break;
                            }
                            j2 = j + 1;
                        } finally {
                        }
                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (IOException e) {
                    throw DbException.convertIOException(e, null);
                }
            }
            this.octetLength = j;
        }
        return j;
    }
}
