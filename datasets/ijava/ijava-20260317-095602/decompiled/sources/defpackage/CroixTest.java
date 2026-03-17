package defpackage;

import ijava2.clitools.StudentInteractionSequence;

/* loaded from: ijava.jar:CroixTest.class */
class CroixTest extends HiddenTest {
    CroixTest() {
    }

    void test_1_u(Program program) {
        expectPrompt(program);
        provideInput(program, 1);
        expectPrompt(program);
        provideInput(program, 'u');
        expectOutput(program, StudentInteractionSequence.OutputCondition.terminatesWith("u\n"));
        program.algorithm();
    }

    void test_5_u(Program program) {
        expectPrompt(program);
        provideInput(program, 5);
        expectPrompt(program);
        provideInput(program, 'u');
        expectOutput(program, StudentInteractionSequence.OutputCondition.terminatesWith("u   u\n u u\n  u\n u u\nu   u\n"));
        program.algorithm();
    }
}
