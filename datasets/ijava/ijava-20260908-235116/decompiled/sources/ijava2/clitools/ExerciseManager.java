package ijava2.clitools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/* loaded from: ijava.jar:ijava2/clitools/ExerciseManager.class */
public class ExerciseManager {
    private final StateManager stateManager;

    public ExerciseManager(StateManager stateManager) {
        this.stateManager = stateManager;
    }

    public List<String> getSessionExercises(String str) {
        try {
            String str2 = "/syllabus/" + str + "/" + str + ".progression";
            InputStream resourceAsStream = getClass().getResourceAsStream(str2);
            if (resourceAsStream == null) {
                System.err.println("Session progression not found in JAR: " + str2);
                return new ArrayList();
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resourceAsStream));
            try {
                List<String> list = (List) bufferedReader.lines().map((v0) -> {
                    return v0.trim();
                }).filter(str3 -> {
                    return !str3.isEmpty();
                }).collect(Collectors.toList());
                bufferedReader.close();
                return list;
            } finally {
            }
        } catch (IOException e) {
            System.err.println("Error reading session content: " + e.getMessage());
            return new ArrayList();
        }
    }

    public List<ExerciseStatus> getExerciseProgress(String str) {
        List<String> sessionExercises = getSessionExercises(str);
        ArrayList arrayList = new ArrayList();
        Path workspaceDir = this.stateManager.getWorkspaceDir();
        for (int i = 0; i < sessionExercises.size(); i++) {
            arrayList.add(determineExerciseStatus(workspaceDir, sessionExercises.get(i), i + 1));
        }
        return arrayList;
    }

    public boolean initializeExercise(String str, String str2) {
        try {
            Path resolve = this.stateManager.getWorkspaceDir().resolve(str2 + ".java");
            if (Files.exists(resolve, new LinkOption[0])) {
                System.out.println("Exercise " + str2 + " already exists in workspace");
                return true;
            }
            String str3 = "/skeletons/" + str + "/" + str2 + ".java";
            InputStream resourceAsStream = getClass().getResourceAsStream(str3);
            if (resourceAsStream == null) {
                System.err.println("Skeleton not found in JAR: " + str3);
                return false;
            }
            Files.copy(resourceAsStream, resolve, new CopyOption[0]);
            System.out.println("Initialized exercise: " + str2 + " from skeleton");
            return true;
        } catch (IOException e) {
            System.err.println("Error initializing exercise: " + e.getMessage());
            return false;
        }
    }

    public String getCurrentExercise(String str) {
        return (String) getExerciseProgress(str).stream().filter(exerciseStatus -> {
            return exerciseStatus.getStatus() != ExerciseStatus.Status.COMPLETED;
        }).map((v0) -> {
            return v0.getName();
        }).findFirst().orElse(null);
    }

    public List<String> getNextExercises(String str) {
        return (List) getExerciseProgress(str).stream().filter(exerciseStatus -> {
            return exerciseStatus.getStatus() == ExerciseStatus.Status.NOT_STARTED;
        }).map((v0) -> {
            return v0.getName();
        }).collect(Collectors.toList());
    }

    public List<String> getCompletedExercises(String str) {
        return (List) getExerciseProgress(str).stream().filter(exerciseStatus -> {
            return exerciseStatus.getStatus() == ExerciseStatus.Status.COMPLETED;
        }).map((v0) -> {
            return v0.getName();
        }).collect(Collectors.toList());
    }

    public List<ExerciseStatus> getAllExerciseStatuses(String str) {
        return getExerciseProgress(str);
    }

    public boolean exerciseExists(String str, String str2) {
        return getSessionExercises(str).contains(str2);
    }

    private ExerciseStatus determineExerciseStatus(Path path, String str, int i) {
        if (str.startsWith("QCM")) {
            return new ExerciseStatus(str, ExerciseStatus.Status.IN_PROGRESS, i);
        }
        Path resolve = path.resolve(str + ".java");
        Path resolve2 = path.resolve(str + ".class");
        if (!Files.exists(resolve, new LinkOption[0])) {
            return new ExerciseStatus(str, ExerciseStatus.Status.NOT_STARTED, i);
        }
        if (!Files.exists(resolve2, new LinkOption[0])) {
            return new ExerciseStatus(str, ExerciseStatus.Status.IN_PROGRESS, i);
        }
        return new ExerciseStatus(str, ExerciseStatus.Status.COMPLETED, i);
    }

    /* loaded from: ijava.jar:ijava2/clitools/ExerciseManager$ExerciseStatus.class */
    public static class ExerciseStatus {
        private final String name;
        private final Status status;
        private final int number;
        private String testStatus = null;

        /* loaded from: ijava.jar:ijava2/clitools/ExerciseManager$ExerciseStatus$Status.class */
        public enum Status {
            NOT_STARTED("❌"),
            IN_PROGRESS("��"),
            COMPLETED("✅");

            private final String emoji;

            Status(String str) {
                this.emoji = str;
            }

            public String getEmoji() {
                return this.emoji;
            }
        }

        public ExerciseStatus(String str, Status status, int i) {
            this.name = str;
            this.status = status;
            this.number = i;
        }

        public String getName() {
            return this.name;
        }

        public Status getStatus() {
            return this.status;
        }

        public int getNumber() {
            return this.number;
        }

        public String getTestStatus() {
            return this.testStatus;
        }

        public void setTestStatus(String str) {
            this.testStatus = str;
        }

        public String toString() {
            return this.number + ". " + this.status.getEmoji() + " " + this.name;
        }
    }
}
