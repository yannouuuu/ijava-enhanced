package org.h2.result;

import org.h2.value.Value;
import org.h2.value.ValueBigint;
import org.h2.value.ValueNull;

/* loaded from: ijava.jar:org/h2/result/SimpleRowValue.class */
public class SimpleRowValue extends SearchRow {
    private int index;
    private final int virtualColumnCount;
    private Value data;

    public SimpleRowValue(int i) {
        this.virtualColumnCount = i;
    }

    public SimpleRowValue(int i, int i2) {
        this.virtualColumnCount = i;
        this.index = i2;
    }

    @Override // org.h2.result.SearchRow
    public int getColumnCount() {
        return this.virtualColumnCount;
    }

    @Override // org.h2.result.SearchRow
    public Value getValue(int i) {
        if (i == -1) {
            return ValueBigint.get(getKey());
        }
        if (i == this.index) {
            return this.data;
        }
        return null;
    }

    @Override // org.h2.result.SearchRow
    public void setValue(int i, Value value) {
        if (i == -1) {
            setKey(value.getLong());
        }
        this.index = i;
        this.data = value;
    }

    @Override // org.h2.value.Value
    public String toString() {
        long j = this.key;
        if (this.data != null) {
            this.data.getTraceSQL();
        }
        return "( /* " + j + " */ " + j + " )";
    }

    @Override // org.h2.result.SearchRow, org.h2.value.Value
    public int getMemory() {
        return 40 + (this.data == null ? 0 : this.data.getMemory());
    }

    @Override // org.h2.result.SearchRow
    public boolean isNull(int i) {
        return i != this.index || this.data == null || this.data == ValueNull.INSTANCE;
    }

    @Override // org.h2.result.SearchRow
    public void copyFrom(SearchRow searchRow) {
        setKey(searchRow.getKey());
        setValue(this.index, searchRow.getValue(this.index));
    }
}
