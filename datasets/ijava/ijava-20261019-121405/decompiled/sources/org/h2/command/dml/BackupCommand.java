package org.h2.command.dml;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.zip.ZipOutputStream;
import org.h2.api.ErrorCode;
import org.h2.command.Prepared;
import org.h2.engine.Constants;
import org.h2.engine.Database;
import org.h2.engine.SessionLocal;
import org.h2.expression.Expression;
import org.h2.message.DbException;
import org.h2.mvstore.db.Store;
import org.h2.result.ResultInterface;
import org.h2.store.FileLister;
import org.h2.store.fs.FileUtils;

/* loaded from: ijava.jar:org/h2/command/dml/BackupCommand.class */
public class BackupCommand extends Prepared {
    private Expression fileNameExpr;

    public BackupCommand(SessionLocal sessionLocal) {
        super(sessionLocal);
    }

    public void setFileName(Expression expression) {
        this.fileNameExpr = expression;
    }

    @Override // org.h2.command.Prepared
    public long update() {
        String string = this.fileNameExpr.getValue(this.session).getString();
        this.session.getUser().checkAdmin();
        backupTo(string);
        return 0L;
    }

    private void backupTo(String str) {
        Database database = getDatabase();
        if (!database.isPersistent()) {
            throw DbException.get(ErrorCode.DATABASE_IS_NOT_PERSISTENT);
        }
        try {
            Store store = database.getStore();
            store.flush();
            String name = FileUtils.getName(database.getName());
            OutputStream newOutputStream = FileUtils.newOutputStream(str, false);
            try {
                ZipOutputStream zipOutputStream = new ZipOutputStream(newOutputStream);
                database.flush();
                synchronized (database.getLobSyncObject()) {
                    Iterator<String> it = FileLister.getDatabaseFiles(FileLister.getDir(FileUtils.getParent(database.getDatabasePath())), name, true).iterator();
                    while (it.hasNext()) {
                        if (it.next().endsWith(Constants.SUFFIX_MV_FILE)) {
                            store.getMvStore().getFileStore().backup(zipOutputStream);
                        }
                    }
                }
                zipOutputStream.close();
                if (newOutputStream != null) {
                    newOutputStream.close();
                }
            } finally {
            }
        } catch (IOException e) {
            throw DbException.convertIOException(e, str);
        }
    }

    @Override // org.h2.command.Prepared
    public boolean isTransactional() {
        return true;
    }

    public static String correctFileName(String str) {
        String replace = str.replace('\\', '/');
        if (replace.startsWith("/")) {
            replace = replace.substring(1);
        }
        return replace;
    }

    @Override // org.h2.command.Prepared
    public boolean needRecompile() {
        return false;
    }

    @Override // org.h2.command.Prepared
    public ResultInterface queryMeta() {
        return null;
    }

    @Override // org.h2.command.Prepared
    public int getType() {
        return 56;
    }
}
