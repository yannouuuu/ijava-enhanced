package defpackage;

/* loaded from: ijava.jar:WatorSolution.class */
class WatorSolution extends Program {
    final int NOMBRE_COLONNES = 10;
    final int NOMBRE_LIGNES = 10;
    final int GESTATION_THON = 2;
    final int GESTATION_REQUIN = 6;
    final int NOURRITURE_REQUIN = 3;
    final double PROBA_THON = 0.25d;
    final double PROBA_REQUIN = 0.1d;
    final int LATENCE = 10;
    final String SAUVEGARDE = "ocean.csv";

    WatorSolution() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // defpackage.Program
    public void algorithm() {
        Poisson[][] charger = charger("ocean.csv");
        if (estVide(charger)) {
            initialiser(charger, 0.25d, 0.1d);
        }
        int i = 0;
        while (!estVide(charger)) {
            int random = random(10);
            int random2 = random(10);
            appliquerRegles(charger, random, random2);
            println("On applique les régles en " + random + ", " + random2);
            println(toString(charger));
            sleep(10L);
            i++;
            if (i == 10) {
                println("Voulez-vous sauvegarder ? (O/N)");
                if (readChar() == 'O') {
                    sauver(charger, "ocean.csv");
                }
                i = 0;
            }
        }
    }

    void initialiser(Poisson[][] poissonArr, double d, double d2) {
        for (int i = 0; i < length(poissonArr, 1); i++) {
            for (int i2 = 0; i2 < length(poissonArr, 2); i2++) {
                poissonArr[i][i2] = newPoisson(d, d2);
            }
        }
    }

    Poisson newPoisson(double d, double d2) {
        Poisson poisson;
        double random = random();
        if (random < d) {
            poisson = newThon();
        } else if (random < d + d2) {
            poisson = newRequin();
        } else {
            poisson = new Poisson();
            poisson.espece = Espece.PLANCTON;
        }
        return poisson;
    }

    Poisson newThon() {
        Poisson poisson = new Poisson();
        poisson.famine = 1;
        poisson.gestation = 2;
        poisson.espece = Espece.THON;
        return poisson;
    }

    Poisson newRequin() {
        Poisson poisson = new Poisson();
        poisson.famine = 3;
        poisson.gestation = 6;
        poisson.espece = Espece.REQUIN;
        return poisson;
    }

    Poisson get(Poisson[][] poissonArr, int i, int i2) {
        return poissonArr[i][i2];
    }

    boolean estVide(Poisson[][] poissonArr) {
        for (int i = 0; i < length(poissonArr, 1); i++) {
            for (int i2 = 0; i2 < length(poissonArr, 2); i2++) {
                if (get(poissonArr, i, i2).espece != Espece.PLANCTON) {
                    return false;
                }
            }
        }
        return true;
    }

    int random(int i) {
        return (int) (random() * i);
    }

    void aFaim(Poisson poisson) {
    }

    /* JADX WARN: Multi-variable type inference failed */
    boolean deplacement(Poisson[][] poissonArr, int i, int i2, Espece espece) {
        int[] iArr = new int[8];
        int[] iArr2 = new int[2];
        iArr2[0] = -1;
        iArr2[1] = 0;
        iArr[0] = iArr2;
        int[] iArr3 = new int[2];
        iArr3[0] = 1;
        iArr3[1] = 0;
        iArr[1] = iArr3;
        int[] iArr4 = new int[2];
        iArr4[0] = 0;
        iArr4[1] = -1;
        iArr[2] = iArr4;
        int[] iArr5 = new int[2];
        iArr5[0] = 0;
        iArr5[1] = 1;
        iArr[3] = iArr5;
        int[] iArr6 = new int[2];
        iArr6[0] = -1;
        iArr6[1] = -1;
        iArr[4] = iArr6;
        int[] iArr7 = new int[2];
        iArr7[0] = 1;
        iArr7[1] = 1;
        iArr[5] = iArr7;
        int[] iArr8 = new int[2];
        iArr8[0] = -1;
        iArr8[1] = 1;
        iArr[6] = iArr8;
        int[] iArr9 = new int[2];
        iArr9[0] = -1;
        iArr9[1] = -1;
        iArr[7] = iArr9;
        for (int i3 = 0; i3 < 10; i3++) {
            int random = random(4);
            int random2 = random(4);
            Object[] objArr = iArr[random];
            iArr[random] = iArr[random2];
            iArr[random2] = objArr;
        }
        for (int i4 = 0; i4 < length(iArr, 1); i4++) {
            int i5 = ((i + iArr[i4][0]) + 10) % 10;
            int i6 = ((i2 + iArr[i4][1]) + 10) % 10;
            Poisson poisson = get(poissonArr, i5, i6);
            if (poisson.espece == Espece.PLANCTON) {
                poissonArr[i5][i6] = get(poissonArr, i, i2);
                return true;
            }
            if (espece == Espece.REQUIN && poisson.espece == Espece.THON) {
                Poisson poisson2 = get(poissonArr, i, i2);
                poisson2.famine = 3;
                poissonArr[i5][i6] = poisson2;
                return true;
            }
        }
        return false;
    }

    Poisson reproduction(Poisson poisson) {
        Poisson poisson2 = new Poisson();
        poisson2.espece = Espece.PLANCTON;
        return poisson2;
    }

    Poisson[][] charger(String str) {
        return new Poisson[10][10];
    }

    void sauver(Poisson[][] poissonArr, String str) {
    }

    String toString(Poisson[][] poissonArr) {
        String str = "";
        for (int i = 0; i < length(poissonArr, 1); i++) {
            for (int i2 = 0; i2 < length(poissonArr, 2); i2++) {
                Poisson poisson = get(poissonArr, i, i2);
                if (poisson.espece == Espece.PLANCTON) {
                    str = str + ". ";
                } else if (poisson.espece == Espece.THON) {
                    str = str + "T ";
                } else if (poisson.espece == Espece.REQUIN) {
                    str = str + "R ";
                }
            }
            str = str + "\n";
        }
        return str;
    }

    void appliquerRegles(Poisson[][] poissonArr, int i, int i2) {
        get(poissonArr, i, i2);
    }
}
