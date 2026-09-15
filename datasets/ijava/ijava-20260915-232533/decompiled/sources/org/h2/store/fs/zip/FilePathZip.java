package org.h2.store.fs.zip;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.h2.message.DbException;
import org.h2.store.fs.FilePath;
import org.h2.store.fs.disk.FilePathDisk;

/* loaded from: ijava.jar:org/h2/store/fs/zip/FilePathZip.class */
public class FilePathZip extends FilePath {
    @Override // org.h2.store.fs.FilePath
    public FilePathZip getPath(String str) {
        FilePathZip filePathZip = new FilePathZip();
        filePathZip.name = str;
        return filePathZip;
    }

    @Override // org.h2.store.fs.FilePath
    public void createDirectory() {
    }

    @Override // org.h2.store.fs.FilePath
    public boolean createFile() {
        throw DbException.getUnsupportedException("write");
    }

    @Override // org.h2.store.fs.FilePath
    public void delete() {
        throw DbException.getUnsupportedException("write");
    }

    @Override // org.h2.store.fs.FilePath
    public boolean exists() {
        try {
            String entryName = getEntryName();
            if (entryName.isEmpty()) {
                return true;
            }
            ZipFile openZipFile = openZipFile();
            try {
                boolean z = openZipFile.getEntry(entryName) != null;
                if (openZipFile != null) {
                    openZipFile.close();
                }
                return z;
            } catch (Throwable th) {
                if (openZipFile != null) {
                    try {
                        openZipFile.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        } catch (IOException e) {
            return false;
        }
    }

    @Override // org.h2.store.fs.FilePath
    public long lastModified() {
        return 0L;
    }

    @Override // org.h2.store.fs.FilePath
    public FilePath getParent() {
        int lastIndexOf = this.name.lastIndexOf(47);
        if (lastIndexOf < 0) {
            return null;
        }
        return getPath(this.name.substring(0, lastIndexOf));
    }

    @Override // org.h2.store.fs.FilePath
    public boolean isAbsolute() {
        return FilePath.get(translateFileName(this.name)).isAbsolute();
    }

    @Override // org.h2.store.fs.FilePath
    public FilePath unwrap() {
        return FilePath.get(this.name.substring(getScheme().length() + 1));
    }

    @Override // org.h2.store.fs.FilePath
    public boolean isDirectory() {
        return isRegularOrDirectory(true);
    }

    @Override // org.h2.store.fs.FilePath
    public boolean isRegularFile() {
        return isRegularOrDirectory(false);
    }

    private boolean isRegularOrDirectory(boolean z) {
        try {
            String entryName = getEntryName();
            if (entryName.isEmpty()) {
                return z;
            }
            ZipFile openZipFile = openZipFile();
            try {
                Enumeration<? extends ZipEntry> entries = openZipFile.entries();
                while (entries.hasMoreElements()) {
                    ZipEntry nextElement = entries.nextElement();
                    String name = nextElement.getName();
                    if (name.equals(entryName)) {
                        boolean z2 = nextElement.isDirectory() == z;
                        if (openZipFile != null) {
                            openZipFile.close();
                        }
                        return z2;
                    }
                    if (name.startsWith(entryName) && name.length() == entryName.length() + 1 && name.equals(entryName + "/")) {
                        if (openZipFile != null) {
                            openZipFile.close();
                        }
                        return z;
                    }
                }
                if (openZipFile != null) {
                    openZipFile.close();
                    return false;
                }
                return false;
            } catch (Throwable th) {
                if (openZipFile != null) {
                    try {
                        openZipFile.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        } catch (IOException e) {
            return false;
        }
    }

    @Override // org.h2.store.fs.FilePath
    public boolean canWrite() {
        return false;
    }

    @Override // org.h2.store.fs.FilePath
    public boolean setReadOnly() {
        return true;
    }

    @Override // org.h2.store.fs.FilePath
    public long size() {
        try {
            ZipFile openZipFile = openZipFile();
            try {
                ZipEntry entry = openZipFile.getEntry(getEntryName());
                long size = entry == null ? 0L : entry.getSize();
                if (openZipFile != null) {
                    openZipFile.close();
                }
                return size;
            } finally {
            }
        } catch (IOException e) {
            return 0L;
        }
    }

    @Override // org.h2.store.fs.FilePath
    public ArrayList<FilePath> newDirectoryStream() {
        String str = this.name;
        ArrayList<FilePath> arrayList = new ArrayList<>();
        try {
            if (str.indexOf(33) < 0) {
                str = str + "!";
            }
            if (!str.endsWith("/")) {
                str = str + "/";
            }
            ZipFile openZipFile = openZipFile();
            try {
                String entryName = getEntryName();
                String substring = str.substring(0, str.length() - entryName.length());
                Enumeration<? extends ZipEntry> entries = openZipFile.entries();
                while (entries.hasMoreElements()) {
                    String name = entries.nextElement().getName();
                    if (name.startsWith(entryName) && name.length() > entryName.length()) {
                        int indexOf = name.indexOf(47, entryName.length());
                        if (indexOf < 0 || indexOf >= name.length() - 1) {
                            arrayList.add(getPath(substring + name));
                        }
                    }
                }
                if (openZipFile != null) {
                    openZipFile.close();
                }
                return arrayList;
            } finally {
            }
        } catch (IOException e) {
            throw DbException.convertIOException(e, "listFiles " + str);
        }
    }

    @Override // org.h2.store.fs.FilePath
    public FileChannel open(String str) throws IOException {
        ZipFile openZipFile = openZipFile();
        ZipEntry entry = openZipFile.getEntry(getEntryName());
        if (entry == null) {
            openZipFile.close();
            throw new FileNotFoundException(this.name);
        }
        return new FileZip(openZipFile, entry);
    }

    @Override // org.h2.store.fs.FilePath
    public OutputStream newOutputStream(boolean z) throws IOException {
        throw new IOException("write");
    }

    @Override // org.h2.store.fs.FilePath
    public void moveTo(FilePath filePath, boolean z) {
        throw DbException.getUnsupportedException("write");
    }

    private static String translateFileName(String str) {
        if (str.startsWith("zip:")) {
            str = str.substring("zip:".length());
        }
        int indexOf = str.indexOf(33);
        if (indexOf >= 0) {
            str = str.substring(0, indexOf);
        }
        return FilePathDisk.expandUserHomeDirectory(str);
    }

    @Override // org.h2.store.fs.FilePath
    public FilePath toRealPath() {
        return this;
    }

    private String getEntryName() {
        String substring;
        int indexOf = this.name.indexOf(33);
        if (indexOf <= 0) {
            substring = "";
        } else {
            substring = this.name.substring(indexOf + 1);
        }
        String replace = substring.replace('\\', '/');
        if (replace.startsWith("/")) {
            replace = replace.substring(1);
        }
        return replace;
    }

    private ZipFile openZipFile() throws IOException {
        return new ZipFile(translateFileName(this.name));
    }

    @Override // org.h2.store.fs.FilePath
    public FilePath createTempFile(String str, boolean z) throws IOException {
        if (!z) {
            throw new IOException("File system is read-only");
        }
        return new FilePathDisk().getPath(this.name).createTempFile(str, true);
    }

    @Override // org.h2.store.fs.FilePath
    public String getScheme() {
        return "zip";
    }
}
