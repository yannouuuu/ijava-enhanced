package org.h2.index;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import org.h2.command.query.Query;
import org.h2.engine.SessionLocal;
import org.h2.expression.Expression;
import org.h2.expression.ExpressionColumn;
import org.h2.expression.ExpressionList;
import org.h2.expression.ExpressionVisitor;
import org.h2.expression.ValueExpression;
import org.h2.message.DbException;
import org.h2.result.ResultInterface;
import org.h2.table.Column;
import org.h2.table.IndexColumn;
import org.h2.table.TableType;
import org.h2.value.Value;
import org.h2.value.ValueArray;
import org.h2.value.ValueRow;

/* loaded from: ijava.jar:org/h2/index/IndexCondition.class */
public class IndexCondition {
    public static final int EQUALITY = 1;
    public static final int START = 2;
    public static final int END = 4;
    public static final int RANGE = 6;
    public static final int ALWAYS_FALSE = 8;
    public static final int SPATIAL_INTERSECTS = 16;
    private final Column column;
    private final Column[] columns;
    private final boolean compoundColumns;
    private final int compareType;
    private final Expression expression;
    private final List<Expression> expressionList;
    private final Query expressionQuery;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !IndexCondition.class.desiredAssertionStatus();
    }

    private IndexCondition(int i, ExpressionColumn expressionColumn, Column[] columnArr, Expression expression, List<Expression> list, Query query) {
        this.compareType = i;
        if (expressionColumn != null) {
            this.column = expressionColumn.getColumn();
            this.columns = null;
            this.compoundColumns = false;
        } else if (columnArr != null) {
            this.column = null;
            this.columns = columnArr;
            this.compoundColumns = true;
        } else {
            this.column = null;
            this.columns = null;
            this.compoundColumns = false;
        }
        this.expression = expression;
        this.expressionList = list;
        this.expressionQuery = query;
    }

    public static IndexCondition get(int i, ExpressionColumn expressionColumn, Expression expression) {
        return new IndexCondition(i, expressionColumn, null, expression, null, null);
    }

    public static IndexCondition getInList(ExpressionColumn expressionColumn, List<Expression> list) {
        return new IndexCondition(10, expressionColumn, null, null, list, null);
    }

    public static IndexCondition getCompoundInList(ExpressionList expressionList, List<Expression> list) {
        int subexpressionCount = expressionList.getSubexpressionCount();
        Column[] columnArr = new Column[subexpressionCount];
        int i = subexpressionCount;
        while (true) {
            i--;
            if (i >= 0) {
                columnArr[i] = ((ExpressionColumn) expressionList.getSubexpression(i)).getColumn();
            } else {
                return new IndexCondition(10, null, columnArr, null, list, null);
            }
        }
    }

    public static IndexCondition getInArray(ExpressionColumn expressionColumn, Expression expression) {
        return new IndexCondition(11, expressionColumn, null, expression, null, null);
    }

    public static IndexCondition getInQuery(ExpressionColumn expressionColumn, Query query) {
        if ($assertionsDisabled || query.isRandomAccessResult()) {
            return new IndexCondition(12, expressionColumn, null, null, null, query);
        }
        throw new AssertionError();
    }

    public Value getCurrentValue(SessionLocal sessionLocal) {
        return this.expression.getValue(sessionLocal);
    }

    public Value[] getCurrentValueList(SessionLocal sessionLocal) {
        TreeSet treeSet = new TreeSet(sessionLocal.getDatabase().getCompareMode());
        if (this.compareType == 10) {
            if (isCompoundColumns()) {
                Column[] columns = getColumns();
                Iterator<Expression> it = this.expressionList.iterator();
                while (it.hasNext()) {
                    treeSet.add(Column.convert(sessionLocal, columns, (ValueRow) it.next().getValue(sessionLocal)));
                }
            } else {
                Column column = getColumn();
                Iterator<Expression> it2 = this.expressionList.iterator();
                while (it2.hasNext()) {
                    treeSet.add(column.convert(sessionLocal, it2.next().getValue(sessionLocal)));
                }
            }
        } else if (this.compareType == 11) {
            Value value = this.expression.getValue(sessionLocal);
            if (value instanceof ValueArray) {
                for (Value value2 : ((ValueArray) value).getList()) {
                    treeSet.add(value2);
                }
            }
        } else {
            throw DbException.getInternalError("compareType = " + this.compareType);
        }
        Value[] valueArr = (Value[]) treeSet.toArray(new Value[treeSet.size()]);
        Arrays.sort(valueArr, sessionLocal.getDatabase().getCompareMode());
        return valueArr;
    }

    public ResultInterface getCurrentResult() {
        return this.expressionQuery.query(0L);
    }

    public String getSQL(int i) {
        if (this.compareType == 9) {
            return "FALSE";
        }
        StringBuilder sb = new StringBuilder();
        return (isCompoundColumns() ? buildSql(i, sb) : buildSql(i, getColumn(), sb)).toString();
    }

    private StringBuilder buildSql(int i, StringBuilder sb) {
        if (this.compareType == 10) {
            sb.append(" IN(");
            int size = this.expressionList.size();
            for (int i2 = 0; i2 < size; i2++) {
                if (i2 > 0) {
                    sb.append(", ");
                }
                sb.append(this.expressionList.get(i2).getSQL(i));
            }
            return sb.append(')');
        }
        throw DbException.getInternalError("Multiple columns can only be used with compound IN lists.");
    }

    private StringBuilder buildSql(int i, Column column, StringBuilder sb) {
        String str;
        column.getSQL(sb, i);
        switch (this.compareType) {
            case 0:
                sb.append(" = ");
                break;
            case 1:
            case 7:
            case 9:
            default:
                throw DbException.getInternalError("type=" + this.compareType);
            case 2:
                sb.append(" < ");
                break;
            case 3:
                sb.append(" > ");
                break;
            case 4:
                sb.append(" <= ");
                break;
            case 5:
                sb.append(" >= ");
                break;
            case 6:
                if (this.expression.isNullConstant() || (column.getType().getValueType() == 8 && this.expression.isConstant())) {
                    str = " IS ";
                } else {
                    str = " IS NOT DISTINCT FROM ";
                }
                sb.append(str);
                break;
            case 8:
                sb.append(" && ");
                break;
            case 10:
                Expression.writeExpressions(sb.append(" IN("), this.expressionList, i).append(')');
                break;
            case 11:
                return this.expression.getSQL(sb.append(" = ANY("), i, 0).append(')');
            case 12:
                sb.append(" IN(");
                this.expressionQuery.getPlanSQL(sb, i);
                sb.append(')');
                break;
        }
        if (this.expression != null) {
            this.expression.getSQL(sb, i, 0);
        }
        return sb;
    }

    public int getMask(ArrayList<IndexCondition> arrayList) {
        switch (this.compareType) {
            case 0:
            case 6:
                return 1;
            case 1:
            case 7:
            default:
                throw DbException.getInternalError("type=" + this.compareType);
            case 2:
            case 4:
                return 4;
            case 3:
            case 5:
                return 2;
            case 8:
                return 16;
            case 9:
                return 8;
            case 10:
            case 11:
            case 12:
                if (arrayList.size() > 1) {
                    if (isCompoundColumns()) {
                        Column[] columns = getColumns();
                        int length = columns.length;
                        do {
                            length--;
                            if (length < 0) {
                                return 1;
                            }
                        } while (TableType.TABLE == columns[length].getTable().getTableType());
                        return 0;
                    }
                    if (TableType.TABLE != getColumn().getTable().getTableType()) {
                        return 0;
                    }
                    return 1;
                }
                return 1;
        }
    }

    public boolean isAlwaysFalse() {
        return this.compareType == 9;
    }

    public boolean isStart() {
        switch (this.compareType) {
            case 0:
            case 3:
            case 5:
            case 6:
                return true;
            case 1:
            case 2:
            case 4:
            default:
                return false;
        }
    }

    public boolean isEnd() {
        switch (this.compareType) {
            case 0:
            case 2:
            case 4:
            case 6:
                return true;
            case 1:
            case 3:
            case 5:
            default:
                return false;
        }
    }

    public boolean isSpatialIntersects() {
        switch (this.compareType) {
            case 8:
                return true;
            default:
                return false;
        }
    }

    public int getCompareType() {
        return this.compareType;
    }

    public Column getColumn() {
        if (!isCompoundColumns()) {
            return this.column;
        }
        throw DbException.getInternalError("The getColumn() method cannot be with multiple columns.");
    }

    public Column[] getColumns() {
        if (isCompoundColumns()) {
            return this.columns;
        }
        throw DbException.getInternalError("The getColumns() method cannot be with a single column.");
    }

    public boolean isCompoundColumns() {
        return this.compoundColumns;
    }

    public Expression getExpression() {
        return this.expression;
    }

    public List<Expression> getExpressionList() {
        return this.expressionList;
    }

    public Query getExpressionQuery() {
        return this.expressionQuery;
    }

    public boolean isEvaluatable() {
        if (this.expression != null) {
            return this.expression.isEverything(ExpressionVisitor.EVALUATABLE_VISITOR);
        }
        if (this.expressionList != null) {
            Iterator<Expression> it = this.expressionList.iterator();
            while (it.hasNext()) {
                if (!it.next().isEverything(ExpressionVisitor.EVALUATABLE_VISITOR)) {
                    return false;
                }
            }
            return true;
        }
        return this.expressionQuery.isEverything(ExpressionVisitor.EVALUATABLE_VISITOR);
    }

    public IndexCondition cloneWithIndexColumns(Index index) {
        if (!isCompoundColumns()) {
            throw DbException.getInternalError("The cloneWithColumns() method cannot be with a single column.");
        }
        IndexColumn[] indexColumns = index.getIndexColumns();
        int length = indexColumns.length;
        if (length != this.columns.length) {
            return null;
        }
        int[] iArr = new int[length];
        int i = 0;
        for (int i2 = 0; i2 < length; i2++) {
            if (indexColumns[i2] == null || indexColumns[i2].column == null) {
                return null;
            }
            for (int i3 = 0; i3 < this.columns.length; i3++) {
                if (this.columns[i3] == indexColumns[i2].column) {
                    iArr[i3] = i2;
                    i++;
                }
            }
        }
        if (i != length) {
            return null;
        }
        Column[] columnArr = new Column[length];
        for (int i4 = 0; i4 < length; i4++) {
            columnArr[i4] = this.columns[iArr[i4]];
        }
        ArrayList arrayList = new ArrayList(length);
        for (Expression expression : this.expressionList) {
            if (expression instanceof ValueExpression) {
                arrayList.add(ValueExpression.get(((ValueRow) ((ValueExpression) expression).getValue(null)).cloneWithOrder(iArr)));
            } else if (expression instanceof ExpressionList) {
                arrayList.add(((ExpressionList) expression).cloneWithOrder(iArr));
            } else {
                throw DbException.getInternalError("Unexpected expression type: " + expression.getClass());
            }
        }
        return new IndexCondition(10, null, columnArr, null, arrayList, null);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (!isCompoundColumns()) {
            sb.append("column=").append(this.column);
        } else {
            sb.append("columns=");
            Column.writeColumns(sb, this.columns, 3);
        }
        sb.append(", compareType=");
        return compareTypeToString(sb, this.compareType).append(", expression=").append(this.expression).append(", expressionList=").append(this.expressionList).append(", expressionQuery=").append(this.expressionQuery).toString();
    }

    private static StringBuilder compareTypeToString(StringBuilder sb, int i) {
        boolean z = false;
        if ((i & 1) == 1) {
            z = true;
            sb.append("EQUALITY");
        }
        if ((i & 2) == 2) {
            if (z) {
                sb.append(", ");
            }
            z = true;
            sb.append("START");
        }
        if ((i & 4) == 4) {
            if (z) {
                sb.append(", ");
            }
            z = true;
            sb.append("END");
        }
        if ((i & 8) == 8) {
            if (z) {
                sb.append(", ");
            }
            z = true;
            sb.append("ALWAYS_FALSE");
        }
        if ((i & 16) == 16) {
            if (z) {
                sb.append(", ");
            }
            sb.append("SPATIAL_INTERSECTS");
        }
        return sb;
    }
}
