package defpackage;

/* compiled from: DessineIut.java */
/* loaded from: ijava.jar:Iut.class */
class Iut extends Program {
    Iut() {
    }

    void dessineLigne(char c, int i) {
        int i2 = 0;
        while (true) {
            int i3 = i2;
            if (i3 < i) {
                print(c);
                i2 = i3 + 1;
            } else {
                return;
            }
        }
    }

    void dessineMilieu(char c, int i) {
        if (i > 0) {
            dessineLigne(' ', i / 2);
            print(c);
        }
    }

    void dessineExtremites(char c, int i) {
        if (i > 1) {
            print(c);
            dessineLigne(' ', i - 2);
            print(c);
        }
    }

    void dessineI(char c, int i) {
        dessineLigne(c, i);
        println();
        int i2 = 0;
        while (true) {
            int i3 = i2;
            if (i3 < i - 2) {
                dessineMilieu(c, i);
                println();
                i2 = i3 + 1;
            } else {
                dessineLigne(c, i);
                println();
                return;
            }
        }
    }

    void dessineU(char c, int i) {
        int i2 = 0;
        while (true) {
            int i3 = i2;
            if (i3 < i - 1) {
                dessineExtremites(c, i);
                println();
                i2 = i3 + 1;
            } else {
                dessineLigne(c, i);
                println();
                return;
            }
        }
    }

    void dessineT(char c, int i) {
        dessineLigne(c, i);
        println();
        int i2 = 0;
        while (true) {
            int i3 = i2;
            if (i3 < i - 1) {
                dessineMilieu(c, i);
                println();
                i2 = i3 + 1;
            } else {
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // defpackage.Program
    public void algorithm() {
        print("Taille : ");
        int readInt = readInt();
        dessineI('I', readInt);
        println();
        dessineU('U', readInt);
        println();
        dessineT('T', readInt);
    }
}
