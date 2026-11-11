package defpackage;

import ijava2.clitools.StudentInteractionSequence;

/* loaded from: ijava.jar:Tri3Test.class */
class Tri3Test extends HiddenTest {
    Tri3Test() {
    }

    void _test(Program program, char c, char c2, char c3, String str) {
        provideInput(program, Character.valueOf(c));
        provideInput(program, Character.valueOf(c2));
        provideInput(program, Character.valueOf(c3));
        expectOutput(program, StudentInteractionSequence.OutputCondition.is(str));
        program.algorithm();
    }

    void test_abc(Program program) {
        _test(program, 'a', 'b', 'c', "abc\n");
    }

    void test_bac(Program program) {
        _test(program, 'b', 'a', 'c', "abc\n");
    }

    void test_cba(Program program) {
        _test(program, 'c', 'b', 'a', "abc\n");
    }

    void test_bca(Program program) {
        _test(program, 'b', 'c', 'a', "abc\n");
    }

    void test_cab(Program program) {
        _test(program, 'c', 'a', 'b', "abc\n");
    }

    void test_edb(Program program) {
        _test(program, 'e', 'd', 'b', "bde\n");
    }
}
