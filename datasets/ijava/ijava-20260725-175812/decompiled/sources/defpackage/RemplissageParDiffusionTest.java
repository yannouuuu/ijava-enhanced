package defpackage;

/* loaded from: ijava.jar:RemplissageParDiffusionTest.class */
class RemplissageParDiffusionTest extends HiddenTest {
    RemplissageParDiffusionTest() {
    }

    private char[][] imageFromStrings(String... strArr) {
        int length = length(strArr);
        int length2 = length(strArr[0]);
        char[][] cArr = new char[length][length2];
        for (int i = 0; i < length; i++) {
            for (int i2 = 0; i2 < length2; i2++) {
                cArr[i][i2] = charAt(strArr[i], i2);
            }
        }
        return cArr;
    }

    void test_diffuser_exists(Program program) {
        assertFunctionIsDefined(Void.TYPE, "diffuser", new Class[]{char[][].class, Integer.TYPE, Integer.TYPE, Character.TYPE, Character.TYPE}, program);
    }

    void test_diffuser_cas_general(Program program) {
        char[][] imageFromStrings = imageFromStrings("XXOXX", "XOOXO", "OOXXO", "XOOXX", "XOXXX");
        ((RemplissageParDiffusion) program).diffuser(imageFromStrings, 0, 2, 'O', '-');
        assertArrayEquals(imageFromStrings("XX-XX", "X--XO", "--XXO", "X--XX", "X-XXX"), imageFromStrings);
    }

    void test_diffuser_ancienne_couleur_non_conforme(Program program) {
        String[] strArr = {"XXOXX", "XOOXO", "OOXXO", "XOOXX", "XOXXX"};
        char[][] imageFromStrings = imageFromStrings(strArr);
        ((RemplissageParDiffusion) program).diffuser(imageFromStrings, 0, 2, 'X', '-');
        assertArrayEquals(imageFromStrings(strArr), imageFromStrings);
    }

    void test_diffuser_non_propagation_sur_diagonale(Program program) {
        char[][] imageFromStrings = imageFromStrings("XOX", "XXO", "XXX");
        ((RemplissageParDiffusion) program).diffuser(imageFromStrings, 0, 1, 'O', '-');
        assertArrayEquals(imageFromStrings("X-X", "XXO", "XXX"), imageFromStrings);
    }
}
