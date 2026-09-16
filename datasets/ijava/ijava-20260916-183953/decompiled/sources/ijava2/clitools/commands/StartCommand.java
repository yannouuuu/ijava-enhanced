package ijava2.clitools.commands;

import ijava2.clitools.AnalyticsLogger;
import ijava2.clitools.Command;
import ijava2.clitools.ProgressManager;
import ijava2.clitools.StateManager;
import java.io.IOException;
import java.lang.ProcessBuilder;
import java.net.Socket;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.Iterator;

/* loaded from: ijava.jar:ijava2/clitools/commands/StartCommand.class */
public class StartCommand implements Command {
    private final StateManager stateManager;

    public StartCommand(StateManager stateManager) {
        this.stateManager = stateManager;
    }

    @Override // ijava2.clitools.Command
    public boolean execute(String[] strArr) {
        try {
            AnalyticsLogger.logStart();
            Path resolve = Paths.get(System.getProperty("user.home"), new String[0]).resolve("ijava2");
            if (!Files.exists(resolve, new LinkOption[0])) {
                Files.createDirectories(resolve, new FileAttribute[0]);
                System.out.println("Created workspace directory: " + String.valueOf(resolve));
            }
            if (!this.stateManager.isInitialized()) {
                System.out.println("Initializing ijava2 environment...");
                this.stateManager.initialize();
            }
            System.out.println("Starting web server...");
            try {
                startWebServerProcess();
            } catch (Exception e) {
                System.err.println("Warning: Could not start web server: " + e.getMessage());
            }
            try {
                ProgressManager progressManager = new ProgressManager();
                progressManager.populateExercisesFromProgression();
                progressManager.syncWithLogs();
                progressManager.close();
            } catch (Exception e2) {
                System.err.println("Warning: Could not sync progress data: " + e2.getMessage());
            }
            System.out.println();
            System.out.println("✅ ijava2 environment ready!");
            System.out.println("   • Web server running at http://localhost:8080");
            System.out.println("   • Use 'ijava status' to see detailed progress");
            System.out.println("   • Use 'ijava init <exercise>' to start working\n");
            return true;
        } catch (IOException e3) {
            System.err.println("Error creating workspace directory: " + e3.getMessage());
            return false;
        } catch (Exception e4) {
            System.err.println("Error executing start command: " + e4.getMessage());
            return false;
        }
    }

    @Override // ijava2.clitools.Command
    public String getName() {
        return "start";
    }

    @Override // ijava2.clitools.Command
    public String getDescription() {
        return "Initialize workspace and show status";
    }

    @Override // ijava2.clitools.Command
    public String getUsage() {
        return "ijava2 start";
    }

    private void startWebServerProcess() throws IOException {
        if (isWebServerRunning()) {
            System.out.println("Web server already running on http://localhost:8080");
            return;
        }
        String findJarPath = findJarPath();
        if (findJarPath == null) {
            System.err.println("Could not locate ijava2.jar for web server");
            return;
        }
        ProcessBuilder processBuilder = new ProcessBuilder("java", "-cp", findJarPath, "ijava2.webapp.UnifiedWebApp");
        processBuilder.redirectOutput(ProcessBuilder.Redirect.DISCARD);
        processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);
        Process start = processBuilder.start();
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        if (start.isAlive()) {
            System.out.println("Web server started on http://localhost:8080 (daemon process)");
        } else {
            System.err.println("Failed to start web server process");
        }
    }

    private boolean isWebServerRunning() {
        try {
            new Socket("localhost", 8080).close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private String findJarPath() {
        String currentJarPath = getCurrentJarPath();
        if (currentJarPath != null) {
            return currentJarPath;
        }
        Path path = Paths.get("production/ijava2.jar", new String[0]);
        if (Files.exists(path, new LinkOption[0])) {
            return path.toAbsolutePath().toString();
        }
        Path path2 = Paths.get("build/classes", new String[0]);
        Path path3 = Paths.get("libs", new String[0]);
        if (Files.exists(path2, new LinkOption[0]) && Files.exists(path3, new LinkOption[0])) {
            try {
                StringBuilder sb = new StringBuilder();
                sb.append(path2.toAbsolutePath());
                DirectoryStream<Path> newDirectoryStream = Files.newDirectoryStream(path3, "*.jar");
                try {
                    Iterator<Path> it = newDirectoryStream.iterator();
                    while (it.hasNext()) {
                        sb.append(":").append(it.next().toAbsolutePath());
                    }
                    if (newDirectoryStream != null) {
                        newDirectoryStream.close();
                    }
                    return sb.toString();
                } finally {
                }
            } catch (IOException e) {
                return null;
            }
        }
        return null;
    }

    private String getCurrentJarPath() {
        try {
            String path = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
            if (path.endsWith(".jar")) {
                return path;
            }
            String property = System.getProperty("java.class.path");
            if (property != null) {
                for (String str : property.split(":")) {
                    if (str.endsWith("ijava2.jar") && Files.exists(Paths.get(str, new String[0]), new LinkOption[0])) {
                        return str;
                    }
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
