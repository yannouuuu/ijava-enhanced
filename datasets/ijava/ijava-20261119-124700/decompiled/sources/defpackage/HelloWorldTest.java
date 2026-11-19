package defpackage;

import ijava2.clitools.StudentInteractionSequence;

/* loaded from: ijava.jar:HelloWorldTest.class */
class HelloWorldTest extends HiddenTest {
    HelloWorldTest() {
    }

    void testBasicHelloOutput(Program program) {
        expectOutput(program, StudentInteractionSequence.OutputCondition.is("Hello World !\n"));
        program.algorithm();
    }
}
