package defpackage;

/* loaded from: ijava.jar:BaseTableaux.class */
class BaseTableaux extends Program {
    BaseTableaux() {
    }

    void testCreerTableau() {
        assertArrayEquals(new int[]{1, 1, 1, 1, 1, 2, 2, 2, 2, 2}, creerTableau());
    }

    int[] creerTableau() {
        int[] iArr = new int[10];
        for (int i = 0; i < length(iArr) / 2; i++) {
            iArr[i] = 1;
        }
        for (int length = length(iArr) / 2; length < length(iArr); length++) {
            iArr[length] = 2;
        }
        return iArr;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // defpackage.Program
    public void algorithm() {
    }
}
