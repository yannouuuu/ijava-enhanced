package org.h2.util.json;

/* loaded from: ijava.jar:org/h2/util/json/JSONBoolean.class */
public final class JSONBoolean extends JSONValue {
    public static final JSONBoolean FALSE = new JSONBoolean(false);
    public static final JSONBoolean TRUE = new JSONBoolean(true);
    private final boolean value;

    private JSONBoolean(boolean z) {
        this.value = z;
    }

    @Override // org.h2.util.json.JSONValue
    public void addTo(JSONTarget<?> jSONTarget) {
        if (this.value) {
            jSONTarget.valueTrue();
        } else {
            jSONTarget.valueFalse();
        }
    }

    public boolean getBoolean() {
        return this.value;
    }
}
