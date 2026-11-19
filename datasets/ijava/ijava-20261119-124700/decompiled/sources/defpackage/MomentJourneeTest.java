package defpackage;

import ijava2.clitools.StudentInteractionSequence;

/* loaded from: ijava.jar:MomentJourneeTest.class */
public class MomentJourneeTest extends HiddenTest {
    private void _test(Program program, int i, String str) {
        expectPrompt(program);
        provideInput(program, Integer.valueOf(i));
        expectOutput(program, StudentInteractionSequence.OutputCondition.is(str + "\n"));
        program.algorithm();
    }

    void test_0_nuit(Program program) {
        _test(program, 0, "nuit");
    }

    void test_4_nuit(Program program) {
        _test(program, 4, "nuit");
    }

    void test_6_matinee(Program program) {
        _test(program, 6, "matinée");
    }

    void test_11_matinee(Program program) {
        _test(program, 11, "matinée");
    }

    void test_14_apresMidi(Program program) {
        _test(program, 14, "après-midi");
    }

    void test_19_soiree(Program program) {
        _test(program, 19, "soirée");
    }

    void test_23_nuit(Program program) {
        _test(program, 23, "nuit");
    }

    void test_24_erreur(Program program) {
        _test(program, 24, "erreur");
    }

    void testNegatifErreur(Program program) {
        _test(program, -3, "erreur");
    }
}
