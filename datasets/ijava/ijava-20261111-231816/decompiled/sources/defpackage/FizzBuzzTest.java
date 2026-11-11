package defpackage;

import ijava2.clitools.StudentInteractionSequence;

/* loaded from: ijava.jar:FizzBuzzTest.class */
class FizzBuzzTest extends HiddenTest {
    FizzBuzzTest() {
    }

    void _test(Program program, int i, String str) {
        expectOutput(program, StudentInteractionSequence.OutputCondition.beginsWith("Entrez un nombre :"));
        provideInput(program, Integer.valueOf(i));
        expectOutput(program, StudentInteractionSequence.OutputCondition.terminatesWith(str));
        program.algorithm();
    }

    void test3(Program program) {
        _test(program, 3, "fizz\n");
    }

    void test9(Program program) {
        _test(program, 9, "fizz\n");
    }

    void test5(Program program) {
        _test(program, 5, "buzz\n");
    }

    void test10(Program program) {
        _test(program, 10, "buzz\n");
    }

    void test15(Program program) {
        _test(program, 15, "fizzbuzz\n");
    }

    void test7(Program program) {
        _test(program, 7, "7\n");
    }
}
