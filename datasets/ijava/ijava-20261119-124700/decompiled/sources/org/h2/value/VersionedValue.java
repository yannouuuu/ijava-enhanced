package org.h2.value;

/* loaded from: ijava.jar:org/h2/value/VersionedValue.class */
public class VersionedValue<T> {
    public boolean isCommitted() {
        return true;
    }

    public long getOperationId() {
        return 0L;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public T getCurrentValue() {
        return this;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public T getCommittedValue() {
        return this;
    }
}
