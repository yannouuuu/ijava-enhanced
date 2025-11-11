package defpackage;

import ijava2.clitools.TestAssertions;

/* loaded from: ijava.jar:Semaine3Test.class */
class Semaine3Test extends HiddenTest {
    Semaine3Test() {
    }

    void test_variable_globale_lineTurtle_existe(Program program) {
        assertFieldIsDefined(Integer.TYPE, "lineTurtle", (Semaine3) program);
    }

    void test_variable_globale_columnTurtle_existe(Program program) {
        assertFieldIsDefined(Integer.TYPE, "columnTurtle", (Semaine3) program);
    }

    void test_variable_globale_penDown_existe(Program program) {
        assertFieldIsDefined(Boolean.TYPE, "penDown", (Semaine3) program);
    }

    void test_variable_globale_colorPen_existe(Program program) {
        assertFieldIsDefined(String.class, "colorPen", (Semaine3) program);
    }

    void test_variable_globale_currentImage_existe(Program program) {
        assertFieldIsDefined(String.class, "currentImage", (Semaine3) program);
    }

    void test_togglePen(Program program) {
        assertFunctionIsDefined(Void.TYPE, "togglePen", new Class[0], program);
        Semaine3 semaine3 = (Semaine3) program;
        semaine3.penDown = true;
        semaine3.togglePen();
        TestAssertions.assertFalse(semaine3.penDown, "test_togglePen", "togglePen() should change penDown from true to false");
        semaine3.togglePen();
        TestAssertions.assertTrue(semaine3.penDown, "test_togglePen", "togglePen() should change penDown from false to true");
    }

    void test_within(Program program) {
        assertFunctionIsDefined(Boolean.TYPE, "within", new Class[]{Integer.TYPE, Integer.TYPE, Integer.TYPE}, program);
        Semaine3 semaine3 = (Semaine3) program;
        TestAssertions.assertTrue(semaine3.within(0, 10, 5), "test_within", "within(0, 10, 5) should return true - 5 is between 0 and 10 (inclusive)");
        TestAssertions.assertTrue(semaine3.within(0, 10, 0), "test_within", "within(0, 10, 0) should return true - boundaries are inclusive");
        TestAssertions.assertTrue(semaine3.within(0, 10, 10), "test_within", "within(0, 10, 10) should return true - boundaries are inclusive");
        TestAssertions.assertFalse(semaine3.within(0, 10, -1), "test_within", "within(0, 10, -1) should return false - -1 is less than the minimum (0)");
        TestAssertions.assertFalse(semaine3.within(0, 10, 11), "test_within", "within(0, 10, 11) should return false - 11 is greater than the maximum (10)");
    }

    void test_inside(Program program) {
        assertFunctionIsDefined(Boolean.TYPE, "inside", new Class[]{Integer.TYPE, Integer.TYPE, Integer.TYPE}, program);
        Semaine3 semaine3 = (Semaine3) program;
        TestAssertions.assertTrue(semaine3.inside(10, 5, 5), "test_inside", "inside(10, 5, 5) should return true - position (5,5) is inside a 10x10 grid (0-9)");
        TestAssertions.assertTrue(semaine3.inside(10, 0, 0), "test_inside", "inside(10, 0, 0) should return true - (0,0) is the top-left corner of the grid");
        TestAssertions.assertTrue(semaine3.inside(10, 9, 9), "test_inside", "inside(10, 9, 9) should return true - (9,9) is the bottom-right corner of a 10x10 grid");
        TestAssertions.assertFalse(semaine3.inside(10, -1, 5), "test_inside", "inside(10, -1, 5) should return false - negative row is outside the grid");
        TestAssertions.assertFalse(semaine3.inside(10, 5, -1), "test_inside", "inside(10, 5, -1) should return false - negative column is outside the grid");
        TestAssertions.assertFalse(semaine3.inside(10, 10, 5), "test_inside", "inside(10, 10, 5) should return false - row 10 is outside a 10x10 grid (valid range: 0-9)");
        TestAssertions.assertFalse(semaine3.inside(10, 5, 10), "test_inside", "inside(10, 5, 10) should return false - column 10 is outside a 10x10 grid (valid range: 0-9)");
    }

    void test_set(Program program) {
        assertFunctionIsDefined(String.class, "set", new Class[]{String.class, Integer.TYPE, Integer.TYPE, String.class}, program);
        TestAssertions.assertEqual("255255255255255255255255255255255255000000000255255255255255255255255255255255255", ((Semaine3) program).set("255255255".repeat(9), 1, 1, "000000000"), "test_set", "set() should replace the pixel at the specified position with the new color");
    }

    void test_go_penUp(Program program) {
        assertFunctionIsDefined(Void.TYPE, "go", new Class[]{Integer.TYPE, Integer.TYPE}, program);
        Semaine3 semaine3 = (Semaine3) program;
        semaine3.setCurrentImage("255255255".repeat(25));
        semaine3.penDown = false;
        TestAssertions.assertFalse(semaine3.go(9, 9), "test_go_penUp", "go(9, 9) should return false - position (9,9) is outside the 5x5 grid");
        TestAssertions.assertFalse(semaine3.go(-1, 4), "test_go_penUp", "go(-1, 4) should return false - negative row is invalid");
        TestAssertions.assertFalse(semaine3.go(4, -1), "test_go_penUp", "go(4, -1) should return false - negative column is invalid");
        TestAssertions.assertTrue(semaine3.go(4, 4), "test_go_penUp", "go(4, 4) should return true - position (4,4) is valid within the 5x5 grid");
        TestAssertions.assertEqual("255255255", semaine3.get(semaine3.currentImage, 4, 4), "test_go_penUp", "Expected white pixel (no drawing) at position (4,4). Make sure no drawing occurs when penDown is false");
        TestAssertions.assertEqual((Object) 4, (Object) Integer.valueOf(semaine3.lineTurtle), "test_go_penUp", "After go(4, 4), lineTurtle should be 4");
        TestAssertions.assertEqual((Object) 4, (Object) Integer.valueOf(semaine3.columnTurtle), "test_go_penUp", "After go(4, 4), columnTurtle should be 4");
        TestAssertions.assertTrue(semaine3.go(0, 0), "test_go_penUp", "go(0, 0) should return true - moving back to origin");
        TestAssertions.assertEqual((Object) 0, (Object) Integer.valueOf(semaine3.lineTurtle), "test_go_penUp", "After go(0, 0), lineTurtle should be 0");
        TestAssertions.assertEqual((Object) 0, (Object) Integer.valueOf(semaine3.columnTurtle), "test_go_penUp", "After go(0, 0), columnTurtle should be 0");
        TestAssertions.assertEqual("255255255", semaine3.get(semaine3.currentImage, 0, 0), "test_go_penUp", "Expected white pixel (no drawing) at position (0,0). Pen should not draw when penDown is false");
    }

    void test_trace_on(Program program) {
        assertFunctionIsDefined(Void.TYPE, "trace", new Class[0], program);
        Semaine3 semaine3 = (Semaine3) program;
        semaine3.setCurrentImage("255255255".repeat(25));
        semaine3.lineTurtle = 2;
        semaine3.columnTurtle = 3;
        semaine3.penDown = true;
        semaine3.colorPen = "000000000";
        semaine3.trace();
        TestAssertions.assertEqual("000000000", semaine3.get(semaine3.currentImage, 2, 3), "test_trace_on", "Expected black pixel at turtle position (2,3). trace() should draw when penDown is true");
    }

    void test_trace_off(Program program) {
        assertFunctionIsDefined(Void.TYPE, "trace", new Class[0], program);
        Semaine3 semaine3 = (Semaine3) program;
        semaine3.setCurrentImage("255255255".repeat(25));
        semaine3.lineTurtle = 2;
        semaine3.columnTurtle = 3;
        semaine3.penDown = false;
        semaine3.colorPen = "000000000";
        semaine3.trace();
        TestAssertions.assertEqual("255255255", semaine3.get(semaine3.currentImage, 2, 3), "test_trace_off", "Expected white pixel at turtle position (2,3). trace() should not draw when penDown is false");
    }

    void test_go_penDown(Program program) {
        assertFunctionIsDefined(Void.TYPE, "go", new Class[]{Integer.TYPE, Integer.TYPE}, program);
        Semaine3 semaine3 = (Semaine3) program;
        semaine3.setCurrentImage("255255255".repeat(25));
        semaine3.penDown = true;
        semaine3.colorPen = "000000000";
        TestAssertions.assertFalse(semaine3.go(9, 9), "test_go_penDown", "go(9, 9) should return false - position (9,9) is outside the 5x5 grid");
        TestAssertions.assertFalse(semaine3.go(-1, 4), "test_go_penDown", "go(-1, 4) should return false - negative row is invalid");
        TestAssertions.assertFalse(semaine3.go(4, -1), "test_go_penDown", "go(4, -1) should return false - negative column is invalid");
        TestAssertions.assertTrue(semaine3.go(4, 4), "test_go_penDown", "go(4, 4) should return true - position (4,4) is valid within the 5x5 grid");
        TestAssertions.assertEqual("000000000", semaine3.get(semaine3.currentImage, 4, 4), "test_go_penDown", "Expected black pixel (drawing) at position (4,4). Make sure penDown is true and trace() is called when moving");
        TestAssertions.assertEqual((Object) 4, (Object) Integer.valueOf(semaine3.lineTurtle), "test_go_penDown", "After go(4, 4), lineTurtle should be 4");
        TestAssertions.assertEqual((Object) 4, (Object) Integer.valueOf(semaine3.columnTurtle), "test_go_penDown", "After go(4, 4), columnTurtle should be 4");
        TestAssertions.assertTrue(semaine3.go(0, 0), "test_go_penDown", "go(0, 0) should return true - moving back to origin while drawing");
        TestAssertions.assertEqual((Object) 0, (Object) Integer.valueOf(semaine3.lineTurtle), "test_go_penDown", "After go(0, 0), lineTurtle should be 0");
        TestAssertions.assertEqual((Object) 0, (Object) Integer.valueOf(semaine3.columnTurtle), "test_go_penDown", "After go(0, 0), columnTurtle should be 0");
        TestAssertions.assertEqual("000000000", semaine3.get(semaine3.currentImage, 0, 0), "test_go_penDown", "Expected black pixel (drawing) at position (0,0). Make sure the pen draws during movement when penDown is true");
    }

    void test_nord_sud_est_ouest(Program program) {
        Semaine3 semaine3 = (Semaine3) program;
        assertFieldIsDefined(new int[0].getClass(), "NORTH", semaine3);
        assertFieldIsDefined(new int[0].getClass(), "SOUTH", semaine3);
        assertFieldIsDefined(new int[0].getClass(), "EAST", semaine3);
        assertFieldIsDefined(new int[0].getClass(), "WEST", semaine3);
    }

    void test_move(Program program) {
        assertFunctionIsDefined(Boolean.TYPE, "move", new Class[]{new int[0].getClass()}, program);
        Semaine3 semaine3 = (Semaine3) program;
        semaine3.setCurrentImage("255255255".repeat(25));
        semaine3.go(3, 3);
        assertTrue(semaine3.move(semaine3.NORTH));
        assertEquals(2, semaine3.lineTurtle);
        assertEquals(3, semaine3.columnTurtle);
        assertTrue(semaine3.move(semaine3.SOUTH));
        assertEquals(3, semaine3.lineTurtle);
        assertEquals(3, semaine3.columnTurtle);
        assertTrue(semaine3.move(semaine3.EAST));
        assertEquals(3, semaine3.lineTurtle);
        assertEquals(4, semaine3.columnTurtle);
        assertTrue(semaine3.move(semaine3.WEST));
        assertEquals(3, semaine3.lineTurtle);
        assertEquals(3, semaine3.columnTurtle);
        semaine3.go(0, 0);
        assertFalse(semaine3.move(semaine3.NORTH));
        assertEquals(0, semaine3.lineTurtle);
        assertEquals(0, semaine3.columnTurtle);
        assertFalse(semaine3.move(semaine3.WEST));
        assertEquals(0, semaine3.lineTurtle);
        assertEquals(0, semaine3.columnTurtle);
        semaine3.go(4, 4);
        semaine3.go(4, 4);
        assertFalse(semaine3.move(semaine3.SOUTH));
        assertEquals(4, semaine3.lineTurtle);
        assertEquals(4, semaine3.columnTurtle);
        assertFalse(semaine3.move(semaine3.EAST));
        assertEquals(4, semaine3.lineTurtle);
        assertEquals(4, semaine3.columnTurtle);
    }
}
