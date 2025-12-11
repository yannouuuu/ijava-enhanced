package defpackage;

import ijava2.clitools.StudentInteractionSequence;

/* loaded from: ijava.jar:RemplacementTest.class */
class RemplacementTest extends HiddenTest {
    RemplacementTest() {
    }

    void _test(Program program, String str, char c, char c2, String str2) {
        expectOutput(program, StudentInteractionSequence.OutputCondition.is("Veuillez saisir votre texte : "));
        provideInput(program, str);
        expectOutput(program, StudentInteractionSequence.OutputCondition.is("Caractère à remplacer : "));
        provideInput(program, Character.valueOf(c));
        expectOutput(program, StudentInteractionSequence.OutputCondition.is("Caractère de remplacement : "));
        provideInput(program, Character.valueOf(c2));
        expectOutput(program, StudentInteractionSequence.OutputCondition.is(str2));
        program.algorithm();
    }

    void test_HELLo_o_O_HELLO(Program program) {
        _test(program, "HELLo", 'o', 'O', "HELLO\n");
    }

    void test_BlaBla_i_u_BlaBla(Program program) {
        _test(program, "BlaBla", 'i', 'u', "BlaBla\n");
    }

    void test_oooTITREooo_o_x_xxxTITRExxx(Program program) {
        _test(program, "oooTITREooo", 'o', 'x', "xxxTITRExxx\n");
    }
}
