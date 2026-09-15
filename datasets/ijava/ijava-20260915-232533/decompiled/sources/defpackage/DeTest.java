package defpackage;

import ijava2.clitools.StudentInteractionSequence;

/* loaded from: ijava.jar:DeTest.class */
class DeTest extends HiddenTest {
    DeTest() {
    }

    private int runOnce(Program program) {
        expectOutput(program, StudentInteractionSequence.OutputCondition.matchesRE("[1-6]\\n?", "un nombre entre 1 et 6"));
        program.algorithm();
        return Integer.parseInt(program.getSilentTestsOutput().trim());
    }

    void testAllValuesAppear(Program program) {
        program.setSilentTests(true);
        boolean[] zArr = new boolean[6];
        for (int i = 0; i < 100; i++) {
            int runOnce = runOnce(program);
            program.clearTestInformation();
            zArr[runOnce - 1] = true;
        }
        for (int i2 = 0; i2 < zArr.length; i2++) {
            if (!zArr[i2]) {
                throw new RuntimeException("The number " + (i2 + 1) + " seems to never be randomly chosen.");
            }
        }
    }
}
