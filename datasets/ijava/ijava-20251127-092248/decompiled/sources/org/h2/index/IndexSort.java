package org.h2.index;

/* loaded from: ijava.jar:org/h2/index/IndexSort.class */
public final class IndexSort implements Comparable<IndexSort> {
    public static final int FULLY_SORTED = Integer.MAX_VALUE;
    private final Index index;
    private final int sortedColumns;
    private final boolean reverse;

    public IndexSort(Index index, boolean z) {
        this(index, FULLY_SORTED, z);
    }

    public IndexSort(Index index, int i, boolean z) {
        this.index = index;
        this.sortedColumns = i;
        this.reverse = z;
    }

    public Index getIndex() {
        return this.index;
    }

    public int getSortedColumns() {
        return this.sortedColumns;
    }

    public boolean isReverse() {
        return this.reverse;
    }

    @Override // java.lang.Comparable
    public int compareTo(IndexSort indexSort) {
        return indexSort.sortedColumns - this.sortedColumns;
    }
}
