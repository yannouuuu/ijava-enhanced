package ijava2.clitools.commands;

import ijava2.clitools.Command;
import ijava2.clitools.StateManager;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
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
            String str2 = null;
            if (strArr.length == 0) {
                str = findAnyTestClass();
                if (str == null) {
                    System.err.println("No *Test.class found in current session. Specify a test class, e.g., 'ijava2 test CalculatorTest'.");
                    return false;
                }
            } else {
                str2 = strArr[0];
                str = strArr[0] + "Test";
            }
            return runTestClass(str, str2);
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

    private boolean runTestClass(String str, String str2) {
        try {
            String str3 = this.stateManager.getWorkspaceDir().toString() + System.getProperty("path.separator") + getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
            if (!classExistsInClasspath(str)) {
                if (str2 != null) {
                    if (Files.exists(this.stateManager.getWorkspaceDir().resolve(str2 + ".class"), new LinkOption[0])) {
                        System.out.println("⚠️  No professor test class found (" + str + ".class)");
                        System.out.println("    Running student's own tests from " + str2 + ".java\n");
                        return runStudentTests(str3, str2);
                    }
                    System.err.println("Neither " + str + ".class nor " + str2 + ".class found.");
                    System.err.println("Please compile the class first: ijava2 compile " + str2 + ".java");
                    return false;
                }
                System.err.println("Test class not found: " + str + ".class");
                return false;
            }
            ArrayList arrayList = new ArrayList();
            arrayList.add("java");
            arrayList.add("-cp");
            arrayList.add(str3);
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

    private boolean runStudentTests(String str, String str2) throws IOException, InterruptedException {
        String str3 = str2 + "_DynamicTest";
        Path resolve = this.stateManager.getWorkspaceDir().resolve(str3 + ".java");
        try {
            Files.writeString(resolve, String.format("@ijava2.clitools.TestClass(studentClass = \"%s\")\nclass %s extends HiddenTest {\n    void algorithm() {\n        super.algorithm();\n    }\n}\n", str2, str3), StandardCharsets.UTF_8, new OpenOption[0]);
            if (!compileTempTestClass(str, resolve)) {
                return false;
            }
            ArrayList arrayList = new ArrayList();
            arrayList.add("java");
            arrayList.add("-cp");
            arrayList.add(str);
            arrayList.add(str3);
            arrayList.add(str3);
            ProcessBuilder processBuilder = new ProcessBuilder(arrayList);
            processBuilder.directory(this.stateManager.getWorkspaceDir().toFile());
            processBuilder.inheritIO();
            boolean z = processBuilder.start().waitFor() == 0;
            Files.deleteIfExists(resolve);
            Files.deleteIfExists(this.stateManager.getWorkspaceDir().resolve(str3 + ".class"));
            return z;
        } finally {
            Files.deleteIfExists(resolve);
            Files.deleteIfExists(this.stateManager.getWorkspaceDir().resolve(str3 + ".class"));
        }
    }

    private boolean classExistsInClasspath(String str) {
        try {
            Class.forName(str);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    private boolean compileTempTestClass(String str, Path path) throws IOException, InterruptedException {
        ArrayList arrayList = new ArrayList();
        arrayList.add("javac");
        arrayList.add("-cp");
        arrayList.add(str);
        arrayList.add(path.toString());
        ProcessBuilder processBuilder = new ProcessBuilder(arrayList);
        processBuilder.directory(this.stateManager.getWorkspaceDir().toFile());
        processBuilder.redirectErrorStream(true);
        if (processBuilder.start().waitFor() != 0) {
            System.err.println("Failed to compile temporary test class");
            return false;
        }
        return true;
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
