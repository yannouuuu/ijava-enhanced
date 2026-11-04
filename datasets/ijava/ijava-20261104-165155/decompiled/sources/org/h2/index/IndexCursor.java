package org.h2.index;

import java.util.ArrayList;
import org.h2.engine.SessionLocal;
import org.h2.message.DbException;
import org.h2.result.ResultInterface;
import org.h2.result.Row;
import org.h2.result.SearchRow;
import org.h2.table.Column;
import org.h2.table.IndexColumn;
import org.h2.table.Table;
import org.h2.value.Value;
import org.h2.value.ValueNull;
import org.h2.value.ValueRow;

/* loaded from: ijava.jar:org/h2/index/IndexCursor.class */
public class IndexCursor implements Cursor {
    private SessionLocal session;
    private Index index;
    private boolean reverse;
    private Table table;
    private IndexColumn[] indexColumns;
    private boolean alwaysFalse;
    private SearchRow start;
    private SearchRow end;
    private SearchRow intersects;
    private Cursor cursor;
    private Object inColumn;
    private int inListIndex;
    private Value[] inList;
    private ResultInterface inResult;

    public void setIndex(Index index, boolean z) {
        this.index = index;
        this.reverse = z;
        this.table = index.getTable();
        Column[] columns = this.table.getColumns();
        this.indexColumns = new IndexColumn[columns.length];
        IndexColumn[] indexColumns = index.getIndexColumns();
        if (indexColumns != null) {
            int length = columns.length;
            for (int i = 0; i < length; i++) {
                int columnIndex = index.getColumnIndex(columns[i]);
                if (columnIndex >= 0) {
                    this.indexColumns[i] = indexColumns[columnIndex];
                }
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:100:0x01e9, code lost:
    
        if (r7.inColumn == null) goto L106;
     */
    /* JADX WARN: Code restructure failed: missing block: B:101:0x01ec, code lost:
    
        r7.start = r7.table.getTemplateRow();
     */
    /* JADX WARN: Code restructure failed: missing block: B:102:0x01f7, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:103:?, code lost:
    
        return;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void prepare(org.h2.engine.SessionLocal r8, java.util.ArrayList<org.h2.index.IndexCondition> r9) {
        /*
            Method dump skipped, instructions count: 504
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.h2.index.IndexCursor.prepare(org.h2.engine.SessionLocal, java.util.ArrayList):void");
    }

    public void find(SessionLocal sessionLocal, ArrayList<IndexCondition> arrayList) {
        SearchRow searchRow;
        SearchRow searchRow2;
        prepare(sessionLocal, arrayList);
        if (this.inColumn == null && !this.alwaysFalse) {
            if (this.reverse) {
                searchRow = this.end;
                searchRow2 = this.start;
            } else {
                searchRow = this.start;
                searchRow2 = this.end;
            }
            if (this.intersects != null && (this.index instanceof SpatialIndex)) {
                this.cursor = ((SpatialIndex) this.index).findByGeometry(this.session, searchRow, searchRow2, this.reverse, this.intersects);
            } else if (this.index != null) {
                this.cursor = this.index.find(this.session, searchRow, searchRow2, this.reverse);
            }
        }
    }

    private boolean canUseIndexForIn(Column column) {
        if (this.inColumn != null) {
            return false;
        }
        return canUseIndexFor(column);
    }

    private boolean canUseIndexFor(Column column) {
        IndexColumn indexColumn;
        IndexColumn[] indexColumns = this.index.getIndexColumns();
        return indexColumns == null || (indexColumn = indexColumns[0]) == null || indexColumn.column == column;
    }

    private boolean canUseIndexForIn(Column[] columnArr) {
        if (this.inColumn != null) {
            return false;
        }
        return canUseIndexForIn(this.index, columnArr);
    }

    public static boolean canUseIndexForIn(Index index, Column[] columnArr) {
        IndexColumn[] indexColumns = index.getIndexColumns();
        if (indexColumns == null || indexColumns.length != columnArr.length) {
            return false;
        }
        for (int i = 0; i < indexColumns.length; i++) {
            IndexColumn indexColumn = indexColumns[i];
            if (indexColumn != null && indexColumn.column != columnArr[i]) {
                return false;
            }
        }
        return true;
    }

    private SearchRow getSpatialSearchRow(SearchRow searchRow, int i, Value value) {
        if (searchRow == null) {
            searchRow = this.table.getTemplateRow();
        } else if (searchRow.getValue(i) != null) {
            value = value.convertToGeometry(null).getEnvelopeUnion(searchRow.getValue(i).convertToGeometry(null));
        }
        if (i == -1) {
            searchRow.setKey(value == ValueNull.INSTANCE ? Long.MIN_VALUE : value.getLong());
        } else {
            searchRow.setValue(i, value);
        }
        return searchRow;
    }

    private SearchRow getSearchRow(SearchRow searchRow, int i, Value value, boolean z) {
        if (searchRow == null) {
            searchRow = this.table.getTemplateRow();
        } else {
            value = getMax(searchRow.getValue(i), value, z);
        }
        if (i == -1) {
            searchRow.setKey(value == ValueNull.INSTANCE ? Long.MIN_VALUE : value.getLong());
        } else {
            searchRow.setValue(i, value);
        }
        return searchRow;
    }

    private Value getMax(Value value, Value value2, boolean z) {
        if (value == null) {
            return value2;
        }
        if (value2 == null) {
            return value;
        }
        if (value == ValueNull.INSTANCE) {
            return value2;
        }
        if (value2 == ValueNull.INSTANCE) {
            return value;
        }
        int compare = this.session.compare(value, value2);
        if (compare == 0) {
            return value;
        }
        return (compare > 0) == z ? value : value2;
    }

    public boolean isAlwaysFalse() {
        return this.alwaysFalse;
    }

    public SearchRow getStart() {
        return this.start;
    }

    public SearchRow getEnd() {
        return this.end;
    }

    @Override // org.h2.index.Cursor
    public Row get() {
        if (this.cursor == null) {
            return null;
        }
        return this.cursor.get();
    }

    @Override // org.h2.index.Cursor
    public SearchRow getSearchRow() {
        return this.cursor.getSearchRow();
    }

    @Override // org.h2.index.Cursor
    public boolean next() {
        while (true) {
            if (this.cursor == null) {
                nextCursor();
                if (this.cursor == null) {
                    return false;
                }
            }
            if (this.cursor.next()) {
                return true;
            }
            this.cursor = null;
        }
    }

    private void nextCursor() {
        Value convert;
        if (this.inList == null) {
            if (this.inResult == null) {
                return;
            }
            while (this.inResult.next()) {
                Value value = this.inResult.currentRow()[0];
                if (value != ValueNull.INSTANCE) {
                    if (this.inColumn instanceof Column[]) {
                        convert = Column.convert(this.session, (Column[]) this.inColumn, (ValueRow) value);
                    } else {
                        convert = ((Column) this.inColumn).convert(this.session, value);
                    }
                    find(convert);
                    return;
                }
            }
            return;
        }
        while (this.inListIndex < this.inList.length) {
            Value[] valueArr = this.inList;
            int i = this.inListIndex;
            this.inListIndex = i + 1;
            Value value2 = valueArr[i];
            if (value2 != ValueNull.INSTANCE) {
                find(value2);
                return;
            }
        }
    }

    private void find(Value value) {
        if (this.inColumn instanceof Column[]) {
            Column[] columnArr = (Column[]) this.inColumn;
            Value[] list = Column.convert(this.session, columnArr, (ValueRow) value).getList();
            int length = columnArr.length;
            while (true) {
                length--;
                if (length < 0) {
                    break;
                } else {
                    this.start.setValue(columnArr[length].getColumnId(), list[length]);
                }
            }
        } else {
            Column column = (Column) this.inColumn;
            this.start.setValue(column.getColumnId(), column.convert(this.session, value));
        }
        this.cursor = this.index.find(this.session, this.start, this.start, false);
    }

    @Override // org.h2.index.Cursor
    public boolean previous() {
        throw DbException.getInternalError(toString());
    }
}
