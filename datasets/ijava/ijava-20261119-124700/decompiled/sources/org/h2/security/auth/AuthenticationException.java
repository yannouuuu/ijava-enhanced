package org.h2.security.auth;

/* loaded from: ijava.jar:org/h2/security/auth/AuthenticationException.class */
public class AuthenticationException extends Exception {
    private static final long serialVersionUID = 1;

    public AuthenticationException() {
    }

    public AuthenticationException(String str) {
        super(str);
    }

    public AuthenticationException(Throwable th) {
        super(th);
    }

    public AuthenticationException(String str, Throwable th) {
        super(str, th);
    }
}
