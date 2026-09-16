package org.h2.expression.analysis;

/* loaded from: ijava.jar:org/h2/expression/analysis/WindowFrameUnits.class */
public enum WindowFrameUnits {
    ROWS,
    RANGE,
    GROUPS;

    public String getSQL() {
        return name();
    }
}
