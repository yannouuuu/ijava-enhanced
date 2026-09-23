package defpackage;

import ijava2.clitools.StudentInteractionSequence;

/* loaded from: ijava.jar:NiOuiNiNonTest.class */
class NiOuiNiNonTest extends HiddenTest {
    NiOuiNiNonTest() {
    }

    void _test(Program program, String[] strArr) {
        for (String str : strArr) {
            provideInput(program, str);
        }
        expectOutput(program, StudentInteractionSequence.OutputCondition.terminatesWith("Perdu !\n"));
        program.algorithm();
    }

    void test_oui(Program program) {
        _test(program, new String[]{"oui"});
    }

    void test_non(Program program) {
        _test(program, new String[]{"non"});
    }

    void test_OUI(Program program) {
        _test(program, new String[]{"OUI"});
    }

    void test_NON(Program program) {
        _test(program, new String[]{"NON"});
    }

    void test_vide_non(Program program) {
        _test(program, new String[]{"", "non"});
    }

    void test_hello_NON(Program program) {
        _test(program, new String[]{"hello", "NON"});
    }

    void test_plein_d_entrees_oui(Program program) {
        String[] strArr = new String[11];
        for (int i = 0; i < 10; i++) {
            strArr[i] = "hello n°" + (i + 1);
        }
        strArr[10] = "oui";
        _test(program, strArr);
    }
}
