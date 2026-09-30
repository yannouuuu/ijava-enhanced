package org.h2.mvstore.tx;

import org.h2.value.VersionedValue;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: ijava.jar:org/h2/mvstore/tx/VersionedValueUncommitted.class */
public class VersionedValueUncommitted<T> extends VersionedValueCommitted<T> {
    private final long operationId;
    private final T committedValue;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !VersionedValueUncommitted.class.desiredAssertionStatus();
    }

    private VersionedValueUncommitted(long j, T t, T t2) {
        super(t);
        if (!$assertionsDisabled && j == 0) {
            throw new AssertionError();
        }
        this.operationId = j;
        this.committedValue = t2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <X> VersionedValue<X> getInstance(long j, X x, X x2) {
        return new VersionedValueUncommitted(j, x, x2);
    }

    @Override // org.h2.value.VersionedValue
    public boolean isCommitted() {
        return false;
    }

    @Override // org.h2.value.VersionedValue
    public long getOperationId() {
        return this.operationId;
    }

    @Override // org.h2.mvstore.tx.VersionedValueCommitted, org.h2.value.VersionedValue
    public T getCommittedValue() {
        return this.committedValue;
    }

    @Override // org.h2.mvstore.tx.VersionedValueCommitted
    public String toString() {
        String versionedValueCommitted = super.toString();
        int transactionId = TransactionStore.getTransactionId(this.operationId);
        long logId = TransactionStore.getLogId(this.operationId);
        T t = this.committedValue;
        return versionedValueCommitted + " " + transactionId + "/" + logId + " " + versionedValueCommitted;
    }
}
