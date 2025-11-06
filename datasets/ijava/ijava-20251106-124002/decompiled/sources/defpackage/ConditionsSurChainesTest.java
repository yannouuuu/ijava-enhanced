package defpackage;

import ijava2.clitools.StudentInteractionSequence;

/* loaded from: ijava.jar:ConditionsSurChainesTest.class */
public class ConditionsSurChainesTest extends HiddenTest {
    void _test(Program program, String str, String str2, String str3, String str4) {
        provideInput(program, str);
        provideInput(program, str2);
        provideInput(program, str3);
        expectOutput(program, StudentInteractionSequence.OutputCondition.terminatesWith(str4));
        program.algorithm();
    }

    void test_bonjour_jour_bon(Program program) {
        _test(program, "bonjour", "jour", "bon", "|bonjour| a 5 caractères ou plus\n|bonjour| n'est pas égal à |jour|\n|jour| n'est pas avant |bonjour| dans le dictionnaire\n|bon| est préfixe de |bonjour|\n|bonjour| est plus long que |bon|\n");
    }

    void test_chaineVide_aa_aaa(Program program) {
        _test(program, "", "aa", "aaa", "|| a moins de 5 caractères\n|| n'est pas égal à |aa|\n|aa| n'est pas avant || dans le dictionnaire\n|aaa| n'est pas préfixe de ||\n|aaa| est au moins aussi long que ||\n");
    }

    void test_aaa_aaa_x(Program program) {
        _test(program, "aaa", "aaa", "x", "|aaa| a moins de 5 caractères\n|aaa|=|aaa|\n|aaa| n'est pas avant |aaa| dans le dictionnaire\n|x| n'est pas préfixe de |aaa|\n|aaa| est plus long que |x|\n");
    }
}
