package ijava2.clitools.commands;

import ijava2.clitools.AnalyticsLogger;
import ijava2.clitools.Command;
import ijava2.clitools.StateManager;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;

/* loaded from: ijava.jar:ijava2/clitools/commands/ExecuteCommand.class */
public class ExecuteCommand implements Command {
    private final StateManager stateManager;

    public ExecuteCommand(StateManager stateManager) {
        this.stateManager = stateManager;
    }

    @Override // ijava2.clitools.Command
    public boolean execute(String[] strArr) {
        String str;
        try {
            if (!this.stateManager.isInitialized()) {
                this.stateManager.initialize();
            }
            String[] strArr2 = new String[0];
            if (strArr.length == 0) {
                str = findCurrentExercise();
                if (str == null) {
                    System.err.println("No compiled Java classes found in current session");
                    return false;
                }
            } else {
                str = strArr[0];
                if (strArr.length > 1) {
                    strArr2 = (String[]) Arrays.copyOfRange(strArr, 1, strArr.length);
                }
            }
            AnalyticsLogger.logExecute(str);
            return executeClass(str, strArr2);
        } catch (Exception e) {
            System.err.println("Error executing: " + e.getMessage());
            return false;
        }
    }

    private String findCurrentExercise() {
        try {
            return (String) Files.list(this.stateManager.getWorkspaceDir()).filter(path -> {
                return path.toString().endsWith(".class");
            }).map(path2 -> {
                return path2.getFileName().toString();
            }).map(str -> {
                return str.substring(0, str.length() - 6);
            }).findFirst().orElse(null);
        } catch (IOException e) {
            return null;
        }
    }

    private boolean executeClass(String str, String[] strArr) {
        try {
            String str2 = this.stateManager.getWorkspaceDir().toString() + System.getProperty("path.separator") + getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
            ArrayList arrayList = new ArrayList();
            arrayList.add("java");
            arrayList.add("-cp");
            arrayList.add(str2);
            arrayList.add(str);
            arrayList.add(str);
            arrayList.addAll(Arrays.asList(strArr));
            ProcessBuilder processBuilder = new ProcessBuilder(arrayList);
            processBuilder.directory(this.stateManager.getWorkspaceDir().toFile());
            processBuilder.inheritIO();
            int waitFor = processBuilder.start().waitFor();
            if (waitFor == 0) {
                return true;
            }
            System.err.println("Execution failed with exit code: " + waitFor);
            return false;
        } catch (IOException | InterruptedException e) {
            System.err.println("Error running program: " + e.getMessage());
            return false;
        }
    }

    @Override // ijava2.clitools.Command
    public String getName() {
        return "execute";
    }

    @Override // ijava2.clitools.Command
    public String getDescription() {
        return "Execute the current exercise or specified Java class";
    }

    @Override // ijava2.clitools.Command
    public String getUsage() {
        return "ijava2 execute [classname] [arguments...]";
    }
}
