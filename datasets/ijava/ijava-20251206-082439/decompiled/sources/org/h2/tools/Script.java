package org.h2.tools;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import org.h2.util.JdbcUtils;
import org.h2.util.StringUtils;
import org.h2.util.Tool;

/* loaded from: ijava.jar:org/h2/tools/Script.class */
public class Script extends Tool {
    public static void main(String... strArr) throws SQLException {
        new Script().runTool(strArr);
    }

    @Override // org.h2.util.Tool
    public void runTool(String... strArr) throws SQLException {
        String str = null;
        String str2 = "";
        String str3 = "";
        String str4 = "backup.sql";
        String str5 = "";
        String str6 = "";
        int i = 0;
        while (strArr != null && i < strArr.length) {
            String str7 = strArr[i];
            if (str7.equals("-url")) {
                i++;
                str = strArr[i];
            } else if (str7.equals("-user")) {
                i++;
                str2 = strArr[i];
            } else if (str7.equals("-password")) {
                i++;
                str3 = strArr[i];
            } else if (str7.equals("-script")) {
                i++;
                str4 = strArr[i];
            } else if (str7.equals("-options")) {
                StringBuilder sb = new StringBuilder();
                StringBuilder sb2 = new StringBuilder();
                while (true) {
                    i++;
                    if (i >= strArr.length) {
                        break;
                    }
                    String upperEnglish = StringUtils.toUpperEnglish(strArr[i]);
                    if ("SIMPLE".equals(upperEnglish) || upperEnglish.startsWith("NO") || "DROP".equals(upperEnglish)) {
                        sb.append(' ');
                        sb.append(strArr[i]);
                    } else if ("BLOCKSIZE".equals(upperEnglish)) {
                        sb.append(' ');
                        sb.append(strArr[i]);
                        i++;
                        sb.append(' ');
                        sb.append(strArr[i]);
                    } else {
                        sb2.append(' ');
                        sb2.append(strArr[i]);
                    }
                }
                str5 = sb.toString();
                str6 = sb2.toString();
            } else {
                if (str7.equals("-help") || str7.equals("-?")) {
                    showUsage();
                    return;
                }
                showUsageAndThrowUnsupportedOption(str7);
            }
            i++;
        }
        if (str == null) {
            showUsage();
            throw new SQLException("URL not set");
        }
        process(str, str2, str3, str4, str5, str6);
    }

    public static void process(String str, String str2, String str3, String str4, String str5, String str6) throws SQLException {
        Connection connection = JdbcUtils.getConnection(null, str, str2, str3);
        try {
            process(connection, str4, str5, str6);
            if (connection != null) {
                connection.close();
            }
        } catch (Throwable th) {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    public static void process(Connection connection, String str, String str2, String str3) throws SQLException {
        Statement createStatement = connection.createStatement();
        try {
            createStatement.execute("SCRIPT " + str2 + " TO '" + str + "' " + str3);
            if (createStatement != null) {
                createStatement.close();
            }
        } catch (Throwable th) {
            if (createStatement != null) {
                try {
                    createStatement.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }
}
