package defpackage;

import ijava2.clitools.StudentInteractionSequence;

/* loaded from: ijava.jar:LettreAuHasardTest.class */
class LettreAuHasardTest extends HiddenTest {
    LettreAuHasardTest() {
    }

    private char runOnce(Program program) {
        expectOutput(program, StudentInteractionSequence.OutputCondition.matchesRE("[A-Z]\n?", "une lettre majuscule entre A et Z"));
        program.algorithm();
        return program.getSilentTestsOutput().charAt(0);
    }

    void testAllLettersAppear(Program program) {
        program.setSilentTests(true);
        boolean[] zArr = new boolean[26];
        for (int i = 0; i < 500; i++) {
            char runOnce = runOnce(program);
            program.clearTestInformation();
            zArr[runOnce - 'A'] = true;
        }
        for (int i2 = 0; i2 < zArr.length; i2++) {
            if (!zArr[i2]) {
                throw new RuntimeException("The letter " + ((char) (i2 + 65)) + " seems to never be randomly chosen.");
            }
        }
    }
}
