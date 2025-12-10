class Cercle extends Program {

    double circonference(double rayon) {
        return 3.14 * diametre(rayon);
    }

    double diametre(double rayon) {
        return 2 * rayon;
    }

    double aire(double rayon) {
        return 3.14 * rayon * rayon;
    }

    double volume(double rayon) {
        return 4.0/3.0 * 3.14 * rayon * rayon *rayon;
    }

    void algorithm() {
    }

}
