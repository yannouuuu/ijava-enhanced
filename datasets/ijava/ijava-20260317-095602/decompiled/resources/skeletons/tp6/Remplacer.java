class Remplacer extends Program {

    void algorithm() {
            print("Phrase : ");
            String texte = readString();
            print("Avant : ");
            String chercher = readString();
            print("Après : ");
            String remplacer = readString();
            println(copieEnRemplacant(texte, chercher, remplacer));
        }

}
