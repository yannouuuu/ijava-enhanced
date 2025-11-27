package org.h2.api;

import java.sql.Connection;
import java.sql.SQLException;

/* loaded from: ijava.jar:org/h2/api/Aggregate.class */
public interface Aggregate {
    int getInternalType(int[] iArr) throws SQLException;

    void add(Object obj) throws SQLException;

    Object getResult() throws SQLException;

    default void init(Connection connection) throws SQLException {
    }
}
