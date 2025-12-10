package ijava2.clitools;

import ijava2.tools.SkillsMetadata;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/* loaded from: ijava.jar:ijava2/clitools/AnalyticsLogger.class */
public class AnalyticsLogger {
    private static final String LOGS_DIR = "logs";
    private static final String CSV_HEADER = "timestamp,session,command";
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public static void logStart() {
        String currentSession = getCurrentSession();
        if (currentSession == null) {
            currentSession = "ijava2";
        }
        logSimpleCommand(currentSession, "start");
    }

    public static void logStatus() {
        String currentSession = getCurrentSession();
        if (currentSession == null) {
            currentSession = "ijava2";
        }
        logSimpleCommand(currentSession, "status");
    }

    public static void logInit(String str) {
        String currentSession = getCurrentSession();
        if (currentSession == null) {
            currentSession = "ijava2";
        }
        logWithExtraColumn(currentSession, "init", str);
    }

    public static void logCompile(String str, boolean z, String str2) {
        String currentSession = getCurrentSession();
        if (currentSession == null) {
            currentSession = "ijava2";
        }
        if (z) {
            writeToLogFile(String.format("%s,%s,compile,%s,SUCCESS%n", LocalDateTime.now().format(TIMESTAMP_FORMAT), currentSession, str));
            return;
        }
        Object[] objArr = new Object[4];
        objArr[0] = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        objArr[1] = currentSession;
        objArr[2] = str;
        objArr[3] = escapeCsvField(str2 != null ? str2 : "Compilation failed");
        writeToLogFile(String.format("%s,%s,compile,%s,ERROR,\"%s\"%n", objArr));
    }

    public static void logExecute(String str) {
        String currentSession = getCurrentSession();
        if (currentSession == null) {
            currentSession = "ijava2";
        }
        logWithExtraColumn(currentSession, "execute", str);
    }

    public static void logTest(String str, List<String> list, int i, List<String> list2, int i2, int i3) {
        String format;
        String currentSession = getCurrentSession();
        if (currentSession == null) {
            currentSession = "ijava2";
        }
        String formatTestList = formatTestList(list);
        String formatTestList2 = formatTestList(list2);
        SkillsMetadata.ExerciseMetadata loadForExercise = SkillsMetadata.loadForExercise(currentSession, str);
        if (loadForExercise != null && loadForExercise.totalStudentTests != null && loadForExercise.totalProfTests != null) {
            format = String.format("%s,%s,test,%s,[%s],%d,[%s],%d,%d,%d,%d%n", LocalDateTime.now().format(TIMESTAMP_FORMAT), currentSession, str, formatTestList, Integer.valueOf(i), formatTestList2, Integer.valueOf(i2), Integer.valueOf(i3), loadForExercise.totalStudentTests, loadForExercise.totalProfTests);
        } else {
            format = String.format("%s,%s,test,%s,[%s],%d,[%s],%d,%d%n", LocalDateTime.now().format(TIMESTAMP_FORMAT), currentSession, str, formatTestList, Integer.valueOf(i), formatTestList2, Integer.valueOf(i2), Integer.valueOf(i3));
        }
        writeToLogFile(format);
    }

    public static void logQCM(String str, String str2, Map<String, String> map, int i, int i2) {
        String format;
        new StringBuilder();
        ArrayList arrayList = new ArrayList();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            arrayList.add(entry.getKey() + ":" + escapeCsvField(entry.getValue()));
        }
        SkillsMetadata.ExerciseMetadata loadForExercise = SkillsMetadata.loadForExercise(str, str2);
        if (loadForExercise != null && loadForExercise.totalQuestions != null) {
            format = String.format("%s,%s,qcm,%s,\"%s\",%d,%d,%d%n", LocalDateTime.now().format(TIMESTAMP_FORMAT), str, str2, String.join(",", arrayList), Integer.valueOf(i), Integer.valueOf(i2), loadForExercise.totalQuestions);
        } else {
            format = String.format("%s,%s,qcm,%s,\"%s\",%d,%d%n", LocalDateTime.now().format(TIMESTAMP_FORMAT), str, str2, String.join(",", arrayList), Integer.valueOf(i), Integer.valueOf(i2));
        }
        writeToLogFile(format);
    }

    private static String formatTestList(List<String> list) {
        if (list == null || list.isEmpty()) {
            return "";
        }
        return String.join(", ", list);
    }

    private static void logSimpleCommand(String str, String str2) {
        writeToLogFile(String.format("%s,%s,%s%n", LocalDateTime.now().format(TIMESTAMP_FORMAT), str, str2));
    }

    private static void logWithExtraColumn(String str, String str2, String str3) {
        writeToLogFile(String.format("%s,%s,%s,%s%n", LocalDateTime.now().format(TIMESTAMP_FORMAT), str, str2, str3));
    }

    private static void writeToLogFile(String str) {
        try {
            Files.writeString(ensureLogFileExists(), str, new OpenOption[]{StandardOpenOption.APPEND});
        } catch (IOException e) {
            System.err.println("Warning: Could not write to analytics log: " + e.getMessage());
        }
    }

    @Deprecated
    public static void logCommand(String str, String str2, String str3, long j, String str4, String str5) {
        logInteraction(str, str2, str3, j, str4, str5, "", 0, 0, 0);
    }

    public static void logTestExecution(String str, String str2, String str3, long j, String str4, String str5, Map<String, TestResult> map, int i) {
        logInteraction(str, str2, str3, j, str4, str5, formatTestResults(map), countPassedTests(map), i, calculateProgression(map, i));
    }

    private static void logInteraction(String str, String str2, String str3, long j, String str4, String str5, String str6, int i, int i2, int i3) {
        try {
            Files.writeString(ensureLogFileExists(), String.format("%s,%s,%s,%s,%d,\"%s\",\"%s\",\"%s\",%d,%d,%d%n", LocalDateTime.now().format(TIMESTAMP_FORMAT), str, str2, str3, Long.valueOf(j), escapeCsvField(str4), escapeCsvField(str5), escapeCsvField(str6), Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3)), new OpenOption[]{StandardOpenOption.APPEND});
        } catch (IOException e) {
            System.err.println("Warning: Could not write to analytics log: " + e.getMessage());
        }
    }

    private static Path ensureLogFileExists() throws IOException {
        Path resolve = Paths.get(System.getProperty("user.home"), new String[0]).resolve(".ijava2").resolve(LOGS_DIR);
        Files.createDirectories(resolve, new FileAttribute[0]);
        Path resolve2 = resolve.resolve(getCurrentUsername() + "-" + LocalDate.now().toString() + ".csv");
        if (!Files.exists(resolve2, new LinkOption[0])) {
            Files.writeString(resolve2, "timestamp,session,command" + System.lineSeparator(), new OpenOption[0]);
        }
        return resolve2;
    }

    private static String formatTestResults(Map<String, TestResult> map) {
        if (map.isEmpty()) {
            return "";
        }
        return (String) map.entrySet().stream().map(entry -> {
            return ((String) entry.getKey()) + ":" + (((TestResult) entry.getValue()).passed ? "PASS" : "FAIL");
        }).collect(Collectors.joining("|"));
    }

    private static int countPassedTests(Map<String, TestResult> map) {
        return (int) map.values().stream().filter(testResult -> {
            return testResult.passed;
        }).count();
    }

    private static int calculateProgression(Map<String, TestResult> map, int i) {
        if (i == 0) {
            return 0;
        }
        return Math.round((countPassedTests(map) / i) * 100.0f);
    }

    private static String escapeCsvField(String str) {
        if (str == null) {
            return "";
        }
        String replace = str.replace("\n", "⏎").replace("\r", "⏎");
        if (replace.contains("\"") || replace.contains(",")) {
            return replace.replace("\"", "\"\"");
        }
        return replace;
    }

    public static void logCommandWithTiming(String str, String str2, Runnable runnable) {
        long currentTimeMillis = System.currentTimeMillis();
        String str3 = "SUCCESS";
        String str4 = "";
        try {
            try {
                runnable.run();
                logCommand(str, str2, str3, System.currentTimeMillis() - currentTimeMillis, str4, getCurrentExerciseSkills(str2));
            } catch (Exception e) {
                str3 = "ERROR";
                str4 = e.getMessage();
                throw e;
            }
        } catch (Throwable th) {
            logCommand(str, str2, str3, System.currentTimeMillis() - currentTimeMillis, str4, getCurrentExerciseSkills(str2));
            throw th;
        }
    }

    private static String getCurrentUsername() {
        String property = System.getProperty("user.name");
        if (property == null || property.trim().isEmpty()) {
            property = "unknown";
        }
        return property.replaceAll("[^a-zA-Z0-9._-]", "_");
    }

    public static String getCurrentSession() {
        try {
            Path path = Paths.get(System.getProperty("user.dir"), new String[0]);
            Path path2 = Paths.get(System.getProperty("user.home"), new String[0]);
            Path resolve = path2.resolve("ijava2");
            if (path.getParent() != null && path.getParent().equals(resolve)) {
                String path3 = path.getFileName().toString();
                if (path3.startsWith("tp")) {
                    return path3;
                }
            }
            for (Path path4 = path; path4 != null; path4 = path4.getParent()) {
                if (!path4.equals(path2)) {
                    if (path4.getParent() != null && path4.getParent().equals(resolve)) {
                        String path5 = path4.getFileName().toString();
                        if (path5.startsWith("tp")) {
                            return path5;
                        }
                    }
                } else {
                    return null;
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    @Deprecated
    private static String getCurrentExerciseSkills(String str) {
        return "";
    }
}
