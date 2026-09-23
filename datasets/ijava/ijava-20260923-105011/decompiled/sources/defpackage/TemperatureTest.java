package defpackage;

import ijava2.clitools.StudentInteractionSequence;

/* loaded from: ijava.jar:TemperatureTest.class */
class TemperatureTest extends HiddenTest {
    TemperatureTest() {
    }

    void test_0_moins273(Program program) {
        expectPrompt(program);
        provideInput(program, 0);
        provideInput(program, -273);
        expectOutput(program, StudentInteractionSequence.OutputCondition.terminatesWith("Le maximum est 0.\n"));
        program.algorithm();
    }

    void test_0_26_moins273(Program program) {
        expectPrompt(program);
        provideInput(program, 0);
        provideInput(program, 26);
        provideInput(program, -273);
        expectOutput(program, StudentInteractionSequence.OutputCondition.terminatesWith("Le maximum est 26.\n"));
        program.algorithm();
    }

    void test_moins17_moins273(Program program) {
        expectPrompt(program);
        provideInput(program, -17);
        provideInput(program, -273);
        expectOutput(program, StudentInteractionSequence.OutputCondition.terminatesWith("Le maximum est -17.\n"));
        program.algorithm();
    }
}
