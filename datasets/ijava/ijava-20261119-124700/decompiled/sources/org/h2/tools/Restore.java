package org.h2.tools;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.h2.engine.Constants;
import org.h2.message.DbException;
import org.h2.store.fs.FileUtils;
import org.h2.util.IOUtils;
import org.h2.util.Tool;

/* loaded from: ijava.jar:org/h2/tools/Restore.class */
public class Restore extends Tool {
    public static void main(String... strArr) throws SQLException {
        new Restore().runTool(strArr);
    }

    @Override // org.h2.util.Tool
    public void runTool(String... strArr) throws SQLException {
        String str = "backup.zip";
        String str2 = ".";
        String str3 = null;
        int i = 0;
        while (strArr != null && i < strArr.length) {
            String str4 = strArr[i];
            if (str4.equals("-dir")) {
                i++;
                str2 = strArr[i];
            } else if (str4.equals("-file")) {
                i++;
                str = strArr[i];
            } else if (str4.equals("-db")) {
                i++;
                str3 = strArr[i];
            } else if (str4.equals("-quiet")) {
                continue;
            } else {
                if (str4.equals("-help") || str4.equals("-?")) {
                    showUsage();
                    return;
                }
                showUsageAndThrowUnsupportedOption(str4);
            }
            i++;
        }
        execute(str, str2, str3);
    }

    private static String getOriginalDbName(String str, String str2) throws IOException {
        InputStream newInputStream = FileUtils.newInputStream(str);
        try {
            ZipInputStream zipInputStream = new ZipInputStream(newInputStream);
            String str3 = null;
            boolean z = false;
            while (true) {
                ZipEntry nextEntry = zipInputStream.getNextEntry();
                if (nextEntry == null) {
                    break;
                }
                String name = nextEntry.getName();
                zipInputStream.closeEntry();
                String databaseNameFromFileName = getDatabaseNameFromFileName(name);
                if (databaseNameFromFileName != null) {
                    if (str2.equals(databaseNameFromFileName)) {
                        str3 = databaseNameFromFileName;
                        break;
                    }
                    if (str3 == null) {
                        str3 = databaseNameFromFileName;
                    } else {
                        z = true;
                    }
                }
            }
            zipInputStream.close();
            if (z && !str2.equals(str3)) {
                throw new IOException("Multiple databases found, but not " + str2);
            }
            String str4 = str3;
            if (newInputStream != null) {
                newInputStream.close();
            }
            return str4;
        } catch (Throwable th) {
            if (newInputStream != null) {
                try {
                    newInputStream.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    private static String getDatabaseNameFromFileName(String str) {
        if (str.endsWith(Constants.SUFFIX_MV_FILE)) {
            return str.substring(0, str.length() - Constants.SUFFIX_MV_FILE.length());
        }
        return null;
    }

    /* JADX WARN: Finally extract failed */
    public static void execute(String str, String str2, String str3) {
        try {
            try {
                if (!FileUtils.exists(str)) {
                    throw new IOException("File not found: " + str);
                }
                String str4 = null;
                int i = 0;
                if (str3 != null) {
                    str4 = getOriginalDbName(str, str3);
                    if (str4 == null) {
                        throw new IOException("No database named " + str3 + " found");
                    }
                    if (str4.startsWith(File.separator)) {
                        str4 = str4.substring(1);
                    }
                    i = str4.length();
                }
                InputStream newInputStream = FileUtils.newInputStream(str);
                ZipInputStream zipInputStream = new ZipInputStream(newInputStream);
                while (true) {
                    try {
                        ZipEntry nextEntry = zipInputStream.getNextEntry();
                        if (nextEntry != null) {
                            String nameSeparatorsToNative = IOUtils.nameSeparatorsToNative(nextEntry.getName());
                            if (nameSeparatorsToNative.startsWith(File.separator)) {
                                nameSeparatorsToNative = nameSeparatorsToNative.substring(1);
                            }
                            boolean z = false;
                            if (str3 == null) {
                                z = true;
                            } else if (nameSeparatorsToNative.startsWith(str4 + ".")) {
                                nameSeparatorsToNative = str3 + nameSeparatorsToNative.substring(i);
                                z = true;
                            }
                            if (z) {
                                OutputStream outputStream = null;
                                try {
                                    outputStream = FileUtils.newOutputStream(str2 + File.separatorChar + nameSeparatorsToNative, false);
                                    IOUtils.copy(zipInputStream, outputStream);
                                    outputStream.close();
                                    IOUtils.closeSilently(outputStream);
                                } catch (Throwable th) {
                                    IOUtils.closeSilently(outputStream);
                                    throw th;
                                }
                            }
                            zipInputStream.closeEntry();
                        } else {
                            zipInputStream.closeEntry();
                            zipInputStream.close();
                            IOUtils.closeSilently(newInputStream);
                            return;
                        }
                    } catch (Throwable th2) {
                        try {
                            zipInputStream.close();
                        } catch (Throwable th3) {
                            th2.addSuppressed(th3);
                        }
                        throw th2;
                    }
                }
            } catch (Throwable th4) {
                IOUtils.closeSilently(null);
                throw th4;
            }
        } catch (IOException e) {
            throw DbException.convertIOException(e, str);
        }
    }
}
