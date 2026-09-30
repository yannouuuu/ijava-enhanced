package org.h2.util;

/* loaded from: ijava.jar:org/h2/util/CacheHead.class */
public class CacheHead extends CacheObject {
    @Override // org.h2.util.CacheObject
    public boolean canRemove() {
        return false;
    }

    @Override // org.h2.util.CacheObject
    public int getMemory() {
        return 0;
    }
}
