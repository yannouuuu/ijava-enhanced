package defpackage;

import ijava2.clitools.StudentInteractionSequence;

/* loaded from: ijava.jar:MinMaxMoyenneTest.class */
class MinMaxMoyenneTest extends HiddenTest {
    MinMaxMoyenneTest() {
    }

    void _test(Program program, int[] iArr, int i, int i2, char c) {
        expectPrompt(program);
        for (int i3 : iArr) {
            provideInput(program, Integer.valueOf(i3));
        }
        expectOutput(program, StudentInteractionSequence.OutputCondition.is("Minimum = " + i + " / Maximum = " + i2 + " / Moyenne = " + c + "\n"));
        program.algorithm();
    }

    void test_0(Program program) {
        _test(program, new int[]{0}, 0, 0, '?');
    }

    void test_m2_2_m1_1_0(Program program) {
        _test(program, new int[]{-2, 2, -1, 1, 0}, -2, 2, '0');
    }

    void test_1_2_3_4_5_0(Program program) {
        _test(program, new int[]{1, 2, 3, 4, 5, 0}, 1, 5, '3');
    }
}
