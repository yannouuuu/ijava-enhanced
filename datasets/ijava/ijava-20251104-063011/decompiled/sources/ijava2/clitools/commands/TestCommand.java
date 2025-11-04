package ijava2.clitools.commands;

import ijava2.clitools.Command;
import ijava2.clitools.StateManager;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

/* loaded from: ijava.jar:ijava2/clitools/commands/TestCommand.class */
public class TestCommand implements Command {
    private final StateManager stateManager;

    public TestCommand(StateManager stateManager) {
        this.stateManager = stateManager;
    }

    @Override // ijava2.clitools.Command
    public boolean execute(String[] strArr) {
        String str;
        try {
            if (!this.stateManager.isInitialized()) {
                this.stateManager.initialize();
            }
            if (strArr.length == 0) {
                str = findAnyTestClass();
                if (str == null) {
                    System.err.println("No *Test.class found in current session. Specify a test class, e.g., 'ijava2 test CalculatorTest'.");
                    return false;
                }
            } else {
                str = strArr[0] + "Test";
            }
            return runTestClass(str);
        } catch (Exception e) {
            System.err.println("Error running tests: " + e.getMessage());
            return false;
        }
    }

    private String findAnyTestClass() throws IOException {
        return (String) Files.list(this.stateManager.getWorkspaceDir()).filter(path -> {
            return path.getFileName().toString().endsWith("Test.class");
        }).map(path2 -> {
            return path2.getFileName().toString().substring(0, path2.getFileName().toString().length() - 6);
        }).findFirst().orElse(null);
    }

    private boolean runTestClass(String str) {
        try {
            String str2 = this.stateManager.getWorkspaceDir().toString() + System.getProperty("path.separator") + getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
            ArrayList arrayList = new ArrayList();
            arrayList.add("java");
            arrayList.add("-cp");
            arrayList.add(str2);
            arrayList.add(str);
            arrayList.add(str);
            ProcessBuilder processBuilder = new ProcessBuilder(arrayList);
            processBuilder.directory(this.stateManager.getWorkspaceDir().toFile());
            processBuilder.inheritIO();
            return processBuilder.start().waitFor() == 0;
        } catch (IOException | InterruptedException e) {
            System.err.println("Error launching test JVM: " + e.getMessage());
            return false;
        }
    }

    @Override // ijava2.clitools.Command
    public String getName() {
        return "test";
    }

    @Override // ijava2.clitools.Command
    public String getDescription() {
        return "Run a *Test class (e.g., CalculatorTest). If not provided, runs the first *Test.class found.";
    }

    @Override // ijava2.clitools.Command
    public String getUsage() {
        return "ijava2 test [TestClassName|ExerciseName]";
    }
}
