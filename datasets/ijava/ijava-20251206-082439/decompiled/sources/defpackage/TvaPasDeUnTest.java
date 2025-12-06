package defpackage;

import ijava2.clitools.StudentInteractionSequence;

/* loaded from: ijava.jar:TvaPasDeUnTest.class */
public class TvaPasDeUnTest extends HiddenTest {
    private void _test(Program program, int i) {
        String[] strArr = new String[5 * i];
        for (int i2 = 0; i2 < i; i2++) {
            strArr[5 * i2] = (i2 + 1);
            strArr[(5 * i2) + 1] = "HT";
            strArr[(5 * i2) + 2] = ((int) (1.196d * (i2 + 1)));
            strArr[(5 * i2) + 3] = "TTC";
            strArr[(5 * i2) + 4] = "\n";
        }
        expectPrompt(program);
        provideInput(program, Integer.valueOf(i));
        expectOutput(program, StudentInteractionSequence.OutputCondition.containsSequence(strArr));
        program.algorithm();
    }

    void test10(Program program) {
        _test(program, 10);
    }

    void test0(Program program) {
        expectPrompt(program);
        provideInput(program, 0);
        program.algorithm();
    }
}
