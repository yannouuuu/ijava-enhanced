package ijava2.clitools;

/* loaded from: ijava.jar:ijava2/clitools/TestResult.class */
public class TestResult {
    public String methodName;
    public boolean passed;
    public String errorType;
    public String expectedValue;
    public String actualValue;
    public int lineNumber;
    public String[] inputs;

    public String toString() {
        return "errorType : " + this.errorType + " / expected : " + this.expectedValue + " vs. actual : " + this.actualValue + " at line " + this.lineNumber;
    }

    public TestResult(String str, boolean z) {
        this.methodName = str;
        this.passed = z;
    }

    public TestResult(String[] strArr, boolean z) {
        this.inputs = strArr;
        this.passed = z;
    }

    public TestResult(String str, String str2, String str3, String str4, int i) {
        this.methodName = str;
        this.passed = false;
        this.errorType = str2;
        this.expectedValue = str3;
        this.actualValue = str4;
        this.lineNumber = i;
    }

    public TestResult(String str, String str2, String str3, String[] strArr) {
        this.methodName = str;
        this.inputs = strArr;
        this.expectedValue = str2;
        this.actualValue = str3;
    }
}
