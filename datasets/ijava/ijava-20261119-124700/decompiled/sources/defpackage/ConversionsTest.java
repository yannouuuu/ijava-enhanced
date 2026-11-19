package defpackage;

import ijava2.clitools.StudentInteractionSequence;

/* loaded from: ijava.jar:ConversionsTest.class */
class ConversionsTest extends HiddenTest {
    ConversionsTest() {
    }

    void testOutputIsGood(Program program) {
        expectOutput(program, StudentInteractionSequence.OutputCondition.is("(int) 4.6 ->4\n(double) 4 ->4.0\n2.1 + 3 -> 5.1\n(int) 'A' -> 65\n (char) 66 -> B\n (int) 3.7 * 2 -> 6\n(int) (3.7 * 2) -> 7\n \"ABC\" + (char) 65 ABCA\n"));
        program.algorithm();
    }
}
