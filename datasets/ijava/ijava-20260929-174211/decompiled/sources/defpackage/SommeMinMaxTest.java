package defpackage;

import ijava2.clitools.StudentInteractionSequence;

/* loaded from: ijava.jar:SommeMinMaxTest.class */
class SommeMinMaxTest extends HiddenTest {
    SommeMinMaxTest() {
    }

    void _test(Program program, int i, int i2, int i3) {
        provideInput(program, Integer.valueOf(i));
        provideInput(program, Integer.valueOf(i2));
        expectOutput(program, StudentInteractionSequence.OutputCondition.is(i3 + "\n"));
        program.algorithm();
    }

    void test_4_6(Program program) {
        _test(program, 4, 6, 15);
    }

    void test_moins4_3(Program program) {
        _test(program, -4, 3, -4);
    }
}
