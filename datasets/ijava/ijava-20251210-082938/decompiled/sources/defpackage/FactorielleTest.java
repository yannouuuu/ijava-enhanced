package defpackage;

import ijava2.clitools.StudentInteractionSequence;

/* loaded from: ijava.jar:FactorielleTest.class */
public class FactorielleTest extends HiddenTest {
    void test_3is6(Program program) {
        expectPrompt(program);
        provideInput(program, 3);
        expectOutput(program, StudentInteractionSequence.OutputCondition.is("3! = 6\n"));
        program.algorithm();
    }

    void test_4is24(Program program) {
        expectPrompt(program);
        provideInput(program, 4);
        expectOutput(program, StudentInteractionSequence.OutputCondition.is("4! = 24\n"));
        program.algorithm();
    }

    void test_0is1(Program program) {
        expectPrompt(program);
        provideInput(program, 0);
        expectOutput(program, StudentInteractionSequence.OutputCondition.is("0! = 1\n"));
        program.algorithm();
    }

    void test_1is1(Program program) {
        expectPrompt(program);
        provideInput(program, 1);
        expectOutput(program, StudentInteractionSequence.OutputCondition.is("1! = 1\n"));
        program.algorithm();
    }
}
