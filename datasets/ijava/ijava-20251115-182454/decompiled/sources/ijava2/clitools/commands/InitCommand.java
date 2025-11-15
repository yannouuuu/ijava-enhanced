package ijava2.clitools.commands;

import ijava2.clitools.AnalyticsLogger;
import ijava2.clitools.Command;
import ijava2.clitools.ExerciseManager;
import ijava2.clitools.StateManager;
import ijava2.webapp.QCMDefinition;
import ijava2.webapp.QCMLoader;
import ijava2.webapp.UnifiedWebApp;
import java.awt.Desktop;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;
import java.util.List;

/* loaded from: ijava.jar:ijava2/clitools/commands/InitCommand.class */
public class InitCommand implements Command {
    private final StateManager stateManager;
    private final ExerciseManager exerciseManager;

    public InitCommand(StateManager stateManager) {
        this.stateManager = stateManager;
        this.exerciseManager = new ExerciseManager(stateManager);
    }

    @Override // ijava2.clitools.Command
    public boolean execute(String[] strArr) {
        String str;
        try {
            if (!this.stateManager.isInitialized()) {
                this.stateManager.initialize();
            }
            String currentSession = this.stateManager.getCurrentSession();
            if (strArr.length == 0) {
                str = this.exerciseManager.getCurrentExercise(currentSession);
                if (str == null) {
                    System.out.println("All exercises in " + currentSession + " are completed!");
                    return true;
                }
            } else {
                str = strArr[0];
                if (!this.exerciseManager.exerciseExists(currentSession, str)) {
                    System.err.println("❌ Exercise '" + str + "' not found in session '" + currentSession + "'");
                    System.err.println();
                    System.err.println("Available exercises in " + currentSession + ":");
                    List<String> sessionExercises = this.exerciseManager.getSessionExercises(currentSession);
                    if (sessionExercises.isEmpty()) {
                        System.err.println("  (no exercises found in this session)");
                        return false;
                    }
                    Iterator<String> it = sessionExercises.iterator();
                    while (it.hasNext()) {
                        System.err.println("  - " + it.next());
                    }
                    return false;
                }
            }
            displayExerciseDescription(currentSession, str);
            if (str.startsWith("QCM")) {
                AnalyticsLogger.logInit(str);
                return initializeAndLaunchQCM(currentSession, str);
            }
            openExerciseInBrowser(currentSession, str);
            AnalyticsLogger.logInit(str);
            return this.exerciseManager.initializeExercise(currentSession, str);
        } catch (Exception e) {
            System.err.println("Error initializing exercise: " + e.getMessage());
            return false;
        }
    }

    @Override // ijava2.clitools.Command
    public String getName() {
        return "init";
    }

    @Override // ijava2.clitools.Command
    public String getDescription() {
        return "Initialize current exercise or specified exercise from skeleton";
    }

    @Override // ijava2.clitools.Command
    public String getUsage() {
        return "ijava2 init [exercise-name]";
    }

    private void displayExerciseDescription(String str, String str2) {
        try {
            InputStream resourceAsStream = getClass().getResourceAsStream("/syllabus/" + str + "/" + str2 + ".md");
            try {
                if (resourceAsStream != null) {
                    String str3 = new String(resourceAsStream.readAllBytes(), StandardCharsets.UTF_8);
                    if (str2.startsWith("QCM")) {
                        str3 = stripCorrectAnswers(str3);
                    }
                    Path path = Paths.get(str2 + ".md", new String[0]);
                    Files.write(path, str3.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                    System.out.println("�� Generated exercise description: " + String.valueOf(path.toAbsolutePath()));
                    System.out.println("   Exercise: " + str2 + " (" + str + ")");
                } else {
                    Path path2 = Paths.get(str2 + ".md", new String[0]);
                    Files.write(path2, ("# " + str2 + "\n\nExercise from session: " + str + "\n\n*No detailed description available.*\n").getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                    System.out.println("�� Generated minimal exercise description: " + String.valueOf(path2.toAbsolutePath()));
                    System.out.println("   Exercise: " + str2 + " (" + str + ")");
                }
                if (resourceAsStream != null) {
                    resourceAsStream.close();
                }
            } finally {
            }
        } catch (IOException e) {
            System.err.println("Error generating markdown file: " + e.getMessage());
        } catch (Exception e2) {
            System.out.println("�� Initializing exercise: " + str2);
        }
    }

    private String stripCorrectAnswers(String str) {
        return str.replaceAll("(?i)- \\[x\\]", "- [ ]");
    }

    private void openExerciseInBrowser(String str, String str2) {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new URI("http://localhost:8080/" + str + "/" + str2));
            } else {
                System.out.println("�� For HTML view, visit: http://localhost:8080/" + str + "/" + str2);
            }
        } catch (Exception e) {
            System.out.println("�� For HTML view, visit: http://localhost:8080/" + str + "/" + str2);
        }
    }

    private boolean initializeAndLaunchQCM(String str, String str2) {
        try {
            System.out.println("Loading QCM: " + str2);
            QCMDefinition loadFromResource = QCMLoader.loadFromResource(str, str2);
            if (loadFromResource == null) {
                System.err.println("QCM definition not found: " + str2);
                System.err.println("Expected file: /syllabus/" + str + "/" + str2 + ".md (or .qcm)");
                return false;
            }
            System.out.println("Starting QCM: " + loadFromResource.title);
            System.out.println("Questions: " + loadFromResource.getTotalQuestions());
            System.out.println("Opening QCM in browser...");
            UnifiedWebApp.getInstance().openBrowserToQCM(str, str2);
            System.out.println("QCM " + str2 + " is now available in your browser.");
            System.out.println("Complete it at your own pace. The web server will remain active.");
            return true;
        } catch (Exception e) {
            System.err.println("Error launching QCM: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
