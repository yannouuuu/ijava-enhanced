package defpackage;

/* loaded from: ijava.jar:Semaine1Test.class */
class Semaine1Test extends HiddenTest {
    final int RED = 0;
    final int GREEN = 1;
    final int BLUE = 2;

    Semaine1Test() {
    }

    void test_charToInt(Program program) {
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 < 10) {
                assertEquals(i2, ((Semaine1) program).charToInt((char) (48 + i2)));
                i = i2 + 1;
            } else {
                return;
            }
        }
    }

    void test_toString_255_255(Program program) {
        assertEquals("255", ((Semaine1) program).toString(255));
    }

    void test_toString_55_055(Program program) {
        assertEquals("055", ((Semaine1) program).toString(55));
    }

    void test_toString_5_005(Program program) {
        assertEquals("005", ((Semaine1) program).toString(5));
    }

    void test_color_225_55_5_255055005(Program program) {
        assertEquals("225055005", ((Semaine1) program).color(225, 55, 5));
    }

    void test_color_2_25_255_002025255(Program program) {
        assertEquals("002025255", ((Semaine1) program).color(2, 25, 255));
    }

    void test_color_255_255_255_255255255(Program program) {
        assertEquals("255255255", ((Semaine1) program).color(255, 255, 255));
    }

    void test_color_0_0_0_000000000(Program program) {
        assertEquals("000000000", ((Semaine1) program).color(0, 0, 0));
    }

    void test_primaryColorToInt_255_255(Program program) {
        assertEquals(255, ((Semaine1) program).primaryColorToInt("255"));
    }

    void test_primaryColorToInt_025_25(Program program) {
        assertEquals(25, ((Semaine1) program).primaryColorToInt("025"));
    }

    void test_primaryColorToInt_005_5(Program program) {
        assertEquals(5, ((Semaine1) program).primaryColorToInt("005"));
    }

    void test_primaryColorIndex_RED_0(Program program) {
        assertEquals(0, ((Semaine1) program).primaryColorIndex(0));
    }

    void test_primaryColorIndex_GREEN_3(Program program) {
        assertEquals(3, ((Semaine1) program).primaryColorIndex(1));
    }

    void test_primaryColorIndex_GREEN_6(Program program) {
        assertEquals(6, ((Semaine1) program).primaryColorIndex(2));
    }

    void test_get_111222333_RED_111(Program program) {
        assertEquals(111, ((Semaine1) program).get("111222333", 0));
    }

    void test_get_111222333_GREEN_222(Program program) {
        assertEquals(222, ((Semaine1) program).get("111222333", 1));
    }

    void test_get_111222333_BLUE_333(Program program) {
        assertEquals(333, ((Semaine1) program).get("111222333", 2));
    }

    void test_set_111222333_RED_000(Program program) {
        assertEquals("000222333", ((Semaine1) program).set("111222333", 0, 0));
    }

    void test_set_111222333_GREEN_000(Program program) {
        assertEquals("111000333", ((Semaine1) program).set("111222333", 1, 0));
    }

    void test_set_111222333_BLUE_000(Program program) {
        assertEquals("111222000", ((Semaine1) program).set("111222333", 2, 0));
    }
}
