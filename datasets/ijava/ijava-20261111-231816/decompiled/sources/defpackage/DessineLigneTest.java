package defpackage;

import ijava2.clitools.StudentInteractionSequence;

/* loaded from: ijava.jar:DessineLigneTest.class */
class DessineLigneTest extends HiddenTest {
    DessineLigneTest() {
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
        _test(program, '*', 5, "*****");
    }

    void test_espace_7(Program program) {
        _test(program, ' ', 7, "       ");
    }

    void test_dollar_1(Program program) {
        _test(program, '$', 1, "$");
    }
}
