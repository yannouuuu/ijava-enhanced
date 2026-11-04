package org.h2.security.auth;

import java.util.ArrayList;
import java.util.List;

/* loaded from: ijava.jar:org/h2/security/auth/UserToRolesMapperConfig.class */
public class UserToRolesMapperConfig implements HasConfigProperties {
    private String className;
    private List<PropertyConfig> properties;

    public String getClassName() {
        return this.className;
    }

    public void setClassName(String str) {
        this.className = str;
    }

    @Override // org.h2.security.auth.HasConfigProperties
    public List<PropertyConfig> getProperties() {
        if (this.properties == null) {
            this.properties = new ArrayList();
        }
        return this.properties;
    }
}
