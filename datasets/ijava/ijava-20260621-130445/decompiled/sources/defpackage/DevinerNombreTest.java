package defpackage;

import ijava2.clitools.StudentInteractionSequence;

/* loaded from: ijava.jar:DevinerNombreTest.class */
class DevinerNombreTest extends HiddenTest {
    DevinerNombreTest() {
    }

    void test_50(Program program) {
        expectOutput(program, StudentInteractionSequence.OutputCondition.is("Est-ce que le nombre est 50 ?\n"));
        provideInput(program, '=');
        expectOutput(program, StudentInteractionSequence.OutputCondition.terminatesWith("Il fallait trouver 50 !\n"));
        program.algorithm();
    }

    void test_25(Program program) {
        expectOutput(program, StudentInteractionSequence.OutputCondition.is("Est-ce que le nombre est 50 ?\n"));
        provideInput(program, '-');
        expectOutput(program, StudentInteractionSequence.OutputCondition.is("Est-ce que le nombre est 25 ?\n"));
        provideInput(program, '=');
        expectOutput(program, StudentInteractionSequence.OutputCondition.terminatesWith("Il fallait trouver 25 !\n"));
        program.algorithm();
    }

    void test_75(Program program) {
        expectOutput(program, StudentInteractionSequence.OutputCondition.is("Est-ce que le nombre est 50 ?\n"));
        provideInput(program, '+');
        expectOutput(program, StudentInteractionSequence.OutputCondition.is("Est-ce que le nombre est 75 ?\n"));
        provideInput(program, '=');
        expectOutput(program, StudentInteractionSequence.OutputCondition.terminatesWith("Il fallait trouver 75 !\n"));
        program.algorithm();
    }
}
