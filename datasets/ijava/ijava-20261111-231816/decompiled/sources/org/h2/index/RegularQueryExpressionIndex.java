package org.h2.index;

import java.util.ArrayList;
import java.util.Iterator;
import org.h2.command.query.AllColumnsForPlan;
import org.h2.command.query.Query;
import org.h2.engine.SessionLocal;
import org.h2.expression.Parameter;
import org.h2.result.SearchRow;
import org.h2.result.SortOrder;
import org.h2.table.Column;
import org.h2.table.IndexColumn;
import org.h2.table.QueryExpressionTable;
import org.h2.table.TableFilter;
import org.h2.util.IntArray;
import org.h2.value.Value;

/* loaded from: ijava.jar:org/h2/index/RegularQueryExpressionIndex.class */
public final class RegularQueryExpressionIndex extends QueryExpressionIndex implements SpatialIndex {
    private final int[] indexMasks;
    private final long evaluatedAt;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !RegularQueryExpressionIndex.class.desiredAssertionStatus();
    }

    public RegularQueryExpressionIndex(QueryExpressionTable queryExpressionTable, String str, ArrayList<Parameter> arrayList, SessionLocal sessionLocal, int[] iArr) {
        super(queryExpressionTable, str, arrayList);
        this.indexMasks = iArr;
        Query prepareQueryExpression = sessionLocal.prepareQueryExpression(str, queryExpressionTable.getQueryScope());
        if (iArr != null && prepareQueryExpression.allowGlobalConditions()) {
            prepareQueryExpression = addConditions(queryExpressionTable, str, arrayList, sessionLocal, iArr, prepareQueryExpression);
        }
        prepareQueryExpression.preparePlan();
        this.query = prepareQueryExpression;
        this.evaluatedAt = queryExpressionTable.getTopQuery() == null ? System.nanoTime() : 0L;
    }

    private Query addConditions(QueryExpressionTable queryExpressionTable, String str, ArrayList<Parameter> arrayList, SessionLocal sessionLocal, int[] iArr, Query query) {
        int parameterOffset = queryExpressionTable.getParameterOffset(arrayList);
        IntArray intArray = new IntArray();
        int i = 0;
        for (int i2 = 0; i2 < iArr.length; i2++) {
            int i3 = iArr[i2];
            if (i3 != 0) {
                i++;
                int bitCount = Integer.bitCount(i3);
                for (int i4 = 0; i4 < bitCount; i4++) {
                    intArray.add(i2);
                }
            }
        }
        int size = intArray.size();
        ArrayList arrayList2 = new ArrayList(size);
        int i5 = 0;
        while (i5 < size) {
            int i6 = intArray.get(i5);
            arrayList2.add(queryExpressionTable.getColumn(i6));
            int i7 = iArr[i6];
            if ((i7 & 1) != 0) {
                query.addGlobalCondition(new Parameter(parameterOffset + i5), i6, 6);
                i5++;
            }
            if ((i7 & 2) != 0) {
                query.addGlobalCondition(new Parameter(parameterOffset + i5), i6, 5);
                i5++;
            }
            if ((i7 & 4) != 0) {
                query.addGlobalCondition(new Parameter(parameterOffset + i5), i6, 4);
                i5++;
            }
            if ((i7 & 16) != 0) {
                query.addGlobalCondition(new Parameter(parameterOffset + i5), i6, 8);
                i5++;
            }
        }
        this.columns = (Column[]) arrayList2.toArray(new Column[0]);
        this.indexColumns = new IndexColumn[i];
        this.columnIds = new int[i];
        int i8 = 0;
        for (int i9 = 0; i9 < 2; i9++) {
            for (int i10 = 0; i10 < iArr.length; i10++) {
                int i11 = iArr[i10];
                if (i11 != 0) {
                    if (i9 == 0) {
                        if ((i11 & 1) == 0) {
                        }
                        Column column = queryExpressionTable.getColumn(i10);
                        this.indexColumns[i8] = new IndexColumn(column);
                        this.columnIds[i8] = column.getColumnId();
                        i8++;
                    } else {
                        if ((i11 & 1) != 0) {
                        }
                        Column column2 = queryExpressionTable.getColumn(i10);
                        this.indexColumns[i8] = new IndexColumn(column2);
                        this.columnIds[i8] = column2.getColumnId();
                        i8++;
                    }
                }
            }
        }
        String planSQL = query.getPlanSQL(0);
        if (!planSQL.equals(str)) {
            query = sessionLocal.prepareQueryExpression(planSQL, queryExpressionTable.getQueryScope());
        }
        return query;
    }

    @Override // org.h2.index.QueryExpressionIndex
    public boolean isExpired() {
        return this.table.getTopQuery() == null && System.nanoTime() - this.evaluatedAt > 10000000000L;
    }

    @Override // org.h2.index.Index
    public double getCost(SessionLocal sessionLocal, int[] iArr, TableFilter[] tableFilterArr, int i, SortOrder sortOrder, AllColumnsForPlan allColumnsForPlan) {
        return this.query.getCost();
    }

    @Override // org.h2.index.Index
    public Cursor find(SessionLocal sessionLocal, SearchRow searchRow, SearchRow searchRow2, boolean z) {
        return find(sessionLocal, searchRow, searchRow2, (SearchRow) null);
    }

    @Override // org.h2.index.SpatialIndex
    public Cursor findByGeometry(SessionLocal sessionLocal, SearchRow searchRow, SearchRow searchRow2, boolean z, SearchRow searchRow3) {
        if ($assertionsDisabled || !z) {
            return find(sessionLocal, searchRow, searchRow2, searchRow3);
        }
        throw new AssertionError();
    }

    private Cursor find(SessionLocal sessionLocal, SearchRow searchRow, SearchRow searchRow2, SearchRow searchRow3) {
        int i;
        ArrayList<Parameter> parameters = this.query.getParameters();
        if (this.originalParameters != null) {
            Iterator<Parameter> it = this.originalParameters.iterator();
            while (it.hasNext()) {
                Parameter next = it.next();
                if (next != null) {
                    setParameter(parameters, next.getIndex(), next.getValue(sessionLocal));
                }
            }
        }
        if (searchRow != null) {
            i = searchRow.getColumnCount();
        } else if (searchRow2 != null) {
            i = searchRow2.getColumnCount();
        } else if (searchRow3 != null) {
            i = searchRow3.getColumnCount();
        } else {
            i = 0;
        }
        int parameterOffset = this.table.getParameterOffset(this.originalParameters);
        for (int i2 = 0; i2 < i; i2++) {
            int i3 = this.indexMasks[i2];
            if ((i3 & 1) != 0) {
                int i4 = parameterOffset;
                parameterOffset++;
                setParameter(parameters, i4, searchRow.getValue(i2));
            }
            if ((i3 & 2) != 0) {
                int i5 = parameterOffset;
                parameterOffset++;
                setParameter(parameters, i5, searchRow.getValue(i2));
            }
            if ((i3 & 4) != 0) {
                int i6 = parameterOffset;
                parameterOffset++;
                setParameter(parameters, i6, searchRow2.getValue(i2));
            }
            if ((i3 & 16) != 0) {
                int i7 = parameterOffset;
                parameterOffset++;
                setParameter(parameters, i7, searchRow3.getValue(i2));
            }
        }
        return new QueryExpressionCursor(this, this.query.query(0L), searchRow, searchRow2);
    }

    private static void setParameter(ArrayList<Parameter> arrayList, int i, Value value) {
        if (i >= arrayList.size()) {
            return;
        }
        arrayList.get(i).setValue(value);
    }
}
