package defpackage;

import ijava2.clitools.StudentInteractionSequence;

/* loaded from: ijava.jar:JeuxDeTypeTest.class */
class JeuxDeTypeTest extends HiddenTest {
    JeuxDeTypeTest() {
    }

    void testBasicJeuxDeType(Program program) {
        expectOutput(program, StudentInteractionSequence.OutputCondition.is("A. Turing aurait eu 110 ans en 2022\n"));
        program.algorithm();
    }
}
