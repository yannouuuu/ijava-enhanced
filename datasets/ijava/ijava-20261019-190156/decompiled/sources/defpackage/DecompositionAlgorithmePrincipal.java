package defpackage;

/* loaded from: ijava.jar:DecompositionAlgorithmePrincipal.class */
class DecompositionAlgorithmePrincipal extends Program {
    DecompositionAlgorithmePrincipal() {
    }

    char[] initialiser(int i) {
        return new char[0];
    }

    String toString(char[] cArr) {
        return "";
    }

    boolean fini(char[] cArr) {
        return false;
    }

    int saisir(char[] cArr) {
        return 0;
    }

    void appliquer(char[] cArr, int i) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // defpackage.Program
    @inject("    /*\n     * Pour chacune des fonctions ci-dessous, définir leur\n     * signature et le corps de la fonction doit retourner\n     * une valeur (quelconque) par défaut.\n     *\n     * L'objectif est que vous ayez un programme pouvant\n     * compiler avec les signatures et les fonctions \"vides\"\n     * ainsi que les appels à ces fonctions dans l'algorithme\n     * principal.\n     */\n\n     // fonction initialiser (copier la signature existante, mais pas le corps)\n\n     // fonction toString (afficher) (idem initialisation)\n\n     // fonction fini\n\n     // fonction saisir\n\n     // fonction appliquer\n\n     void algorithm() {\n        // initialiser la prairie (FAIT !)\n        // tant que le jeu n’est pas fini\n        //    afficher la prairie (FAIT !)\n        //    saisir le coup (valide) du joueur\n        //    appliquer le déplacement\n    }\n\n")
    public void algorithm() {
        char[] initialiser = initialiser(7);
        while (!fini(initialiser)) {
            System.out.println(toString(initialiser));
            appliquer(initialiser, saisir(initialiser));
        }
    }
}
