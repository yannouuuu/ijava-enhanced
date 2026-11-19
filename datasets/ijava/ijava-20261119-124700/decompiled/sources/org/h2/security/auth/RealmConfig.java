package org.h2.security.auth;

import java.util.ArrayList;
import java.util.List;

/* loaded from: ijava.jar:org/h2/security/auth/RealmConfig.class */
public class RealmConfig implements HasConfigProperties {
    private String name;
    private String validatorClass;
    private List<PropertyConfig> properties;

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getValidatorClass() {
        return this.validatorClass;
    }

    public void setValidatorClass(String str) {
        this.validatorClass = str;
    }

    @Override // org.h2.security.auth.HasConfigProperties
    public List<PropertyConfig> getProperties() {
        if (this.properties == null) {
            this.properties = new ArrayList();
        }
        return this.properties;
    }
}
