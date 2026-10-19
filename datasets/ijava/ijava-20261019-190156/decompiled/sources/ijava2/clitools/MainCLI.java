package ijava2.clitools;

import ijava2.clitools.commands.CompileCommand;
import ijava2.clitools.commands.ExecuteCommand;
import ijava2.clitools.commands.InitCommand;
import ijava2.clitools.commands.StartCommand;
import ijava2.clitools.commands.StatusCommand;
import ijava2.clitools.commands.StopCommand;
import ijava2.clitools.commands.TestCommand;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/* loaded from: ijava.jar:ijava2/clitools/MainCLI.class */
public class MainCLI {
    private final StateManager stateManager = new StateManager();
    private final Map<String, Command> commands = new HashMap();

    public MainCLI() {
        registerCommand(new StartCommand(this.stateManager));
        registerCommand(new StopCommand(this.stateManager));
        registerCommand(new StatusCommand(this.stateManager));
        registerCommand(new InitCommand(this.stateManager));
        registerCommand(new CompileCommand(this.stateManager));
        registerCommand(new ExecuteCommand(this.stateManager));
        registerCommand(new TestCommand(this.stateManager));
    }

    private void registerCommand(Command command) {
        this.commands.put(command.getName(), command);
    }

    public void run(String[] strArr) {
        if (!this.stateManager.isInitialized()) {
            System.out.println("Initializing ijava2 environment...");
            this.stateManager.initialize();
        }
        if (strArr.length == 0) {
            showHelp();
            return;
        }
        String str = strArr[0];
        Command command = this.commands.get(str);
        if (command == null) {
            System.err.println("Unknown command: " + str);
            System.err.println("Available commands: " + String.join(", ", this.commands.keySet()));
            showHelp();
        } else if (!command.execute((String[]) Arrays.copyOfRange(strArr, 1, strArr.length))) {
            System.exit(1);
        }
    }

    private void showHelp() {
        System.out.println("ijava - Imperative Java Learning Toolkit");
        System.out.println();
        System.out.println("Usage: ijava <command> [arguments...]");
        System.out.println();
        System.out.println("Available commands:");
        for (Command command : this.commands.values()) {
            System.out.printf("  %-10s %s%n", command.getName(), command.getDescription());
        }
        System.out.println();
        System.out.println("For detailed usage of a specific command:");
        System.out.println("  ijava <command> --help");
    }

    public static void main(String[] strArr) {
        new MainCLI().run(strArr);
    }
}
