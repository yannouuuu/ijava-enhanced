package org.h2.table;

import org.h2.engine.SessionLocal;
import org.h2.message.DbException;
import org.h2.result.ResultInterface;
import org.h2.schema.Schema;

/* loaded from: ijava.jar:org/h2/table/ShadowTable.class */
public class ShadowTable extends VirtualConstructedTable {
    public ShadowTable(Schema schema, String str, Column[] columnArr) {
        super(schema, 0, str);
        setColumns(columnArr);
    }

    @Override // org.h2.table.VirtualConstructedTable
    public ResultInterface getResult(SessionLocal sessionLocal) {
        throw DbException.getInternalError("shadow table");
    }

    @Override // org.h2.table.Table
    public boolean isDeterministic() {
        return false;
    }

    @Override // org.h2.table.Table
    public boolean canGetRowCount(SessionLocal sessionLocal) {
        return false;
    }

    @Override // org.h2.table.Table
    public long getRowCount(SessionLocal sessionLocal) {
        return Long.MAX_VALUE;
    }

    @Override // org.h2.table.Table
    public long getRowCountApproximation(SessionLocal sessionLocal) {
        return Long.MAX_VALUE;
    }
}
