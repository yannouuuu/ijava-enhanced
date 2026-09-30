package defpackage;

import ijava2.clitools.StudentInteractionSequence;

/* loaded from: ijava.jar:DessineMilieuTest.class */
class DessineMilieuTest extends HiddenTest {
    DessineMilieuTest() {
    }

    void _test(Program program, char c, int i, String str) {
        expectPrompt(program);
        provideInput(program, Character.valueOf(c));
        expectPrompt(program);
        provideInput(program, Integer.valueOf(i));
        expectOutput(program, StudentInteractionSequence.OutputCondition.is(str));
        program.algorithm();
    }

    void test_etoiles_5(Program program) {
        _test(program, '*', 5, "  *  ");
    }

    void test_i_4(Program program) {
        _test(program, 'i', 4, "  i ");
    }

    void test_pourcent_1(Program program) {
        _test(program, '%', 1, "%");
    }
}
