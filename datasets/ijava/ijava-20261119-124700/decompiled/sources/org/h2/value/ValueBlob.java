package org.h2.value;

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
import org.h2.util.DateTimeUtils;
import org.h2.util.IOUtils;
import org.h2.util.MathUtils;
import org.h2.util.StringUtils;
import org.h2.util.Utils;
import org.h2.value.lob.LobData;
import org.h2.value.lob.LobDataDatabase;
import org.h2.value.lob.LobDataFetchOnDemand;
import org.h2.value.lob.LobDataFile;
import org.h2.value.lob.LobDataInMemory;

/* loaded from: ijava.jar:org/h2/value/ValueBlob.class */
public final class ValueBlob extends ValueLob {
    public static ValueBlob createSmall(byte[] bArr) {
        return new ValueBlob(new LobDataInMemory(bArr), bArr.length);
    }

    public static ValueBlob createTempBlob(InputStream inputStream, long j, DataHandler dataHandler) {
        byte[] newBytes;
        int readFully;
        long j2 = Long.MAX_VALUE;
        if (j >= 0 && j < Long.MAX_VALUE) {
            j2 = j;
        }
        try {
            int bufferSize = ValueLob.getBufferSize(dataHandler, j2);
            if (bufferSize >= Integer.MAX_VALUE) {
                newBytes = IOUtils.readBytesAndClose(inputStream, -1);
                readFully = newBytes.length;
            } else {
                newBytes = Utils.newBytes(bufferSize);
                readFully = IOUtils.readFully(inputStream, newBytes, bufferSize);
            }
            if (readFully <= dataHandler.getMaxLengthInplaceLob()) {
                return createSmall(Utils.copyBytes(newBytes, readFully));
            }
            return createTemporary(dataHandler, newBytes, readFully, inputStream, j2);
        } catch (IOException e) {
            throw DbException.convertIOException(e, null);
        }
    }

    private static ValueBlob createTemporary(DataHandler dataHandler, byte[] bArr, int i, InputStream inputStream, long j) throws IOException {
        String createTempLobFileName = ValueLob.createTempLobFileName(dataHandler);
        FileStore openFile = dataHandler.openFile(createTempLobFileName, "rw", false);
        openFile.autoDelete();
        long j2 = 0;
        FileStoreOutputStream fileStoreOutputStream = new FileStoreOutputStream(openFile, null);
        do {
            try {
                j2 += i;
                fileStoreOutputStream.write(bArr, 0, i);
                j -= i;
                if (j <= 0) {
                    break;
                }
                i = IOUtils.readFully(inputStream, bArr, ValueLob.getBufferSize(dataHandler, j));
            } catch (Throwable th) {
                try {
                    fileStoreOutputStream.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        } while (i > 0);
        fileStoreOutputStream.close();
        return new ValueBlob(new LobDataFile(dataHandler, createTempLobFileName, openFile), j2);
    }

    public ValueBlob(LobData lobData, long j) {
        super(lobData, j, -1L);
    }

    @Override // org.h2.value.Value
    public int getValueType() {
        return 7;
    }

    @Override // org.h2.value.Value
    public String getString() {
        String readString;
        long j = this.charLength;
        if (j >= 0) {
            if (j > DateTimeUtils.NANOS_PER_SECOND) {
                throw getStringTooLong(j);
            }
            return readString((int) j);
        }
        if (this.octetLength > 3000000000L) {
            throw getStringTooLong(charLength());
        }
        if (this.lobData instanceof LobDataInMemory) {
            readString = new String(((LobDataInMemory) this.lobData).getSmall(), StandardCharsets.UTF_8);
        } else {
            readString = readString(IndexSort.FULLY_SORTED);
        }
        long length = readString.length();
        this.charLength = length;
        if (length > DateTimeUtils.NANOS_PER_SECOND) {
            throw getStringTooLong(length);
        }
        return readString;
    }

    @Override // org.h2.value.ValueLob
    byte[] getBytesInternal() {
        if (this.octetLength > DateTimeUtils.NANOS_PER_SECOND) {
            throw getBinaryTooLong(this.octetLength);
        }
        return readBytes((int) this.octetLength);
    }

    @Override // org.h2.value.Value
    public InputStream getInputStream() {
        return this.lobData.getInputStream(this.octetLength);
    }

    @Override // org.h2.value.Value
    public InputStream getInputStream(long j, long j2) {
        long j3 = this.octetLength;
        return rangeInputStream(this.lobData.getInputStream(j3), j, j2, j3);
    }

    @Override // org.h2.value.Value
    public Reader getReader(long j, long j2) {
        return rangeReader(getReader(), j, j2, -1L);
    }

    @Override // org.h2.value.Value
    public int compareTypeSafe(Value value, CompareMode compareMode, CastDataProvider castDataProvider) {
        if (value == this) {
            return 0;
        }
        ValueBlob valueBlob = (ValueBlob) value;
        LobData lobData = this.lobData;
        LobData lobData2 = valueBlob.lobData;
        if (lobData.getClass() == lobData2.getClass()) {
            if (lobData instanceof LobDataInMemory) {
                return Integer.signum(Arrays.compareUnsigned(((LobDataInMemory) lobData).getSmall(), ((LobDataInMemory) lobData2).getSmall()));
            }
            if (lobData instanceof LobDataDatabase) {
                if (((LobDataDatabase) lobData).getLobId() == ((LobDataDatabase) lobData2).getLobId()) {
                    return 0;
                }
            } else if ((lobData instanceof LobDataFetchOnDemand) && ((LobDataFetchOnDemand) lobData).getLobId() == ((LobDataFetchOnDemand) lobData2).getLobId()) {
                return 0;
            }
        }
        return compare(this, valueBlob);
    }

    private static int compare(ValueBlob valueBlob, ValueBlob valueBlob2) {
        int read;
        int read2;
        try {
            InputStream inputStream = valueBlob.getInputStream();
            try {
                InputStream inputStream2 = valueBlob2.getInputStream();
                try {
                    byte[] bArr = new byte[512];
                    byte[] bArr2 = new byte[512];
                    for (long min = Math.min(valueBlob.octetLength, valueBlob2.octetLength); min >= 512; min -= 512) {
                        if (IOUtils.readFully(inputStream, bArr, 512) != 512 || IOUtils.readFully(inputStream2, bArr2, 512) != 512) {
                            throw DbException.getUnsupportedException("Invalid LOB");
                        }
                        int signum = Integer.signum(Arrays.compareUnsigned(bArr, bArr2));
                        if (signum != 0) {
                            if (inputStream2 != null) {
                                inputStream2.close();
                            }
                            if (inputStream != null) {
                                inputStream.close();
                            }
                            return signum;
                        }
                    }
                    do {
                        read = inputStream.read();
                        read2 = inputStream2.read();
                        if (read < 0) {
                            int i = read2 < 0 ? 0 : -1;
                            if (inputStream2 != null) {
                                inputStream2.close();
                            }
                            if (inputStream != null) {
                                inputStream.close();
                            }
                            return i;
                        }
                        if (read2 < 0) {
                            if (inputStream2 != null) {
                                inputStream2.close();
                            }
                            if (inputStream != null) {
                                inputStream.close();
                            }
                            return 1;
                        }
                    } while (read == read2);
                    int i2 = (read & 255) < (read2 & 255) ? -1 : 1;
                    if (inputStream2 != null) {
                        inputStream2.close();
                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    return i2;
                } catch (Throwable th) {
                    if (inputStream2 != null) {
                        try {
                            inputStream2.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                if (inputStream != null) {
                    try {
                        inputStream.close();
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
        if ((i & 2) != 0 && (!(this.lobData instanceof LobDataInMemory) || this.octetLength > SysProperties.MAX_TRACE_DATA_LENGTH)) {
            sb.append("CAST(REPEAT(CHAR(0), ").append(this.octetLength).append(") AS BINARY VARYING");
            formatLobDataComment(sb);
        } else if ((i & 6) == 0) {
            sb.append("CAST(X'");
            StringUtils.convertBytesToHex(sb, getBytesNoCopy()).append("' AS BINARY LARGE OBJECT(").append(this.octetLength).append("))");
        } else {
            sb.append("X'");
            StringUtils.convertBytesToHex(sb, getBytesNoCopy()).append('\'');
        }
        return sb;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ValueBlob convertPrecision(long j) {
        ValueBlob createSmall;
        if (this.octetLength <= j) {
            return this;
        }
        DataHandler dataHandler = this.lobData.getDataHandler();
        if (dataHandler != null) {
            createSmall = createTempBlob(getInputStream(), j, dataHandler);
        } else {
            try {
                createSmall = createSmall(IOUtils.readBytesAndClose(getInputStream(), MathUtils.convertLongToInt(j)));
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
                ValueBlob createBlob = dataHandler.getLobStorage().createBlob(getInputStream(), this.octetLength);
                ValueLob copy = createBlob.copy(dataHandler, i);
                createBlob.remove();
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
        long j = this.charLength;
        if (j < 0) {
            if (this.lobData instanceof LobDataInMemory) {
                j = new String(((LobDataInMemory) this.lobData).getSmall(), StandardCharsets.UTF_8).length();
            } else {
                try {
                    Reader reader = getReader();
                    long j2 = 0;
                    while (true) {
                        try {
                            j = j2 + reader.skip(Long.MAX_VALUE);
                            if (reader.read() < 0) {
                                break;
                            }
                            j2 = j + 1;
                        } finally {
                        }
                    }
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    throw DbException.convertIOException(e, null);
                }
            }
            this.charLength = j;
        }
        return j;
    }

    @Override // org.h2.value.Value
    public long octetLength() {
        return this.octetLength;
    }
}
