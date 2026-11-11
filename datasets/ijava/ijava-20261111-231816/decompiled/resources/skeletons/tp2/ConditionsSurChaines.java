class ConditionsSurChaines extends Program {
    void algorithm() {
        println("Saisissez successivement trois chaînes de caractères");
        String a = readString();
        String b = readString();
        String c = readString();
        // Écrire les conditions à la place des valeurs false
        // Condition la longueur de a est inférieure à 5
        boolean condLongAinf5 = false ;//À MODIFIER
        // Condition a et b sont la même chaîne
        boolean condAEgalB = false ;//À MODIFIER
        // Condition la longueur de a est plus grande que celle de c
        boolean condAplusLongueQueC = false ;//À MODIFIER

        // Ce qui suit sert à tester vos conditions; ne pas le modifier
        if (condLongAinf5) {
            println("|" + a + "| a moins de 5 caractères");
        } else {
            println("|" + a + "| a 5 caractères ou plus");
        }

        if (condAEgalB) {
            println("|" + a + "|=|" + b + "|");
        } else {
            println("|" + a + "| n'est pas égal à |" + b + "|");
        }

        if (condAplusLongueQueC) {
            println("|" + a + "| est plus long que |" + c + "|");
        } else {
            println("|" + c + "| est au moins aussi long que |" + a + "|");
        }
    }

}