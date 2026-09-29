package ijava2.clitools;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: ijava.jar:ijava2/clitools/TestClass.class */
public @interface TestClass {
    String studentClass();

    String description() default "";

    String[] skills() default {};
}
