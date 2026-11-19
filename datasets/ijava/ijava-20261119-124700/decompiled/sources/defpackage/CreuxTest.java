package defpackage;

import ijava2.clitools.StudentInteractionSequence;

/* loaded from: ijava.jar:CreuxTest.class */
class CreuxTest extends HiddenTest {
    CreuxTest() {
    }

    void test_1_a(Program program) {
        expectPrompt(program);
        provideInput(program, 1);
        expectPrompt(program);
        provideInput(program, 'a');
        expectOutput(program, StudentInteractionSequence.OutputCondition.terminatesWith("a\n"));
        program.algorithm();
    }

    void test_5_x(Program program) {
        expectPrompt(program);
        provideInput(program, 5);
        expectPrompt(program);
        provideInput(program, 'x');
        expectOutput(program, StudentInteractionSequence.OutputCondition.terminatesWith("x\nxx\nx x\nx  x\nxxxxx\n"));
        program.algorithm();
    }
}
