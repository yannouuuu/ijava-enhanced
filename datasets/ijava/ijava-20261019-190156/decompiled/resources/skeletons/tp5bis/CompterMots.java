class CompterMots extends Program {

    int nombreDeMots(String texte) {
        // TODO: Complete this method
    }


    void algorithm() {
        println("Entrez un texte : ");
        String texte = readString();
        println("Il y a " + nombreDeMots(texte) + " mot(s) dans ce texte :\"" + texte + "\".");
    }


}