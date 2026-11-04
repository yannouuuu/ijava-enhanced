package defpackage;

import ijava2.clitools.StudentInteractionSequence;

/* loaded from: ijava.jar:DessineIutTest.class */
class DessineIutTest extends HiddenTest {
    DessineIutTest() {
    }

    void _test(Program program, int i, String str) {
        expectPrompt(program);
        provideInput(program, Integer.valueOf(i));
        expectOutput(program, StudentInteractionSequence.OutputCondition.is(str));
        program.algorithm();
    }

    void test_taille_3(Program program) {
        _test(program, 3, "\nIII\n I \nIII\n\nU U\nU U\nUUU\n\nTTT\n T \n T \n\n");
    }

    void test_taille_5(Program program) {
        _test(program, 5, "\nIIIII\n  I  \n  I  \n  I  \nIIIII\n\nU   U\nU   U\nU   U\nU   U\nUUUUU\n\nTTTTT\n  T  \n  T  \n  T  \n  T  \n\n");
    }
}
