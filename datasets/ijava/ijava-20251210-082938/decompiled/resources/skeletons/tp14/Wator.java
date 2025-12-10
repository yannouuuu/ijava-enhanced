class Wator extends Program {

    //Paramètres de la simulation
        final int NOMBRE_COLONNES   = 10;
        final int NOMBRE_LIGNES     = 10;
        final int GESTATION_THON    =  2; // Le temps de gestation d'un thon
        final int GESTATION_REQUIN  =  6; // idem pour le requin
        final int GESTATION_ORQUE   =  8; // idem pour l'orque
        final int NOURRITURE_REQUIN =  3; // La nourriture de départ d'un requin
        final int NOURRITURE_ORQUE  =  5; // La nourriture de départ d'un requin

        final double PROBA_THON     = 0.25;
        final double PROBA_REQUIN   = 0.10;
        final double PROBA_ORQUE    = 0.05;
        final double PROBA_ORQUE_VS_REQUIN = 0.5;

        final int LATENCE = 10; // à diminuer pour augmenter la vitesse de la simulation

        final String SAUVEGARDE = "ocean.csv";

        void algorithm(){
            Poisson[][] ocean = charger(SAUVEGARDE);
            if (estVide(ocean)){
                initialiser(ocean, PROBA_THON, PROBA_REQUIN, PROBA_ORQUE);
            }
            int nombreTours = 0;
            while(!estVide(ocean)){
                // clearScreen(); // TODO: implement clear screen
                int ligne   = random(NOMBRE_LIGNES);
                int colonne = random(NOMBRE_COLONNES);
                appliquerRegles(ocean, ligne, colonne);
                println("On applique les régles en " + ligne + ", " + colonne);
                println(toString(ocean));
                sleep(LATENCE);
                nombreTours++;
                if (nombreTours == 10){
                    println("Voulez-vous sauvegarder ? (O/N)");
                    if (readChar() == 'O'){
                        sauver(ocean, SAUVEGARDE);
                    }
                    nombreTours = 0;
                }
            }
        }

        // Initialise le tableau de poissons aléatoirement selon des probabilités
        void initialiser(Poisson[][] tab, double probaThon, double probaRequin, double probaOrque){
            for (int idxL = 0; idxL < length(tab,1); idxL++) {
                for (int idxC = 0; idxC < length(tab,2); idxC++) {
                    tab[idxL][idxC] = newPoisson(probaThon, probaRequin, probaOrque);
                }
            }
        }

        // Créer un poisson ou du plancton en fonction des probabilités fournies
        Poisson newPoisson(double probaThon, double probaRequin, double probaOrque){
            double alea = random();
            Poisson p;
            if (alea < probaThon) {
                p = newThon();
            } else if (alea < probaThon + probaRequin) {
                p = newRequin();
            } else if (alea<probaThon + probaRequin+probaOrque) {
                p = newOrque();
            } else { //il n'y a pas de poisson
                p = new Poisson();
                p.espece = Espece.PLANCTON;
            }
            return p;
        }

        // Créer un thon à partir des caractéristiques en constantes globales
        Poisson newThon(){
            Poisson p   = new Poisson();
            p.famine    = 1;
            p.gestation = GESTATION_THON;
            p.espece    = Espece.THON;
            return p;
        }

        // Créer un requin à partir des caractéristiques en constantes globales
        Poisson newRequin(){
            Poisson p   = new Poisson();
            p.famine    = NOURRITURE_REQUIN;
            p.gestation = GESTATION_REQUIN;
            p.espece    = Espece.REQUIN;
            return p;
        }

        // Créer un requin à partir des caractéristiques en constantes globales
        Poisson newOrque(){
            Poisson p   = new Poisson();
            p.famine    = NOURRITURE_ORQUE;
            p.gestation = GESTATION_ORQUE;
            p.espece    = Espece.ORQUE;
            return p;
        }

        // Retourne le poisson à la position (ligne, colonne) dans l'océan
        Poisson get(Poisson[][] tab, int ligne, int colonne){
    	    return tab[ligne][colonne];
        }

        // Indique si le tableau ne contient plus que du plancton
        boolean estVide(Poisson[][] ocean){
            for (int idxL = 0; idxL < length(ocean,1); idxL++){
                for (int idxC = 0; idxC < length(ocean,2); idxC++) {
                    if (get(ocean, idxL, idxC).espece != Espece.PLANCTON) {
                        return false;
                    }
                }
            }
            return true;
        }

        // Retourne un entier au hasard entre 0 et max exclus
        int random(int max){
    	    return (int) (random()*max) ;
        }

        // Gère la faim du poisson (décrémente sa nourriture)
        /**@inject
         * @remove
         */
        void aFaim(Poisson p){
            if (p.espece != Espece.PLANCTON && p.espece != Espece.THON) {
                p.famine--;
            }
        }

        // Déplace un poisson vers une case adjacente
        // Retourne true si le poisson s'est déplacé (permet la reproduction)
        boolean deplacement(Poisson[][] tab, int lig, int col, Espece espece){
            // Chercher une case adjacente disponible
            int[][] directions = {
                {-1,0}, {1,0}, {0,-1}, {0,1}, // haut, bas, gauche, droite
                {-1,-1}, {1,1}, {-1,1}, {-1,-1} // diagonales
            };

            // Mélanger les directions aléatoirement
            for (int i = 0; i < 10; i++) {
                int idx1 = random(4);
                int idx2 = random(4);
                int[] temp = directions[idx1];
                directions[idx1] = directions[idx2];
                directions[idx2] = temp;
            }

            for (int i = 0; i < length(directions,1); i++) {
                int nouvLig = (lig + directions[i][0] + NOMBRE_LIGNES) % NOMBRE_LIGNES;
                int nouvCol = (col + directions[i][1] + NOMBRE_COLONNES) % NOMBRE_COLONNES;

                Poisson cible = get(tab, nouvLig, nouvCol);

                // Si la case est du plancton (vide)
                if (cible.espece == Espece.PLANCTON) {
                    // Déplacer le poisson
                    Poisson poisson = get(tab, lig, col);
                    tab[nouvLig][nouvCol] = poisson;
                    return true; // Déplacement réussi
                }

                // Si c'est un prédateur qui peut manger la proie
                if (espece == Espece.REQUIN && cible.espece == Espece.THON) {
                    Poisson requin = get(tab, lig, col);
                    requin.famine = NOURRITURE_REQUIN; // Se nourrir
                    tab[nouvLig][nouvCol] = requin;
                    return true;
                }

                if (espece == Espece.ORQUE && cible.espece == Espece.REQUIN) {
                    // L'orque peut manger le requin avec une certaine probabilité
                    if (random() < PROBA_ORQUE_VS_REQUIN) {
                        Poisson orque = get(tab, lig, col);
                        orque.famine = NOURRITURE_ORQUE;
                        tab[nouvLig][nouvCol] = orque;
                        return true;
                    }
                }

                if (espece == Espece.ORQUE && cible.espece == Espece.THON) {
                    Poisson orque = get(tab, lig, col);
                    orque.famine = NOURRITURE_ORQUE;
                    tab[nouvLig][nouvCol] = orque;
                    return true;
                }
            }

            return false; // Aucun déplacement possible
        }

        // Gère la reproduction du poisson
        // Retourne un nouveau poisson si reproduction, ou du plancton sinon
        /**@inject
         * @remove
         */
        Poisson reproduction(Poisson p){
            p.gestation--;

            if (p.gestation <= 0) {
                // Le poisson se reproduit, on laisse un nouveau-né à la place
                if (p.espece == Espece.THON) {
                    return newThon();
                } else if (p.espece == Espece.REQUIN) {
                    return newRequin();
                } else if (p.espece == Espece.ORQUE) {
                    return newOrque();
                }
            }

            // Pas de reproduction, la case devient du plancton
            Poisson plancton = new Poisson();
            plancton.espece = Espece.PLANCTON;
            return plancton;
        }

        // Charge l'océan depuis un fichier CSV (à implémenter)
        Poisson[][] charger(String filename){
            Poisson[][] ocean = new Poisson[NOMBRE_LIGNES][NOMBRE_COLONNES];
            // TODO: implémenter le chargement
            return ocean;
        }

        // Sauvegarde l'océan dans un fichier CSV (à implémenter)
        void sauver(Poisson[][] ocean, String filename){
            // TODO: implémenter la sauvegarde
        }

        // Convertit l'océan en chaîne de caractères pour l'affichage
        String toString(Poisson[][] ocean){
            String result = "";
            for (int i = 0; i < length(ocean,1); i++){
                for (int j = 0; j < length(ocean,2); j++){
                    Poisson p = get(ocean, i, j);
                    if (p.espece == Espece.PLANCTON) {
                        result += ". ";
                    } else if (p.espece == Espece.THON) {
                        result += "T ";
                    } else if (p.espece == Espece.REQUIN) {
                        result += "R ";
                    } else if (p.espece == Espece.ORQUE) {
                        result += "O ";
                    }
                }
                result += '
    ';
            }
            return result;
        }

        //fonction qui applique les règles de la simulation sur une case dont les coordonnées sont données en paramètre
        void appliquerRegles(Poisson[][] tab, int lig, int col){
    	    Poisson p = get(tab,lig,col);
            // à compléter
        }

}
