package org.h2.index;

import java.util.ArrayList;
import org.h2.api.ErrorCode;
import org.h2.command.Parser;
import org.h2.command.query.AllColumnsForPlan;
import org.h2.command.query.Query;
import org.h2.command.query.SelectUnion;
import org.h2.engine.SessionLocal;
import org.h2.expression.Parameter;
import org.h2.message.DbException;
import org.h2.result.LocalResult;
import org.h2.result.ResultInterface;
import org.h2.result.SearchRow;
import org.h2.result.SortOrder;
import org.h2.table.CTE;
import org.h2.table.QueryExpressionTable;
import org.h2.table.TableFilter;

/* loaded from: ijava.jar:org/h2/index/RecursiveIndex.class */
public final class RecursiveIndex extends QueryExpressionIndex {
    private final SessionLocal createSession;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !RecursiveIndex.class.desiredAssertionStatus();
    }

    public RecursiveIndex(QueryExpressionTable queryExpressionTable, String str, ArrayList<Parameter> arrayList, SessionLocal sessionLocal) {
        super(queryExpressionTable, str, arrayList);
        this.createSession = sessionLocal;
    }

    @Override // org.h2.index.QueryExpressionIndex
    public boolean isExpired() {
        return false;
    }

    @Override // org.h2.index.Index
    public double getCost(SessionLocal sessionLocal, int[] iArr, TableFilter[] tableFilterArr, int i, SortOrder sortOrder, AllColumnsForPlan allColumnsForPlan) {
        return 1000.0d;
    }

    @Override // org.h2.index.Index
    public Cursor find(SessionLocal sessionLocal, SearchRow searchRow, SearchRow searchRow2, boolean z) {
        if (!$assertionsDisabled && z) {
            throw new AssertionError();
        }
        CTE cte = (CTE) this.table;
        ResultInterface recursiveResult = cte.getRecursiveResult();
        if (recursiveResult != null) {
            recursiveResult.reset();
            return new QueryExpressionCursor(this, recursiveResult, searchRow, searchRow2);
        }
        if (this.query == null) {
            Parser parser = new Parser(this.createSession);
            parser.setRightsChecked(true);
            parser.setSuppliedParameters(this.originalParameters);
            parser.setQueryScope(this.table.getQueryScope());
            this.query = (Query) parser.prepare(this.querySQL);
            this.query.setNeverLazy(true);
        }
        if (!this.query.isUnion()) {
            throw DbException.get(ErrorCode.SYNTAX_ERROR_2, "recursive queries without UNION");
        }
        SelectUnion selectUnion = (SelectUnion) this.query;
        Query left = selectUnion.getLeft();
        left.setNeverLazy(true);
        left.disableCache();
        ResultInterface query = left.query(0L);
        LocalResult emptyResult = selectUnion.getEmptyResult();
        emptyResult.setMaxMemoryRows(IndexSort.FULLY_SORTED);
        while (query.next()) {
            emptyResult.addRow(query.currentRow());
        }
        Query right = selectUnion.getRight();
        right.setNeverLazy(true);
        query.reset();
        cte.setRecursiveResult(query);
        right.disableCache();
        while (true) {
            ResultInterface query2 = right.query(0L);
            if (query2.hasNext()) {
                while (query2.next()) {
                    emptyResult.addRow(query2.currentRow());
                }
                query2.reset();
                cte.setRecursiveResult(query2);
            } else {
                cte.setRecursiveResult(null);
                emptyResult.done();
                return new QueryExpressionCursor(this, emptyResult, searchRow, searchRow2);
            }
        }
    }
}
