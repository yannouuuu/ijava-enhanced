package org.h2.mvstore.tx;

import java.util.BitSet;

/* loaded from: ijava.jar:org/h2/mvstore/tx/VersionedBitSet.class */
final class VersionedBitSet extends BitSet {
    private static final long serialVersionUID = 1;
    private long version;

    public long getVersion() {
        return this.version;
    }

    public void setVersion(long j) {
        this.version = j;
    }

    @Override // java.util.BitSet
    public VersionedBitSet clone() {
        return (VersionedBitSet) super.clone();
    }
}
