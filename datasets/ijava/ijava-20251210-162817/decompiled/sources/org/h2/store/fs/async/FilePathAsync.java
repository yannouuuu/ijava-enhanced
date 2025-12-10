package org.h2.store.fs.async;

import java.io.IOException;
import java.nio.channels.FileChannel;
import org.h2.store.fs.FilePathWrapper;

/* loaded from: ijava.jar:org/h2/store/fs/async/FilePathAsync.class */
public class FilePathAsync extends FilePathWrapper {
    @Override // org.h2.store.fs.FilePathWrapper, org.h2.store.fs.FilePath
    public FileChannel open(String str) throws IOException {
        return new FileAsync(this.name.substring(getScheme().length() + 1), str);
    }

    @Override // org.h2.store.fs.FilePath
    public String getScheme() {
        return "async";
    }
}
