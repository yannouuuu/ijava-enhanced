package ijava2.clitools;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;

/* loaded from: ijava.jar:ijava2/clitools/StateManager.class */
public class StateManager {
    private static final String IJAVA2_DIR = System.getProperty("user.home") + "/.ijava2";
    private static final String LOGS_DIR = System.getProperty("user.home") + "/.ijava2/logs";
    private final Path homeDir = Paths.get(System.getProperty("user.home"), new String[0]);
    private final Path ijava2Dir = this.homeDir.resolve(IJAVA2_DIR);
    private final Path logsDir = this.ijava2Dir.resolve(LOGS_DIR);
    private final Path workspaceDir = Paths.get(System.getProperty("user.dir"), new String[0]);

    public void initialize() {
        try {
            if (!Files.exists(this.ijava2Dir, new LinkOption[0])) {
                Files.createDirectories(this.ijava2Dir, new FileAttribute[0]);
                System.out.println("Created .ijava2 directory for session data");
            }
            if (!Files.exists(this.logsDir, new LinkOption[0])) {
                Files.createDirectories(this.logsDir, new FileAttribute[0]);
                System.out.println("Created logs directory for test results");
            }
        } catch (IOException e) {
            System.err.println("Error initializing ijava2 directories: " + e.getMessage());
        }
    }

    public Path getWorkspaceDir() {
        return this.workspaceDir;
    }

    public Path getIjava2Dir() {
        return this.ijava2Dir;
    }

    public Path getLogsDir() {
        return this.logsDir;
    }

    public String getCurrentSession() {
        return Paths.get(System.getProperty("user.dir"), new String[0]).getFileName().toString();
    }

    public boolean isInitialized() {
        return Files.exists(this.ijava2Dir, new LinkOption[0]) && Files.exists(this.logsDir, new LinkOption[0]);
    }
}
