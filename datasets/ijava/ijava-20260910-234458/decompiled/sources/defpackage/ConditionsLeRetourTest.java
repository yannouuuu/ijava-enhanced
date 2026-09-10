package defpackage;

import ijava2.clitools.StudentInteractionSequence;

/* loaded from: ijava.jar:ConditionsLeRetourTest.class */
public class ConditionsLeRetourTest extends HiddenTest {
    void _test(Program program, String str, String str2, String str3, String str4) {
        expectOutput(program, StudentInteractionSequence.OutputCondition.beginsWith("Saisissez successivement trois chaînes de caractères"));
        provideInput(program, str);
        provideInput(program, str2);
        provideInput(program, str3);
        expectOutput(program, StudentInteractionSequence.OutputCondition.terminatesWith(str4));
        program.algorithm();
    }

    void test_bonjour_jour_bon(Program program) {
        _test(program, "bonjour", "jour", "bon", "|jour| n'est pas avant |bonjour| dans le dictionnaire\n|bon| est préfixe de |bonjour|\n");
    }

    void test_chaineVide_aa_aaa(Program program) {
        _test(program, "", "aa", "aaa", "|aa| n'est pas avant || dans le dictionnaire\n|aaa| n'est pas préfixe de ||\n");
    }

    void test_aaa_aaa_x(Program program) {
        _test(program, "aaa", "aaa", "x", "|aaa| n'est pas avant |aaa| dans le dictionnaire\n|x| n'est pas préfixe de |aaa|\n");
    }
}
