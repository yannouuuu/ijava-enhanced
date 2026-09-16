package defpackage;

import ijava2.clitools.StudentInteractionSequence;

/* loaded from: ijava.jar:DeMultifaceTest.class */
class DeMultifaceTest extends HiddenTest {
    DeMultifaceTest() {
    }

    private int runOnce(Program program, int i) {
        expectPrompt(program);
        provideInput(program, Integer.valueOf(i));
        expectOutput(program, StudentInteractionSequence.OutputCondition.matchesRE(String.format("[1-%d]\\n?", Integer.valueOf(i)), "un nombre entre 1 et " + i));
        program.algorithm();
        String trim = program.getSilentTestsOutput().trim();
        return Integer.parseInt(trim.charAt(trim.length() - 1));
    }

    void test11FacesAppear(Program program) {
        program.setSilentTests(true);
        boolean[] zArr = new boolean[9];
        for (int i = 0; i < 200; i++) {
            int runOnce = runOnce(program, 9);
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
