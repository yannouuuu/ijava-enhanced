package org.h2.table;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.h2.api.ErrorCode;
import org.h2.command.Prepared;
import org.h2.command.QueryScope;
import org.h2.command.query.AllColumnsForPlan;
import org.h2.command.query.Query;
import org.h2.engine.Database;
import org.h2.engine.SessionLocal;
import org.h2.index.Index;
import org.h2.index.QueryExpressionIndex;
import org.h2.index.RegularQueryExpressionIndex;
import org.h2.message.DbException;
import org.h2.result.SortOrder;
import org.h2.schema.Schema;
import org.h2.util.StringUtils;
import org.h2.util.Utils;

/* loaded from: ijava.jar:org/h2/table/TableView.class */
public final class TableView extends QueryExpressionTable {
    private String querySQL;
    private Column[] columnTemplates;
    private DbException createException;

    public TableView(Schema schema, int i, String str, String str2, Column[] columnArr, SessionLocal sessionLocal) {
        super(schema, i, str);
        init(str2, columnArr, sessionLocal);
    }

    @Override // org.h2.table.QueryExpressionTable
    protected QueryExpressionIndex createIndex(SessionLocal sessionLocal, int[] iArr) {
        return new RegularQueryExpressionIndex(this, this.querySQL, null, sessionLocal, iArr);
    }

    public void replace(String str, Column[] columnArr, SessionLocal sessionLocal, boolean z) {
        String str2 = this.querySQL;
        Column[] columnArr2 = this.columnTemplates;
        init(str, columnArr, sessionLocal);
        DbException recompile = recompile(sessionLocal, z, true);
        if (recompile != null) {
            init(str2, columnArr2, sessionLocal);
            recompile(sessionLocal, true, false);
            throw recompile;
        }
    }

    private synchronized void init(String str, Column[] columnArr, SessionLocal sessionLocal) {
        this.querySQL = str;
        this.columnTemplates = columnArr;
        initColumnsAndTables(sessionLocal);
    }

    private static Query compileViewQuery(SessionLocal sessionLocal, String str) {
        sessionLocal.setParsingCreateView(true);
        try {
            Prepared prepare = sessionLocal.prepare(str, false, false, null);
            if (!(prepare instanceof Query)) {
                throw DbException.getSyntaxError(str, 0);
            }
            return (Query) prepare;
        } finally {
            sessionLocal.setParsingCreateView(false);
        }
    }

    public synchronized DbException recompile(SessionLocal sessionLocal, boolean z, boolean z2) {
        try {
            compileViewQuery(sessionLocal, this.querySQL);
        } catch (DbException e) {
            if (!z) {
                return e;
            }
        }
        ArrayList arrayList = new ArrayList(getDependentViews());
        initColumnsAndTables(sessionLocal);
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            DbException recompile = ((TableView) it.next()).recompile(sessionLocal, z, false);
            if (recompile != null && !z) {
                return recompile;
            }
        }
        if (z2) {
            clearIndexCaches(this.database);
        }
        if (z) {
            return null;
        }
        return this.createException;
    }

    private void initColumnsAndTables(SessionLocal sessionLocal) {
        Column[] columnArr;
        removeCurrentViewFromOtherTables();
        try {
            Query compileViewQuery = compileViewQuery(sessionLocal, this.querySQL);
            this.querySQL = compileViewQuery.getPlanSQL(0);
            this.tables = new ArrayList<>(compileViewQuery.getTables());
            columnArr = initColumns(sessionLocal, this.columnTemplates, compileViewQuery, false);
            this.createException = null;
            this.viewQuery = compileViewQuery;
        } catch (DbException e) {
            if (e.getErrorCode() == 90156) {
                throw e;
            }
            e.addSQL(getCreateSQL());
            this.createException = e;
            this.tables = Utils.newSmallArrayList();
            columnArr = new Column[0];
        }
        setColumns(columnArr);
        if (getId() != 0) {
            addDependentViewToTables();
        }
    }

    public boolean isInvalid() {
        return this.createException != null;
    }

    @Override // org.h2.table.QueryExpressionTable
    public Query getTopQuery() {
        return null;
    }

    @Override // org.h2.engine.DbObject
    public String getDropSQL() {
        return getSQL(new StringBuilder("DROP VIEW IF EXISTS "), 0).append(" CASCADE").toString();
    }

    @Override // org.h2.engine.DbObject
    public String getCreateSQLForCopy(Table table, String str) {
        return getCreateSQL(false, true, str);
    }

    @Override // org.h2.engine.DbObject
    public String getCreateSQL() {
        return getCreateSQL(false, true);
    }

    public String getCreateSQL(boolean z, boolean z2) {
        return getCreateSQL(z, z2, getSQL(0));
    }

    private String getCreateSQL(boolean z, boolean z2, String str) {
        StringBuilder sb = new StringBuilder("CREATE ");
        if (z) {
            sb.append("OR REPLACE ");
        }
        if (z2) {
            sb.append("FORCE ");
        }
        sb.append("VIEW ");
        sb.append(str);
        if (this.comment != null) {
            sb.append(" COMMENT ");
            StringUtils.quoteStringSQL(sb, this.comment);
        }
        if (this.columns != null && this.columns.length > 0) {
            sb.append('(');
            Column.writeColumns(sb, this.columns, 0);
            sb.append(')');
        } else if (this.columnTemplates != null) {
            sb.append('(');
            Column.writeColumns(sb, this.columnTemplates, 0);
            sb.append(')');
        }
        return sb.append(" AS\n").append(this.querySQL).toString();
    }

    @Override // org.h2.table.Table
    public boolean canDrop() {
        return true;
    }

    @Override // org.h2.table.Table
    public TableType getTableType() {
        return TableType.VIEW;
    }

    @Override // org.h2.table.Table, org.h2.engine.DbObject
    public void removeChildrenAndResources(SessionLocal sessionLocal) {
        removeCurrentViewFromOtherTables();
        super.removeChildrenAndResources(sessionLocal);
        this.querySQL = null;
        clearIndexCaches(this.database);
        invalidate();
    }

    public static void clearIndexCaches(Database database) {
        for (SessionLocal sessionLocal : database.getSessions(true)) {
            sessionLocal.clearViewIndexCache();
        }
    }

    public String getQuerySQL() {
        return this.querySQL;
    }

    @Override // org.h2.table.QueryExpressionTable
    public QueryScope getQueryScope() {
        return null;
    }

    @Override // org.h2.table.QueryExpressionTable, org.h2.table.Table
    public Index getScanIndex(SessionLocal sessionLocal, int[] iArr, TableFilter[] tableFilterArr, int i, SortOrder sortOrder, AllColumnsForPlan allColumnsForPlan) {
        if (this.createException != null) {
            throw DbException.get(ErrorCode.VIEW_IS_INVALID_2, this.createException, getTraceSQL(), this.createException.getMessage());
        }
        return super.getScanIndex(sessionLocal, iArr, tableFilterArr, i, sortOrder, allColumnsForPlan);
    }

    @Override // org.h2.table.QueryExpressionTable, org.h2.table.Table
    public long getMaxDataModificationId() {
        if (this.createException != null || this.viewQuery == null) {
            return Long.MAX_VALUE;
        }
        return super.getMaxDataModificationId();
    }

    private void removeCurrentViewFromOtherTables() {
        if (this.tables != null) {
            Iterator<Table> it = this.tables.iterator();
            while (it.hasNext()) {
                it.next().removeDependentView(this);
            }
            this.tables.clear();
        }
    }

    private void addDependentViewToTables() {
        Iterator<Table> it = this.tables.iterator();
        while (it.hasNext()) {
            it.next().addDependentView(this);
        }
    }

    @Override // org.h2.table.QueryExpressionTable, org.h2.table.Table
    public boolean isDeterministic() {
        if (this.viewQuery == null) {
            return false;
        }
        return super.isDeterministic();
    }

    public List<Table> getTables() {
        return this.tables;
    }
}
