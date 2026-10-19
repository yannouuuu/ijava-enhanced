package defpackage;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.SOURCE)
/* loaded from: ijava.jar:Student.class */
public @interface Student {

    /* loaded from: ijava.jar:Student$Mode.class */
    public enum Mode {
        IGNORE,
        COPY,
        STUB
    }

    Mode value() default Mode.STUB;
}
