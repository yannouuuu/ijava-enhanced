package org.h2.util.json;

import java.math.BigDecimal;

/* loaded from: ijava.jar:org/h2/util/json/JSONNumber.class */
public final class JSONNumber extends JSONValue {
    private final BigDecimal value;

    /* JADX INFO: Access modifiers changed from: package-private */
    public JSONNumber(BigDecimal bigDecimal) {
        this.value = bigDecimal;
    }

    @Override // org.h2.util.json.JSONValue
    public void addTo(JSONTarget<?> jSONTarget) {
        jSONTarget.valueNumber(this.value);
    }

    public BigDecimal getBigDecimal() {
        return this.value;
    }
}
