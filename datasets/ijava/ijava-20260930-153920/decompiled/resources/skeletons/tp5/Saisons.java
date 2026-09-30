class Saisons extends Program {

    String saisonMeteorologique(int mois) {
        // TODO: Complete this method
    }


    int nombreJoursMois(int numeroMois) {
        // à compléter
        return 0;
    }


    String saisonAstronomique(int jour, int mois) {
        // à compléter
        return "";
    }


    void algorithm() {
        for (int m = 0; m <= 13; m = m + 1) {
            println("mois " + m + " : " + saisonMeteorologique(m));
        }
    }

}