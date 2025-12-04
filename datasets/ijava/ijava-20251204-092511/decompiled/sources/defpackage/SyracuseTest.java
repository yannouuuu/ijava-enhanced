package defpackage;

import ijava2.clitools.StudentInteractionSequence;

/* loaded from: ijava.jar:SyracuseTest.class */
class SyracuseTest extends HiddenTest {
    SyracuseTest() {
    }

    void _test(Program program, int i, String str) {
        expectOutput(program, StudentInteractionSequence.OutputCondition.is("Entrez un nombre: "));
        provideInput(program, Integer.valueOf(i));
        expectOutput(program, StudentInteractionSequence.OutputCondition.terminatesWith("Trajectoire: " + str + ".\n"));
        program.algorithm();
    }

    void test_7(Program program) {
        _test(program, 7, "7, 22, 11, 34, 17, 52, 26, 13, 40, 20, 10, 5, 16, 8, 4, 2, 1");
    }

    void test_8(Program program) {
        _test(program, 8, "8, 4, 2, 1");
    }
}
