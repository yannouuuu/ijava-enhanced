class SousChaine extends Program {

    boolean contient(String chaine, String motif) {
        // TODO: Complete this method
    }


    void algorithm() {
        String chaine, motif;
        print("Veuillez entrer un texte : ");
        chaine = readString();
        print("Motif à chercher : ");
        motif = readString();
        if (contient(chaine, motif)) {
            println("trouvé");
        } else {
            println("pas trouvé");
        }
    }


}