package org.h2.util;

/* loaded from: ijava.jar:org/h2/util/Utils21.class */
public final class Utils21 {
    public static Thread newVirtualThread(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.setDaemon(true);
        return thread;
    }

    private Utils21() {
    }
}
