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

    void test0nuit(Program program) {
        _test(program, 0, "nuit");
    }

    void test4nuit(Program program) {
        _test(program, 4, "nuit");
    }

    void test6matinee(Program program) {
        _test(program, 6, "matinée");
    }

    void test11matinee(Program program) {
        _test(program, 11, "matinée");
    }

    void test14apresMidi(Program program) {
        _test(program, 14, "après-midi");
    }

    void test19soiree(Program program) {
        _test(program, 19, "soirée");
    }

    void test23nuit(Program program) {
        _test(program, 23, "nuit");
    }

    void test24erreur(Program program) {
        _test(program, 24, "erreur");
    }

    void testNegatifErreur(Program program) {
        _test(program, -3, "erreur");
    }
}
