package defpackage;

import ijava2.clitools.AnalyticsLogger;
import ijava2.clitools.AssertionTestException;
import ijava2.clitools.MissingMethodException;
import ijava2.clitools.ProfessorTestError;
import ijava2.clitools.StudentInteractionSequence;
import ijava2.clitools.StudentTestException;
import ijava2.clitools.TestClass;
import ijava2.clitools.TestResult;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/* loaded from: ijava.jar:HiddenTest.class */
public class HiddenTest extends Program {
    Class testClass;
    Class studentClass;
    Program studentProgram;
    List<TestResult> studentTestResults = new ArrayList();
    List<TestResult> professorTestResults = new ArrayList();
    private boolean silentMode = false;

    /* JADX INFO: Access modifiers changed from: package-private */
    public HiddenTest() {
        try {
            Class<?> cls = getClass();
            this.testClass = cls;
            TestClass testClass = (TestClass) cls.getAnnotation(TestClass.class);
            if (testClass != null) {
                String studentClass = testClass.studentClass();
                if (!studentClass.contains(".")) {
                    String name = cls.getPackage() != null ? cls.getPackage().getName() : "";
                    try {
                        this.studentClass = Class.forName(name.isEmpty() ? studentClass : name + "." + studentClass);
                    } catch (ClassNotFoundException e) {
                        this.studentClass = Class.forName(studentClass);
                    }
                } else {
                    this.studentClass = Class.forName(studentClass);
                }
            } else {
                String name2 = cls.getName();
                if (name2.endsWith("Test")) {
                    try {
                        this.studentClass = Class.forName(name2.substring(0, name2.length() - 4));
                    } catch (ClassNotFoundException e2) {
                    }
                } else {
                    this.studentClass = cls;
                }
            }
            if (this.studentClass != null && Program.class.isAssignableFrom(this.studentClass)) {
                if (this.testClass == this.studentClass) {
                    this.studentProgram = this;
                } else {
                    this.studentProgram = (Program) this.studentClass.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
                }
            }
        } catch (Exception e3) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // defpackage.Program
    public void algorithm() {
        if (this.studentClass == null) {
            System.err.println("No test class found ...");
            return;
        }
        try {
            for (Method method : this.studentClass.getDeclaredMethods()) {
                if (method.getName().startsWith("test")) {
                    runStudentTest(method);
                }
            }
            if (this.testClass != this.studentClass) {
                for (Method method2 : this.testClass.getDeclaredMethods()) {
                    if (method2.getName().startsWith("test")) {
                        runProfessorTest(method2);
                    }
                }
            }
            if (!this.silentMode) {
                printResults();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TestResultData runTestsForCache() {
        this.silentMode = true;
        algorithm();
        this.silentMode = false;
        return new TestResultData(this.studentClass.getSimpleName(), this.studentTestResults, this.professorTestResults);
    }

    public static TestResultData runTestsForCaching(Object obj) {
        if (obj instanceof HiddenTest) {
            return ((HiddenTest) obj).runTestsForCache();
        }
        return null;
    }

    /* loaded from: ijava.jar:HiddenTest$TestResultData.class */
    public static class TestResultData {
        public String exerciseName;
        public List<TestResult> studentTests;
        public List<TestResult> professorTests;

        public TestResultData(String str, List<TestResult> list, List<TestResult> list2) {
            this.exerciseName = str;
            this.studentTests = new ArrayList(list);
            this.professorTests = new ArrayList(list2);
        }
    }

    private void runStudentTest(Method method) {
        try {
            method.invoke(this.studentProgram, new Object[0]);
            this.studentTestResults.add(new TestResult(method.getName(), true));
        } catch (Exception e) {
            if (isMissingMethodError(e)) {
                System.out.println("\u001b[33m⚠️  Skipping test " + method.getName() + ": method '" + extractMissingMethodName(e) + "()' not yet implemented by student\u001b[0m");
                this.studentTestResults.add(new TestResult(method.getName(), false));
            } else {
                this.studentTestResults.add(parseTestError(method.getName(), e));
            }
        }
    }

    void runProfessorTest(Method method) {
        try {
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length > 0 && Program.class.isAssignableFrom(parameterTypes[0])) {
                this.studentProgram = (Program) this.studentClass.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
                this.studentProgram.setTestMode(true);
                method.invoke(this, this.studentProgram);
                this.studentProgram.clearTestInformation();
            } else {
                method.invoke(this, new Object[0]);
            }
            this.professorTestResults.add(new TestResult(method.getName(), true));
        } catch (Exception e) {
            if (isMissingMethodError(e)) {
                System.out.println("\u001b[33m⚠️  Skipping professor test " + method.getName() + ": method '" + extractMissingMethodName(e) + "()' not yet implemented by student\u001b[0m\n");
                this.professorTestResults.add(new TestResult(method.getName(), false));
                return;
            }
            String str = "Error in professor test.";
            if (e instanceof StudentInteractionSequence.UnexpectedIO) {
                StudentInteractionSequence.UnexpectedIO unexpectedIO = (StudentInteractionSequence.UnexpectedIO) e;
                str = "Expected: " + unexpectedIO.expected + ", but received: " + unexpectedIO.found;
            } else if (e.getCause() instanceof StudentInteractionSequence.UnexpectedIO) {
                StudentInteractionSequence.UnexpectedIO unexpectedIO2 = (StudentInteractionSequence.UnexpectedIO) e.getCause();
                str = "Expected: " + unexpectedIO2.expected + ", but received: " + unexpectedIO2.found;
            } else if (e.getCause() != null && e.getCause().getMessage() != null) {
                str = e.getCause().getMessage();
            } else if (e.getMessage() != null) {
                str = e.getMessage();
            }
            System.out.println("\u001b[31m❌ " + method.getName() + " : " + str + "\u001b[0m\n");
            this.professorTestResults.add(new TestResult(method.getName(), false));
        }
    }

    private TestResult parseTestError(String str, Exception exc) {
        Throwable cause = exc.getCause() != null ? exc.getCause() : exc;
        if (cause instanceof StudentTestException) {
            StudentTestException studentTestException = (StudentTestException) cause;
            if (studentTestException instanceof AssertionTestException) {
                AssertionTestException assertionTestException = (AssertionTestException) studentTestException;
                return new TestResult(str, assertionTestException.assertionType, String.valueOf(assertionTestException.expectedValue), String.valueOf(assertionTestException.actualValue), assertionTestException.lineNumber);
            }
            if (studentTestException instanceof MissingMethodException) {
                return new TestResult(str, "missingMethod", ((MissingMethodException) studentTestException).getMethodSignature(), "not implemented", 0);
            }
            return new TestResult(str, "error", "", studentTestException.getMessage(), studentTestException.lineNumber);
        }
        if (cause instanceof ProfessorTestError) {
            ProfessorTestError professorTestError = (ProfessorTestError) cause;
            return new TestResult(str, professorTestError.expected, professorTestError.actual, professorTestError.inputs);
        }
        if (cause instanceof StudentInteractionSequence.UnexpectedIO) {
            StudentInteractionSequence.UnexpectedIO unexpectedIO = (StudentInteractionSequence.UnexpectedIO) cause;
            return new TestResult(str, "unexpectedIO", unexpectedIO.expected, unexpectedIO.found, 0);
        }
        String message = cause.getMessage() != null ? cause.getMessage() : cause.getClass().getSimpleName();
        int i = 0;
        StackTraceElement[] stackTrace = cause.getStackTrace();
        String name = this.studentClass != null ? this.studentClass.getName() : "";
        for (StackTraceElement stackTraceElement : stackTrace) {
            if (!stackTraceElement.getClassName().equals(name)) {
                if (!stackTraceElement.getClassName().endsWith("." + (this.studentClass != null ? this.studentClass.getSimpleName() : ""))) {
                }
            }
            i = stackTraceElement.getLineNumber();
        }
        return new TestResult(str, "error", "", message, i);
    }

    private void printResults() {
        System.out.printf("%s%s%s\n\n", "\u001b[1m", "Summary of " + this.studentClass.getSimpleName() + " Test Results", "\u001b[0m");
        int count = (int) this.studentTestResults.stream().filter(testResult -> {
            return testResult.passed;
        }).count();
        int size = this.studentTestResults.size();
        System.out.println("Your tests: " + (size > 0 ? (count * 100) / size : 0) + "% (" + count + "/" + size + ")");
        for (TestResult testResult2 : this.studentTestResults) {
            if (testResult2.passed) {
                System.out.println("\u001b[32m✅ " + testResult2.methodName + "\u001b[0m");
            } else {
                String str = testResult2.methodName;
                if (testResult2.errorType != null && !testResult2.errorType.isEmpty() && !"error".equals(testResult2.errorType)) {
                    str = str + " : fails on " + testResult2.errorType + "()";
                    if (testResult2.lineNumber > 0) {
                        str = str + " at line " + testResult2.lineNumber;
                    }
                }
                System.out.println("\u001b[31m❌ " + str + "\u001b[0m");
                printHiddenTestErrorDetails(testResult2);
            }
        }
        System.out.println();
        int count2 = (int) this.professorTestResults.stream().filter(testResult3 -> {
            return testResult3.passed;
        }).count();
        int size2 = this.professorTestResults.size();
        System.out.println("Professor tests: " + (size2 > 0 ? (count2 * 100) / size2 : 0) + "% (" + count2 + "/" + size2 + ")");
        for (TestResult testResult4 : this.professorTestResults) {
            if (testResult4.passed) {
                System.out.println("\u001b[32m✅ " + testResult4.methodName + "\u001b[0m");
            } else {
                System.out.println("\u001b[31m❌ " + testResult4.methodName + "\u001b[0m");
            }
        }
        System.out.println();
        int i = count + count2;
        int i2 = size + size2;
        System.out.println("\u001b[33mTotal: " + (i2 > 0 ? (i * 100) / i2 : 0) + "% (" + count + "/" + size + " yours | " + count2 + "/" + size2 + " professor)\u001b[0m");
        logResults();
    }

    private void printHiddenTestErrorDetails(String str, StudentInteractionSequence.UnexpectedIO unexpectedIO) {
        System.out.printf("\n%s❌ Error in professor test [%s]\n%s    Expected: %s%s\n%s    Received: %s%s%s\n", "\u001b[91m", str, "\u001b[32m", "\u001b[33m", unexpectedIO.expected, "\u001b[91m", "\u001b[33m", colorizeCharacterDifferences(unexpectedIO.expected, unexpectedIO.found), "\u001b[0m");
    }

    private void printSuccess(String str) {
        System.out.printf("%s✅ Professor test [%s] passed.%s\n\n", "\u001b[32m", str, "\u001b[0m");
    }

    private void printError(String str) {
        System.out.printf("%s%s%s\n", "\u001b[91m", str, "\u001b[0m");
    }

    private void printStartTest(String str) {
        System.out.printf("\n%s�� Running professor test [%s]%s\n", "\u001b[1m", str, "\u001b[0m");
    }

    private void printHiddenTestErrorDetails(TestResult testResult) {
        if ("error".equals(testResult.errorType) && (testResult.expectedValue == null || testResult.expectedValue.isEmpty())) {
            String str = "Runtime error";
            if (testResult.lineNumber > 0) {
                str = str + " at line " + testResult.lineNumber + " in " + this.studentClass.getSimpleName() + ".java";
            }
            System.out.println("   \u001b[31m" + (str + ": " + testResult.actualValue) + "\u001b[0m");
            return;
        }
        if (testResult.inputs == null) {
            System.out.println("   \u001b[32mExpected:\u001b[33m|" + (testResult.expectedValue != null ? testResult.expectedValue : "null") + "|\u001b[0m");
            System.out.println("   \u001b[31mReceived:\u001b[33m|" + colorizeCharacterDifferences(testResult.expectedValue != null ? testResult.expectedValue : "null", testResult.actualValue != null ? testResult.actualValue : "null") + "|\u001b[0m");
        } else {
            System.out.printf("   Testing with inputs: \n      - %s%n", String.join("\n      - ", testResult.inputs));
            System.out.println("   \u001b[32mExpected:\u001b[33m|" + (testResult.expectedValue != null ? testResult.expectedValue : "null") + "|\u001b[0m");
            System.out.println("   \u001b[31mReceived:\u001b[33m|" + colorizeCharacterDifferences(testResult.expectedValue != null ? testResult.expectedValue : "null", testResult.actualValue != null ? testResult.actualValue : "null") + "|\u001b[0m");
        }
    }

    private String colorizeCharacterDifferences(String str, String str2) {
        boolean z = false;
        StringBuilder sb = new StringBuilder();
        if (str == null) {
            str = "null";
        }
        if (str2 == null) {
            str2 = "null";
        }
        int length = str.length() < str2.length() ? str.length() : str2.length();
        for (int i = 0; i < length; i++) {
            if (!z && str.charAt(i) != str2.charAt(i)) {
                sb.append("\u001b[31m");
                z = true;
            }
            sb.append(str2.charAt(i));
        }
        if (str2.length() > str.length()) {
            if (!z) {
                sb.append("\u001b[31m");
            }
            sb.append(str2.substring(str.length()));
        }
        return sb.toString();
    }

    private boolean isMissingMethodError(Exception exc) {
        Throwable cause = exc.getCause();
        if ((cause instanceof MissingMethodException) || (cause instanceof NoSuchMethodError)) {
            return true;
        }
        if ((cause instanceof RuntimeException) && cause.getMessage() != null) {
            String lowerCase = cause.getMessage().toLowerCase();
            return lowerCase.contains("no such method") || lowerCase.contains("method not found");
        }
        return false;
    }

    private String extractMissingMethodName(Exception exc) {
        String message;
        Throwable cause = exc.getCause();
        if (cause instanceof MissingMethodException) {
            return ((MissingMethodException) cause).missingMethodName;
        }
        if (cause instanceof NoSuchMethodError) {
            return extractMethodNameFromNoSuchMethodError(cause.getMessage());
        }
        if (cause != null && cause.getMessage() != null) {
            message = cause.getMessage();
        } else {
            message = exc.getMessage() != null ? exc.getMessage() : "";
        }
        return extractMethodNameFromMessage(message);
    }

    private String extractMethodNameFromNoSuchMethodError(String str) {
        if (str == null) {
            return "unknown method";
        }
        if (str.contains(".") && str.contains("(")) {
            int lastIndexOf = str.lastIndexOf(".");
            int indexOf = str.indexOf("(", lastIndexOf);
            if (lastIndexOf >= 0 && indexOf > lastIndexOf) {
                return str.substring(lastIndexOf + 1, indexOf);
            }
        }
        return extractMethodNameFromMessage(str);
    }

    private String extractMethodNameFromMessage(String str) {
        if (str == null || str.isEmpty()) {
            return "unknown method";
        }
        if (str.toLowerCase().contains("method") && str.contains("'")) {
            int indexOf = str.indexOf("'");
            int indexOf2 = str.indexOf("'", indexOf + 1);
            if (indexOf >= 0 && indexOf2 > indexOf) {
                return str.substring(indexOf + 1, indexOf2);
            }
        }
        if (str.contains(".") && str.contains("(")) {
            int lastIndexOf = str.lastIndexOf(".");
            int indexOf3 = str.indexOf("(", lastIndexOf);
            if (lastIndexOf >= 0 && indexOf3 > lastIndexOf) {
                return str.substring(lastIndexOf + 1, indexOf3);
            }
            return "unknown method";
        }
        return "unknown method";
    }

    private void logResults() {
        String simpleName = this.studentClass.getSimpleName();
        int count = (int) this.studentTestResults.stream().filter(testResult -> {
            return testResult.passed;
        }).count();
        int size = this.studentTestResults.size();
        int i = size > 0 ? (count * 100) / size : 0;
        int count2 = (int) this.professorTestResults.stream().filter(testResult2 -> {
            return testResult2.passed;
        }).count();
        int size2 = this.professorTestResults.size();
        int i2 = size2 > 0 ? (count2 * 100) / size2 : 0;
        int i3 = count + count2;
        int i4 = size + size2;
        int i5 = i4 > 0 ? (i3 * 100) / i4 : 0;
        ArrayList arrayList = new ArrayList();
        for (TestResult testResult3 : this.studentTestResults) {
            arrayList.add(testResult3.methodName + ":" + (testResult3.passed ? "✅" : "❌"));
        }
        ArrayList arrayList2 = new ArrayList();
        for (TestResult testResult4 : this.professorTestResults) {
            arrayList2.add(testResult4.methodName + ":" + (testResult4.passed ? "✅" : "❌"));
        }
        AnalyticsLogger.logTest(simpleName, arrayList, i, arrayList2, i2, i5);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void expectOutput(Program program, StudentInteractionSequence.OutputCondition... outputConditionArr) {
        if (outputConditionArr.length == 0) {
            throw new IllegalArgumentException("[expectOutput] requires at least one condition");
        }
        program.io.addOutput(outputConditionArr);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void expectPrompt(Program program) {
        program.io.addOutput(new StudentInteractionSequence.OutputCondition[]{StudentInteractionSequence.OutputCondition.any()});
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void provideInput(Program program, Object obj) {
        program.io.addInput(obj);
    }

    boolean functionIsDefined(String str, Program program) {
        try {
            return program.getClass().getDeclaredMethod(str, new Class[0]) != null;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    boolean functionIsDefined(String str, Class[] clsArr, Program program) {
        try {
            return program.getClass().getDeclaredMethod(str, clsArr) != null;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void assertFunctionIsDefined(Class<?> cls, String str, Class<?>[] clsArr, Program program) {
        try {
            program.getClass().getDeclaredMethod(str, clsArr);
        } catch (NoSuchMethodException e) {
            throw new MissingMethodException("assertFunctionIsDefined", str, clsArr, cls);
        }
    }

    void assertFieldIsDefined(Class cls, String str, Program program) {
        try {
            program.getClass().getDeclaredField(str).trySetAccessible();
        } catch (NoSuchFieldException e) {
            throw new StudentTestException("assertFieldIsDefined", String.format("Field %s %s should be defined but is not.", cls.getSimpleName(), str), 0);
        }
    }

    void assertFieldIsDefined(Class cls, String str, Class<?> cls2) {
        try {
            cls2.getDeclaredField(str).trySetAccessible();
        } catch (NoSuchFieldException e) {
            throw new StudentTestException("assertFieldIsDefined", String.format("Field %s %s should be defined but is not.", cls.getSimpleName(), str), 0);
        }
    }
}
