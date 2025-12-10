package ijava2.clitools;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Objects;

/* loaded from: ijava.jar:ijava2/clitools/TestAssertions.class */
public class TestAssertions {
    public static void assertEqual(Object obj, Object obj2, String str, int i) {
        assertEqual(obj, obj2, str, i, null, null);
    }

    public static void assertEqual(Object obj, Object obj2, String str, int i, String[] strArr) {
        assertEqual(obj, obj2, str, i, strArr, null);
    }

    public static void assertEqual(Object obj, Object obj2, String str, int i, String str2) {
        assertEqual(obj, obj2, str, i, null, str2);
    }

    public static void assertEqual(Object obj, Object obj2, String str, int i, String[] strArr, String str2) {
        if (!Objects.equals(obj, obj2)) {
            throw new AssertionTestException(str, "assertEqual", obj, obj2, i, strArr, str2);
        }
    }

    public static void assertTrue(boolean z, String str, int i) {
        assertTrue(z, str, i, null);
    }

    public static void assertTrue(boolean z, String str, int i, String str2) {
        if (!z) {
            throw new AssertionTestException(str, "assertTrue", true, false, i, null, str2);
        }
    }

    public static void assertFalse(boolean z, String str, int i) {
        assertFalse(z, str, i, null);
    }

    public static void assertFalse(boolean z, String str, int i, String str2) {
        if (z) {
            throw new AssertionTestException(str, "assertFalse", false, true, i, null, str2);
        }
    }

    public static void assertNull(Object obj, String str, int i) {
        assertNull(obj, str, i, null);
    }

    public static void assertNull(Object obj, String str, int i, String str2) {
        if (obj != null) {
            throw new AssertionTestException(str, "assertNull", null, obj, i, null, str2);
        }
    }

    public static void assertNotNull(Object obj, String str, int i) {
        assertNotNull(obj, str, i, null);
    }

    public static void assertNotNull(Object obj, String str, int i, String str2) {
        if (obj == null) {
            throw new AssertionTestException(str, "assertNotNull", "<non-null>", null, i, null, str2);
        }
    }

    public static void assertSame(Object obj, Object obj2, String str, int i) {
        if (obj != obj2) {
            throw new AssertionTestException(str, "assertSame", obj, obj2, i);
        }
    }

    public static void assertNotSame(Object obj, Object obj2, String str, int i) {
        if (obj == obj2) {
            throw new AssertionTestException(str, "assertNotSame", "<different object>", obj2, i);
        }
    }

    public static void assertArrayEquals(Object[] objArr, Object[] objArr2, String str, int i) {
        if (!Arrays.deepEquals(objArr, objArr2)) {
            throw new AssertionTestException(str, "assertArrayEquals", formatArray(objArr), formatArray(objArr2), i);
        }
    }

    private static String formatArray(Object obj) {
        if (obj == null) {
            return "null";
        }
        if (!obj.getClass().isArray()) {
            return obj.toString();
        }
        StringBuilder sb = new StringBuilder("[");
        int length = Array.getLength(obj);
        for (int i = 0; i < length; i++) {
            Object obj2 = Array.get(obj, i);
            if (obj2 != null && obj2.getClass().isArray()) {
                sb.append(formatArray(obj2));
            } else {
                sb.append(obj2);
            }
            if (i < length - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public static void assertContains(String str, String str2, String str3, int i) {
        if (str == null || !str.contains(str2)) {
            throw new AssertionTestException(str3, "assertContains", "string containing '" + str2 + "'", str, i);
        }
    }

    public static void assertStartsWith(String str, String str2, String str3, int i) {
        if (str == null || !str.startsWith(str2)) {
            throw new AssertionTestException(str3, "assertStartsWith", "string starting with '" + str2 + "'", str, i);
        }
    }

    public static void assertEndsWith(String str, String str2, String str3, int i) {
        if (str == null || !str.endsWith(str2)) {
            throw new AssertionTestException(str3, "assertEndsWith", "string ending with '" + str2 + "'", str, i);
        }
    }

    public static void fail(String str, String str2, int i) {
        throw new AssertionTestException(str2, "fail", "<success>", str, i);
    }

    private static int extractLineNumber() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (int i = 3; i < stackTrace.length; i++) {
            StackTraceElement stackTraceElement = stackTrace[i];
            String className = stackTraceElement.getClassName();
            if (!className.startsWith("ijava2.clitools") && !className.equals("HiddenTest") && !className.endsWith("Test")) {
                return stackTraceElement.getLineNumber();
            }
        }
        return 0;
    }

    public static void assertEqual(Object obj, Object obj2, String str, String str2) {
        assertEqual(obj, obj2, str, 0, str2);
    }

    public static void assertTrue(boolean z, String str, String str2) {
        assertTrue(z, str, 0, str2);
    }

    public static void assertFalse(boolean z, String str, String str2) {
        assertFalse(z, str, 0, str2);
    }

    public static void assertEqualAuto(Object obj, Object obj2, String str, String str2) {
        assertEqual(obj, obj2, str, extractLineNumber(), str2);
    }

    public static void assertTrueAuto(boolean z, String str, String str2) {
        assertTrue(z, str, extractLineNumber(), str2);
    }

    public static void assertFalseAuto(boolean z, String str, String str2) {
        assertFalse(z, str, extractLineNumber(), str2);
    }
}
