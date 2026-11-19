package org.h2.security.auth;

/* loaded from: ijava.jar:org/h2/security/auth/AuthenticatorFactory.class */
public class AuthenticatorFactory {
    public static Authenticator createAuthenticator() {
        return DefaultAuthenticator.getInstance();
    }
}
