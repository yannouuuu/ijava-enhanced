package ijava2.clitools;

/* loaded from: ijava.jar:ijava2/clitools/ProfessorTestError.class */
public class ProfessorTestError extends Error {
    public final String[] inputs;
    public final String expected;
    public final String actual;

    public ProfessorTestError(String str, String str2, String[] strArr) {
        this.inputs = strArr;
        this.expected = str;
        this.actual = str2;
    }
}
