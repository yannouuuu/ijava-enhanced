package defpackage;

import ijava2.clitools.StudentInteractionSequence;

/* loaded from: ijava.jar:NombreMajusculesTest.class */
class NombreMajusculesTest extends HiddenTest {
    NombreMajusculesTest() {
    }

    /* renamed from: testExempleÉnoncé, reason: contains not printable characters */
    void m7testExemplenonc(Program program) {
        expectOutput(program, StudentInteractionSequence.OutputCondition.is("Votre texte : "));
        provideInput(program, "Java c’est vraiment trop COOL !");
        expectOutput(program, StudentInteractionSequence.OutputCondition.is("Il y a 5 majuscule(s) dans votre texte.\n"));
        program.algorithm();
    }

    void testPasDeMajuscule(Program program) {
        expectOutput(program, StudentInteractionSequence.OutputCondition.is("Votre texte : "));
        provideInput(program, "ijava c’est encore mieux !");
        expectOutput(program, StudentInteractionSequence.OutputCondition.is("Il y a 0 majuscule(s) dans votre texte.\n"));
        program.algorithm();
    }

    void testUniquementDesMajuscule(Program program) {
        expectOutput(program, StudentInteractionSequence.OutputCondition.is("Votre texte : "));
        provideInput(program, "IJAVAFORLIFE");
        expectOutput(program, StudentInteractionSequence.OutputCondition.is("Il y a " + length("IJAVAFORLIFE") + " majuscule(s) dans votre texte.\n"));
        program.algorithm();
    }
}
