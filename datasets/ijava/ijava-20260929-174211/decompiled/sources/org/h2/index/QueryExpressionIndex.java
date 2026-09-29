package org.h2.index;

import java.util.ArrayList;
import org.h2.command.query.Query;
import org.h2.engine.SessionLocal;
import org.h2.expression.Parameter;
import org.h2.message.DbException;
import org.h2.result.Row;
import org.h2.table.Column;
import org.h2.table.QueryExpressionTable;

/* loaded from: ijava.jar:org/h2/index/QueryExpressionIndex.class */
public abstract class QueryExpressionIndex extends Index {
    final QueryExpressionTable table;
    final String querySQL;
    final ArrayList<Parameter> originalParameters;
    Query query;

    public abstract boolean isExpired();

    /* JADX INFO: Access modifiers changed from: package-private */
    public QueryExpressionIndex(QueryExpressionTable queryExpressionTable, String str, ArrayList<Parameter> arrayList) {
        super(queryExpressionTable, 0, null, null, 0, IndexType.createNonUnique(false));
        this.table = queryExpressionTable;
        this.querySQL = str;
        this.originalParameters = arrayList;
        this.columns = new Column[0];
    }

    @Override // org.h2.index.Index
    public String getPlanSQL() {
        if (this.query == null) {
            return null;
        }
        return this.query.getPlanSQL(11);
    }

    public Query getQuery() {
        return this.query;
    }

    @Override // org.h2.index.Index
    public void close(SessionLocal sessionLocal) {
    }

    @Override // org.h2.index.Index
    public void add(SessionLocal sessionLocal, Row row) {
        throw DbException.getUnsupportedException(getClass().getSimpleName() + ".add");
    }

    @Override // org.h2.index.Index
    public void remove(SessionLocal sessionLocal, Row row) {
        throw DbException.getUnsupportedException(getClass().getSimpleName() + ".remove");
    }

    @Override // org.h2.index.Index
    public void remove(SessionLocal sessionLocal) {
        throw DbException.getUnsupportedException(getClass().getSimpleName() + ".remove");
    }

    @Override // org.h2.index.Index
    public void truncate(SessionLocal sessionLocal) {
        throw DbException.getUnsupportedException(getClass().getSimpleName() + ".truncate");
    }

    @Override // org.h2.engine.DbObject
    public void checkRename() {
        throw DbException.getUnsupportedException(getClass().getSimpleName() + ".checkRename");
    }

    @Override // org.h2.index.Index
    public boolean needRebuild() {
        return false;
    }

    @Override // org.h2.index.Index
    public long getRowCount(SessionLocal sessionLocal) {
        return 0L;
    }

    @Override // org.h2.index.Index
    public long getRowCountApproximation(SessionLocal sessionLocal) {
        return 0L;
    }
}
