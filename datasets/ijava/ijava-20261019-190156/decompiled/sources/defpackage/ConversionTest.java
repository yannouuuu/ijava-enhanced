package defpackage;

import ijava2.clitools.StudentInteractionSequence;

/* loaded from: ijava.jar:ConversionTest.class */
public class ConversionTest extends HiddenTest {
    private void _test(Program program, int i) {
        String[] strArr = new String[5 * i];
        for (int i2 = 0; i2 < i; i2++) {
            strArr[5 * i2] = (i2 + 1) + " euros";
            strArr[(5 * i2) + 1] = "=";
            strArr[(5 * i2) + 2] = ((int) (135.9d * (i2 + 1)));
            strArr[(5 * i2) + 3] = "yens";
            strArr[(5 * i2) + 4] = "\\n";
        }
        expectOutput(program, StudentInteractionSequence.OutputCondition.is("Combien de lignes souhaitez-vous ? "));
        provideInput(program, Integer.valueOf(i));
        expectOutput(program, StudentInteractionSequence.OutputCondition.containsSequence(strArr));
        program.algorithm();
    }

    /* renamed from: testExempleÉnoncéTP, reason: contains not printable characters */
    void m0testExemplenoncTP(Program program) {
        _test(program, 11);
    }

    void testZeroLigne(Program program) {
        expectOutput(program, StudentInteractionSequence.OutputCondition.is("Combien de lignes souhaitez-vous ? "));
        provideInput(program, 0);
        program.algorithm();
    }
}
