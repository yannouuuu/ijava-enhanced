package defpackage;

import ijava2.clitools.StudentInteractionSequence;

/* loaded from: ijava.jar:HeureEuVersUsTest.class */
public class HeureEuVersUsTest extends HiddenTest {
    void _test(Program program, int i, int i2, int i3, int i4, String str) {
        expectOutput(program, StudentInteractionSequence.OutputCondition.hasSubstring("heures"));
        provideInput(program, Integer.valueOf(i));
        expectOutput(program, StudentInteractionSequence.OutputCondition.hasSubstring("minutes"));
        provideInput(program, Integer.valueOf(i2));
        if (i4 < 10) {
            expectOutput(program, StudentInteractionSequence.OutputCondition.beginsWith(i3 + ":"), StudentInteractionSequence.OutputCondition.terminatesWith(i4 + str));
        } else {
            expectOutput(program, StudentInteractionSequence.OutputCondition.is(String.format("%d:%d%s\n", Integer.valueOf(i3), Integer.valueOf(i4), str)));
        }
        program.algorithm();
    }

    void test5_20(Program program) {
        _test(program, 5, 20, 5, 20, "AM");
    }

    void test17_20(Program program) {
        _test(program, 17, 20, 5, 20, "PM");
    }

    void test22_49(Program program) {
        _test(program, 22, 49, 10, 49, "PM");
    }

    void test0_10(Program program) {
        _test(program, 0, 10, 12, 10, "AM");
    }

    void test12_10(Program program) {
        _test(program, 12, 10, 12, 10, "PM");
    }
}
