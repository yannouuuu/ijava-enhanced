package ijava2.clitools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

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
                int parseInt = Integer.parseInt(parseCsvLine.get(parseCsvLine.size() - 2).trim());
                int parseInt2 = Integer.parseInt(parseCsvLine.get(parseCsvLine.size() - 1).trim());
                int round = parseInt2 > 0 ? (int) Math.round((parseInt / parseInt2) * 100.0d) : 0;
                updateQcmMetrics(trim2, normalizeExerciseName, "qcm", round, round, round, trim);
                return true;
            }
            if ("init".equals(trim3)) {
                updateInitTimestamp(trim2, normalizeExerciseName, trim);
                return true;
            }
            if ("compile".equals(trim3)) {
                updateCompilationMetrics(trim2, normalizeExerciseName, parseCsvLine.size() > 4 && "SUCCESS".equals(parseCsvLine.get(4).trim()));
                return true;
            }
            if ("execute".equals(trim3)) {
                updateExecutionMetrics(trim2, normalizeExerciseName);
                return true;
            }
            return false;
        } catch (Exception e) {
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
            PreparedStatement prepareStatement = this.connection.prepareStatement("    SELECT compilation_count, compilation_failures, execution_count, test_count, init_timestamp\n    FROM exercise_progress\n    WHERE session_name = ? AND exercise_name = ?\n");
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
                }
                if (prepareStatement != null) {
                    prepareStatement.close();
                }
                int i8 = i7 + 1;
                Timestamp timestamp2 = null;
                if (str5.equals("completed")) {
                    timestamp2 = valueOf;
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
                    prepareStatement.setTimestamp(12, timestamp2);
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

    private void updateQcmMetrics(String str, String str2, String str3, int i, int i2, int i3, String str4) {
        try {
            if (!getProgressionOrder(str).contains(str2)) {
                return;
            }
            ensureSessionExists(str);
            ensureExerciseExists(str, str2, str3);
            Timestamp valueOf = Timestamp.valueOf(LocalDateTime.parse(str4, DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            int i4 = 0;
            int i5 = 0;
            int i6 = 0;
            int i7 = 0;
            Timestamp timestamp = null;
            PreparedStatement prepareStatement = this.connection.prepareStatement("    SELECT compilation_count, compilation_failures, execution_count, test_count, init_timestamp\n    FROM exercise_progress\n    WHERE session_name = ? AND exercise_name = ?\n");
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
                }
                if (prepareStatement != null) {
                    prepareStatement.close();
                }
                prepareStatement = this.connection.prepareStatement("    MERGE INTO exercise_progress\n    (session_name, exercise_name, status, student_score, professor_score, global_score,\n     compilation_count, compilation_failures, execution_count, test_count,\n     init_timestamp, completion_timestamp, last_updated)\n    KEY(session_name, exercise_name)\n    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)\n");
                try {
                    prepareStatement.setString(1, str);
                    prepareStatement.setString(2, str2);
                    prepareStatement.setString(3, "completed");
                    prepareStatement.setInt(4, i);
                    prepareStatement.setInt(5, i2);
                    prepareStatement.setInt(6, i3);
                    prepareStatement.setInt(7, i4);
                    prepareStatement.setInt(8, i5);
                    prepareStatement.setInt(9, i6);
                    prepareStatement.setInt(10, i7);
                    prepareStatement.setTimestamp(11, timestamp);
                    prepareStatement.setTimestamp(12, valueOf);
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
            System.err.println("⚠️ Error updating QCM metrics for " + str + "/" + str2 + ": " + e.getMessage());
        }
    }

    private void updateInitTimestamp(String str, String str2, String str3) {
        try {
            if (!getProgressionOrder(str).contains(str2)) {
                return;
            }
            ensureSessionExists(str);
            ensureExerciseExists(str, str2, str2.startsWith("QCM") ? "qcm" : "java");
            PreparedStatement prepareStatement = this.connection.prepareStatement("    MERGE INTO exercise_progress\n    (session_name, exercise_name, status, student_score, professor_score, global_score,\n     compilation_count, compilation_failures, execution_count, test_count,\n     init_timestamp, completion_timestamp, last_updated)\n    KEY(session_name, exercise_name)\n    VALUES (?, ?, 'started', 0, 0, 0, 0, 0, 0, 0, ?, NULL, ?)\n");
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
        } catch (SQLException e) {
            System.err.println("⚠️ Error updating init timestamp for " + str + "/" + str2 + ": " + e.getMessage());
        }
    }

    private void updateCompilationMetrics(String str, String str2, boolean z) {
        try {
            if (!getProgressionOrder(str).contains(str2)) {
                return;
            }
            PreparedStatement prepareStatement = this.connection.prepareStatement("    UPDATE exercise_progress\n    SET compilation_count = compilation_count + 1,\n        compilation_failures = compilation_failures + ?,\n        last_updated = CURRENT_TIMESTAMP\n    WHERE session_name = ? AND exercise_name = ?\n");
            try {
                prepareStatement.setInt(1, z ? 0 : 1);
                prepareStatement.setString(2, str);
                prepareStatement.setString(3, str2);
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

    private void updateExecutionMetrics(String str, String str2) {
        try {
            if (!getProgressionOrder(str).contains(str2)) {
                return;
            }
            PreparedStatement prepareStatement = this.connection.prepareStatement("    UPDATE exercise_progress\n    SET execution_count = execution_count + 1,\n        last_updated = CURRENT_TIMESTAMP\n    WHERE session_name = ? AND exercise_name = ?\n");
            try {
                prepareStatement.setString(1, str);
                prepareStatement.setString(2, str2);
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
                prepareStatement = this.connection.prepareStatement("    SELECT\n        e.exercise_name,\n        e.exercise_type,\n        COALESCE(ep.status, 'not_started') as status,\n        COALESCE(ep.student_score, 0) as student_score,\n        COALESCE(ep.professor_score, 0) as professor_score,\n        COALESCE(ep.global_score, 0) as global_score,\n        COALESCE(ep.compilation_count, 0) as compilation_count,\n        COALESCE(ep.compilation_failures, 0) as compilation_failures,\n        COALESCE(ep.execution_count, 0) as execution_count,\n        COALESCE(ep.test_count, 0) as test_count,\n        ep.init_timestamp,\n        ep.completion_timestamp,\n        ep.last_updated\n    FROM exercises e\n    LEFT JOIN exercise_progress ep ON e.session_name = ep.session_name AND e.exercise_name = ep.exercise_name\n    WHERE e.session_name = ?\n    ORDER BY e.exercise_name\n");
                try {
                    prepareStatement.setString(1, str);
                    ResultSet executeQuery = prepareStatement.executeQuery();
                    while (executeQuery.next()) {
                        arrayList.add(new ExerciseProgress(executeQuery.getString("exercise_name"), executeQuery.getString("exercise_type"), executeQuery.getString("status"), executeQuery.getInt("student_score"), executeQuery.getInt("professor_score"), executeQuery.getInt("global_score"), executeQuery.getInt("compilation_count"), executeQuery.getInt("compilation_failures"), executeQuery.getInt("execution_count"), executeQuery.getInt("test_count"), executeQuery.getTimestamp("init_timestamp"), executeQuery.getTimestamp("completion_timestamp"), executeQuery.getTimestamp("last_updated")));
                    }
                    if (prepareStatement != null) {
                        prepareStatement.close();
                    }
                } finally {
                }
            } else {
                StringBuilder sb = new StringBuilder("    SELECT\n        e.exercise_name,\n        e.exercise_type,\n        COALESCE(ep.status, 'not_started') as status,\n        COALESCE(ep.student_score, 0) as student_score,\n        COALESCE(ep.professor_score, 0) as professor_score,\n        COALESCE(ep.global_score, 0) as global_score,\n        COALESCE(ep.compilation_count, 0) as compilation_count,\n        COALESCE(ep.compilation_failures, 0) as compilation_failures,\n        COALESCE(ep.execution_count, 0) as execution_count,\n        COALESCE(ep.test_count, 0) as test_count,\n        ep.init_timestamp,\n        ep.completion_timestamp,\n        ep.last_updated\n    FROM exercises e\n    LEFT JOIN exercise_progress ep ON e.session_name = ep.session_name AND e.exercise_name = ep.exercise_name\n    WHERE e.session_name = ? AND e.exercise_name IN (\n");
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
                        ExerciseProgress exerciseProgress = new ExerciseProgress(executeQuery2.getString("exercise_name"), executeQuery2.getString("exercise_type"), executeQuery2.getString("status"), executeQuery2.getInt("student_score"), executeQuery2.getInt("professor_score"), executeQuery2.getInt("global_score"), executeQuery2.getInt("compilation_count"), executeQuery2.getInt("compilation_failures"), executeQuery2.getInt("execution_count"), executeQuery2.getInt("test_count"), executeQuery2.getTimestamp("init_timestamp"), executeQuery2.getTimestamp("completion_timestamp"), executeQuery2.getTimestamp("last_updated"));
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
                            arrayList.add(new ExerciseProgress(str2, str2.startsWith("QCM") ? "qcm" : "java", "not_started", 0, 0, 0, 0, 0, 0, 0, null, null, null));
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

    public long calculateActiveWorkTime(String str, String str2) {
        try {
            Path resolve = Paths.get(System.getProperty("user.home"), new String[0]).resolve(".ijava2").resolve("logs");
            ArrayList arrayList = new ArrayList();
            DirectoryStream<Path> newDirectoryStream = Files.newDirectoryStream(resolve, "*.csv");
            try {
                Iterator<Path> it = newDirectoryStream.iterator();
                while (it.hasNext()) {
                    try {
                        for (String str3 : Files.readAllLines(it.next())) {
                            if (!str3.startsWith("timestamp") && !str3.trim().isEmpty()) {
                                List<String> parseCsvLine = parseCsvLine(str3);
                                if (parseCsvLine.size() >= 4) {
                                    String trim = parseCsvLine.get(1).trim();
                                    String trim2 = parseCsvLine.get(2).trim();
                                    String normalizeExerciseName = normalizeExerciseName(parseCsvLine.get(3).trim());
                                    if (str.equals(trim) && str2.equals(normalizeExerciseName) && ("init".equals(trim2) || "compile".equals(trim2) || "execute".equals(trim2) || "test".equals(trim2) || "qcm".equals(trim2))) {
                                        try {
                                            arrayList.add(Timestamp.valueOf(LocalDateTime.parse(parseCsvLine.get(0).trim(), DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
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
                if (arrayList.size() < 2) {
                    return 0L;
                }
                arrayList.sort((v0, v1) -> {
                    return v0.compareTo(v1);
                });
                long j = 0;
                for (int i = 1; i < arrayList.size(); i++) {
                    long time = ((Timestamp) arrayList.get(i)).getTime() - ((Timestamp) arrayList.get(i - 1)).getTime();
                    if (time / 1000 < 3600) {
                        j += time;
                    }
                }
                return j;
            } finally {
            }
        } catch (Exception e3) {
            return 0L;
        }
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

        public ExerciseProgress(String str, String str2, String str3, int i, int i2, int i3, int i4, int i5, int i6, int i7, Timestamp timestamp, Timestamp timestamp2, Timestamp timestamp3) {
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
            return getCompletionTimeString(null, null);
        }

        public String getCompletionTimeString(ProgressManager progressManager, String str) {
            if (this.initTimestamp == null) {
                return ProgressManager.DB_PASSWORD;
            }
            Timestamp timestamp = this.completionTimestamp != null ? this.completionTimestamp : this.lastUpdated;
            if (timestamp == null) {
                return ProgressManager.DB_PASSWORD;
            }
            long time = (timestamp.getTime() - this.initTimestamp.getTime()) / 1000;
            if (time > 14400) {
                if (progressManager != null && str != null) {
                    long calculateActiveWorkTime = progressManager.calculateActiveWorkTime(str, this.exerciseName);
                    if (calculateActiveWorkTime > 0) {
                        long j = calculateActiveWorkTime / 1000;
                        return String.format("%d:%02d", Long.valueOf(j / 60), Long.valueOf(j % 60));
                    }
                    return "multi-day";
                }
                return "multi-day";
            }
            return String.format("%d:%02d", Long.valueOf(time / 60), Long.valueOf(time % 60));
        }
    }
}
