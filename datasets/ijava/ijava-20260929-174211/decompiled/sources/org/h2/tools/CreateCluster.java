package org.h2.tools;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.h2.jdbc.JdbcConnection;
import org.h2.util.Tool;

/* loaded from: ijava.jar:org/h2/tools/CreateCluster.class */
public class CreateCluster extends Tool {
    public static void main(String... strArr) throws SQLException {
        new CreateCluster().runTool(strArr);
    }

    @Override // org.h2.util.Tool
    public void runTool(String... strArr) throws SQLException {
        String str = null;
        String str2 = null;
        String str3 = "";
        String str4 = "";
        String str5 = null;
        int i = 0;
        while (strArr != null && i < strArr.length) {
            String str6 = strArr[i];
            if (str6.equals("-urlSource")) {
                i++;
                str = strArr[i];
            } else if (str6.equals("-urlTarget")) {
                i++;
                str2 = strArr[i];
            } else if (str6.equals("-user")) {
                i++;
                str3 = strArr[i];
            } else if (str6.equals("-password")) {
                i++;
                str4 = strArr[i];
            } else if (str6.equals("-serverList")) {
                i++;
                str5 = strArr[i];
            } else {
                if (str6.equals("-help") || str6.equals("-?")) {
                    showUsage();
                    return;
                }
                showUsageAndThrowUnsupportedOption(str6);
            }
            i++;
        }
        if (str == null || str2 == null || str5 == null) {
            showUsage();
            throw new SQLException("Source URL, target URL, or server list not set");
        }
        process(str, str2, str3, str4, str5);
    }

    public void execute(String str, String str2, String str3, String str4, String str5) throws SQLException {
        process(str, str2, str3, str4, str5);
    }

    private static void process(String str, String str2, String str3, String str4, String str5) throws SQLException {
        JdbcConnection jdbcConnection = new JdbcConnection(str + ";CLUSTER=''", null, str3, str4, false);
        try {
            Statement createStatement = jdbcConnection.createStatement();
            try {
                createStatement.execute("SET EXCLUSIVE 2");
                try {
                    performTransfer(createStatement, str2, str3, str4, str5);
                    createStatement.execute("SET EXCLUSIVE FALSE");
                    if (createStatement != null) {
                        createStatement.close();
                    }
                    jdbcConnection.close();
                } catch (Throwable th) {
                    createStatement.execute("SET EXCLUSIVE FALSE");
                    throw th;
                }
            } finally {
            }
        } catch (Throwable th2) {
            try {
                jdbcConnection.close();
            } catch (Throwable th3) {
                th2.addSuppressed(th3);
            }
            throw th2;
        }
    }

    private static void performTransfer(Statement statement, String str, String str2, String str3, String str4) throws SQLException {
        JdbcConnection jdbcConnection = new JdbcConnection(str + ";CLUSTER=''", null, str2, str3, false);
        try {
            Statement createStatement = jdbcConnection.createStatement();
            try {
                createStatement.execute("DROP ALL OBJECTS DELETE FILES");
                if (createStatement != null) {
                    createStatement.close();
                }
                jdbcConnection.close();
                try {
                    PipedReader pipedReader = new PipedReader();
                    try {
                        Future<?> startWriter = startWriter(pipedReader, statement);
                        jdbcConnection = new JdbcConnection(str, null, str2, str3, false);
                        try {
                            Statement createStatement2 = jdbcConnection.createStatement();
                            try {
                                RunScript.execute(jdbcConnection, pipedReader);
                                try {
                                    startWriter.get();
                                    statement.executeUpdate("SET CLUSTER '" + str4 + "'");
                                    createStatement2.executeUpdate("SET CLUSTER '" + str4 + "'");
                                    if (createStatement2 != null) {
                                        createStatement2.close();
                                    }
                                    jdbcConnection.close();
                                    pipedReader.close();
                                } catch (InterruptedException e) {
                                    throw new SQLException(e);
                                } catch (ExecutionException e2) {
                                    throw new SQLException(e2.getCause());
                                }
                            } catch (Throwable th) {
                                if (createStatement2 != null) {
                                    try {
                                        createStatement2.close();
                                    } catch (Throwable th2) {
                                        th.addSuppressed(th2);
                                    }
                                }
                                throw th;
                            }
                        } finally {
                        }
                    } finally {
                    }
                } catch (IOException e3) {
                    throw new SQLException(e3);
                }
            } finally {
            }
        } finally {
        }
    }

    private static Future<?> startWriter(PipedReader pipedReader, Statement statement) throws IOException {
        ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(1);
        PipedWriter pipedWriter = new PipedWriter(pipedReader);
        Future<?> submit = newFixedThreadPool.submit(() -> {
            try {
                try {
                    ResultSet executeQuery = statement.executeQuery("SCRIPT");
                    while (executeQuery.next()) {
                        try {
                            pipedWriter.write(executeQuery.getString(1) + "\n");
                        } catch (Throwable th) {
                            if (executeQuery != null) {
                                try {
                                    executeQuery.close();
                                } catch (Throwable th2) {
                                    th.addSuppressed(th2);
                                }
                            }
                            throw th;
                        }
                    }
                    if (executeQuery != null) {
                        executeQuery.close();
                    }
                    if (pipedWriter != null) {
                        pipedWriter.close();
                    }
                } finally {
                }
            } catch (IOException | SQLException e) {
                throw new IllegalStateException("Producing script from the source DB is failing.", e);
            }
        });
        newFixedThreadPool.shutdown();
        return submit;
    }
}
