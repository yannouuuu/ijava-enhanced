package defpackage;

import ijava2.clitools.StudentInteractionSequence;

/* loaded from: ijava.jar:ChiffreAuHasardTest.class */
class ChiffreAuHasardTest extends HiddenTest {
    ChiffreAuHasardTest() {
    }

    private int runOnce(Program program) {
        expectOutput(program, StudentInteractionSequence.OutputCondition.matchesRE("[0-9]\\n?", "un chiffre entre 0 et 9"));
        program.algorithm();
        return Integer.parseInt(program.getSilentTestsOutput().trim());
    }

    void testAllNumbersAppear(Program program) {
        program.setSilentTests(true);
        boolean[] zArr = new boolean[10];
        for (int i = 0; i < 500; i++) {
            int runOnce = runOnce(program);
            program.clearTestInformation();
            zArr[runOnce] = true;
        }
        for (int i2 = 0; i2 < zArr.length; i2++) {
            if (!zArr[i2]) {
                throw new RuntimeException("The number " + i2 + " seems to never be randomly chosen.");
            }
        }
    }
}
