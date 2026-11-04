package defpackage;

import ijava2.clitools.StudentInteractionSequence;

/* loaded from: ijava.jar:SuffixeTest.class */
public class SuffixeTest extends HiddenTest {
    void _test(Program program, String str, int i, String str2) {
        expectPrompt(program);
        provideInput(program, str);
        expectPrompt(program);
        provideInput(program, Integer.valueOf(i));
        expectOutput(program, StudentInteractionSequence.OutputCondition.terminatesWith(str2));
        program.algorithm();
    }

    void testBonjour_4(Program program) {
        _test(program, "Bonjour", 4, "Résultat : jour\n");
    }

    void testAuRevoir_6(Program program) {
        _test(program, "Au revoir", 6, "Résultat : revoir\n");
    }

    void testBonjour_10(Program program) {
        _test(program, "Bonjour", 10, "Erreur, pas assez de caractères !\n");
    }
}
