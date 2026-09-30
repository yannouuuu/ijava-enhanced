package org.h2.value;

/* loaded from: ijava.jar:org/h2/value/ExtTypeInfoNumeric.class */
public final class ExtTypeInfoNumeric extends ExtTypeInfo {
    public static final ExtTypeInfoNumeric DECIMAL = new ExtTypeInfoNumeric();

    private ExtTypeInfoNumeric() {
    }

    @Override // org.h2.util.HasSQL
    public StringBuilder getSQL(StringBuilder sb, int i) {
        return sb.append("DECIMAL");
    }
}
