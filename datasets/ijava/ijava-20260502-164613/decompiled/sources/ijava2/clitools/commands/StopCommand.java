package ijava2.clitools.commands;

import ijava2.clitools.Command;
import ijava2.clitools.StateManager;
import java.io.IOException;
import java.net.Socket;

/* loaded from: ijava.jar:ijava2/clitools/commands/StopCommand.class */
public class StopCommand implements Command {
    private final StateManager stateManager;

    public StopCommand(StateManager stateManager) {
        this.stateManager = stateManager;
    }

    @Override // ijava2.clitools.Command
    public boolean execute(String[] strArr) {
        try {
            System.out.println("Stopping web server...");
            if (!isWebServerRunning()) {
                System.out.println("Web server is not running");
                return true;
            }
            if (killWebServerProcess()) {
                System.out.println("✅ Web server stopped successfully");
                return true;
            }
            System.err.println("❌ Failed to stop web server");
            return false;
        } catch (Exception e) {
            System.err.println("Error stopping web server: " + e.getMessage());
            return false;
        }
    }

    @Override // ijava2.clitools.Command
    public String getName() {
        return "stop";
    }

    @Override // ijava2.clitools.Command
    public String getDescription() {
        return "Stop the web server daemon (QCM and exercise descriptions)";
    }

    @Override // ijava2.clitools.Command
    public String getUsage() {
        return "ijava2 stop";
    }

    private boolean isWebServerRunning() {
        try {
            new Socket("localhost", 8080).close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private boolean killWebServerProcess() {
        try {
            int waitFor = new ProcessBuilder("pkill", "-f", "ijava2.webapp.UnifiedWebApp").start().waitFor();
            return waitFor == 0 || waitFor == 1;
        } catch (IOException | InterruptedException e) {
            System.err.println("Error executing pkill command: " + e.getMessage());
            return false;
        }
    }
}
