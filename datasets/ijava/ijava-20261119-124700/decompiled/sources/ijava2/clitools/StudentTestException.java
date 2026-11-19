package ijava2.clitools;

/* loaded from: ijava.jar:ijava2/clitools/StudentTestException.class */
public class StudentTestException extends RuntimeException {
    public final String testMethod;
    public final String skillAnnotation;
    public final int lineNumber;

    public StudentTestException(String str, String str2, int i) {
        super(str2);
        this.testMethod = str;
        this.lineNumber = i;
        this.skillAnnotation = null;
    }

    public StudentTestException(String str, String str2, int i, String str3) {
        super(str2);
        this.testMethod = str;
        this.lineNumber = i;
        this.skillAnnotation = str3;
    }

    public StudentTestException(String str, String str2, Throwable th, int i) {
        super(str2, th);
        this.testMethod = str;
        this.lineNumber = i;
        this.skillAnnotation = null;
    }
}
