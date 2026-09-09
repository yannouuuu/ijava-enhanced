package org.h2.util;

import org.h2.message.Trace;

/* loaded from: ijava.jar:org/h2/util/CacheWriter.class */
public interface CacheWriter {
    void writeBack(CacheObject cacheObject);

    void flushLog();

    Trace getTrace();
}
