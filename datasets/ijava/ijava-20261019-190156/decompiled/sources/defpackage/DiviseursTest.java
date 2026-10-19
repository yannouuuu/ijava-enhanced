package defpackage;

import ijava2.clitools.StudentInteractionSequence;

/* loaded from: ijava.jar:DiviseursTest.class */
class DiviseursTest extends HiddenTest {
    DiviseursTest() {
    }

    void _test(Program program, int i, String str) {
        provideInput(program, Integer.valueOf(i));
        expectOutput(program, StudentInteractionSequence.OutputCondition.terminatesWith("Diviseurs :" + str + ".\n"));
        program.algorithm();
    }

    void test_1(Program program) {
        _test(program, 1, " 1");
    }

    void test_2(Program program) {
        _test(program, 2, " 2 1");
    }

    void test_5(Program program) {
        _test(program, 5, " 5 1");
    }

    void test_9(Program program) {
        _test(program, 9, " 9 3 1");
    }

    void test_10(Program program) {
        _test(program, 10, " 10 5 2 1");
    }
}
