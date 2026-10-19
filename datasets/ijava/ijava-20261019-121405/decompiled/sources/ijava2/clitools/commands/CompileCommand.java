package ijava2.clitools.commands;

import ijava2.clitools.AnalyticsLogger;
import ijava2.clitools.Command;
import ijava2.clitools.StateManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

/* loaded from: ijava.jar:ijava2/clitools/commands/CompileCommand.class */
public class CompileCommand implements Command {
    private final StateManager stateManager;
    private String compilerErrorOutput = null;

    public CompileCommand(StateManager stateManager) {
        this.stateManager = stateManager;
    }

    @Override // ijava2.clitools.Command
    public boolean execute(String[] strArr) {
        String str = null;
        String str2 = null;
        try {
            try {
                if (!this.stateManager.isInitialized()) {
                    this.stateManager.initialize();
                }
                String currentSession = this.stateManager.getCurrentSession();
                Path workspaceDir = this.stateManager.getWorkspaceDir();
                if (strArr.length == 0) {
                    System.err.println("No filename (.java file) has been given ...");
                    if (0 != 0) {
                        AnalyticsLogger.logCompile(str.endsWith(".java") ? str.substring(0, str.length() - 5) : null, false, "No filename (.java file) has been given ...");
                    }
                    return false;
                }
                String str3 = strArr[0];
                Path resolve = workspaceDir.resolve(str3);
                if (!Files.exists(resolve, new LinkOption[0])) {
                    String str4 = "File not found: " + String.valueOf(resolve);
                    System.err.println(str4);
                    if (str3 != null) {
                        AnalyticsLogger.logCompile(str3.endsWith(".java") ? str3.substring(0, str3.length() - 5) : str3, false, str4);
                    }
                    return false;
                }
                boolean compileFile = compileFile(resolve, currentSession);
                if (!compileFile) {
                    str2 = this.compilerErrorOutput != null ? this.compilerErrorOutput.replace("\n", "⏎").replace("\r", "⏎") : "Compilation failed";
                }
                if (str3 != null) {
                    AnalyticsLogger.logCompile(str3.endsWith(".java") ? str3.substring(0, str3.length() - 5) : str3, compileFile, str2);
                }
                return compileFile;
            } catch (Exception e) {
                String message = e.getMessage();
                System.err.println("Error compiling: " + e.getMessage());
                if (0 != 0) {
                    AnalyticsLogger.logCompile(str.endsWith(".java") ? str.substring(0, str.length() - 5) : null, false, message);
                }
                return false;
            }
        } catch (Throwable th) {
            if (0 != 0) {
                AnalyticsLogger.logCompile(str.endsWith(".java") ? str.substring(0, str.length() - 5) : null, false, null);
            }
            throw th;
        }
    }

    private String findCurrentExercise(Path path) {
        try {
            return (String) Files.list(path).filter(path2 -> {
                return path2.toString().endsWith(".java");
            }).map(path3 -> {
                return path3.getFileName().toString();
            }).findFirst().orElse(null);
        } catch (IOException e) {
            return null;
        }
    }

    private boolean compileFile(Path path, String str) {
        try {
            System.out.println("Compiling " + path.getFileName().toString() + " in " + str);
            ProcessBuilder processBuilder = new ProcessBuilder("javac", "-cp", this.stateManager.getWorkspaceDir().toString() + System.getProperty("path.separator") + getClass().getProtectionDomain().getCodeSource().getLocation().getPath(), path.toString());
            processBuilder.redirectErrorStream(true);
            Process start = processBuilder.start();
            StringBuilder sb = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(start.getInputStream()));
            while (true) {
                try {
                    String readLine = bufferedReader.readLine();
                    if (readLine == null) {
                        break;
                    }
                    System.out.println(readLine);
                    sb.append(readLine).append("\n");
                } catch (Throwable th) {
                    try {
                        bufferedReader.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                    throw th;
                }
            }
            bufferedReader.close();
            int waitFor = start.waitFor();
            if (waitFor == 0) {
                System.out.println("Compilation successful");
                this.compilerErrorOutput = null;
                return true;
            }
            this.compilerErrorOutput = sb.toString().trim();
            if (this.compilerErrorOutput.isEmpty()) {
                this.compilerErrorOutput = "Compilation failed with exit code: " + waitFor;
            }
            System.err.println("Compilation failed with exit code: " + waitFor);
            return false;
        } catch (IOException | InterruptedException e) {
            this.compilerErrorOutput = "Error running compiler: " + e.getMessage();
            System.err.println(this.compilerErrorOutput);
            return false;
        }
    }

    @Override // ijava2.clitools.Command
    public String getName() {
        return "compile";
    }

    @Override // ijava2.clitools.Command
    public String getDescription() {
        return "Compile the current exercise or specified Java file";
    }

    @Override // ijava2.clitools.Command
    public String getUsage() {
        return "ijava2 compile [filename.java]";
    }

    private String getCurrentExerciseSkills(String str) {
        try {
            String currentSession = this.stateManager.getCurrentSession();
            if (currentSession == null) {
                return "";
            }
            Path path = Paths.get("src/main/java/syllabus/" + currentSession + "/" + currentSession + ".skills", new String[0]);
            if (!Files.exists(path, new LinkOption[0])) {
                return "";
            }
            for (String str2 : Files.readAllLines(path)) {
                if (str2.startsWith(str + " : ")) {
                    return str2.substring(str2.indexOf(91) + 1, str2.indexOf(93)).replace("'", "").replace(", ", "|");
                }
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }
}
