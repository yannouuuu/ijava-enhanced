package defpackage;

import ijava2.clitools.StudentInteractionSequence;

/* loaded from: ijava.jar:IutTest.class */
class IutTest extends HiddenTest {
    IutTest() {
    }

    void _test(Program program, int i, String str) {
        expectPrompt(program);
        provideInput(program, Integer.valueOf(i));
        expectOutput(program, StudentInteractionSequence.OutputCondition.terminatesWith(str));
        program.algorithm();
    }

    void test_taille_3(Program program) {
        _test(program, 3, "III\n I\nIII\n\nU U\nU U\nUUU\n\nTTT\n T\n T\n");
    }

    void test_taille_5(Program program) {
        _test(program, 5, "IIIII\n  I\n  I\n  I\nIIIII\n\nU   U\nU   U\nU   U\nU   U\nUUUUU\n\nTTTTT\n  T\n  T\n  T\n  T\n");
    }
}
