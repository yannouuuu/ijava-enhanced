package defpackage;

import ijava2.clitools.StudentInteractionSequence;

/* loaded from: ijava.jar:NiOuiNiNonTest.class */
class NiOuiNiNonTest extends HiddenTest {
    NiOuiNiNonTest() {
    }

    void test_oui(Program program) {
        provideInput(program, "oui");
        expectOutput(program, StudentInteractionSequence.OutputCondition.terminatesWith("Perdu!\n"));
        program.algorithm();
    }

    void test_non(Program program) {
        provideInput(program, "non");
        expectOutput(program, StudentInteractionSequence.OutputCondition.terminatesWith("Perdu!\n"));
        program.algorithm();
    }

    void test_vide_non(Program program) {
        provideInput(program, "");
        provideInput(program, "non");
        expectOutput(program, StudentInteractionSequence.OutputCondition.terminatesWith("Perdu!\n"));
        program.algorithm();
    }

    void test_hello_non(Program program) {
        provideInput(program, "hello");
        provideInput(program, "non");
        expectOutput(program, StudentInteractionSequence.OutputCondition.terminatesWith("Perdu!\n"));
        program.algorithm();
    }

    void test_plein_d_entrees_oui(Program program) {
        for (int i = 0; i < 10; i++) {
            provideInput(program, "hello n°" + (i + 1));
        }
        provideInput(program, "non");
        expectOutput(program, StudentInteractionSequence.OutputCondition.terminatesWith("Perdu!\n"));
        program.algorithm();
    }
}
