package defpackage;

/* loaded from: ijava.jar:Calculator.class */
class Calculator extends Program {
    Calculator() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // defpackage.Program
    @skill({"io-basic", "variable-declaration", "io-conversion"})
    public void algorithm() {
        print("First number: ");
        int readInt = readInt();
        print("Second number: ");
        int readInt2 = readInt();
        print("Sum: ");
        println(sum(readInt, readInt2));
        print("Product: ");
        println(product(readInt, readInt2));
    }

    int sum(int i, int i2) {
        return i + i2;
    }

    int product(int i, int i2) {
        return i * i2;
    }

    void testZeroValues() {
    }
}
