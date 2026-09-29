package ijava2.tools;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/* loaded from: ijava.jar:ijava2/tools/SolutionChecker.class */
public class SolutionChecker {
    private static final String PROJECT_ROOT = System.getProperty("user.dir");
    private static final String BUILD_CLASSES_DIR = PROJECT_ROOT + "/build/classes";

    public static void main(String[] strArr) {
        if (strArr.length == 0) {
            printUsage();
            System.exit(1);
        }
        System.exit(checkSolutions(strArr[0]) ? 0 : 1);
    }

    private static void printUsage() {
        System.err.println("Usage: java -cp build/classes ijava2.tools.SolutionChecker <session>");
        System.err.println("Example: java -cp build/classes ijava2.tools.SolutionChecker tp3");
        System.err.println("         java -cp build/classes ijava2.tools.SolutionChecker Semaine3");
        System.err.println();
        System.err.println("This tool tests all solution classes for the specified session.");
    }

    private static boolean checkSolutions(String str) {
        try {
            Path findProgressionFile = findProgressionFile(str);
            if (findProgressionFile == null) {
                System.err.println("❌ No progression file found for session: " + str);
                return false;
            }
            List<String> readExercisesFromProgression = readExercisesFromProgression(findProgressionFile);
            if (readExercisesFromProgression.isEmpty()) {
                System.err.println("⚠️  No exercises found in progression file: " + String.valueOf(findProgressionFile));
                return true;
            }
            System.out.println("=== Testing Solutions for " + str + " ===");
            System.out.println("Found " + readExercisesFromProgression.size() + " exercise(s): " + String.join(", ", readExercisesFromProgression));
            System.out.println();
            boolean z = true;
            int i = 0;
            Iterator<String> it = readExercisesFromProgression.iterator();
            while (it.hasNext()) {
                if (testSolutionClass(it.next())) {
                    i++;
                } else {
                    z = false;
                }
            }
            System.out.println();
            System.out.println("=== Summary ===");
            System.out.println("Tested: " + i + "/" + readExercisesFromProgression.size() + " solutions");
            if (z && i > 0) {
                System.out.println("✅ All solutions passed their tests!");
            } else if (i == 0) {
                System.out.println("⚠️  No solutions were tested (missing Test classes or solution classes)");
            } else {
                System.out.println("❌ Some solutions failed their tests");
            }
            return z;
        } catch (IOException e) {
            System.err.println("❌ Error reading files: " + e.getMessage());
            return false;
        }
    }

    private static Path findProgressionFile(String str) throws IOException {
        Path path = Paths.get(PROJECT_ROOT, "src", "main", "java", "syllabus");
        if (!Files.exists(path, new LinkOption[0])) {
            return null;
        }
        Path resolve = path.resolve(str).resolve(str + ".progression");
        if (Files.exists(resolve, new LinkOption[0])) {
            return resolve;
        }
        return (Path) Files.list(path).filter(path2 -> {
            return Files.isDirectory(path2, new LinkOption[0]);
        }).filter(path3 -> {
            return path3.getFileName().toString().toLowerCase().contains(str.toLowerCase());
        }).map(path4 -> {
            return path4.resolve(path4.getFileName().toString() + ".progression");
        }).filter(path5 -> {
            return Files.exists(path5, new LinkOption[0]);
        }).findFirst().orElse(null);
    }

    private static List<String> readExercisesFromProgression(Path path) throws IOException {
        return (List) Files.lines(path).map((v0) -> {
            return v0.trim();
        }).filter(str -> {
            return (str.isEmpty() || str.startsWith("#")) ? false : true;
        }).collect(Collectors.toList());
    }

    private static boolean testSolutionClass(String str) {
        try {
            if (!Files.exists(Paths.get(BUILD_CLASSES_DIR, str + ".class"), new LinkOption[0])) {
                System.out.println("⚠️  " + str + ": Solution class not found (skipped)");
                return true;
            }
            if (!Files.exists(Paths.get(BUILD_CLASSES_DIR, str + "Test.class"), new LinkOption[0])) {
                System.out.println("⚠️  " + str + ": Test class not found (skipped)");
                return true;
            }
            System.out.print("Testing " + str + "... ");
            Constructor<?> declaredConstructor = Class.forName(str + "Test").getDeclaredConstructor(new Class[0]);
            declaredConstructor.setAccessible(true);
            Object invoke = Class.forName("HiddenTest").getMethod("runTestsForCaching", Object.class).invoke(null, declaredConstructor.newInstance(new Object[0]));
            Class<?> cls = invoke.getClass();
            List list = (List) cls.getField("studentTests").get(invoke);
            List list2 = (List) cls.getField("professorTests").get(invoke);
            long sum = list.stream().mapToLong(SolutionChecker::isTestPassed).sum();
            long sum2 = list2.stream().mapToLong(SolutionChecker::isTestPassed).sum();
            int size = list.size() + list2.size();
            int i = (int) (sum + sum2);
            if (size == 0) {
                System.out.println("⚠️  NO TESTS FOUND");
                return true;
            }
            if (i == size) {
                System.out.println("✅ PASSED (" + i + "/" + size + ")");
                return true;
            }
            System.out.println("❌ FAILED (" + i + "/" + size + " passed)");
            System.out.println();
            printFailedTests(str, list, list2);
            return false;
        } catch (Exception e) {
            System.out.println("❌ ERROR: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private static long isTestPassed(Object obj) {
        try {
            return obj.getClass().getField("passed").getBoolean(obj) ? 1L : 0L;
        } catch (Exception e) {
            return 0L;
        }
    }

    private static void printFailedTests(String str, List<Object> list, List<Object> list2) {
        boolean z = false;
        for (Object obj : list) {
            if (!isTestResultPassed(obj)) {
                if (!z) {
                    System.out.println("   Failed Tests for " + str + ":");
                    System.out.println("   " + "─".repeat(60));
                    z = true;
                }
                printTestResultDetails(obj, "Student Test");
            }
        }
        for (Object obj2 : list2) {
            if (!isTestResultPassed(obj2)) {
                if (!z) {
                    System.out.println("   Failed Tests for " + str + ":");
                    System.out.println("   " + "─".repeat(60));
                    z = true;
                }
                printTestResultDetails(obj2, "Professor Test");
            }
        }
        if (z) {
            System.out.println("   " + "─".repeat(60));
            System.out.println();
        }
    }

    private static boolean isTestResultPassed(Object obj) {
        try {
            return obj.getClass().getField("passed").getBoolean(obj);
        } catch (Exception e) {
            return false;
        }
    }

    private static void printTestResultDetails(Object obj, String str) {
        ParsedAssertion parseOldAssertionFormat;
        try {
            Class<?> cls = obj.getClass();
            String stringField = getStringField(cls, obj, "methodName", "unknown");
            String stringField2 = getStringField(cls, obj, "errorType", null);
            String stringField3 = getStringField(cls, obj, "expectedValue", null);
            String stringField4 = getStringField(cls, obj, "actualValue", null);
            Integer intField = getIntField(cls, obj, "lineNumber");
            if (stringField4 != null && stringField4.contains("expected : |") && stringField4.contains("| but have |") && (parseOldAssertionFormat = parseOldAssertionFormat(stringField4)) != null) {
                stringField3 = parseOldAssertionFormat.expected;
                stringField4 = parseOldAssertionFormat.actual;
                if (stringField2 == null) {
                    stringField2 = parseOldAssertionFormat.assertionType;
                }
                if (intField == null || intField.intValue() == 0) {
                    intField = parseOldAssertionFormat.lineNumber;
                }
            }
            System.out.println("   ❌ " + str + ": " + stringField);
            if (stringField2 != null && !"error".equals(stringField2)) {
                System.out.println("      Assertion: " + stringField2);
            }
            if (intField != null && intField.intValue() > 0) {
                System.out.println("      Line: " + intField);
            }
            if (stringField3 != null && stringField4 != null && !stringField3.isEmpty()) {
                System.out.println("      Expected: |" + stringField3 + "|");
                System.out.println("      Actual:   |" + stringField4 + "|");
            } else if (stringField4 != null) {
                System.out.println("      Error: " + stringField4);
            }
            System.out.println();
        } catch (Exception e) {
            System.out.println("   ❌ Test failed (details unavailable): " + e.getMessage());
            System.out.println();
        }
    }

    private static String getStringField(Class<?> cls, Object obj, String str, String str2) {
        try {
            Object obj2 = cls.getField(str).get(obj);
            return obj2 != null ? obj2.toString() : str2;
        } catch (Exception e) {
            return str2;
        }
    }

    private static Integer getIntField(Class<?> cls, Object obj, String str) {
        try {
            Object obj2 = cls.getField(str).get(obj);
            if (obj2 instanceof Integer) {
                return (Integer) obj2;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: ijava.jar:ijava2/tools/SolutionChecker$ParsedAssertion.class */
    public static class ParsedAssertion {
        String assertionType;
        String expected;
        String actual;
        Integer lineNumber;

        ParsedAssertion(String str, String str2, String str3, Integer num) {
            this.assertionType = str;
            this.expected = str2;
            this.actual = str3;
            this.lineNumber = num;
        }
    }

    private static ParsedAssertion parseOldAssertionFormat(String str) {
        int i;
        int indexOf;
        try {
            String str2 = null;
            String str3 = null;
            String str4 = null;
            Integer num = null;
            if (str.startsWith("[") && str.contains("]")) {
                str2 = str.substring(1, str.indexOf("]"));
            }
            int indexOf2 = str.indexOf("expected : |");
            if (indexOf2 >= 0 && (indexOf = str.indexOf("| but have |", (i = indexOf2 + 12))) >= 0) {
                str3 = str.substring(i, indexOf);
            }
            int indexOf3 = str.indexOf("| but have |");
            if (indexOf3 >= 0) {
                int i2 = indexOf3 + 12;
                int indexOf4 = str.indexOf("| in line", i2);
                if (indexOf4 >= 0) {
                    str4 = str.substring(i2, indexOf4);
                } else {
                    int indexOf5 = str.indexOf("|", i2);
                    if (indexOf5 >= 0) {
                        str4 = str.substring(i2, indexOf5);
                    }
                }
            }
            int indexOf6 = str.indexOf(" in line ");
            if (indexOf6 >= 0) {
                try {
                    num = Integer.valueOf(Integer.parseInt(str.substring(indexOf6 + 9).replace("|", "").trim()));
                } catch (NumberFormatException e) {
                }
            }
            if (str3 != null && str4 != null) {
                return new ParsedAssertion(str2, str3, str4, num);
            }
            return null;
        } catch (Exception e2) {
            return null;
        }
    }
}
