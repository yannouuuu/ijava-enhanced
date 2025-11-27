package defpackage;

import extensions.CSVFile;
import ijava2.clitools.StudentInteractionSequence;
import ijava2.tools.ANSI;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/* loaded from: ijava.jar:Program.class */
public abstract class Program implements InputOutput, Strings, Math, Misc, Files, ANSI {
    final StudentInteractionSequence io = new StudentInteractionSequence();
    private boolean silentTests = false;
    private final StringBuilder silentTestsOutput = new StringBuilder();
    public static final String SUCCESS = "\u001b[32m";
    public static final String FAILURE = "\u001b[31m";
    public static final String WARNING = "\u001b[33m";
    public static final String INFO = "\u001b[36m";
    public static final String MUTED = "\u001b[90m";
    public static final String HIGHLIGHT = "\u001b[97m";
    public static final String ERROR = "\u001b[91m";
    public static final String DEBUG = "\u001b[35m";
    private static final Scanner scanner = new Scanner(System.in);
    private static boolean testMode = false;
    private static StringBuilder testEcho = new StringBuilder();
    private static Queue<String> testInputs = new LinkedList();
    private static StringBuilder capturedOutput = new StringBuilder();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void algorithm();

    @Override // defpackage.Misc
    public long getTime() {
        return new Date().getTime();
    }

    @Override // defpackage.Misc
    public <T extends Enum<T>> T[] values(Class<T> cls) {
        return cls.getEnumConstants();
    }

    @Override // defpackage.Misc
    public int stringToInt(String str) {
        return Integer.parseInt(str);
    }

    public <T extends Enum<T>> T valueOf(String str) {
        String[] split = str.split("\\.");
        if (split.length != 2) {
            throw new IllegalArgumentException("Invalid enum string format. Expected 'Enum.VALUE'");
        }
        try {
            return (T) Enum.valueOf(Class.forName(split[0]), split[1]);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Enum not found: " + split[0], e);
        }
    }

    @Override // defpackage.Misc
    public String rgb(int i, int i2, int i3, boolean z) {
        return ANSI.rgb(i, i2, i3, z);
    }

    @Override // defpackage.Math
    public double random() {
        return Math.random();
    }

    @Override // defpackage.Math
    public int random(int i, int i2) {
        return i + ((int) (((i2 - i) + 1) * random()));
    }

    @Override // defpackage.Math
    public int sqrt(int i) {
        int i2 = -1;
        int i3 = 1;
        while (i3 < (i / 2) + 1 && i3 * i3 != i) {
            i3++;
        }
        if (i3 * i3 == i) {
            i2 = i3;
        }
        return i2;
    }

    @Override // defpackage.InputOutput
    public void println() {
        print("\n");
    }

    @Override // defpackage.InputOutput
    public void println(String str) {
        print(str + "\n");
    }

    @Override // defpackage.InputOutput
    public void println(char c) {
        println(c);
    }

    public void println(byte b) {
        println(b);
    }

    public void println(short s) {
        println(s);
    }

    @Override // defpackage.InputOutput
    public void println(int i) {
        println(i);
    }

    public void println(long j) {
        println(j);
    }

    public void println(float f) {
        println(f);
    }

    @Override // defpackage.InputOutput
    public void println(double d) {
        println(d);
    }

    @Override // defpackage.InputOutput
    public void println(boolean z) {
        println(z);
    }

    public void println(char[] cArr) {
        println(String.valueOf((Object) cArr));
    }

    public void println(byte[] bArr) {
        println(String.valueOf(bArr));
    }

    public void println(short[] sArr) {
        println(String.valueOf(sArr));
    }

    public void println(int[] iArr) {
        println(String.valueOf(iArr));
    }

    public void println(long[] jArr) {
        println(String.valueOf(jArr));
    }

    public void println(float[] fArr) {
        println(String.valueOf(fArr));
    }

    public void println(double[] dArr) {
        println(String.valueOf(dArr));
    }

    public void println(boolean[] zArr) {
        println(String.valueOf(zArr));
    }

    public void println(String[] strArr) {
        println(String.valueOf(strArr));
    }

    @Override // defpackage.InputOutput
    public void print(String str) {
        if (testMode) {
            if (this.silentTests) {
                this.silentTestsOutput.append(str);
            } else {
                System.out.print(str);
            }
            this.io.print(str);
            return;
        }
        System.out.print(str);
    }

    @Override // defpackage.InputOutput
    public void print(char c) {
        print(c);
    }

    public void print(byte b) {
        print(b);
    }

    public void print(short s) {
        print(s);
    }

    @Override // defpackage.InputOutput
    public void print(int i) {
        print(i);
    }

    public void print(long j) {
        print(j);
    }

    public void print(float f) {
        print(f);
    }

    @Override // defpackage.InputOutput
    public void print(double d) {
        print(d);
    }

    @Override // defpackage.InputOutput
    public void print(boolean z) {
        print(z);
    }

    public void print(char[] cArr) {
        print(String.valueOf((Object) cArr));
    }

    public void print(byte[] bArr) {
        print(String.valueOf(bArr));
    }

    public void print(short[] sArr) {
        print(String.valueOf(sArr));
    }

    public void print(int[] iArr) {
        print(String.valueOf(iArr));
    }

    public void print(long[] jArr) {
        print(String.valueOf(jArr));
    }

    public void print(float[] fArr) {
        print(String.valueOf(fArr));
    }

    public void print(double[] dArr) {
        print(String.valueOf(dArr));
    }

    public void print(boolean[] zArr) {
        print(String.valueOf(zArr));
    }

    public void print(String[] strArr) {
        print(String.valueOf(strArr));
    }

    @Override // defpackage.InputOutput
    public String readString() {
        if (testMode) {
            String str = (String) this.io.read(String.class);
            echoTestRead(str);
            return str;
        }
        return scanner.nextLine();
    }

    public char readChar() {
        if (testMode) {
            char charValue = ((Character) this.io.read(Character.class)).charValue();
            echoTestRead(Character.valueOf(charValue));
            return charValue;
        }
        return readString().charAt(0);
    }

    @Override // defpackage.InputOutput
    public int readInt() {
        if (testMode) {
            int intValue = ((Integer) this.io.read(Integer.class)).intValue();
            echoTestRead(Integer.valueOf(intValue));
            return intValue;
        }
        return Integer.parseInt(readString());
    }

    @Override // defpackage.InputOutput
    public double readDouble() {
        if (testMode) {
            double doubleValue = ((Double) this.io.read(Double.class)).doubleValue();
            echoTestRead(Double.valueOf(doubleValue));
            return doubleValue;
        }
        return Double.parseDouble(readString());
    }

    private void echoTestRead(Object obj) {
        if (this.silentTests) {
            this.silentTestsOutput.append(obj);
        } else {
            System.out.println("\u001b[93m\u001b[1m" + String.valueOf(obj) + "\u001b[0m");
        }
    }

    @Override // defpackage.Strings
    public int length(String str) {
        return str.length();
    }

    public int length(Object obj) {
        if (obj.getClass().isArray()) {
            return length(obj, 1);
        }
        throw new RuntimeException("length(" + String.valueOf(obj) + ") : le paramètre n'est pas un tableau !");
    }

    public int length(Object obj, int i) {
        if (obj.getClass().isArray()) {
            if (i == 1) {
                return Array.getLength(obj);
            }
            return length(Array.get(obj, 0), i - 1);
        }
        throw new RuntimeException("length(" + String.valueOf(obj) + ", " + i + ") : le premier paramètre n'est pas un tableau !");
    }

    @Override // defpackage.Strings
    public boolean equals(String str, String str2) {
        return str.equals(str2);
    }

    @Override // defpackage.Strings
    public String substring(String str, int i, int i2) {
        return str.substring(i, i2);
    }

    @Override // defpackage.Strings
    public boolean startsWith(String str, String str2) {
        return str.startsWith(str2);
    }

    @Override // defpackage.Strings
    public boolean endsWith(String str, String str2) {
        return str.endsWith(str2);
    }

    @Override // defpackage.Strings
    public boolean contains(String str, String str2) {
        return str.contains(str2);
    }

    @Override // defpackage.Strings
    public int indexOf(String str, String str2) {
        return str.indexOf(str2);
    }

    @Override // defpackage.Strings
    public boolean matches(String str, String str2) {
        return str.matches(str2);
    }

    public char charAt(String str, int i) {
        return str.charAt(i);
    }

    @Override // defpackage.Strings
    public String toLowerCase(String str) {
        return str.toLowerCase();
    }

    @Override // defpackage.Strings
    public String toUpperCase(String str) {
        return str.toUpperCase();
    }

    @Override // defpackage.Files
    public String[] getAllFilesFromDirectory(String str) {
        return new File(str).list();
    }

    @Override // defpackage.Files
    public String[] getAllFilesFromCurrentDirectory() {
        return getAllFilesFromDirectory(".");
    }

    @Override // defpackage.Files
    public extensions.File newFile(String str) {
        return new extensions.File(str);
    }

    @Override // defpackage.Files
    public boolean ready(extensions.File file) {
        return file.ready();
    }

    @Override // defpackage.Files
    public String readLine(extensions.File file) {
        return file.readLine();
    }

    @Override // defpackage.Files
    public CSVFile loadCSV(String str) {
        return new CSVFile(str);
    }

    @Override // defpackage.Files
    public CSVFile loadCSV(String str, char c) {
        return new CSVFile(str, c);
    }

    @Override // defpackage.Files
    public int rowCount(CSVFile cSVFile) {
        return cSVFile.rowCount();
    }

    @Override // defpackage.Files
    public int columnCount(CSVFile cSVFile) {
        return cSVFile.columnCount();
    }

    @Override // defpackage.Files
    public int columnCount(CSVFile cSVFile, int i) {
        return cSVFile.columnCount(i);
    }

    @Override // defpackage.Files
    public String getCell(CSVFile cSVFile, int i, int i2) {
        return cSVFile.getCell(i, i2);
    }

    @Override // defpackage.Files
    public void saveCSV(String[][] strArr, String str) {
        try {
            CSVFile.save(strArr, str, ',');
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override // defpackage.Files
    public void saveCSV(String[][] strArr, String str, char c) {
        try {
            CSVFile.save(strArr, str, c);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private int getErrorLineNumber() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (int i = 1; i < stackTrace.length; i++) {
            String className = stackTrace[i].getClassName();
            if (!"Program".equals(className) && !className.startsWith("java.") && !className.startsWith("sun.")) {
                return stackTrace[i].getLineNumber();
            }
        }
        return stackTrace[2].getLineNumber();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void assertEquals(String str, String str2) {
        if (!str.equals(str2)) {
            throw new AssertionError("[assertEquals] expected : |" + str.replace("\n", "⏎") + "| but have |" + str2.replace("\n", "⏎") + "| in line " + getErrorLineNumber());
        }
    }

    protected void assertEquals(double d, double d2) {
        if (Math.abs(d - d2) >= 0.001d) {
            getErrorLineNumber();
            AssertionError assertionError = new AssertionError("[assertEquals] expected : |" + d + "| but have |" + assertionError + "| in line " + d2);
            throw assertionError;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void assertEquals(int i, int i2) {
        if (i != i2) {
            throw new AssertionError("[assertEquals] expected : |" + i + "| but have |" + i2 + "| in line " + getErrorLineNumber());
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void assertEquals(boolean z, boolean z2) {
        if (z != z2) {
            throw new AssertionError("[assertEquals] expected : |" + z + "| but have |" + z2 + "| in line " + getErrorLineNumber());
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void assertTrue(boolean z) {
        if (!z) {
            throw new AssertionError("[assertTrue] expected : |true| but have |false| in line " + getErrorLineNumber());
        }
    }

    private String arrayToString(Object obj) {
        if (obj.getClass().isArray()) {
            String str = "[";
            for (int i = 0; i < Array.getLength(obj); i++) {
                Object obj2 = Array.get(obj, i);
                if (obj2 != null && obj2.getClass().isArray()) {
                    str = str + arrayToString(obj2);
                } else {
                    str = str + String.valueOf(obj2);
                }
                if (i < Array.getLength(obj) - 1) {
                    str = str + ", ";
                }
            }
            return str + "]";
        }
        return "[arrayToString] " + obj.getClass().getName() + " is not an array !";
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void assertArrayEquals(Object obj, Object obj2) {
        if (!arrayEquals(obj, obj2)) {
            throw new AssertionError(String.format("[assertArrayEquals] expected : |%s| but have |%s| in line %d", arrayToString(obj), arrayToString(obj2), Integer.valueOf(getErrorLineNumber())));
        }
    }

    protected void assertNotEquals(Object obj, Object obj2) {
        if (obj == obj2) {
            throw new AssertionError(String.format("[assertNotEquals] expected : |%s| should'nt be equal to |%s| in line %d", arrayToString(obj), arrayToString(obj2), Integer.valueOf(getErrorLineNumber())));
        }
    }

    protected void assertArrayNotEquals(Object obj, Object obj2) {
        if (arrayEquals(obj, obj2)) {
            throw new AssertionError(String.format("[assertArrayNotEquals] expected : |%s| should'nt be equal to |%s| in line %d", arrayToString(obj), arrayToString(obj2), Integer.valueOf(getErrorLineNumber())));
        }
    }

    private boolean arrayEquals(Object obj, Object obj2) {
        if (!obj.getClass().isArray() || !obj2.getClass().isArray() || !obj.getClass().getComponentType().equals(obj2.getClass().getComponentType()) || Array.getLength(obj) != Array.getLength(obj2)) {
            return false;
        }
        for (int i = 0; i < Array.getLength(obj); i++) {
            if (Array.get(obj, i) == null || Array.get(obj2, i) == null) {
                throw new RuntimeException("arrayEquals: null element at index " + i);
            }
            if (Array.get(obj, i).getClass().isArray() || Array.get(obj2, i).getClass().isArray()) {
                if (!arrayEquals(Array.get(obj, i), Array.get(obj2, i))) {
                    return false;
                }
            } else if (!Array.get(obj, i).equals(Array.get(obj2, i))) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void assertFalse(boolean z) {
        if (z) {
            throw new AssertionError("[assertFalse] expected : |false| but have |true| in line " + getErrorLineNumber());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setTestMode(boolean z) {
        boolean z2 = testMode;
        testMode = z;
        if (z && !z2) {
            testInputs.clear();
            capturedOutput.setLength(0);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setSilentTests(boolean z) {
        this.silentTests = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getSilentTestsOutput() {
        return this.silentTestsOutput.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clearTestInformation() {
        this.io.studentAlgorithmComplete();
        this.silentTestsOutput.setLength(0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addInput(Object obj) {
        testInputs.offer(obj.toString());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getCapturedOutput() {
        return capturedOutput.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clearCapturedOutput() {
        capturedOutput.setLength(0);
    }

    protected void assertOutput(String str) {
        String capturedOutput2 = getCapturedOutput();
        if (!str.equals(capturedOutput2)) {
            throw new AssertionError("[assertOutput] expected : |" + str.replace("\n", "⏎") + "| but have |" + capturedOutput2.replace("\n", "⏎") + "| in line " + getErrorLineNumber());
        }
    }

    protected void assertOutputContains(String str) {
        String capturedOutput2 = getCapturedOutput();
        if (!capturedOutput2.contains(str)) {
            throw new AssertionError("[assertOutputContains] expected : |" + str.replace("\n", "⏎") + "| but have |" + capturedOutput2.replace("\n", "⏎") + "| in line " + getErrorLineNumber());
        }
    }

    public static void main(String[] strArr) {
        String className;
        try {
            if (strArr.length > 0) {
                className = strArr[0];
            } else {
                className = Thread.currentThread().getStackTrace()[1].getClassName();
            }
            if ("Program".equals(className)) {
                System.err.println("Cannot instantiate abstract Program class directly");
            } else {
                ((Program) Class.forName(className).getDeclaredConstructor(new Class[0]).newInstance(new Object[0])).algorithm();
            }
        } catch (Exception e) {
            System.err.println("Error running program: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
