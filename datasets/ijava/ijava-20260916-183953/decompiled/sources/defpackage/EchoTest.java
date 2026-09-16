package defpackage;

import ijava2.clitools.StudentInteractionSequence;

/* loaded from: ijava.jar:EchoTest.class */
public class EchoTest extends HiddenTest {
    void testHello2(Program program) {
        expectPrompt(program);
        provideInput(program, "Hello");
        expectPrompt(program);
        provideInput(program, 2);
        expectOutput(program, StudentInteractionSequence.OutputCondition.is("Hello\nHello\n"));
        program.algorithm();
    }

    void testJoyeuxanniversaire3(Program program) {
        expectPrompt(program);
        provideInput(program, "Joyeux anniversaire");
        expectPrompt(program);
        provideInput(program, 3);
        expectOutput(program, StudentInteractionSequence.OutputCondition.is("Joyeux anniversaire\nJoyeux anniversaire\nJoyeux anniversaire\n"));
        program.algorithm();
    }

    void testA0(Program program) {
        expectPrompt(program);
        provideInput(program, "A");
        expectPrompt(program);
        provideInput(program, 0);
        program.algorithm();
    }
}
