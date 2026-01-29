package defpackage;

import ijava2.clitools.StudentInteractionSequence;

/* loaded from: ijava.jar:TvaPasDemiTest.class */
public class TvaPasDemiTest extends HiddenTest {
    void test5LignesApartir5(Program program) {
        expectPrompt(program);
        provideInput(program, 5);
        expectPrompt(program);
        provideInput(program, Double.valueOf(5.0d));
        expectOutput(program, StudentInteractionSequence.OutputCondition.containsSequence("5", "0", "HT", "5", "9", "TTC", "\n", "5", "5", "HT", "6", "5", "TTC", "\n", "6", "0", "HT", "7", "1", "TTC", "\n", "6", "5", "HT", "7", "7", "TTC", "\n", "7", "0", "HT", "8", "3", "TTC", "\n"));
        program.algorithm();
    }

    void test6LignesApartir5(Program program) {
        expectPrompt(program);
        provideInput(program, 6);
        expectPrompt(program);
        provideInput(program, Double.valueOf(5.0d));
        expectOutput(program, StudentInteractionSequence.OutputCondition.containsSequence("5", "0", "HT", "5", "9", "TTC", "\n", "5", "5", "HT", "6", "5", "TTC", "\n", "6", "0", "HT", "7", "1", "TTC", "\n", "6", "5", "HT", "7", "7", "TTC", "\n", "7", "0", "HT", "8", "3", "TTC", "\n", "7", "5", "HT", "8", "9", "TTC", "\n"));
        program.algorithm();
    }
}
