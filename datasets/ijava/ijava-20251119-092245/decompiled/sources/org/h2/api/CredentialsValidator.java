package org.h2.api;

import org.h2.security.auth.AuthenticationInfo;
import org.h2.security.auth.Configurable;

/* loaded from: ijava.jar:org/h2/api/CredentialsValidator.class */
public interface CredentialsValidator extends Configurable {
    boolean validateCredentials(AuthenticationInfo authenticationInfo) throws Exception;
}
