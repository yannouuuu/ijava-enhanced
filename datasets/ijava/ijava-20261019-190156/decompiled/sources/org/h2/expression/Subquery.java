package org.h2.expression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import org.h2.api.ErrorCode;
import org.h2.command.query.Query;
import org.h2.engine.SessionLocal;
import org.h2.message.DbException;
import org.h2.result.ResultInterface;
import org.h2.table.ColumnResolver;
import org.h2.table.TableFilter;
import org.h2.value.ExtTypeInfoRow;
import org.h2.value.TypeInfo;
import org.h2.value.Value;
import org.h2.value.ValueNull;
import org.h2.value.ValueRow;

/* loaded from: ijava.jar:org/h2/expression/Subquery.class */
public final class Subquery extends Expression {
    private final Query query;
    private Expression expression;
    private Value nullValue;
    private HashSet<ColumnResolver> outerResolvers = new HashSet<>();

    public Subquery(Query query) {
        this.query = query;
    }

    @Override // org.h2.expression.Expression
    public Value getValue(SessionLocal sessionLocal) {
        this.query.setSession(sessionLocal);
        ResultInterface query = this.query.query(2L);
        try {
            if (!query.next()) {
                Value value = this.nullValue;
                if (query != null) {
                    query.close();
                }
                return value;
            }
            Value readRow = readRow(query);
            if (query.hasNext()) {
                throw DbException.get(ErrorCode.SCALAR_SUBQUERY_CONTAINS_MORE_THAN_ONE_ROW);
            }
            if (query != null) {
                query.close();
            }
            return readRow;
        } catch (Throwable th) {
            if (query != null) {
                try {
                    query.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    public ArrayList<Value> getAllRows(SessionLocal sessionLocal) {
        ArrayList<Value> arrayList = new ArrayList<>();
        this.query.setSession(sessionLocal);
        ResultInterface query = this.query.query(2147483647L);
        while (query.next()) {
            try {
                arrayList.add(readRow(query));
            } catch (Throwable th) {
                if (query != null) {
                    try {
                        query.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }
        if (query != null) {
            query.close();
        }
        return arrayList;
    }

    private Value readRow(ResultInterface resultInterface) {
        Value[] currentRow = resultInterface.currentRow();
        int visibleColumnCount = resultInterface.getVisibleColumnCount();
        if (visibleColumnCount == 1) {
            return currentRow[0];
        }
        return ValueRow.get(getType(), visibleColumnCount == currentRow.length ? currentRow : (Value[]) Arrays.copyOf(currentRow, visibleColumnCount));
    }

    @Override // org.h2.expression.Expression, org.h2.value.Typed
    public TypeInfo getType() {
        return this.expression.getType();
    }

    @Override // org.h2.expression.Expression
    public void mapColumns(ColumnResolver columnResolver, int i, int i2) {
        this.outerResolvers.add(columnResolver);
        this.query.mapColumns(columnResolver, i + 1, true);
    }

    @Override // org.h2.expression.Expression
    public Expression optimize(SessionLocal sessionLocal) {
        Expression ifSingleRow;
        this.query.prepare();
        if (this.query.isConstantQuery()) {
            setType();
            return ValueExpression.get(getValue(sessionLocal));
        }
        if (this.outerResolvers != null && sessionLocal.getDatabase().getSettings().optimizeSimpleSingleRowSubqueries && (ifSingleRow = this.query.getIfSingleRow()) != null && ifSingleRow.isEverything(ExpressionVisitor.getDecrementQueryLevelVisitor(this.outerResolvers, 0))) {
            ifSingleRow.isEverything(ExpressionVisitor.getDecrementQueryLevelVisitor(this.outerResolvers, 1));
            return ifSingleRow.optimize(sessionLocal);
        }
        this.outerResolvers = null;
        setType();
        return this;
    }

    private void setType() {
        ArrayList<Expression> expressions = this.query.getExpressions();
        int columnCount = this.query.getColumnCount();
        if (columnCount == 1) {
            this.expression = expressions.get(0);
            this.nullValue = ValueNull.INSTANCE;
            return;
        }
        Expression[] expressionArr = new Expression[columnCount];
        Value[] valueArr = new Value[columnCount];
        for (int i = 0; i < columnCount; i++) {
            expressionArr[i] = expressions.get(i);
            valueArr[i] = ValueNull.INSTANCE;
        }
        ExpressionList expressionList = new ExpressionList(expressionArr, false);
        expressionList.initializeType();
        this.expression = expressionList;
        this.nullValue = ValueRow.get(new ExtTypeInfoRow(expressionArr), valueArr);
    }

    @Override // org.h2.expression.Expression
    public void setEvaluatable(TableFilter tableFilter, boolean z) {
        this.query.setEvaluatable(tableFilter, z);
    }

    @Override // org.h2.expression.Expression
    public StringBuilder getUnenclosedSQL(StringBuilder sb, int i) {
        return this.query.getPlanSQL(sb.append('('), i).append(')');
    }

    @Override // org.h2.expression.Expression
    public void updateAggregate(SessionLocal sessionLocal, int i) {
        this.query.updateAggregate(sessionLocal, i);
    }

    @Override // org.h2.expression.Expression
    public boolean isEverything(ExpressionVisitor expressionVisitor) {
        return this.query.isEverything(expressionVisitor);
    }

    public Query getQuery() {
        return this.query;
    }

    @Override // org.h2.expression.Expression
    public int getCost() {
        return this.query.getCostAsExpression();
    }

    @Override // org.h2.expression.Expression
    public TypeInfo getTypeIfStaticallyKnown(SessionLocal sessionLocal) {
        if (this.query.isConstantQuery()) {
            this.query.prepare();
            setType();
            return this.expression.getType();
        }
        return null;
    }

    @Override // org.h2.expression.Expression
    public boolean isConstant() {
        return this.query.isConstantQuery();
    }
}
