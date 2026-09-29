package defpackage;

import ijava2.clitools.StudentInteractionSequence;

/* loaded from: ijava.jar:CinemaTest.class */
class CinemaTest extends HiddenTest {
    CinemaTest() {
    }

    void _test(Program program, int i, int i2, int i3, String str) {
        expectOutput(program, StudentInteractionSequence.OutputCondition.beginsWith("Age du spectateur : "));
        provideInput(program, Integer.valueOf(i));
        expectOutput(program, StudentInteractionSequence.OutputCondition.beginsWith("Option 3D ? (1 si oui, autre chiffre si non) : "));
        provideInput(program, Integer.valueOf(i2));
        expectOutput(program, StudentInteractionSequence.OutputCondition.beginsWith("Abonné ? (1 si oui, autre chiffre si non) : "));
        provideInput(program, Integer.valueOf(i3));
        expectOutput(program, StudentInteractionSequence.OutputCondition.terminatesWith("Coût du billet : " + str + " euros\n"));
        program.algorithm();
    }

    void test_9_1_0(Program program) {
        _test(program, 9, 1, 0, "8.0");
    }

    void test_64_0_1(Program program) {
        _test(program, 64, 0, 1, "7.2");
    }
}
