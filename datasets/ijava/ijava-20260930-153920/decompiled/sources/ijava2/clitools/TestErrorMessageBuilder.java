package ijava2.clitools;

import java.util.Arrays;
import java.util.stream.Collectors;

/* loaded from: ijava.jar:ijava2/clitools/TestErrorMessageBuilder.class */
public class TestErrorMessageBuilder {
    public static String buildMissingMethodMessage(String str, Class<?>[] clsArr, String str2) {
        return String.format("Method '%s(%s)' is not implemented in class %s", str, clsArr != null ? (String) Arrays.stream(clsArr).map((v0) -> {
            return v0.getSimpleName();
        }).collect(Collectors.joining(", ")) : "", str2);
    }

    public static String buildMissingMethodMessage(String str, Class<?>[] clsArr, Class<?> cls, String str2) {
        return String.format("Method '%s%s(%s)' is not implemented in class %s", cls != null ? cls.getSimpleName() + " " : "", str, clsArr != null ? (String) Arrays.stream(clsArr).map((v0) -> {
            return v0.getSimpleName();
        }).collect(Collectors.joining(", ")) : "", str2);
    }

    public static String buildAssertionFailureMessage(String str, Object obj, Object obj2, int i) {
        StringBuilder sb = new StringBuilder();
        sb.append("Assertion ").append(str).append(" failed");
        if (i > 0) {
            sb.append(" at line ").append(i);
        }
        sb.append(":\n  Expected: ").append(formatValue(obj));
        sb.append("\n  Actual:   ").append(formatValue(obj2));
        return sb.toString();
    }

    public static String buildRuntimeErrorMessage(String str, int i, String str2) {
        StringBuilder sb = new StringBuilder();
        sb.append("Runtime error");
        if (i > 0) {
            sb.append(" at line ").append(i).append(" in ").append(str2).append(".java");
        }
        sb.append(": ").append(str != null ? str : "Unknown error");
        return sb.toString();
    }

    public static String buildTestSkippedMessage(String str, String str2) {
        return String.format("⚠️  Skipping test %s: %s", str, str2);
    }

    public static String buildUnexpectedIOMessage(String str, String str2) {
        return String.format("Expected: %s, but received: %s", str, str2);
    }

    public static String formatValue(Object obj) {
        if (obj == null) {
            return "null";
        }
        if (obj instanceof String) {
            return "\"" + String.valueOf(obj) + "\"";
        }
        if (obj instanceof Character) {
            return "'" + String.valueOf(obj) + "'";
        }
        if (obj.getClass().isArray()) {
            if (obj instanceof Object[]) {
                return Arrays.toString((Object[]) obj);
            }
            if (obj instanceof int[]) {
                return Arrays.toString((int[]) obj);
            }
            if (obj instanceof double[]) {
                return Arrays.toString((double[]) obj);
            }
            if (obj instanceof boolean[]) {
                return Arrays.toString((boolean[]) obj);
            }
            if (obj instanceof char[]) {
                return Arrays.toString((char[]) obj);
            }
            if (obj instanceof byte[]) {
                return Arrays.toString((byte[]) obj);
            }
            if (obj instanceof short[]) {
                return Arrays.toString((short[]) obj);
            }
            if (obj instanceof long[]) {
                return Arrays.toString((long[]) obj);
            }
            if (obj instanceof float[]) {
                return Arrays.toString((float[]) obj);
            }
        }
        return obj.toString();
    }

    public static String buildTestSummaryMessage(String str, int i, int i2, int i3) {
        return String.format("%s: %d%% (%d/%d tests passed)", str, Integer.valueOf(i3), Integer.valueOf(i), Integer.valueOf(i2));
    }

    public static String buildStringDifferenceMessage(String str, String str2) {
        if (str == null && str2 == null) {
            return "Both values are null";
        }
        if (str == null) {
            return "Expected: null, but was: \"" + str2 + "\"";
        }
        if (str2 == null) {
            return "Expected: \"" + str + "\", but was: null";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Expected: \"").append(str).append("\"\n");
        sb.append("Actual:   \"").append(str2).append("\"\n");
        if (!str.equals(str2)) {
            int min = Math.min(str.length(), str2.length());
            int i = -1;
            int i2 = 0;
            while (true) {
                if (i2 >= min) {
                    break;
                }
                if (str.charAt(i2) == str2.charAt(i2)) {
                    i2++;
                } else {
                    i = i2;
                    break;
                }
            }
            if (i >= 0) {
                sb.append("First difference at position ").append(i);
                sb.append(": expected '").append(str.charAt(i));
                sb.append("' but was '").append(str2.charAt(i)).append("'");
            } else if (str.length() != str2.length()) {
                sb.append("Length difference: expected ").append(str.length());
                sb.append(" characters, but was ").append(str2.length());
            }
        }
        return sb.toString();
    }
}
