package defpackage;

import ijava2.clitools.StudentInteractionSequence;

/* loaded from: ijava.jar:NettoyerChaineTest.class */
public class NettoyerChaineTest extends HiddenTest {
    void _test(Program program, String str, String str2) {
        provideInput(program, str);
        expectOutput(program, StudentInteractionSequence.OutputCondition.terminatesWith(str2));
        program.algorithm();
    }

    void test_aaa(Program program) {
        _test(program, " aaa ", ">aaa<\n");
    }

    void test_space_aaa(Program program) {
        _test(program, " aaa", ">aaa<\n");
    }

    void test_aaa_space(Program program) {
        _test(program, " aaa", ">aaa<\n");
    }

    void test_space_aaa_space(Program program) {
        _test(program, " aaa ", ">aaa<\n");
    }

    void test_emptyString(Program program) {
        _test(program, "", "><\n");
    }
}
