package ijava2.clitools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/* loaded from: ijava.jar:ijava2/clitools/ProgressManager.class */
public class ProgressManager {
    private static final String DB_URL = "jdbc:h2:~/.ijava2/progress";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";
    private static final int SCHEMA_VERSION = 1;
    private Connection connection;

    public ProgressManager() {
        try {
            Class.forName("org.h2.Driver");
            this.connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            initializeSchema();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("H2 Database driver not found. Ensure h2.jar is in classpath.", e);
        } catch (SQLException e2) {
            throw new RuntimeException("Failed to initialize database connection", e2);
        }
    }

    private void initializeSchema() throws SQLException {
        Statement createStatement = this.connection.createStatement();
        try {
            createStatement.executeUpdate("    CREATE TABLE IF NOT EXISTS sessions (\n        id INTEGER PRIMARY KEY AUTO_INCREMENT,\n        session_name VARCHAR(50) UNIQUE NOT NULL,\n        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP\n    )\n");
            createStatement.executeUpdate("    CREATE TABLE IF NOT EXISTS exercises (\n        id INTEGER PRIMARY KEY AUTO_INCREMENT,\n        session_name VARCHAR(50) NOT NULL,\n        exercise_name VARCHAR(100) NOT NULL,\n        exercise_type VARCHAR(20) NOT NULL,\n        skills VARCHAR(500),\n        UNIQUE(session_name, exercise_name)\n    )\n");
            createStatement.executeUpdate("    CREATE TABLE IF NOT EXISTS exercise_progress (\n        id INTEGER PRIMARY KEY AUTO_INCREMENT,\n        session_name VARCHAR(50) NOT NULL,\n        exercise_name VARCHAR(100) NOT NULL,\n        status VARCHAR(20) NOT NULL,\n        student_score INTEGER DEFAULT 0,\n        professor_score INTEGER DEFAULT 0,\n        global_score INTEGER DEFAULT 0,\n        compilation_count INTEGER DEFAULT 0,\n        compilation_failures INTEGER DEFAULT 0,\n        execution_count INTEGER DEFAULT 0,\n        test_count INTEGER DEFAULT 0,\n        init_timestamp TIMESTAMP NULL,\n        completion_timestamp TIMESTAMP NULL,\n        last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n        UNIQUE(session_name, exercise_name)\n    )\n");
            createStatement.executeUpdate("ALTER TABLE exercise_progress ADD COLUMN IF NOT EXISTS qcm_answers VARCHAR(2000)");
            createStatement.executeUpdate("ALTER TABLE exercise_progress ADD COLUMN IF NOT EXISTS qcm_total_questions INTEGER");
            createStatement.executeUpdate("ALTER TABLE exercise_progress ADD COLUMN IF NOT EXISTS extra_active_ms BIGINT DEFAULT 0");
            createStatement.executeUpdate("    CREATE TABLE IF NOT EXISTS course_topics (\n        id INTEGER PRIMARY KEY AUTO_INCREMENT,\n        topic_key VARCHAR(100) UNIQUE NOT NULL,\n        title VARCHAR(200) NOT NULL,\n        level INTEGER DEFAULT 1,\n        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP\n    )\n");
            createStatement.executeUpdate("    CREATE TABLE IF NOT EXISTS course_content (\n        id INTEGER PRIMARY KEY AUTO_INCREMENT,\n        topic_id INTEGER NOT NULL,\n        section_name VARCHAR(100) NOT NULL,\n        content_type VARCHAR(20) NOT NULL,\n        content TEXT NOT NULL,\n        order_index INTEGER DEFAULT 0,\n        FOREIGN KEY (topic_id) REFERENCES course_topics(id)\n    )\n");
            createStatement.executeUpdate("    CREATE TABLE IF NOT EXISTS sync_metadata (\n        key_name VARCHAR(100) PRIMARY KEY,\n        value_data VARCHAR(500),\n        last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP\n    )\n");
            createStatement.executeUpdate("CREATE INDEX IF NOT EXISTS idx_exercise_progress_session ON exercise_progress(session_name)");
            createStatement.executeUpdate("CREATE INDEX IF NOT EXISTS idx_course_content_topic ON course_content(topic_id, order_index)");
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

    public void populateExercisesFromProgression() {
        try {
            int i = 0;
            for (String str : getExistingStudentSessions()) {
                List<String> progressionOrder = getProgressionOrder(str);
                if (!progressionOrder.isEmpty()) {
                    ensureSessionExists(str);
                    for (String str2 : progressionOrder) {
                        ensureExerciseExists(str, str2, str2.startsWith("QCM") ? "qcm" : "java");
                        i++;
                    }
                }
            }
            if (i > 0) {
                System.out.println("✅ Populated " + i + " exercises from progression files");
            } else {
                System.out.println("ℹ️ No student sessions found in ~/ijava2 directory");
            }
        } catch (Exception e) {
            System.err.println("⚠️ Warning: Could not populate exercises from progression files: " + e.getMessage());
        }
    }

    private List<String> getExistingStudentSessions() {
        ArrayList arrayList = new ArrayList();
        try {
            Path resolve = Paths.get(System.getProperty("user.home"), new String[0]).resolve("ijava2");
            if (Files.exists(resolve, new LinkOption[0]) && Files.isDirectory(resolve, new LinkOption[0])) {
                DirectoryStream<Path> newDirectoryStream = Files.newDirectoryStream(resolve, "tp*");
                try {
                    for (Path path : newDirectoryStream) {
                        if (Files.isDirectory(path, new LinkOption[0])) {
                            arrayList.add(path.getFileName().toString());
                        }
                    }
                    if (newDirectoryStream != null) {
                        newDirectoryStream.close();
                    }
                } finally {
                }
            }
        } catch (IOException e) {
            System.err.println("⚠️ Warning: Could not read ~/ijava2 directory: " + e.getMessage());
        }
        Collections.sort(arrayList);
        return arrayList;
    }

    public void syncWithLogs() {
        try {
            Path resolve = Paths.get(System.getProperty("user.home"), new String[0]).resolve(".ijava2").resolve("logs");
            if (!Files.exists(resolve, new LinkOption[0])) {
                System.out.println("ℹ️ No logs directory found, skipping sync");
                return;
            }
            int i = 0;
            int i2 = 0;
            DirectoryStream<Path> newDirectoryStream = Files.newDirectoryStream(resolve, "*.csv");
            try {
                for (Path path : newDirectoryStream) {
                    if (needsSync(path)) {
                        i2 += processLogFile(path);
                        i++;
                        updateSyncTimestamp(path);
                    }
                }
                if (newDirectoryStream != null) {
                    newDirectoryStream.close();
                }
                if (i > 0) {
                    System.out.println("✅ Synchronized " + i + " log files (" + i2 + " records)");
                    recomputeExerciseCounters();
                    recomputeExtraActiveTime();
                }
            } finally {
            }
        } catch (IOException e) {
            System.err.println("⚠️ Error reading log files: " + e.getMessage());
        }
    }

    private boolean needsSync(Path path) {
        try {
            long millis = Files.getLastModifiedTime(path, new LinkOption[0]).toMillis();
            PreparedStatement prepareStatement = this.connection.prepareStatement("SELECT value_data FROM sync_metadata WHERE key_name = ?");
            try {
                prepareStatement.setString(1, "last_sync_" + path.getFileName().toString());
                ResultSet executeQuery = prepareStatement.executeQuery();
                if (executeQuery.next()) {
                    boolean z = millis > Long.parseLong(executeQuery.getString("value_data"));
                    if (prepareStatement != null) {
                        prepareStatement.close();
                    }
                    return z;
                }
                if (prepareStatement != null) {
                    prepareStatement.close();
                    return true;
                }
                return true;
            } catch (Throwable th) {
                if (prepareStatement != null) {
                    try {
                        prepareStatement.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        } catch (Exception e) {
            return true;
        }
    }

    private int processLogFile(Path path) {
        int i = 0;
        try {
            for (String str : Files.readAllLines(path)) {
                if (!str.startsWith("timestamp") && !str.trim().isEmpty()) {
                    if (processLogEntry(str)) {
                        i++;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("⚠️ Error processing log file " + String.valueOf(path) + ": " + e.getMessage());
        }
        return i;
    }

    private boolean processLogEntry(String str) {
        try {
            List<String> parseCsvLine = parseCsvLine(str);
            if (parseCsvLine.size() < 4) {
                return false;
            }
            String trim = parseCsvLine.get(0).trim();
            String trim2 = parseCsvLine.get(1).trim();
            String trim3 = parseCsvLine.get(2).trim();
            String normalizeExerciseName = normalizeExerciseName(parseCsvLine.get(3).trim());
            if ("test".equals(trim3) && parseCsvLine.size() >= 9) {
                updateTestMetrics(trim2, normalizeExerciseName, "java", Integer.parseInt(parseCsvLine.get(5).trim()), Integer.parseInt(parseCsvLine.get(7).trim()), Integer.parseInt(parseCsvLine.get(8).trim()), trim);
                return true;
            }
            if ("qcm".equals(trim3) && parseCsvLine.size() >= 6) {
                int size = parseCsvLine.size() >= 7 ? 5 : parseCsvLine.size() - 2;
                int size2 = parseCsvLine.size() >= 7 ? 6 : parseCsvLine.size() - 1;
                String trim4 = parseCsvLine.get(4).trim();
                int parseInt = Integer.parseInt(parseCsvLine.get(size).trim());
                int parseInt2 = Integer.parseInt(parseCsvLine.get(size2).trim());
                Integer num = null;
                if (parseCsvLine.size() >= 8) {
                    try {
                        num = Integer.valueOf(Integer.parseInt(parseCsvLine.get(7).trim()));
                    } catch (NumberFormatException e) {
                    }
                }
                updateQcmProgress(trim2, normalizeExerciseName, trim4, parseInt, parseInt2, num, trim);
                return true;
            }
            if ("init".equals(trim3)) {
                updateInitTimestamp(trim2, normalizeExerciseName, trim);
                return true;
            }
            if ("compile".equals(trim3)) {
                updateCompilationMetrics(trim2, normalizeExerciseName, parseCsvLine.size() > 4 && "SUCCESS".equals(parseCsvLine.get(4).trim()), trim);
                return true;
            }
            if ("execute".equals(trim3)) {
                updateExecutionMetrics(trim2, normalizeExerciseName, trim);
                return true;
            }
            return false;
        } catch (Exception e2) {
            return false;
        }
    }

    private void updateExerciseProgress(String str, String str2, String str3, int i, int i2, int i3, String str4) {
        try {
            ensureSessionExists(str);
            ensureExerciseExists(str, str2, str3);
            PreparedStatement prepareStatement = this.connection.prepareStatement("    MERGE INTO exercise_progress (session_name, exercise_name, status, student_score, professor_score, global_score, last_updated)\n    VALUES (?, ?, ?, ?, ?, ?, ?)\n");
            try {
                prepareStatement.setString(1, str);
                prepareStatement.setString(2, str2);
                String str5 = "completed";
                if (str3.equals("java") && i3 < 100) {
                    str5 = "in_progress";
                }
                prepareStatement.setString(3, str5);
                prepareStatement.setInt(4, i);
                prepareStatement.setInt(5, i2);
                prepareStatement.setInt(6, i3);
                prepareStatement.setTimestamp(7, Timestamp.valueOf(LocalDateTime.parse(str4, DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
                prepareStatement.executeUpdate();
                if (prepareStatement != null) {
                    prepareStatement.close();
                }
            } finally {
            }
        } catch (SQLException e) {
            System.err.println("⚠️ Error updating progress for " + str + "/" + str2 + ": " + e.getMessage());
        }
    }

    private void ensureSessionExists(String str) throws SQLException {
        PreparedStatement prepareStatement = this.connection.prepareStatement("MERGE INTO sessions (session_name) KEY(session_name) VALUES (?)");
        try {
            prepareStatement.setString(1, str);
            prepareStatement.executeUpdate();
            if (prepareStatement != null) {
                prepareStatement.close();
            }
        } catch (Throwable th) {
            if (prepareStatement != null) {
                try {
                    prepareStatement.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    private void ensureExerciseExists(String str, String str2, String str3) throws SQLException {
        PreparedStatement prepareStatement = this.connection.prepareStatement("MERGE INTO exercises (session_name, exercise_name, exercise_type) KEY(session_name, exercise_name) VALUES (?, ?, ?)");
        try {
            prepareStatement.setString(1, str);
            prepareStatement.setString(2, str2);
            prepareStatement.setString(3, str3);
            prepareStatement.executeUpdate();
            if (prepareStatement != null) {
                prepareStatement.close();
            }
        } catch (Throwable th) {
            if (prepareStatement != null) {
                try {
                    prepareStatement.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    private void updateTestMetrics(String str, String str2, String str3, int i, int i2, int i3, String str4) {
        try {
            if (!getProgressionOrder(str).contains(str2)) {
                return;
            }
            ensureSessionExists(str);
            ensureExerciseExists(str, str2, str3);
            String str5 = "completed";
            if (str3.equals("java") && i3 < 100) {
                str5 = "in_progress";
            }
            Timestamp valueOf = Timestamp.valueOf(LocalDateTime.parse(str4, DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            int i4 = 0;
            int i5 = 0;
            int i6 = 0;
            int i7 = 0;
            Timestamp timestamp = null;
            Timestamp timestamp2 = null;
            PreparedStatement prepareStatement = this.connection.prepareStatement("    SELECT compilation_count, compilation_failures, execution_count, test_count, init_timestamp, completion_timestamp\n    FROM exercise_progress\n    WHERE session_name = ? AND exercise_name = ?\n");
            try {
                prepareStatement.setString(1, str);
                prepareStatement.setString(2, str2);
                ResultSet executeQuery = prepareStatement.executeQuery();
                if (executeQuery.next()) {
                    i4 = executeQuery.getInt("compilation_count");
                    i5 = executeQuery.getInt("compilation_failures");
                    i6 = executeQuery.getInt("execution_count");
                    i7 = executeQuery.getInt("test_count");
                    timestamp = executeQuery.getTimestamp("init_timestamp");
                    timestamp2 = executeQuery.getTimestamp("completion_timestamp");
                }
                if (prepareStatement != null) {
                    prepareStatement.close();
                }
                int i8 = i7 + 1;
                Timestamp timestamp3 = timestamp2;
                if (str5.equals("completed") && timestamp3 == null) {
                    timestamp3 = valueOf;
                }
                prepareStatement = this.connection.prepareStatement("    MERGE INTO exercise_progress\n    (session_name, exercise_name, status, student_score, professor_score, global_score,\n     compilation_count, compilation_failures, execution_count, test_count,\n     init_timestamp, completion_timestamp, last_updated)\n    KEY(session_name, exercise_name)\n    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)\n");
                try {
                    prepareStatement.setString(1, str);
                    prepareStatement.setString(2, str2);
                    prepareStatement.setString(3, str5);
                    prepareStatement.setInt(4, i);
                    prepareStatement.setInt(5, i2);
                    prepareStatement.setInt(6, i3);
                    prepareStatement.setInt(7, i4);
                    prepareStatement.setInt(8, i5);
                    prepareStatement.setInt(9, i6);
                    prepareStatement.setInt(10, i8);
                    prepareStatement.setTimestamp(11, timestamp);
                    prepareStatement.setTimestamp(12, timestamp3);
                    prepareStatement.setTimestamp(13, valueOf);
                    prepareStatement.executeUpdate();
                    if (prepareStatement != null) {
                        prepareStatement.close();
                    }
                } finally {
                }
            } finally {
                if (prepareStatement != null) {
                    try {
                        prepareStatement.close();
                    } catch (Throwable th) {
                        th.addSuppressed(th);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("⚠️ Error updating test metrics for " + str + "/" + str2 + ": " + e.getMessage());
        }
    }

    private void updateQcmProgress(String str, String str2, String str3, int i, int i2, Integer num, String str4) {
        int intValue;
        try {
            if (!getProgressionOrder(str).contains(str2)) {
                return;
            }
            ensureSessionExists(str);
            ensureExerciseExists(str, str2, "qcm");
            Timestamp valueOf = Timestamp.valueOf(LocalDateTime.parse(str4, DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            int i3 = 0;
            int i4 = 0;
            int i5 = 0;
            int i6 = 0;
            Timestamp timestamp = null;
            Timestamp timestamp2 = null;
            String str5 = null;
            Integer num2 = null;
            PreparedStatement prepareStatement = this.connection.prepareStatement("    SELECT compilation_count, compilation_failures, execution_count, test_count,\n           init_timestamp, completion_timestamp, qcm_answers, qcm_total_questions\n    FROM exercise_progress\n    WHERE session_name = ? AND exercise_name = ?\n");
            try {
                prepareStatement.setString(1, str);
                prepareStatement.setString(2, str2);
                ResultSet executeQuery = prepareStatement.executeQuery();
                if (executeQuery.next()) {
                    i3 = executeQuery.getInt("compilation_count");
                    i4 = executeQuery.getInt("compilation_failures");
                    i5 = executeQuery.getInt("execution_count");
                    i6 = executeQuery.getInt("test_count");
                    timestamp = executeQuery.getTimestamp("init_timestamp");
                    timestamp2 = executeQuery.getTimestamp("completion_timestamp");
                    str5 = executeQuery.getString("qcm_answers");
                    int i7 = executeQuery.getInt("qcm_total_questions");
                    if (!executeQuery.wasNull()) {
                        num2 = Integer.valueOf(i7);
                    }
                }
                if (prepareStatement != null) {
                    prepareStatement.close();
                }
                Map<Integer, Boolean> deserializeQcmAnswers = deserializeQcmAnswers(str5);
                List<String> splitAnswerPairs = splitAnswerPairs(str3);
                if (splitAnswerPairs.size() == 1 && i2 == 1) {
                    Integer parseQuestionIndex = parseQuestionIndex(splitAnswerPairs.get(0));
                    if (parseQuestionIndex != null) {
                        deserializeQcmAnswers.put(parseQuestionIndex, Boolean.valueOf(i > 0));
                    }
                } else if (splitAnswerPairs.size() > 1) {
                    deserializeQcmAnswers.clear();
                    int max = Math.max(0, Math.min(i, i2));
                    int i8 = 0;
                    while (i8 < i2) {
                        deserializeQcmAnswers.put(Integer.valueOf(i8), Boolean.valueOf(i8 < max));
                        i8++;
                    }
                }
                if (num != null) {
                    intValue = num.intValue();
                } else {
                    intValue = num2 != null ? num2.intValue() : Math.max(i2, deserializeQcmAnswers.size());
                }
                int i9 = intValue;
                int i10 = 0;
                Iterator<Boolean> it = deserializeQcmAnswers.values().iterator();
                while (it.hasNext()) {
                    if (it.next().booleanValue()) {
                        i10++;
                    }
                }
                int size = deserializeQcmAnswers.size();
                int round = i9 > 0 ? (int) Math.round((i10 / i9) * 100.0d) : 0;
                boolean z = i9 > 0 && size >= i9;
                String str6 = z ? "completed" : "in_progress";
                Timestamp timestamp3 = null;
                if (z) {
                    timestamp3 = timestamp2 != null ? timestamp2 : valueOf;
                }
                prepareStatement = this.connection.prepareStatement("    MERGE INTO exercise_progress\n    (session_name, exercise_name, status, student_score, professor_score, global_score,\n     compilation_count, compilation_failures, execution_count, test_count,\n     init_timestamp, completion_timestamp, last_updated, qcm_answers, qcm_total_questions)\n    KEY(session_name, exercise_name)\n    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)\n");
                try {
                    prepareStatement.setString(1, str);
                    prepareStatement.setString(2, str2);
                    prepareStatement.setString(3, str6);
                    prepareStatement.setInt(4, round);
                    prepareStatement.setInt(5, round);
                    prepareStatement.setInt(6, round);
                    prepareStatement.setInt(7, i3);
                    prepareStatement.setInt(8, i4);
                    prepareStatement.setInt(9, i5);
                    prepareStatement.setInt(10, i6);
                    prepareStatement.setTimestamp(11, timestamp);
                    prepareStatement.setTimestamp(12, timestamp3);
                    prepareStatement.setTimestamp(13, valueOf);
                    prepareStatement.setString(14, serializeQcmAnswers(deserializeQcmAnswers));
                    prepareStatement.setInt(15, i9);
                    prepareStatement.executeUpdate();
                    if (prepareStatement != null) {
                        prepareStatement.close();
                    }
                } finally {
                    if (prepareStatement != null) {
                        try {
                            prepareStatement.close();
                        } catch (Throwable th) {
                            th.addSuppressed(th);
                        }
                    }
                }
            } finally {
            }
        } catch (SQLException e) {
            System.err.println("⚠️ Error updating QCM progress for " + str + "/" + str2 + ": " + e.getMessage());
        }
    }

    private List<String> splitAnswerPairs(String str) {
        ArrayList arrayList = new ArrayList();
        if (str == null || str.isEmpty()) {
            return arrayList;
        }
        for (String str2 : str.split(",")) {
            if (!str2.trim().isEmpty()) {
                arrayList.add(str2.trim());
            }
        }
        return arrayList;
    }

    private Integer parseQuestionIndex(String str) {
        try {
            if (str.length() <= 1) {
                return null;
            }
            if (str.charAt(0) == 'q' || str.charAt(0) == 'Q') {
                int indexOf = str.indexOf(58);
                return Integer.valueOf(Integer.parseInt((indexOf >= 0 ? str.substring(1, indexOf) : str.substring(1)).trim()));
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    private String serializeQcmAnswers(Map<Integer, Boolean> map) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, Boolean> entry : map.entrySet()) {
            if (sb.length() > 0) {
                sb.append(";");
            }
            sb.append(entry.getKey()).append("=").append(entry.getValue().booleanValue() ? 1 : 0);
        }
        return sb.toString();
    }

    private Map<Integer, Boolean> deserializeQcmAnswers(String str) {
        TreeMap treeMap = new TreeMap();
        if (str == null || str.isEmpty()) {
            return treeMap;
        }
        for (String str2 : str.split(";")) {
            String[] split = str2.split("=");
            if (split.length == 2) {
                try {
                    treeMap.put(Integer.valueOf(Integer.parseInt(split[0].trim())), Boolean.valueOf("1".equals(split[1].trim())));
                } catch (NumberFormatException e) {
                }
            }
        }
        return treeMap;
    }

    private void updateInitTimestamp(String str, String str2, String str3) {
        try {
            if (!getProgressionOrder(str).contains(str2)) {
                return;
            }
            PreparedStatement prepareStatement = this.connection.prepareStatement("SELECT 1 FROM exercise_progress WHERE session_name = ? AND exercise_name = ?");
            try {
                prepareStatement.setString(1, str);
                prepareStatement.setString(2, str2);
                if (prepareStatement.executeQuery().next()) {
                    if (prepareStatement != null) {
                        prepareStatement.close();
                        return;
                    }
                    return;
                }
                if (prepareStatement != null) {
                    prepareStatement.close();
                }
                ensureSessionExists(str);
                ensureExerciseExists(str, str2, str2.startsWith("QCM") ? "qcm" : "java");
                prepareStatement = this.connection.prepareStatement("    MERGE INTO exercise_progress\n    (session_name, exercise_name, status, student_score, professor_score, global_score,\n     compilation_count, compilation_failures, execution_count, test_count,\n     init_timestamp, completion_timestamp, last_updated)\n    KEY(session_name, exercise_name)\n    VALUES (?, ?, 'started', 0, 0, 0, 0, 0, 0, 0, ?, NULL, ?)\n");
                try {
                    Timestamp valueOf = Timestamp.valueOf(LocalDateTime.parse(str3, DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                    prepareStatement.setString(1, str);
                    prepareStatement.setString(2, str2);
                    prepareStatement.setTimestamp(3, valueOf);
                    prepareStatement.setTimestamp(4, valueOf);
                    prepareStatement.executeUpdate();
                    if (prepareStatement != null) {
                        prepareStatement.close();
                    }
                } finally {
                }
            } finally {
                if (prepareStatement != null) {
                    try {
                        prepareStatement.close();
                    } catch (Throwable th) {
                        th.addSuppressed(th);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("⚠️ Error updating init timestamp for " + str + "/" + str2 + ": " + e.getMessage());
        }
    }

    private void updateCompilationMetrics(String str, String str2, boolean z, String str3) {
        try {
            if (!getProgressionOrder(str).contains(str2)) {
                return;
            }
            PreparedStatement prepareStatement = this.connection.prepareStatement("    UPDATE exercise_progress\n    SET compilation_count = compilation_count + 1,\n        compilation_failures = compilation_failures + ?,\n        last_updated = ?\n    WHERE session_name = ? AND exercise_name = ?\n");
            try {
                prepareStatement.setInt(1, z ? 0 : 1);
                prepareStatement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.parse(str3, DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
                prepareStatement.setString(3, str);
                prepareStatement.setString(4, str2);
                prepareStatement.executeUpdate();
                if (prepareStatement != null) {
                    prepareStatement.close();
                }
            } finally {
            }
        } catch (SQLException e) {
            System.err.println("⚠️ Error updating compilation metrics for " + str + "/" + str2 + ": " + e.getMessage());
        }
    }

    private void updateExecutionMetrics(String str, String str2, String str3) {
        try {
            if (!getProgressionOrder(str).contains(str2)) {
                return;
            }
            PreparedStatement prepareStatement = this.connection.prepareStatement("    UPDATE exercise_progress\n    SET execution_count = execution_count + 1,\n        last_updated = ?\n    WHERE session_name = ? AND exercise_name = ?\n");
            try {
                prepareStatement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.parse(str3, DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
                prepareStatement.setString(2, str);
                prepareStatement.setString(3, str2);
                prepareStatement.executeUpdate();
                if (prepareStatement != null) {
                    prepareStatement.close();
                }
            } finally {
            }
        } catch (SQLException e) {
            System.err.println("⚠️ Error updating execution metrics for " + str + "/" + str2 + ": " + e.getMessage());
        }
    }

    private void updateSyncTimestamp(Path path) {
        try {
            long currentTimeMillis = System.currentTimeMillis();
            PreparedStatement prepareStatement = this.connection.prepareStatement("MERGE INTO sync_metadata (key_name, value_data) VALUES (?, ?)");
            try {
                prepareStatement.setString(1, "last_sync_" + path.getFileName().toString());
                prepareStatement.setString(2, String.valueOf(currentTimeMillis));
                prepareStatement.executeUpdate();
                if (prepareStatement != null) {
                    prepareStatement.close();
                }
            } finally {
            }
        } catch (SQLException e) {
            System.err.println("⚠️ Error updating sync timestamp: " + e.getMessage());
        }
    }

    public SessionProgressSummary getSessionProgress(String str) {
        PreparedStatement prepareStatement;
        ResultSet executeQuery;
        try {
            prepareStatement = this.connection.prepareStatement("    SELECT\n        COUNT(*) as total_exercises,\n        COUNT(CASE WHEN ep.status = 'completed' THEN 1 END) as completed_exercises,\n        COUNT(CASE WHEN ep.status = 'in_progress' THEN 1 END) as in_progress_exercises,\n        AVG(CASE WHEN ep.global_score > 0 THEN ep.global_score END) as avg_score\n    FROM exercises e\n    LEFT JOIN exercise_progress ep ON e.session_name = ep.session_name AND e.exercise_name = ep.exercise_name\n    WHERE e.session_name = ?\n");
            try {
                prepareStatement.setString(1, str);
                executeQuery = prepareStatement.executeQuery();
            } finally {
            }
        } catch (SQLException e) {
            System.err.println("⚠️ Error getting session progress: " + e.getMessage());
        }
        if (executeQuery.next()) {
            SessionProgressSummary sessionProgressSummary = new SessionProgressSummary(str, executeQuery.getInt("total_exercises"), executeQuery.getInt("completed_exercises"), executeQuery.getInt("in_progress_exercises"), executeQuery.getDouble("avg_score"));
            if (prepareStatement != null) {
                prepareStatement.close();
            }
            return sessionProgressSummary;
        }
        if (prepareStatement != null) {
            prepareStatement.close();
        }
        return new SessionProgressSummary(str, 0, 0, 0, 0.0d);
    }

    public List<ExerciseProgress> getDetailedSessionProgress(String str) {
        PreparedStatement prepareStatement;
        ArrayList arrayList = new ArrayList();
        try {
            List<String> progressionOrder = getProgressionOrder(str);
            if (progressionOrder.isEmpty()) {
                prepareStatement = this.connection.prepareStatement("    SELECT\n        e.exercise_name,\n        e.exercise_type,\n        COALESCE(ep.status, 'not_started') as status,\n        COALESCE(ep.student_score, 0) as student_score,\n        COALESCE(ep.professor_score, 0) as professor_score,\n        COALESCE(ep.global_score, 0) as global_score,\n        COALESCE(ep.compilation_count, 0) as compilation_count,\n        COALESCE(ep.compilation_failures, 0) as compilation_failures,\n        COALESCE(ep.execution_count, 0) as execution_count,\n        COALESCE(ep.test_count, 0) as test_count,\n        ep.init_timestamp,\n        ep.completion_timestamp,\n        ep.last_updated,\n        COALESCE(ep.extra_active_ms, 0) as extra_active_ms\n    FROM exercises e\n    LEFT JOIN exercise_progress ep ON e.session_name = ep.session_name AND e.exercise_name = ep.exercise_name\n    WHERE e.session_name = ?\n    ORDER BY e.exercise_name\n");
                try {
                    prepareStatement.setString(1, str);
                    ResultSet executeQuery = prepareStatement.executeQuery();
                    while (executeQuery.next()) {
                        arrayList.add(new ExerciseProgress(executeQuery.getString("exercise_name"), executeQuery.getString("exercise_type"), executeQuery.getString("status"), executeQuery.getInt("student_score"), executeQuery.getInt("professor_score"), executeQuery.getInt("global_score"), executeQuery.getInt("compilation_count"), executeQuery.getInt("compilation_failures"), executeQuery.getInt("execution_count"), executeQuery.getInt("test_count"), executeQuery.getTimestamp("init_timestamp"), executeQuery.getTimestamp("completion_timestamp"), executeQuery.getTimestamp("last_updated"), executeQuery.getLong("extra_active_ms")));
                    }
                    if (prepareStatement != null) {
                        prepareStatement.close();
                    }
                } finally {
                }
            } else {
                StringBuilder sb = new StringBuilder("    SELECT\n        e.exercise_name,\n        e.exercise_type,\n        COALESCE(ep.status, 'not_started') as status,\n        COALESCE(ep.student_score, 0) as student_score,\n        COALESCE(ep.professor_score, 0) as professor_score,\n        COALESCE(ep.global_score, 0) as global_score,\n        COALESCE(ep.compilation_count, 0) as compilation_count,\n        COALESCE(ep.compilation_failures, 0) as compilation_failures,\n        COALESCE(ep.execution_count, 0) as execution_count,\n        COALESCE(ep.test_count, 0) as test_count,\n        ep.init_timestamp,\n        ep.completion_timestamp,\n        ep.last_updated,\n        COALESCE(ep.extra_active_ms, 0) as extra_active_ms\n    FROM exercises e\n    LEFT JOIN exercise_progress ep ON e.session_name = ep.session_name AND e.exercise_name = ep.exercise_name\n    WHERE e.session_name = ? AND e.exercise_name IN (\n");
                for (int i = 0; i < progressionOrder.size(); i++) {
                    if (i > 0) {
                        sb.append(", ");
                    }
                    sb.append("?");
                }
                sb.append(")");
                HashMap hashMap = new HashMap();
                prepareStatement = this.connection.prepareStatement(sb.toString());
                try {
                    prepareStatement.setString(1, str);
                    for (int i2 = 0; i2 < progressionOrder.size(); i2++) {
                        prepareStatement.setString(i2 + 2, progressionOrder.get(i2));
                    }
                    ResultSet executeQuery2 = prepareStatement.executeQuery();
                    while (executeQuery2.next()) {
                        ExerciseProgress exerciseProgress = new ExerciseProgress(executeQuery2.getString("exercise_name"), executeQuery2.getString("exercise_type"), executeQuery2.getString("status"), executeQuery2.getInt("student_score"), executeQuery2.getInt("professor_score"), executeQuery2.getInt("global_score"), executeQuery2.getInt("compilation_count"), executeQuery2.getInt("compilation_failures"), executeQuery2.getInt("execution_count"), executeQuery2.getInt("test_count"), executeQuery2.getTimestamp("init_timestamp"), executeQuery2.getTimestamp("completion_timestamp"), executeQuery2.getTimestamp("last_updated"), executeQuery2.getLong("extra_active_ms"));
                        hashMap.put(exerciseProgress.exerciseName, exerciseProgress);
                    }
                    if (prepareStatement != null) {
                        prepareStatement.close();
                    }
                    for (String str2 : progressionOrder) {
                        ExerciseProgress exerciseProgress2 = (ExerciseProgress) hashMap.get(str2);
                        if (exerciseProgress2 != null) {
                            arrayList.add(exerciseProgress2);
                        } else {
                            arrayList.add(new ExerciseProgress(str2, str2.startsWith("QCM") ? "qcm" : "java", "not_started", 0, 0, 0, 0, 0, 0, 0, null, null, null, 0L));
                        }
                    }
                } finally {
                }
            }
        } catch (SQLException e) {
            System.err.println("⚠️ Error getting detailed session progress: " + e.getMessage());
        }
        return arrayList;
    }

    private String normalizeExerciseName(String str) {
        if (str.endsWith(".java")) {
            return str.substring(0, str.length() - 5);
        }
        return str;
    }

    private List<String> getProgressionOrder(String str) {
        ArrayList arrayList = new ArrayList();
        try {
            InputStream resourceAsStream = getClass().getResourceAsStream("/syllabus/" + str + "/" + str + ".progression");
            if (resourceAsStream != null) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resourceAsStream));
                while (true) {
                    try {
                        String readLine = bufferedReader.readLine();
                        if (readLine == null) {
                            break;
                        }
                        String trim = readLine.trim();
                        if (!trim.isEmpty()) {
                            if (trim.contains("|")) {
                                arrayList.add(trim.split("\\|")[1].trim());
                            } else {
                                arrayList.add(trim);
                            }
                        }
                    } finally {
                    }
                }
                bufferedReader.close();
            }
        } catch (Exception e) {
        }
        return arrayList;
    }

    private void recomputeExerciseCounters() {
        try {
            Path resolve = Paths.get(System.getProperty("user.home"), new String[0]).resolve(".ijava2").resolve("logs");
            if (!Files.exists(resolve, new LinkOption[0])) {
                return;
            }
            HashMap hashMap = new HashMap();
            HashMap hashMap2 = new HashMap();
            DirectoryStream<Path> newDirectoryStream = Files.newDirectoryStream(resolve, "*.csv");
            try {
                Iterator<Path> it = newDirectoryStream.iterator();
                while (it.hasNext()) {
                    try {
                        for (String str : Files.readAllLines(it.next())) {
                            if (!str.startsWith("timestamp") && !str.trim().isEmpty()) {
                                List<String> parseCsvLine = parseCsvLine(str);
                                if (parseCsvLine.size() >= 4) {
                                    String trim = parseCsvLine.get(2).trim();
                                    if ("init".equals(trim) || "compile".equals(trim) || "execute".equals(trim) || "test".equals(trim) || "qcm".equals(trim)) {
                                        try {
                                            Timestamp valueOf = Timestamp.valueOf(LocalDateTime.parse(parseCsvLine.get(0).trim(), DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                                            String str2 = parseCsvLine.get(1).trim() + "/" + normalizeExerciseName(parseCsvLine.get(3).trim());
                                            int[] iArr = (int[]) hashMap.computeIfAbsent(str2, str3 -> {
                                                return new int[4];
                                            });
                                            if ("compile".equals(trim)) {
                                                iArr[0] = iArr[0] + 1;
                                                if (!(parseCsvLine.size() > 4 && "SUCCESS".equals(parseCsvLine.get(4).trim()))) {
                                                    iArr[1] = iArr[1] + 1;
                                                }
                                            } else if ("execute".equals(trim)) {
                                                iArr[2] = iArr[2] + 1;
                                            } else if ("test".equals(trim) || "qcm".equals(trim)) {
                                                iArr[3] = iArr[3] + 1;
                                            }
                                            Timestamp timestamp = (Timestamp) hashMap2.get(str2);
                                            if (timestamp == null || valueOf.after(timestamp)) {
                                                hashMap2.put(str2, valueOf);
                                            }
                                        } catch (Exception e) {
                                        }
                                    }
                                }
                            }
                        }
                    } catch (Exception e2) {
                    }
                }
                if (newDirectoryStream != null) {
                    newDirectoryStream.close();
                }
                if (hashMap.isEmpty()) {
                    return;
                }
                PreparedStatement prepareStatement = this.connection.prepareStatement("    UPDATE exercise_progress\n    SET compilation_count = ?, compilation_failures = ?, execution_count = ?, test_count = ?,\n        last_updated = ?\n    WHERE session_name = ? AND exercise_name = ?\n");
                try {
                    for (Map.Entry entry : hashMap.entrySet()) {
                        String str4 = (String) entry.getKey();
                        int indexOf = str4.indexOf(47);
                        String substring = str4.substring(0, indexOf);
                        String substring2 = str4.substring(indexOf + 1);
                        int[] iArr2 = (int[]) entry.getValue();
                        prepareStatement.setInt(1, iArr2[0]);
                        prepareStatement.setInt(2, iArr2[1]);
                        prepareStatement.setInt(3, iArr2[2]);
                        prepareStatement.setInt(4, iArr2[3]);
                        prepareStatement.setTimestamp(5, (Timestamp) hashMap2.get(str4));
                        prepareStatement.setString(6, substring);
                        prepareStatement.setString(7, substring2);
                        prepareStatement.addBatch();
                    }
                    prepareStatement.executeBatch();
                    if (prepareStatement != null) {
                        prepareStatement.close();
                    }
                } finally {
                }
            } finally {
            }
        } catch (Exception e3) {
            System.err.println("⚠️ Error recomputing exercise counters: " + e3.getMessage());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: ijava.jar:ijava2/clitools/ProgressManager$ActivityEvent.class */
    public static final class ActivityEvent extends Record {
        private final Timestamp timestamp;
        private final String exerciseKey;

        private ActivityEvent(Timestamp timestamp, String exerciseKey) {
            this.timestamp = timestamp;
            this.exerciseKey = exerciseKey;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, ActivityEvent.class), ActivityEvent.class, "timestamp;exerciseKey", "FIELD:Lijava2/clitools/ProgressManager$ActivityEvent;->timestamp:Ljava/sql/Timestamp;", "FIELD:Lijava2/clitools/ProgressManager$ActivityEvent;->exerciseKey:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, ActivityEvent.class), ActivityEvent.class, "timestamp;exerciseKey", "FIELD:Lijava2/clitools/ProgressManager$ActivityEvent;->timestamp:Ljava/sql/Timestamp;", "FIELD:Lijava2/clitools/ProgressManager$ActivityEvent;->exerciseKey:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object obj) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, ActivityEvent.class, Object.class), ActivityEvent.class, "timestamp;exerciseKey", "FIELD:Lijava2/clitools/ProgressManager$ActivityEvent;->timestamp:Ljava/sql/Timestamp;", "FIELD:Lijava2/clitools/ProgressManager$ActivityEvent;->exerciseKey:Ljava/lang/String;").dynamicInvoker().invoke(this, obj) /* invoke-custom */;
        }

        public Timestamp timestamp() {
            return this.timestamp;
        }

        public String exerciseKey() {
            return this.exerciseKey;
        }
    }

    private void recomputeExtraActiveTime() {
        try {
            HashMap hashMap = new HashMap();
            Statement createStatement = this.connection.createStatement();
            try {
                ResultSet executeQuery = createStatement.executeQuery("SELECT session_name, exercise_name, completion_timestamp FROM exercise_progress WHERE completion_timestamp IS NOT NULL");
                while (executeQuery.next()) {
                    try {
                        hashMap.put(executeQuery.getString("session_name") + "/" + executeQuery.getString("exercise_name"), executeQuery.getTimestamp("completion_timestamp"));
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
                if (createStatement != null) {
                    createStatement.close();
                }
                if (hashMap.isEmpty()) {
                    return;
                }
                Path resolve = Paths.get(System.getProperty("user.home"), new String[0]).resolve(".ijava2").resolve("logs");
                if (!Files.exists(resolve, new LinkOption[0])) {
                    return;
                }
                ArrayList<ActivityEvent> arrayList = new ArrayList();
                DirectoryStream<Path> newDirectoryStream = Files.newDirectoryStream(resolve, "*.csv");
                try {
                    Iterator<Path> it = newDirectoryStream.iterator();
                    while (it.hasNext()) {
                        try {
                            for (String str : Files.readAllLines(it.next())) {
                                if (!str.startsWith("timestamp") && !str.trim().isEmpty()) {
                                    List<String> parseCsvLine = parseCsvLine(str);
                                    if (parseCsvLine.size() >= 3) {
                                        try {
                                            Timestamp valueOf = Timestamp.valueOf(LocalDateTime.parse(parseCsvLine.get(0).trim(), DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                                            String trim = parseCsvLine.get(1).trim();
                                            String trim2 = parseCsvLine.get(2).trim();
                                            if ("stop".equals(trim2)) {
                                                arrayList.add(new ActivityEvent(valueOf, null));
                                            } else if (parseCsvLine.size() >= 4 && ("init".equals(trim2) || "compile".equals(trim2) || "execute".equals(trim2) || "test".equals(trim2) || "qcm".equals(trim2))) {
                                                arrayList.add(new ActivityEvent(valueOf, trim + "/" + normalizeExerciseName(parseCsvLine.get(3).trim())));
                                            }
                                        } catch (Exception e) {
                                        }
                                    }
                                }
                            }
                        } catch (Exception e2) {
                        }
                    }
                    if (newDirectoryStream != null) {
                        newDirectoryStream.close();
                    }
                    if (arrayList.size() < 2) {
                        return;
                    }
                    arrayList.sort(Comparator.comparing((v0) -> {
                        return v0.timestamp();
                    }));
                    Map<String, Long> hashMap2 = new HashMap<>();
                    String str2 = null;
                    Timestamp timestamp = null;
                    Timestamp timestamp2 = null;
                    for (ActivityEvent activityEvent : arrayList) {
                        String exerciseKey = activityEvent.exerciseKey();
                        if (exerciseKey == null) {
                            finalizeRun(str2, timestamp, timestamp2, hashMap, hashMap2);
                            str2 = null;
                            timestamp = null;
                            timestamp2 = null;
                        } else if (exerciseKey.equals(str2)) {
                            timestamp2 = activityEvent.timestamp();
                        } else {
                            finalizeRun(str2, timestamp, timestamp2, hashMap, hashMap2);
                            str2 = exerciseKey;
                            timestamp = activityEvent.timestamp();
                            timestamp2 = activityEvent.timestamp();
                        }
                    }
                    finalizeRun(str2, timestamp, timestamp2, hashMap, hashMap2);
                    PreparedStatement prepareStatement = this.connection.prepareStatement("UPDATE exercise_progress SET extra_active_ms = ? WHERE session_name = ? AND exercise_name = ?");
                    try {
                        for (String str3 : hashMap.keySet()) {
                            int indexOf = str3.indexOf(47);
                            String substring = str3.substring(0, indexOf);
                            String substring2 = str3.substring(indexOf + 1);
                            prepareStatement.setLong(1, hashMap2.getOrDefault(str3, 0L).longValue());
                            prepareStatement.setString(2, substring);
                            prepareStatement.setString(3, substring2);
                            prepareStatement.addBatch();
                        }
                        prepareStatement.executeBatch();
                        if (prepareStatement != null) {
                            prepareStatement.close();
                        }
                    } finally {
                    }
                } finally {
                }
            } finally {
            }
        } catch (Exception e3) {
            System.err.println("⚠️ Error recomputing extra active time: " + e3.getMessage());
        }
    }

    private void finalizeRun(String str, Timestamp timestamp, Timestamp timestamp2, Map<String, Timestamp> map, Map<String, Long> map2) {
        if (str == null || timestamp == null || timestamp2 == null) {
            return;
        }
        Timestamp timestamp3 = map.get(str);
        if (timestamp3 == null) {
            return;
        }
        Timestamp timestamp4 = timestamp.after(timestamp3) ? timestamp : timestamp3;
        if (!timestamp2.after(timestamp4)) {
            return;
        }
        map2.merge(str, Long.valueOf(timestamp2.getTime() - timestamp4.getTime()), (v0, v1) -> {
            return Long.sum(v0, v1);
        });
    }

    private List<String> parseCsvLine(String str) {
        ArrayList arrayList = new ArrayList();
        StringBuilder sb = new StringBuilder();
        int i = 0;
        boolean z = false;
        for (int i2 = 0; i2 < str.length(); i2++) {
            char charAt = str.charAt(i2);
            if (charAt == '\"' && (i2 == 0 || str.charAt(i2 - 1) != '\\')) {
                z = !z;
            } else {
                if (!z) {
                    if (charAt == '[') {
                        i++;
                    } else if (charAt == ']') {
                        i--;
                    }
                    if (charAt == ',' && i == 0) {
                        arrayList.add(sb.toString().trim());
                        sb.setLength(0);
                    }
                }
                sb.append(charAt);
            }
        }
        arrayList.add(sb.toString().trim());
        return arrayList;
    }

    public void close() {
        try {
            if (this.connection != null && !this.connection.isClosed()) {
                this.connection.close();
            }
        } catch (SQLException e) {
            System.err.println("⚠️ Error closing database connection: " + e.getMessage());
        }
    }

    /* loaded from: ijava.jar:ijava2/clitools/ProgressManager$SessionProgressSummary.class */
    public static class SessionProgressSummary {
        public final String sessionName;
        public final int totalExercises;
        public final int completedExercises;
        public final int inProgressExercises;
        public final double averageScore;

        public SessionProgressSummary(String str, int i, int i2, int i3, double d) {
            this.sessionName = str;
            this.totalExercises = i;
            this.completedExercises = i2;
            this.inProgressExercises = i3;
            this.averageScore = d;
        }

        public double getCompletionPercentage() {
            if (this.totalExercises > 0) {
                return (this.completedExercises / this.totalExercises) * 100.0d;
            }
            return 0.0d;
        }
    }

    /* loaded from: ijava.jar:ijava2/clitools/ProgressManager$ExerciseProgress.class */
    public static class ExerciseProgress {
        public final String exerciseName;
        public final String exerciseType;
        public final String status;
        public final int studentScore;
        public final int professorScore;
        public final int globalScore;
        public final int compilationCount;
        public final int compilationFailures;
        public final int executionCount;
        public final int testCount;
        public final Timestamp initTimestamp;
        public final Timestamp completionTimestamp;
        public final Timestamp lastUpdated;
        public final long extraActiveMs;

        public ExerciseProgress(String str, String str2, String str3, int i, int i2, int i3, int i4, int i5, int i6, int i7, Timestamp timestamp, Timestamp timestamp2, Timestamp timestamp3, long j) {
            this.exerciseName = str;
            this.exerciseType = str2;
            this.status = str3;
            this.studentScore = i;
            this.professorScore = i2;
            this.globalScore = i3;
            this.compilationCount = i4;
            this.compilationFailures = i5;
            this.executionCount = i6;
            this.testCount = i7;
            this.initTimestamp = timestamp;
            this.completionTimestamp = timestamp2;
            this.lastUpdated = timestamp3;
            this.extraActiveMs = j;
        }

        public String getFormattedScore() {
            if ("qcm".equals(this.exerciseType)) {
                return String.format("%3d%%", Integer.valueOf(this.globalScore));
            }
            return String.format("%d%% (%d/%d)", Integer.valueOf(this.globalScore), Integer.valueOf(this.studentScore), Integer.valueOf(this.professorScore));
        }

        public String getMetricsString() {
            if ("qcm".equals(this.exerciseType)) {
                return ProgressManager.DB_PASSWORD;
            }
            return String.format("%d(%d) / %d / %d", Integer.valueOf(this.compilationCount), Integer.valueOf(this.compilationFailures), Integer.valueOf(this.executionCount), Integer.valueOf(this.testCount));
        }

        public String getCompletionTimeString() {
            long time;
            if (this.initTimestamp == null) {
                return ProgressManager.DB_PASSWORD;
            }
            if (this.completionTimestamp != null) {
                time = (this.completionTimestamp.getTime() - this.initTimestamp.getTime()) + this.extraActiveMs;
            } else if (this.lastUpdated != null) {
                time = this.lastUpdated.getTime() - this.initTimestamp.getTime();
            } else {
                return ProgressManager.DB_PASSWORD;
            }
            long j = time / 1000;
            return String.format("%d:%02d", Long.valueOf(j / 60), Long.valueOf(j % 60));
        }
    }
}
