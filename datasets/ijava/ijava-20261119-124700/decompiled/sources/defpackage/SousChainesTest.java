package defpackage;

import ijava2.clitools.StudentInteractionSequence;

/* loaded from: ijava.jar:SousChainesTest.class */
class SousChainesTest extends HiddenTest {
    SousChainesTest() {
    }

    void test_sous_chaines_Hello(Program program) {
        expectPrompt(program);
        provideInput(program, "Hello");
        expectOutput(program, StudentInteractionSequence.OutputCondition.terminatesWith("Hello\nHell\nHel\nHe\nH\nello\nell\nel\ne\nllo\nll\nl\nlo\nl\no\n"));
        program.algorithm();
    }
}
