package ijava2.clitools;

import java.util.Arrays;

/* loaded from: ijava.jar:ijava2/clitools/AssertionTestException.class */
public class AssertionTestException extends StudentTestException {
    public final String assertionType;
    public final Object expectedValue;
    public final Object actualValue;
    public final String[] inputs;
    public final String hint;

    public AssertionTestException(String str, String str2, Object obj, Object obj2, int i, String[] strArr) {
        this(str, str2, obj, obj2, i, strArr, null);
    }

    public AssertionTestException(String str, String str2, Object obj, Object obj2, int i) {
        this(str, str2, obj, obj2, i, null, null);
    }

    public AssertionTestException(String str, String str2, Object obj, Object obj2, int i, String[] strArr, String str3) {
        super(str, buildMessage(str2, obj, obj2, str3), i);
        this.assertionType = str2;
        this.expectedValue = obj;
        this.actualValue = obj2;
        this.inputs = strArr != null ? (String[]) strArr.clone() : null;
        this.hint = str3;
    }

    private static String buildMessage(String str, Object obj, Object obj2) {
        return buildMessage(str, obj, obj2, null);
    }

    private static String buildMessage(String str, Object obj, Object obj2, String str2) {
        String format = String.format("Assertion %s failed: expected <%s> but was <%s>", str, formatValue(obj), formatValue(obj2));
        if (str2 != null && !str2.trim().isEmpty()) {
            return format + " - " + str2;
        }
        return format;
    }

    private static String formatValue(Object obj) {
        return obj == null ? "null" : obj instanceof String ? "\"" + String.valueOf(obj) + "\"" : obj instanceof Character ? "'" + String.valueOf(obj) + "'" : obj.toString();
    }

    public String getDetailedMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("Assertion ").append(this.assertionType).append(" failed");
        if (this.lineNumber > 0) {
            sb.append(" at line ").append(this.lineNumber);
        }
        sb.append(":\n");
        if (this.hint != null && !this.hint.trim().isEmpty()) {
            sb.append("  �� Hint: ").append(this.hint).append("\n");
        }
        sb.append("  Expected: ").append(formatValue(this.expectedValue)).append("\n");
        sb.append("  Actual:   ").append(formatValue(this.actualValue));
        if (this.inputs != null && this.inputs.length > 0) {
            sb.append("\n  Inputs: ").append(Arrays.toString(this.inputs));
        }
        return sb.toString();
    }
}
