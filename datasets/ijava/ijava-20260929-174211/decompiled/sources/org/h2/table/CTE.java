package org.h2.table;

import java.util.ArrayList;
import org.h2.command.QueryScope;
import org.h2.command.query.Query;
import org.h2.engine.SessionLocal;
import org.h2.expression.Parameter;
import org.h2.index.QueryExpressionIndex;
import org.h2.index.RecursiveIndex;
import org.h2.index.RegularQueryExpressionIndex;
import org.h2.result.ResultInterface;
import org.h2.util.ParserUtil;

/* loaded from: ijava.jar:org/h2/table/CTE.class */
public final class CTE extends QueryExpressionTable {
    private final String querySQL;
    private final boolean recursive;
    private final QueryScope queryScope;
    private final ArrayList<Parameter> originalParameters;
    private ResultInterface recursiveResult;

    public CTE(String str, Query query, String str2, ArrayList<Parameter> arrayList, Column[] columnArr, SessionLocal sessionLocal, boolean z, QueryScope queryScope) {
        super(sessionLocal.getDatabase().getMainSchema(), 0, str);
        setTemporary(true);
        this.queryScope = queryScope;
        this.querySQL = str2;
        this.recursive = z;
        this.originalParameters = arrayList;
        this.tables = new ArrayList<>(query.getTables());
        setColumns(initColumns(sessionLocal, columnArr, query, false));
        this.viewQuery = query;
    }

    @Override // org.h2.table.QueryExpressionTable
    protected QueryExpressionIndex createIndex(SessionLocal sessionLocal, int[] iArr) {
        return this.recursive ? new RecursiveIndex(this, this.querySQL, this.originalParameters, sessionLocal) : new RegularQueryExpressionIndex(this, this.querySQL, this.originalParameters, sessionLocal, iArr);
    }

    @Override // org.h2.table.QueryExpressionTable
    public Query getTopQuery() {
        return null;
    }

    @Override // org.h2.engine.DbObject
    public String getCreateSQL() {
        return null;
    }

    @Override // org.h2.table.Table
    public boolean canDrop() {
        return false;
    }

    @Override // org.h2.table.Table
    public TableType getTableType() {
        return null;
    }

    @Override // org.h2.schema.SchemaObject, org.h2.engine.DbObject, org.h2.util.HasSQL
    public StringBuilder getSQL(StringBuilder sb, int i) {
        return ParserUtil.quoteIdentifier(sb, getName(), i);
    }

    public String getQuerySQL() {
        return this.querySQL;
    }

    @Override // org.h2.table.QueryExpressionTable
    public QueryScope getQueryScope() {
        return this.queryScope;
    }

    public boolean isRecursive() {
        return this.recursive;
    }

    @Override // org.h2.table.QueryExpressionTable, org.h2.table.Table
    public boolean isDeterministic() {
        if (this.recursive) {
            return false;
        }
        return super.isDeterministic();
    }

    public void setRecursiveResult(ResultInterface resultInterface) {
        if (this.recursiveResult != null) {
            this.recursiveResult.close();
        }
        this.recursiveResult = resultInterface;
    }

    public ResultInterface getRecursiveResult() {
        return this.recursiveResult;
    }
}
