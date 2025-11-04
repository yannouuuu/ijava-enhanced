package defpackage;

import ijava2.clitools.StudentInteractionSequence;

/* loaded from: ijava.jar:MemeLongueurTest.class */
public class MemeLongueurTest extends HiddenTest {
    void _test(Program program, String str, String str2, String str3) {
        provideInput(program, str);
        provideInput(program, str2);
        expectOutput(program, StudentInteractionSequence.OutputCondition.terminatesWith(str3));
        program.algorithm();
    }

    void testMotsDeMemeLongueurs(Program program) {
        _test(program, "algorithme", "programmes", "Les deux mots sont de même longueur : true\n");
    }

    /* renamed from: testMotsDeLongueursDifférentes, reason: contains not printable characters */
    void m4testMotsDeLongueursDiffrentes(Program program) {
        _test(program, "algorithme", "programme", "Les deux mots sont de même longueur : false\n");
    }
}
