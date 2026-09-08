package org.h2.jmx;

/* loaded from: ijava.jar:org/h2/jmx/DatabaseInfoMBean.class */
public interface DatabaseInfoMBean {
    boolean isExclusive();

    boolean isReadOnly();

    String getMode();

    long getFileWriteCount();

    long getFileReadCount();

    long getFileSize();

    int getCacheSizeMax();

    void setCacheSizeMax(int i);

    int getCacheSize();

    String getVersion();

    int getTraceLevel();

    void setTraceLevel(int i);

    String listSettings();

    String listSessions();
}
