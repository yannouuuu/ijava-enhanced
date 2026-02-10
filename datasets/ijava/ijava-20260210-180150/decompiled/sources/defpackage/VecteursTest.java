package defpackage;

import ijava2.clitools.StudentInteractionSequence;

/* loaded from: ijava.jar:VecteursTest.class */
class VecteursTest extends HiddenTest {
    VecteursTest() {
    }

    void test_allFunctionsDefined(Program program) {
        assertFunctionIsDefined(int[].class, "readVector", new Class[0], program);
        assertFunctionIsDefined(Boolean.TYPE, "equals", new Class[]{int[].class, int[].class}, program);
        assertFunctionIsDefined(String.class, "toString", new Class[]{int[].class}, program);
        assertFunctionIsDefined(int[].class, "add", new Class[]{int[].class, int[].class}, program);
        assertFunctionIsDefined(Integer.TYPE, "scalarProduct", new Class[]{int[].class, int[].class}, program);
    }

    void test_readVector(Program program) {
        expectOutput(program, StudentInteractionSequence.OutputCondition.is("Saisir les trois éléments du vecteur\n"));
        provideInput(program, 1);
        provideInput(program, 2);
        provideInput(program, 3);
        assertArrayEquals(new int[]{1, 2, 3}, ((Vecteurs) program).readVector());
    }
}
