class ConditionsLeRetour extends Program {

    void algorithm() {
        println("Saisissez successivement trois chaînes de caractères");
        String a = readString();
        String b = readString();
        String c = readString();
        // Écrire les conditions à la place des valeurs false

        // Condition la première lettre de b précède la première lettre de a d’après
        // l’ordre du dictionnaire
        boolean condBprecedeA = false ;//À MODIFIER
        // (Optionnel) Condition c est un prefixe de a
        boolean condCprefixeDeA = false ;//À MODIFIER

        // Ce qui suit sert à tester vos conditions; NE PAS MODIFIER !!!!!

        if (condBprecedeA) {
            println("|" + b + "| est avant |" + a + "| dans le dictionnaire");
        } else {
            println("|" + b + "| n'est pas avant |" + a + "| dans le dictionnaire");
        }

        if (condCprefixeDeA) {
            println("|" + c + "| est préfixe de |" + a + "|");
        } else {
            println("|" + c + "| n'est pas préfixe de |" + a + "|");
        }

    }


}