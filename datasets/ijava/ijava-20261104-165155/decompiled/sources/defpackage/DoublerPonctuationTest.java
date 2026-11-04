package defpackage;

import ijava2.clitools.StudentInteractionSequence;

/* loaded from: ijava.jar:DoublerPonctuationTest.class */
class DoublerPonctuationTest extends HiddenTest {
    DoublerPonctuationTest() {
    }

    void _test(Program program, String str, String str2) {
        provideInput(program, str);
        expectOutput(program, StudentInteractionSequence.OutputCondition.is(str2));
        program.algorithm();
    }

    void test_ouiEXC_is_ouiEXCEXC(Program program) {
        _test(program, "oui!", "oui!!");
    }

    /* renamed from: test_exemple_2_énoncé, reason: contains not printable characters */
    void m1test_exemple_2_nonc(Program program) {
        _test(program, "Moi ? je ne l’ai pas fait !", "Moi ?? je ne l’ai pas fait !!");
    }
}
