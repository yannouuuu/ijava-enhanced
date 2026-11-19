package org.h2.jdbc;

/* loaded from: ijava.jar:org/h2/jdbc/JdbcException.class */
public interface JdbcException {
    int getErrorCode();

    String getOriginalMessage();

    String getSQL();

    void setSQL(String str);

    String toString();
}
