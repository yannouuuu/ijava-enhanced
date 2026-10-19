package org.h2.server;

import java.sql.SQLException;

/* loaded from: ijava.jar:org/h2/server/Service.class */
public interface Service {
    void init(String... strArr) throws Exception;

    String getURL();

    void start() throws SQLException;

    void listen();

    void stop();

    boolean isRunning(boolean z);

    boolean getAllowOthers();

    String getName();

    String getType();

    int getPort();

    boolean isDaemon();
}
