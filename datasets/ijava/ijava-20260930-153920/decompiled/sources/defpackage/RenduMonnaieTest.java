package defpackage;

import ijava2.clitools.StudentInteractionSequence;

/* loaded from: ijava.jar:RenduMonnaieTest.class */
class RenduMonnaieTest extends HiddenTest {
    RenduMonnaieTest() {
    }

    void test33(Program program) {
        expectPrompt(program);
        provideInput(program, 33);
        expectOutput(program, StudentInteractionSequence.OutputCondition.is("Nombre de billets de 20 : 1\nNombre de billets de 10 : 1\nNombre de billets de  5 : 0\nNombre de pièces  de  2 : 1\nNombre de pièces  de  1 : 1\n"));
        program.algorithm();
    }
}
