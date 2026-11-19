package defpackage;

/* loaded from: ijava.jar:BissextileTest.class */
class BissextileTest extends HiddenTest {
    BissextileTest() {
    }

    void testBissextile_2020_true(Program program) {
        assertEquals(true, ((Bissextile) program).bissextile(2020));
    }

    void testBissextile_2019_false(Program program) {
        assertEquals(false, ((Bissextile) program).bissextile(2019));
    }

    void testBissextile_1969_false(Program program) {
        assertEquals(false, ((Bissextile) program).bissextile(1969));
    }

    void testBissextile_1968_true(Program program) {
        assertEquals(true, ((Bissextile) program).bissextile(1968));
    }

    void testBissextile_1888_true(Program program) {
        assertEquals(true, ((Bissextile) program).bissextile(1888));
    }
}
