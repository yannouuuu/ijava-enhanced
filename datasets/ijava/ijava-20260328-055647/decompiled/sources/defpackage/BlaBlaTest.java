package defpackage;

import ijava2.clitools.StudentInteractionSequence;

/* loaded from: ijava.jar:BlaBlaTest.class */
class BlaBlaTest extends HiddenTest {
    BlaBlaTest() {
    }

    void _test(Program program, String str, boolean z) {
        provideInput(program, str);
        expectOutput(program, StudentInteractionSequence.OutputCondition.is(str + " = " + z + "\n"));
        program.algorithm();
    }

    void test_couscous(Program program) {
        _test(program, "couscous", true);
    }

    void test_zigzag(Program program) {
        _test(program, "zigzag", false);
    }
}
