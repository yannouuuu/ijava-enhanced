package defpackage;

import ijava2.clitools.StudentInteractionSequence;

/* loaded from: ijava.jar:DoublerCaracteresTest.class */
class DoublerCaracteresTest extends HiddenTest {
    DoublerCaracteresTest() {
    }

    void _test(Program program, String str, String str2) {
        provideInput(program, str);
        expectOutput(program, StudentInteractionSequence.OutputCondition.is(str2));
        program.algorithm();
    }

    void test_abc_is_aabbcc(Program program) {
        _test(program, "abc", "aabbcc");
    }

    void test_hola_isEXCLAMATION_hhoollaaEXCLAMATIONEXCLAMATION(Program program) {
        _test(program, "hola!", "hhoollaa!!");
    }
}
