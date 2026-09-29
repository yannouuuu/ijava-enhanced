package ijava2.clitools;

/* loaded from: ijava.jar:ijava2/clitools/Command.class */
public interface Command {
    boolean execute(String[] strArr);

    String getName();

    String getDescription();

    String getUsage();
}
