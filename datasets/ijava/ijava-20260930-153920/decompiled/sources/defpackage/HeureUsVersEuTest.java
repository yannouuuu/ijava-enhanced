package defpackage;

import ijava2.clitools.StudentInteractionSequence;

/* loaded from: ijava.jar:HeureUsVersEuTest.class */
public class HeureUsVersEuTest extends HiddenTest {
    void _test(Program program, int i, int i2, String str, int i3, int i4) {
        expectOutput(program, StudentInteractionSequence.OutputCondition.hasSubstring("heures"));
        provideInput(program, Integer.valueOf(i));
        expectOutput(program, StudentInteractionSequence.OutputCondition.hasSubstring("minutes"));
        provideInput(program, Integer.valueOf(i2));
        expectPrompt(program);
        provideInput(program, str);
        if (i4 < 10) {
            expectOutput(program, StudentInteractionSequence.OutputCondition.beginsWith(i3 + ":"), StudentInteractionSequence.OutputCondition.terminatesWith(i4 + "\n"));
        } else {
            expectOutput(program, StudentInteractionSequence.OutputCondition.is(String.format("%d:%d\n", Integer.valueOf(i3), Integer.valueOf(i4))));
        }
        program.algorithm();
    }

    void test5_20am(Program program) {
        _test(program, 5, 20, "AM", 5, 20);
    }

    void test5_20pm(Program program) {
        _test(program, 5, 20, "PM", 17, 20);
    }

    void test10_49pm(Program program) {
        _test(program, 10, 49, "PM", 22, 49);
    }

    void test12_10am(Program program) {
        _test(program, 12, 10, "AM", 0, 10);
    }

    void test12_10pm(Program program) {
        _test(program, 12, 10, "PM", 12, 10);
    }

    void test1_5am(Program program) {
        _test(program, 1, 5, "AM", 1, 5);
    }
}
