class JeuxDeType extends Program {

    void algorithm(){
        ... prenom = "Alan";
        ... nom = "Turing";
        ... naissance = 1912;
        ... annee = 2022;
        ... age = annee - naissance;
        ... initiale = charAt(prenom,0);
        println(initiale + ". " + nom + " aurait eu " + age + " ans en " + annee);
    }

}
