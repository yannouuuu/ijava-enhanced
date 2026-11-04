package org.h2.mvstore.tx;

import java.util.BitSet;
import org.h2.mvstore.RootReference;

/* loaded from: ijava.jar:org/h2/mvstore/tx/Snapshot.class */
final class Snapshot<K, V> {
    final RootReference<K, V> root;
    final BitSet committingTransactions;

    /* JADX INFO: Access modifiers changed from: package-private */
    public Snapshot(RootReference<K, V> rootReference, BitSet bitSet) {
        this.root = rootReference;
        this.committingTransactions = bitSet;
    }

    public int hashCode() {
        return (31 * ((31 * 1) + this.committingTransactions.hashCode())) + this.root.hashCode();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Snapshot)) {
            return false;
        }
        Snapshot snapshot = (Snapshot) obj;
        return this.committingTransactions == snapshot.committingTransactions && this.root == snapshot.root;
    }
}
