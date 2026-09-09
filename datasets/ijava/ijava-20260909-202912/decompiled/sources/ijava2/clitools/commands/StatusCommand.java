package ijava2.clitools.commands;

import ijava2.clitools.AnalyticsLogger;
import ijava2.clitools.Command;
import ijava2.clitools.ExerciseManager;
import ijava2.clitools.ProgressManager;
import ijava2.clitools.StateManager;
import ijava2.tools.ANSI;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: ijava.jar:ijava2/clitools/commands/StatusCommand.class */
public class StatusCommand implements Command {
    private final StateManager stateManager;
    private final ExerciseManager exerciseManager;

    public StatusCommand(StateManager stateManager) {
        this.stateManager = stateManager;
        this.exerciseManager = new ExerciseManager(stateManager);
    }

    @Override // ijava2.clitools.Command
    public boolean execute(String[] strArr) {
        try {
            AnalyticsLogger.logStatus();
            if (!this.stateManager.isInitialized()) {
                this.stateManager.initialize();
            }
            Path path = Paths.get(System.getProperty("user.dir"), new String[0]);
            Path resolve = Paths.get(System.getProperty("user.home"), new String[0]).resolve("ijava2");
            if (path.equals(resolve)) {
                showSessionsOverview();
                return true;
            }
            if (path.getParent() != null && path.getParent().equals(resolve)) {
                String path2 = path.getFileName().toString();
                if (path2.startsWith("tp")) {
                    showSpecificTPStatus(path2);
                    return true;
                }
            }
            Path findTPDirectory = findTPDirectory(path, resolve);
            if (findTPDirectory != null) {
                showSpecificTPStatus(findTPDirectory.getFileName().toString());
                return true;
            }
            showSessionsOverview();
            return true;
        } catch (Exception e) {
            System.err.println("Error getting status: " + e.getMessage());
            return false;
        }
    }

    private String formatStatusGrid(String str, List<ExerciseManager.ExerciseStatus> list) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Student Progress Overview ===\n\n");
        sb.append("Session: ").append(str).append("\n\n");
        sb.append(String.format("%-3s| %-30s | %-6s | %s\n", "#", "Exercise", "Status", "Score"));
        sb.append("---|--------------------------------|--------|------------------\n");
        for (int i = 0; i < list.size(); i++) {
            sb.append(formatExerciseTableLine(i + 1, list.get(i))).append("\n");
        }
        sb.append("\n");
        int count = (int) list.stream().filter(exerciseStatus -> {
            String testStatus = exerciseStatus.getTestStatus();
            if (testStatus != null && !testStatus.equals("(not tested)") && !testStatus.equals("(not started)")) {
                String name = exerciseStatus.getName();
                int parseGlobalPercentageFromTestStatus = parseGlobalPercentageFromTestStatus(testStatus);
                return name.startsWith("QCM") ? parseGlobalPercentageFromTestStatus >= 0 : parseGlobalPercentageFromTestStatus == 100;
            }
            return false;
        }).count();
        int size = list.size();
        double d = size > 0 ? (count / size) * 100.0d : 0.0d;
        sb.append(String.format("Overall Progress: %s (%d/%d exercises)", getPercentageColor(d) + String.format("%.0f%%", Double.valueOf(d)) + "\u001b[0m", Integer.valueOf(count), Integer.valueOf(size)));
        return sb.toString();
    }

    private String formatExerciseTableLine(int i, ExerciseManager.ExerciseStatus exerciseStatus) {
        Object obj;
        Object obj2;
        String str;
        String testStatus = exerciseStatus.getTestStatus();
        if (testStatus != null && !testStatus.equals("(not tested)") && !testStatus.equals("(not started)")) {
            int parseGlobalPercentageFromTestStatus = parseGlobalPercentageFromTestStatus(testStatus);
            String name = exerciseStatus.getName();
            if (parseGlobalPercentageFromTestStatus == 100) {
                obj = " PASS ";
                obj2 = ANSI.BRIGHT_GREEN;
            } else if (name.startsWith("QCM")) {
                obj = " DONE ";
                obj2 = "\u001b[33m";
            } else {
                obj = " FAIL ";
                obj2 = "\u001b[91m";
            }
            String percentageColor = getPercentageColor(parseGlobalPercentageFromTestStatus);
            if (name.startsWith("QCM")) {
                str = percentageColor + parseGlobalPercentageFromTestStatus + "%\u001b[0m";
            } else {
                str = percentageColor + testStatus + "\u001b[0m";
            }
        } else {
            obj = " ---- ";
            obj2 = ANSI.WHITE;
            str = testStatus != null ? testStatus : "(not started)";
        }
        return String.format("%-3d| %-30s | %-6s | %s", Integer.valueOf(i), exerciseStatus.getName(), obj2 + obj + "\u001b[0m", str);
    }

    private String getPercentageColor(double d) {
        if (d < 25.0d) {
            return "\u001b[31m";
        }
        if (d >= 25.0d && d < 50.0d) {
            return "\u001b[91m";
        }
        if (d >= 50.0d && d < 75.0d) {
            return ANSI.BRIGHT_GREEN;
        }
        return "\u001b[32m";
    }

    @Override // ijava2.clitools.Command
    public String getName() {
        return "status";
    }

    @Override // ijava2.clitools.Command
    public String getDescription() {
        return "Show sessions overview (from ~/ijava2) or TP status (from ~/ijava2/tpX)";
    }

    @Override // ijava2.clitools.Command
    public String getUsage() {
        return "ijava2 status";
    }

    private void showSessionsOverview() {
        System.out.println("Available Sessions:\n");
        try {
            Path resolve = Paths.get(System.getProperty("user.home"), new String[0]).resolve("ijava2");
            if (!Files.exists(resolve, new LinkOption[0]) || !Files.isDirectory(resolve, new LinkOption[0])) {
                System.out.println("No ijava2 workspace directory found at: " + String.valueOf(resolve));
                System.out.println("Run 'ijava2 start' to initialize the workspace.");
                return;
            }
            ArrayList arrayList = new ArrayList();
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
                if (arrayList.isEmpty()) {
                    System.out.println("No tp sessions found in: " + String.valueOf(resolve));
                    return;
                }
                Collections.sort(arrayList);
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    showSessionProgress((String) it.next());
                }
            } finally {
            }
        } catch (IOException e) {
            System.err.println("Error listing sessions: " + e.getMessage());
        }
    }

    private void showSessionProgress(String str) {
        try {
            ProgressManager progressManager = new ProgressManager();
            progressManager.syncWithLogs();
            ProgressManager.SessionProgressSummary sessionProgress = progressManager.getSessionProgress(str);
            progressManager.close();
            if (sessionProgress.totalExercises == 0) {
                System.out.println(str + "  [░░░░░░░░░░] 0/0 exercises (0%)");
                return;
            }
            double completionPercentage = sessionProgress.getCompletionPercentage();
            int round = (int) Math.round(completionPercentage / 10.0d);
            System.out.printf("%-4s [%s] %d/%d exercises (%.0f%%)%n", str, "█".repeat(round) + "░".repeat(10 - round), Integer.valueOf(sessionProgress.completedExercises), Integer.valueOf(sessionProgress.totalExercises), Double.valueOf(completionPercentage));
        } catch (Exception e) {
            System.out.println(str + "  [░░░░░░░░░░] Error loading progress: " + e.getMessage());
        }
    }

    private void showSpecificTPStatus(String str) {
        try {
            ProgressManager progressManager = new ProgressManager();
            progressManager.syncWithLogs();
            List<ProgressManager.ExerciseProgress> detailedSessionProgress = progressManager.getDetailedSessionProgress(str);
            if (detailedSessionProgress.isEmpty()) {
                progressManager.close();
                System.out.println("No exercises found for session: " + str);
            } else {
                System.out.println(formatEnhancedStatusTable(str, detailedSessionProgress, parseTestResultsFromLogs(str), progressManager));
                progressManager.close();
            }
        } catch (Exception e) {
            System.err.println("Error loading enhanced metrics: " + e.getMessage());
            List<ExerciseManager.ExerciseStatus> allExerciseStatuses = this.exerciseManager.getAllExerciseStatuses(str);
            Map<String, TestProgressData> parseTestResultsFromLogs = parseTestResultsFromLogs(str);
            allExerciseStatuses.forEach(exerciseStatus -> {
                String format;
                if (exerciseStatus.getStatus() != ExerciseManager.ExerciseStatus.Status.NOT_STARTED) {
                    TestProgressData testProgressData = (TestProgressData) parseTestResultsFromLogs.get(exerciseStatus.getName());
                    if (testProgressData != null) {
                        if (testProgressData.hasCompositeScore()) {
                            format = String.format("Q:%d%% C:%d%% (%d%%/%d%%)", testProgressData.questionPercentage, Integer.valueOf(testProgressData.globalPercentage), Integer.valueOf(testProgressData.studentPercentage), Integer.valueOf(testProgressData.professorPercentage));
                        } else if (testProgressData.questionPercentage != null) {
                            format = String.format("%d%%", testProgressData.questionPercentage);
                        } else {
                            format = String.format("%d%% (%d%% / %d%%)", Integer.valueOf(testProgressData.globalPercentage), Integer.valueOf(testProgressData.studentPercentage), Integer.valueOf(testProgressData.professorPercentage));
                        }
                        exerciseStatus.setTestStatus(format);
                        return;
                    }
                    exerciseStatus.setTestStatus("(not tested)");
                }
            });
            System.out.println(formatStatusGrid(str, allExerciseStatuses));
        }
    }

    private Path findTPDirectory(Path path, Path path2) {
        Path path3 = path;
        while (true) {
            Path path4 = path3;
            if (path4 != null && !path4.equals(path2.getParent())) {
                if (path4.getParent() != null && path4.getParent().equals(path2) && path4.getFileName().toString().startsWith("tp")) {
                    return path4;
                }
                path3 = path4.getParent();
            } else {
                return null;
            }
        }
    }

    private Map<String, TestProgressData> parseTestResultsFromLogs(String str) {
        Path resolve;
        HashMap hashMap = new HashMap();
        try {
            resolve = Paths.get(System.getProperty("user.home"), new String[0]).resolve(".ijava2").resolve("logs");
        } catch (IOException e) {
            System.err.println("Warning: Could not read log files: " + e.getMessage());
        }
        if (!Files.exists(resolve, new LinkOption[0])) {
            return hashMap;
        }
        DirectoryStream<Path> newDirectoryStream = Files.newDirectoryStream(resolve, "*.csv");
        try {
            Iterator<Path> it = newDirectoryStream.iterator();
            while (it.hasNext()) {
                parseLogFile(it.next(), str, hashMap);
            }
            if (newDirectoryStream != null) {
                newDirectoryStream.close();
            }
            HashSet hashSet = new HashSet(this.exerciseManager.getSessionExercises(str));
            hashMap.keySet().removeIf(str2 -> {
                return !hashSet.contains(str2);
            });
            return hashMap;
        } finally {
        }
    }

    private void parseLogFile(Path path, String str, Map<String, TestProgressData> map) {
        try {
            for (String str2 : Files.readAllLines(path)) {
                if (!str2.startsWith("timestamp") && !str2.trim().isEmpty()) {
                    List<String> parseCsvLineWithBrackets = parseCsvLineWithBrackets(str2);
                    if (parseCsvLineWithBrackets.size() >= 4) {
                        String trim = parseCsvLineWithBrackets.get(0).trim();
                        String trim2 = parseCsvLineWithBrackets.get(1).trim();
                        String trim3 = parseCsvLineWithBrackets.get(2).trim();
                        String trim4 = parseCsvLineWithBrackets.get(3).trim();
                        if (str.equals(trim2)) {
                            if ("test".equals(trim3) && parseCsvLineWithBrackets.size() >= 9) {
                                try {
                                    int parseInt = Integer.parseInt(parseCsvLineWithBrackets.get(5).trim());
                                    int parseInt2 = Integer.parseInt(parseCsvLineWithBrackets.get(7).trim());
                                    int parseInt3 = Integer.parseInt(parseCsvLineWithBrackets.get(8).trim());
                                    LocalDateTime parse = LocalDateTime.parse(trim, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                                    TestProgressData testProgressData = map.get(trim4);
                                    Integer num = null;
                                    if (testProgressData != null && testProgressData.questionPercentage != null) {
                                        num = testProgressData.questionPercentage;
                                    }
                                    TestProgressData testProgressData2 = new TestProgressData(parseInt, parseInt2, parseInt3, num, parse);
                                    if (testProgressData == null || testProgressData2.timestamp.isAfter(testProgressData.timestamp) || num != null) {
                                        map.put(trim4, testProgressData2);
                                    }
                                } catch (Exception e) {
                                }
                            } else if ("qcm".equals(trim3) && parseCsvLineWithBrackets.size() >= 6) {
                                try {
                                    int size = parseCsvLineWithBrackets.size() >= 7 ? 5 : parseCsvLineWithBrackets.size() - 2;
                                    int size2 = parseCsvLineWithBrackets.size() >= 7 ? 6 : parseCsvLineWithBrackets.size() - 1;
                                    int parseInt4 = Integer.parseInt(parseCsvLineWithBrackets.get(size).trim());
                                    int parseInt5 = Integer.parseInt(parseCsvLineWithBrackets.get(size2).trim());
                                    int round = parseInt5 > 0 ? (int) Math.round((parseInt4 / parseInt5) * 100.0d) : 0;
                                    LocalDateTime parse2 = LocalDateTime.parse(trim, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                                    TestProgressData testProgressData3 = map.get(trim4);
                                    if (testProgressData3 != null && testProgressData3.studentPercentage > 0) {
                                        map.put(trim4, new TestProgressData(testProgressData3.studentPercentage, testProgressData3.professorPercentage, testProgressData3.globalPercentage, Integer.valueOf(round), parse2));
                                    } else {
                                        map.put(trim4, new TestProgressData(0, 0, round, Integer.valueOf(round), parse2));
                                    }
                                } catch (Exception e2) {
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e3) {
        }
    }

    private List<String> parseCsvLineWithBrackets(String str) {
        ArrayList arrayList = new ArrayList();
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (int i2 = 0; i2 < str.length(); i2++) {
            char charAt = str.charAt(i2);
            if (charAt == '[') {
                i++;
            } else if (charAt == ']') {
                i--;
            }
            if (charAt == ',' && i == 0) {
                arrayList.add(sb.toString().trim());
                sb.setLength(0);
            } else {
                sb.append(charAt);
            }
        }
        arrayList.add(sb.toString().trim());
        return arrayList;
    }

    private int parseGlobalPercentageFromTestStatus(String str) {
        if (str == null || str.trim().isEmpty()) {
            return -1;
        }
        try {
            int indexOf = str.indexOf(37);
            if (indexOf == -1) {
                return -1;
            }
            return Integer.parseInt(str.substring(0, indexOf).trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private String formatEnhancedStatusTable(String str, List<ProgressManager.ExerciseProgress> list, Map<String, TestProgressData> map, ProgressManager progressManager) {
        StringBuilder sb = new StringBuilder();
        sb.append("Session: ").append(str).append("\n\n");
        sb.append(String.format("%-3s| %-30s | %-6s | %-14s | %-12s | %s\n", "#", "Exercise", "Status", "Score", "C(F) / E / T", "Time"));
        sb.append("---|--------------------------------|--------|----------------|--------------|------\n");
        for (int i = 0; i < list.size(); i++) {
            ProgressManager.ExerciseProgress exerciseProgress = list.get(i);
            sb.append(formatEnhancedExerciseLine(i + 1, exerciseProgress, map.get(exerciseProgress.exerciseName), progressManager, str)).append("\n");
        }
        sb.append("\n");
        int count = (int) list.stream().filter(exerciseProgress2 -> {
            if ("qcm".equals(exerciseProgress2.exerciseType)) {
                return exerciseProgress2.globalScore > 0;
            }
            return "completed".equals(exerciseProgress2.status);
        }).count();
        int size = list.size();
        double d = size > 0 ? (count / size) * 100.0d : 0.0d;
        sb.append(String.format("Overall Progress: %s (%d/%d exercises)", getPercentageColor(d) + String.format("%.0f%%", Double.valueOf(d)) + "\u001b[0m", Integer.valueOf(count), Integer.valueOf(size)));
        return sb.toString();
    }

    private String formatEnhancedExerciseLine(int i, ProgressManager.ExerciseProgress exerciseProgress, TestProgressData testProgressData, ProgressManager progressManager, String str) {
        Object obj;
        Object obj2;
        String str2;
        if ("not_started".equals(exerciseProgress.status)) {
            obj = " ---- ";
            obj2 = ANSI.WHITE;
        } else if ("qcm".equals(exerciseProgress.exerciseType)) {
            if (exerciseProgress.globalScore > 0) {
                obj = " DONE ";
                obj2 = "\u001b[33m";
            } else {
                obj = " ---- ";
                obj2 = ANSI.WHITE;
            }
        } else if ("completed".equals(exerciseProgress.status) && exerciseProgress.globalScore == 100) {
            obj = " PASS ";
            obj2 = ANSI.BRIGHT_GREEN;
        } else if (exerciseProgress.globalScore > 0) {
            obj = " FAIL ";
            obj2 = "\u001b[91m";
        } else {
            obj = " ---- ";
            obj2 = ANSI.WHITE;
        }
        if ("not_started".equals(exerciseProgress.status)) {
            str2 = "(not started)";
        } else if (testProgressData != null && testProgressData.hasCompositeScore()) {
            str2 = (getPercentageColor(testProgressData.globalPercentage) + String.format("Q:%d%% C:%d%% (%d%%/%d%%)", testProgressData.questionPercentage, Integer.valueOf(testProgressData.globalPercentage), Integer.valueOf(testProgressData.studentPercentage), Integer.valueOf(testProgressData.professorPercentage)) + "\u001b[0m") + "  ";
        } else {
            String str3 = getPercentageColor(exerciseProgress.globalScore) + exerciseProgress.getFormattedScore() + "\u001b[0m";
            if (!str3.contains("(")) {
                str2 = str3 + "          ";
            } else {
                str2 = str3 + "  ";
            }
        }
        return String.format("%-3d| %-30s | %-6s | %-14s | %-12s | %s", Integer.valueOf(i), exerciseProgress.exerciseName, obj2 + obj + "\u001b[0m", str2, exerciseProgress.getMetricsString(), exerciseProgress.getCompletionTimeString(progressManager, str));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: ijava.jar:ijava2/clitools/commands/StatusCommand$TestProgressData.class */
    public static class TestProgressData {
        final int studentPercentage;
        final int professorPercentage;
        final int globalPercentage;
        final Integer questionPercentage;
        final LocalDateTime timestamp;

        TestProgressData(int i, int i2, int i3, LocalDateTime localDateTime) {
            this(i, i2, i3, null, localDateTime);
        }

        TestProgressData(int i, int i2, int i3, Integer num, LocalDateTime localDateTime) {
            this.studentPercentage = i;
            this.professorPercentage = i2;
            this.globalPercentage = i3;
            this.questionPercentage = num;
            this.timestamp = localDateTime;
        }

        boolean hasCompositeScore() {
            return this.questionPercentage != null && (this.studentPercentage > 0 || this.professorPercentage > 0);
        }
    }
}
