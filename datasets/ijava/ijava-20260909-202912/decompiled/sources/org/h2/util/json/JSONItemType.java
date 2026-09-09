package org.h2.util.json;

/* loaded from: ijava.jar:org/h2/util/json/JSONItemType.class */
public enum JSONItemType {
    VALUE,
    ARRAY,
    OBJECT,
    SCALAR;

    public boolean includes(JSONItemType jSONItemType) {
        if (jSONItemType == null) {
            throw new NullPointerException();
        }
        return this == VALUE || this == jSONItemType;
    }
}
