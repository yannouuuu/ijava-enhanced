package org.h2.mvstore;

/* loaded from: ijava.jar:org/h2/mvstore/MVStoreException.class */
public class MVStoreException extends RuntimeException {
    private static final long serialVersionUID = 2847042930249663807L;
    private final int errorCode;

    public MVStoreException(int i, String str) {
        super(str);
        this.errorCode = i;
    }

    public int getErrorCode() {
        return this.errorCode;
    }
}
