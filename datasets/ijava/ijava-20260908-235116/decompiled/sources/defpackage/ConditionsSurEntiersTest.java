package defpackage;

import ijava2.clitools.StudentInteractionSequence;

/* loaded from: ijava.jar:ConditionsSurEntiersTest.class */
public class ConditionsSurEntiersTest extends HiddenTest {
    void _test(Program program, int i, int i2, int i3, String str) {
        provideInput(program, Integer.valueOf(i));
        provideInput(program, Integer.valueOf(i2));
        provideInput(program, Integer.valueOf(i3));
        expectOutput(program, StudentInteractionSequence.OutputCondition.terminatesWith(str));
        program.algorithm();
    }

    void test_0_0_0(Program program) {
        _test(program, 0, 0, 0, "0 n'est pas supérieur à 5\n0+0=0\nIl est faux que 0 est inférieur à 0 et à 0\nIl est vrai que 0<=0<=0\n0 est le plus petit parmi 0,0,0\n");
    }

    void test_1_0_5(Program program) {
        _test(program, 1, 0, 5, "1 n'est pas supérieur à 5\n1+0 n'est pas égal à 5\n0 est inférieur à 1 et à 5\nIl n'est pas vrai que 1<=0<=5\n5 n'est pas le plus petit parmi 1,0,5\n");
    }

    void test_2_4_moins6(Program program) {
        _test(program, 2, 4, -6, "2 n'est pas supérieur à 5\n2+4 n'est pas égal à -6\nIl est faux que 4 est inférieur à 2 et à -6\nIl n'est pas vrai que 2<=4<=-6\n-6 est le plus petit parmi 2,4,-6\n");
    }

    void test_0_0_34(Program program) {
        _test(program, 0, 0, 34, "0 n'est pas supérieur à 5\n0+0 n'est pas égal à 34\nIl est faux que 0 est inférieur à 0 et à 34\nIl est vrai que 0<=0<=34\n34 n'est pas le plus petit parmi 0,0,34\n");
    }

    void test_2_4_2(Program program) {
        _test(program, 2, 4, 2, "2 n'est pas supérieur à 5\n2+4 n'est pas égal à 2\nIl est faux que 4 est inférieur à 2 et à 2\nIl n'est pas vrai que 2<=4<=2\n2 est le plus petit parmi 2,4,2\n");
    }
}
