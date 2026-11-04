class Croix extends Program {

    String nFois(char c, int n) {
        String chaine = "";
        // nFois('c', 5) => "ccccc" et nbFois('$', 0) => ""
        return chaine;
    }


    String croix_qui_brule_la_planete(int taille, char caractere) {
        String resultat = "";
        // à compléter ...
    }


    String croix_developpement_durable(int taille, char caractere) {
        return "";
    }


    void algorithm() {
        print("Taille : ");
        int taille = readInt();
        print("Caractere : ");
        char caractere = readChar();

        croix_qui_brule_la_planete(taille, caractere);
        println();
        croix_developpement_durable(taille, caractere);
    }

}