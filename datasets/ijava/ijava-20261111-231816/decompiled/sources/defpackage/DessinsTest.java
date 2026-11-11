package defpackage;

import ijava2.clitools.StudentInteractionSequence;

/* loaded from: ijava.jar:DessinsTest.class */
class DessinsTest extends HiddenTest {
    DessinsTest() {
    }

    void test_trianglePlein_exists(Program program) {
        assertFunctionIsDefined(String.class, "trianglePlein", new Class[]{Integer.TYPE, Character.TYPE}, program);
        assertEquals("o\noo\nooo\noooo\nooooo\noooooo\n", ((Dessins) program).trianglePlein(6, 'o'));
    }

    void test_triangleCreux_exists(Program program) {
        assertFunctionIsDefined(String.class, "triangleCreux", new Class[]{Integer.TYPE, Character.TYPE}, program);
        assertEquals("o\noo\no o\no  o\no   o\noooooo\n", ((Dessins) program).triangleCreux(6, 'o'));
    }

    void test_algorithm_5_o(Program program) {
        expectPrompt(program);
        provideInput(program, 5);
        expectPrompt(program);
        provideInput(program, 'o');
        expectOutput(program, StudentInteractionSequence.OutputCondition.terminatesWith("o\noo\nooo\noooo\nooooo\n\no\noo\no o\no  o\nooooo\n\n"));
        program.algorithm();
    }
}
