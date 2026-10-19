package ijava2.clitools;

import ijava2.clitools.StudentInteractionSequence;
import java.util.HashMap;
import java.util.Map;

/* loaded from: ijava.jar:ijava2/clitools/EnhancedTestResult.class */
public class EnhancedTestResult extends TestResult {
    public final TestFailureType failureType;
    public final Map<String, Object> metadata;
    public final Throwable originalException;

    /* loaded from: ijava.jar:ijava2/clitools/EnhancedTestResult$TestFailureType.class */
    public enum TestFailureType {
        ASSERTION_FAILURE("Assertion failed"),
        MISSING_METHOD("Method not implemented"),
        RUNTIME_ERROR("Runtime error occurred"),
        UNEXPECTED_IO("Unexpected input/output"),
        COMPILATION_ERROR("Compilation error"),
        PROFESSOR_TEST_ERROR("Professor test specific error"),
        UNKNOWN_ERROR("Unknown error");

        private final String description;

        TestFailureType(String str) {
            this.description = str;
        }

        public String getDescription() {
            return this.description;
        }
    }

    public EnhancedTestResult(String str, boolean z) {
        super(str, z);
        this.failureType = null;
        this.metadata = new HashMap();
        this.originalException = null;
    }

    public EnhancedTestResult(String str, TestFailureType testFailureType, Map<String, Object> map, Throwable th) {
        super(str, false);
        this.failureType = testFailureType;
        this.metadata = map != null ? new HashMap(map) : new HashMap();
        this.originalException = th;
    }

    public static EnhancedTestResult fromAssertionFailure(AssertionTestException assertionTestException) {
        HashMap hashMap = new HashMap();
        hashMap.put("assertionType", assertionTestException.assertionType);
        hashMap.put("expectedValue", assertionTestException.expectedValue);
        hashMap.put("actualValue", assertionTestException.actualValue);
        hashMap.put("lineNumber", Integer.valueOf(assertionTestException.lineNumber));
        if (assertionTestException.inputs != null) {
            hashMap.put("inputs", assertionTestException.inputs.clone());
        }
        return new EnhancedTestResult(assertionTestException.testMethod, TestFailureType.ASSERTION_FAILURE, hashMap, assertionTestException);
    }

    public static EnhancedTestResult fromMissingMethod(MissingMethodException missingMethodException) {
        HashMap hashMap = new HashMap();
        hashMap.put("missingMethodName", missingMethodException.missingMethodName);
        hashMap.put("expectedParameters", missingMethodException.expectedParameters.clone());
        if (missingMethodException.returnType != null) {
            hashMap.put("returnType", missingMethodException.returnType);
        }
        return new EnhancedTestResult(missingMethodException.testMethod, TestFailureType.MISSING_METHOD, hashMap, missingMethodException);
    }

    public static EnhancedTestResult fromProfessorTestError(String str, ProfessorTestError professorTestError) {
        HashMap hashMap = new HashMap();
        hashMap.put("expected", professorTestError.expected);
        hashMap.put("actual", professorTestError.actual);
        if (professorTestError.inputs != null) {
            hashMap.put("inputs", professorTestError.inputs.clone());
        }
        return new EnhancedTestResult(str, TestFailureType.PROFESSOR_TEST_ERROR, hashMap, professorTestError);
    }

    public static EnhancedTestResult fromUnexpectedIO(String str, StudentInteractionSequence.UnexpectedIO unexpectedIO) {
        HashMap hashMap = new HashMap();
        hashMap.put("expected", unexpectedIO.expected);
        hashMap.put("found", unexpectedIO.found);
        return new EnhancedTestResult(str, TestFailureType.UNEXPECTED_IO, hashMap, unexpectedIO);
    }

    public static EnhancedTestResult fromRuntimeError(String str, Throwable th, int i, String str2) {
        HashMap hashMap = new HashMap();
        hashMap.put("errorMessage", th.getMessage());
        hashMap.put("lineNumber", Integer.valueOf(i));
        hashMap.put("studentClass", str2);
        hashMap.put("exceptionType", th.getClass().getSimpleName());
        return new EnhancedTestResult(str, TestFailureType.RUNTIME_ERROR, hashMap, th);
    }

    public static EnhancedTestResult fromStudentTestException(StudentTestException studentTestException) {
        if (studentTestException instanceof AssertionTestException) {
            return fromAssertionFailure((AssertionTestException) studentTestException);
        }
        if (studentTestException instanceof MissingMethodException) {
            return fromMissingMethod((MissingMethodException) studentTestException);
        }
        HashMap hashMap = new HashMap();
        hashMap.put("lineNumber", Integer.valueOf(studentTestException.lineNumber));
        if (studentTestException.skillAnnotation != null) {
            hashMap.put("skillAnnotation", studentTestException.skillAnnotation);
        }
        return new EnhancedTestResult(studentTestException.testMethod, TestFailureType.UNKNOWN_ERROR, hashMap, studentTestException);
    }

    public String getDisplayMessage() {
        if (this.passed) {
            return "Test passed";
        }
        if (this.failureType == null || this.originalException == null) {
            return "Test failed: Unknown error";
        }
        switch (this.failureType) {
            case ASSERTION_FAILURE:
                return buildAssertionFailureMessage();
            case MISSING_METHOD:
                return buildMissingMethodMessage();
            case RUNTIME_ERROR:
                return buildRuntimeErrorMessage();
            case UNEXPECTED_IO:
                return buildUnexpectedIOMessage();
            case COMPILATION_ERROR:
            default:
                return "Test failed: " + (this.originalException.getMessage() != null ? this.originalException.getMessage() : this.failureType.getDescription());
            case PROFESSOR_TEST_ERROR:
                return buildProfessorTestErrorMessage();
        }
    }

    private String buildAssertionFailureMessage() {
        String str = (String) this.metadata.get("assertionType");
        Object obj = this.metadata.get("expectedValue");
        Object obj2 = this.metadata.get("actualValue");
        Integer num = (Integer) this.metadata.get("lineNumber");
        return TestErrorMessageBuilder.buildAssertionFailureMessage(str, obj, obj2, num != null ? num.intValue() : 0);
    }

    private String buildMissingMethodMessage() {
        return TestErrorMessageBuilder.buildMissingMethodMessage((String) this.metadata.get("missingMethodName"), (Class[]) this.metadata.get("expectedParameters"), (Class) this.metadata.get("returnType"), "student class");
    }

    private String buildRuntimeErrorMessage() {
        String str = (String) this.metadata.get("errorMessage");
        Integer num = (Integer) this.metadata.get("lineNumber");
        String str2 = (String) this.metadata.get("studentClass");
        return TestErrorMessageBuilder.buildRuntimeErrorMessage(str, num != null ? num.intValue() : 0, str2 != null ? str2 : "unknown");
    }

    private String buildUnexpectedIOMessage() {
        return TestErrorMessageBuilder.buildUnexpectedIOMessage((String) this.metadata.get("expected"), (String) this.metadata.get("found"));
    }

    private String buildProfessorTestErrorMessage() {
        return String.format("Expected: %s, but received: %s", (String) this.metadata.get("expected"), (String) this.metadata.get("actual"));
    }

    @Override // ijava2.clitools.TestResult
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("EnhancedTestResult{");
        sb.append("methodName='").append(this.methodName).append('\'');
        sb.append(", passed=").append(this.passed);
        if (!this.passed && this.failureType != null) {
            sb.append(", failureType=").append(this.failureType);
            sb.append(", metadata=").append(this.metadata);
        }
        sb.append('}');
        return sb.toString();
    }
}
