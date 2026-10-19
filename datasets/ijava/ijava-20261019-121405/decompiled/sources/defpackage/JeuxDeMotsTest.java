package defpackage;

import ijava2.clitools.StudentInteractionSequence;

/* loaded from: ijava.jar:JeuxDeMotsTest.class */
class JeuxDeMotsTest extends HiddenTest {
    JeuxDeMotsTest() {
    }

    void testOutputForEtatIsTate(Program program) {
        expectOutput(program, StudentInteractionSequence.OutputCondition.is("tate\n"));
        program.algorithm();
    }
}
