class Dessins extends Program {

    String trianglePlein(int taille, char caractere) {
        return "";
    }


    String triangleCreux(int taille, char caractere) {
        return "";
    }


    String nFois(char c, int n) {
        String chaine = "";
        // nFois('c', 5) => "ccccc" et nbFois('$', 0) => ""
        return chaine;
    }



    void algorithm() {
        print("Taille : ");
        int taille = readInt();
        print("Caractere : ");
        char caractere = readChar();

        println(trianglePlein(taille, caractere));
        println(triangleCreux(taille, caractere));
    }


}