package org.h2.tools;

import java.sql.SQLException;

/* loaded from: ijava.jar:org/h2/tools/SimpleRowSource.class */
public interface SimpleRowSource {
    Object[] readRow() throws SQLException;

    void close();

    void reset() throws SQLException;
}
