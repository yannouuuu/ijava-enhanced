package defpackage;

import ijava2.clitools.StudentInteractionSequence;

/* loaded from: ijava.jar:PlusPetitNombreTest.class */
public class PlusPetitNombreTest extends HiddenTest {
    void _test(Program program, int i, int i2, String str) {
        expectOutput(program, StudentInteractionSequence.OutputCondition.beginsWith("Entrez deux nombres :"));
        provideInput(program, Integer.valueOf(i));
        provideInput(program, Integer.valueOf(i2));
        expectOutput(program, StudentInteractionSequence.OutputCondition.terminatesWith(str));
        program.algorithm();
    }

    void test_12_7(Program program) {
        _test(program, 12, 7, "Le plus petit est 7\n");
    }

    void test_7_12(Program program) {
        _test(program, 7, 12, "Le plus petit est 7\n");
    }

    void test_1_1(Program program) {
        _test(program, 1, 1, "Le plus petit est 1\n");
    }
}
