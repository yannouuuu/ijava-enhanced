package ijava2.clitools;

import java.util.Arrays;
import java.util.stream.Collectors;

/* loaded from: ijava.jar:ijava2/clitools/MissingMethodException.class */
public class MissingMethodException extends StudentTestException {
    public final String missingMethodName;
    public final Class<?>[] expectedParameters;
    public final Class<?> returnType;

    public MissingMethodException(String str, String str2, Class<?>[] clsArr) {
        super(str, buildMessage(str2, clsArr, null), 0);
        this.missingMethodName = str2;
        this.expectedParameters = clsArr != null ? (Class[]) clsArr.clone() : new Class[0];
        this.returnType = null;
    }

    public MissingMethodException(String str, String str2, Class<?>[] clsArr, Class<?> cls) {
        super(str, buildMessage(str2, clsArr, cls), 0);
        this.missingMethodName = str2;
        this.expectedParameters = clsArr != null ? (Class[]) clsArr.clone() : new Class[0];
        this.returnType = cls;
    }

    private static String buildMessage(String str, Class<?>[] clsArr, Class<?> cls) {
        return String.format("Method not implemented: %s%s(%s)", cls != null ? cls.getSimpleName() + " " : "", str, clsArr != null ? (String) Arrays.stream(clsArr).map((v0) -> {
            return v0.getSimpleName();
        }).collect(Collectors.joining(", ")) : "");
    }

    public String getMethodSignature() {
        return buildMessage(this.missingMethodName, this.expectedParameters, this.returnType);
    }
}
