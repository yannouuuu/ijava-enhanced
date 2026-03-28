package defpackage;

import ijava2.clitools.StudentInteractionSequence;

/* loaded from: ijava.jar:SommeMultiplesTest.class */
class SommeMultiplesTest extends HiddenTest {
    SommeMultiplesTest() {
    }

    void _test(Program program, int i, int i2, int i3, int i4) {
        provideInput(program, Integer.valueOf(i));
        provideInput(program, Integer.valueOf(i2));
        provideInput(program, Integer.valueOf(i3));
        expectOutput(program, StudentInteractionSequence.OutputCondition.is(i4 + "\n"));
        program.algorithm();
    }

    void test_11_20_5_is_35(Program program) {
        _test(program, 11, 20, 5, 35);
    }

    void test_2_10_2_is_30(Program program) {
        _test(program, 2, 10, 2, 30);
    }
}
