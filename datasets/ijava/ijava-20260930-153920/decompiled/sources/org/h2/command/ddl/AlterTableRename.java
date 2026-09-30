package org.h2.command.ddl;

import org.h2.api.ErrorCode;
import org.h2.engine.Database;
import org.h2.engine.SessionLocal;
import org.h2.message.DbException;
import org.h2.schema.Schema;
import org.h2.table.Table;

/* loaded from: ijava.jar:org/h2/command/ddl/AlterTableRename.class */
public class AlterTableRename extends AlterTable {
    private String newTableName;

    public AlterTableRename(SessionLocal sessionLocal, Schema schema) {
        super(sessionLocal, schema);
    }

    public void setNewTableName(String str) {
        this.newTableName = str;
    }

    @Override // org.h2.command.ddl.AlterTable
    public long update(Table table) {
        Database database = getDatabase();
        if (getSchema().findTableOrView(this.session, this.newTableName) != null || this.newTableName.equals(table.getName())) {
            throw DbException.get(ErrorCode.TABLE_OR_VIEW_ALREADY_EXISTS_1, this.newTableName);
        }
        if (table.isTemporary()) {
            throw DbException.getUnsupportedException("temp table");
        }
        database.renameSchemaObject(this.session, table, this.newTableName);
        return 0L;
    }

    @Override // org.h2.command.Prepared
    public int getType() {
        return 15;
    }
}
