package defpackage;

/* loaded from: ijava.jar:RandomPratique.class */
class RandomPratique extends Program {
    RandomPratique() {
    }

    int randome(int i, int i2) {
        return i + ((int) (random() * ((i2 - i) + 1)));
    }

    int random(int i) {
        return randome(0, i);
    }

    void testCopieSans() {
        assertEquals("Hell", copieSans("Hello", 'o'));
        assertEquals("Heo", copieSans("Hello", 'l'));
        assertEquals("ello", copieSans("Hello", 'H'));
    }

    String copieSans(String str, char c) {
        String str2 = "";
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 < length(str)) {
                if (charAt(str, i2) != c) {
                    str2 = str2 + charAt(str, i2);
                }
                i = i2 + 1;
            } else {
                return str2;
            }
        }
    }

    void testRandomN() {
        String str = "0123456";
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 < 100000) {
                str = copieSans(str, charAt(random(6), 0));
                i = i2 + 1;
            } else {
                assertEquals("", str);
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // defpackage.Program
    public void algorithm() {
        println(1 + " <= " + random(1, 6) + " <= " + 6);
        println(1 + " <= " + random(6) + " <= " + 6);
    }
}
