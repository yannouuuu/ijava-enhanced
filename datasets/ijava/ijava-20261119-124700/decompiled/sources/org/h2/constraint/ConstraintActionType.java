package org.h2.constraint;

/* loaded from: ijava.jar:org/h2/constraint/ConstraintActionType.class */
public enum ConstraintActionType {
    RESTRICT,
    CASCADE,
    SET_DEFAULT,
    SET_NULL;

    public String getSqlName() {
        if (this == SET_DEFAULT) {
            return "SET DEFAULT";
        }
        if (this == SET_NULL) {
            return "SET NULL";
        }
        return name();
    }
}
