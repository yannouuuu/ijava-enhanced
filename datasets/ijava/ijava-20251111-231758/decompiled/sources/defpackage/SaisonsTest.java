package defpackage;

/* loaded from: ijava.jar:SaisonsTest.class */
class SaisonsTest extends HiddenTest {
    SaisonsTest() {
    }

    void testSaisonMeteorologique_0_erreur(Program program) {
        assertEquals("erreur", ((Saisons) program).saisonMeteorologique(0));
    }

    void testSaisonMeteorologique_1_erreur(Program program) {
        assertEquals("hiver", ((Saisons) program).saisonMeteorologique(1));
    }

    void testSaisonMeteorologique_2_erreur(Program program) {
        assertEquals("hiver", ((Saisons) program).saisonMeteorologique(2));
    }

    void testSaisonMeteorologique_3_erreur(Program program) {
        assertEquals("printemps", ((Saisons) program).saisonMeteorologique(3));
    }

    void testSaisonMeteorologique_4_erreur(Program program) {
        assertEquals("printemps", ((Saisons) program).saisonMeteorologique(4));
    }

    void testSaisonMeteorologique_5_erreur(Program program) {
        assertEquals("printemps", ((Saisons) program).saisonMeteorologique(5));
    }

    void testSaisonMeteorologique_6_erreur(Program program) {
        assertEquals("été", ((Saisons) program).saisonMeteorologique(6));
    }

    void testSaisonMeteorologique_7_erreur(Program program) {
        assertEquals("été", ((Saisons) program).saisonMeteorologique(7));
    }

    void testSaisonMeteorologique_8_erreur(Program program) {
        assertEquals("été", ((Saisons) program).saisonMeteorologique(8));
    }

    void testSaisonMeteorologique_9_erreur(Program program) {
        assertEquals("automne", ((Saisons) program).saisonMeteorologique(9));
    }

    void testSaisonMeteorologique_10_erreur(Program program) {
        assertEquals("automne", ((Saisons) program).saisonMeteorologique(10));
    }

    void testSaisonMeteorologique_11_erreur(Program program) {
        assertEquals("automne", ((Saisons) program).saisonMeteorologique(11));
    }

    void testSaisonMeteorologique_12_erreur(Program program) {
        assertEquals("hiver", ((Saisons) program).saisonMeteorologique(12));
    }

    void testSaisonMeteorologique_13_erreur(Program program) {
        assertEquals("erreur", ((Saisons) program).saisonMeteorologique(13));
    }

    void testNombreJours_0(Program program) {
        assertEquals(0, ((Saisons) program).nombreJoursMois(0));
    }

    /* renamed from: testNombreJours_février, reason: contains not printable characters */
    void m7testNombreJours_fvrier(Program program) {
        assertEquals(28, ((Saisons) program).nombreJoursMois(2));
    }

    void testNombreJours_mois_30(Program program) {
        Saisons saisons = (Saisons) program;
        assertEquals(30, saisons.nombreJoursMois(4));
        assertEquals(30, saisons.nombreJoursMois(6));
        assertEquals(30, saisons.nombreJoursMois(9));
        assertEquals(30, saisons.nombreJoursMois(11));
    }

    void testNombreJours_mois_31(Program program) {
        Saisons saisons = (Saisons) program;
        assertEquals(31, saisons.nombreJoursMois(1));
        assertEquals(31, saisons.nombreJoursMois(3));
        assertEquals(31, saisons.nombreJoursMois(5));
        assertEquals(31, saisons.nombreJoursMois(7));
        assertEquals(31, saisons.nombreJoursMois(8));
        assertEquals(31, saisons.nombreJoursMois(10));
        assertEquals(31, saisons.nombreJoursMois(12));
    }

    void testSaisonAstronomique_11_31(Program program) {
        assertEquals("automne", ((Saisons) program).saisonAstronomique(11, 11));
    }

    void testSaisonAstronomique_20_3(Program program) {
        assertEquals("hiver", ((Saisons) program).saisonAstronomique(20, 3));
    }

    void testSaisonAstronomique_31_3(Program program) {
        assertEquals("printemps", ((Saisons) program).saisonAstronomique(31, 3));
    }

    void testSaisonAstronomique_20_6(Program program) {
        assertEquals("printemps", ((Saisons) program).saisonAstronomique(20, 6));
    }

    void testSaisonAstronomique_20_9(Program program) {
        assertEquals("été", ((Saisons) program).saisonAstronomique(20, 9));
    }

    void testSaisonAstronomique_20_12(Program program) {
        assertEquals("automne", ((Saisons) program).saisonAstronomique(20, 12));
    }

    void testSaisonAstronomique_21_12(Program program) {
        assertEquals("hiver", ((Saisons) program).saisonAstronomique(21, 12));
    }
}
