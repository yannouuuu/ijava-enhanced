package org.h2.result;

import org.h2.value.Value;

/* loaded from: ijava.jar:org/h2/result/ResultTarget.class */
public interface ResultTarget {
    void addRow(Value... valueArr);

    long getRowCount();

    void limitsWereApplied();
}
