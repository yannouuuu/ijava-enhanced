package defpackage;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
/* loaded from: ijava.jar:inject.class */
public @interface inject {
    String value();
}
