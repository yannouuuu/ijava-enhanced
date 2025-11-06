class Saisons extends Program {

    String saisonMeteorologique (int mois) {
        return "";
    }
    int nombreJoursMois(int numeroMois) {
        return 0;
    }

    String saisonAstronomique (int jour, int mois) {
        return 0;
    }

    void algorithm () {
        for (int m=0;m<=13;m=m+1){
            println("mois " + m + " : " + saisonMeteorologique(m));
        }
    }

}
