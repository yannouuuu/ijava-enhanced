package defpackage;

import ijava2.clitools.StudentInteractionSequence;

/* loaded from: ijava.jar:SyracuseCompletTest.class */
class SyracuseCompletTest extends HiddenTest {
    SyracuseCompletTest() {
    }

    void _test(Program program, int i, String str, int i2, int i3, int i4, int i5) {
        expectOutput(program, StudentInteractionSequence.OutputCondition.is("Entrez un nombre: "));
        provideInput(program, Integer.valueOf(i));
        expectOutput(program, StudentInteractionSequence.OutputCondition.containsSequence("Trajectoire: ", str, ".\n", "Altitude max: ", i2, ".\n", "Durée de vol: ", i3, ".\n", "Durée de vol en altitude: " + i4, "", ".\n", "Facteur d'expansion: ", i5, ".\n"));
        program.algorithm();
    }

    void _testBrut(Program program, int i, String str) {
        expectOutput(program, StudentInteractionSequence.OutputCondition.is("Entrez un nombre: "));
        provideInput(program, Integer.valueOf(i));
        expectOutput(program, StudentInteractionSequence.OutputCondition.is(str));
        program.algorithm();
    }

    void test_7(Program program) {
        _testBrut(program, 7, "Trajectoire: 7, 22, 11, 34, 17, 52, 26, 13, 40, 20, 10, 5, 16, 8, 4, 2, 1.\nAltitude max: 52\nDurée de vol: 16\nDurée de vol en altitude: 11\nFacteur d'expansion: 7\n");
    }

    void test_8(Program program) {
        _testBrut(program, 8, "Trajectoire: 8, 4, 2, 1.\nAltitude max: 8\nDurée de vol: 3\nDurée de vol en altitude: 1\nFacteur d'expansion: 1\n");
    }
}
