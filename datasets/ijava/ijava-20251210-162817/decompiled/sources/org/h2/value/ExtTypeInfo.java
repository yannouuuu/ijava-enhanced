package org.h2.value;

import org.h2.util.HasSQL;

/* loaded from: ijava.jar:org/h2/value/ExtTypeInfo.class */
public abstract class ExtTypeInfo implements HasSQL {
    public String toString() {
        return getSQL(1);
    }
}
