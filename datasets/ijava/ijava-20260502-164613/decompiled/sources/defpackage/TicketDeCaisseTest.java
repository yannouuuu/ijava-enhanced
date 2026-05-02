package defpackage;

import ijava2.clitools.StudentInteractionSequence;

/* loaded from: ijava.jar:TicketDeCaisseTest.class */
class TicketDeCaisseTest extends HiddenTest {
    TicketDeCaisseTest() {
    }

    void test_0(Program program) {
        provideInput(program, 0);
        expectOutput(program, StudentInteractionSequence.OutputCondition.terminatesWith("Total=0\n"));
        program.algorithm();
    }

    void test_2_4_0(Program program) {
        provideInput(program, 2);
        provideInput(program, 4);
        provideInput(program, 0);
        expectOutput(program, StudentInteractionSequence.OutputCondition.terminatesWith("Total=6\n"));
        program.algorithm();
    }

    void test_moins2_2_0(Program program) {
        provideInput(program, -2);
        provideInput(program, 2);
        provideInput(program, 0);
        expectOutput(program, StudentInteractionSequence.OutputCondition.terminatesWith("Total=0\n"));
        program.algorithm();
    }
}
