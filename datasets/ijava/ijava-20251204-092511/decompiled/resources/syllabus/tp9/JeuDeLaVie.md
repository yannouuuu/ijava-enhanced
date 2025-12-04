# Jeu de la vie

Vous allez implémenter le programme `JeuDeLaVie` qui simule le jeu de la vie présenté en cours. On rappelle ici les fonctions à écrire, que vous êtes invités à faire dans cet ordre.

## `String afficher(boolean[][] monde)`

Écrire la fonction `String afficher(boolean[][] monde)` qui affiche le tableau `monde` représentant le monde du jeu de la vie. Utilisez le caractère `@` pour les cases vivantes (i.e. les cases égales à `true`) et `.` pour les cases mortes. Rappelons que les cases sur la bordure du `monde` ne sont pas significatives et servent uniquement à faciliter les calculs, on peut donc ne pas les afficher. Votre fonction d’affichage, doit valider ce test.

```java
    void test_afficher() {
        boolean[][] monde = {
            {F, F, F, F, F},
            {F, T, T, T, F},
            {F, F, T, F, F},
            {F, F, F, F, F}
        };
        String expected =
            ".....\n" +
            ".@@@.\n" +
            "..@..\n" +
            ".....\n";
        assertEquals(expected, afficher(monde));
    }
```

## `void initialiser(boolean[][] tab, double proba)`

Écrire la procédure `void initialiser(boolean[][] tab, double proba)` qui initialise aléatoirement le tableau `tab` donnée en paramètre de manière à ce que chaque case ait une probabilité (comprise entre 0.0 et 1.0) `proba` d’être vivante. Attention : toutes les cases sur les bordures doivent être mortes. Pour tester la fonction que vous venez d’écrire, modifiez `void algorithm()` en utilisant `initialiser` pour initialiser le tableau, que vous affichez ensuite.

## `int nombreDeVoisins(boolean[][] monde, int lig, int col)`

Écrire la fonction `int nombreDeVoisins(boolean[][] monde, int lig, int col)` qui compte
le nombre de voisins vivants de la case de coordonnées `(lig, col)` dans `monde`. On utilisera cette fonction que sur les cases *à l'intérieur du tableau*, c'est-à-dire que l'on ne l'appellera pas sur les cases de la première/dernière ligne et colonne. 

Utilisez cette fonction de test pour valider votre fonction.

```java
void test_nombreDeVoisins() {
    // Initialiser un monde exemple
    boolean[][] monde = new boolean[][]
        {{F, F, F, F, F},
         {F, F, F, F, F},
         {F, T, T, T, F},
         {F, F, F, F, F},
         {F, F, F, F, F}};
    assertEquals(2, nombreDeVoisins(monde, 2, 2));
    assertEquals(2, nombreDeVoisins(monde, 1, 1));
    assertEquals(3, nombreDeVoisins(monde, 3, 2));
}
```

## `boolean evolution(int nombreDeVoisins, boolean etatCellule)`

Écrire la fonction `boolean evolution(int nombreDeVoisins, boolean etatCellule)` qui calcule l’état d’une cellule à la génération suivante en fonction de son nombre de voisins et de son état actuel. La fonction doit passer ce test.

```java
void test_evolution () {
    assertEquals(false, evolution(0, false));
    assertEquals(false, evolution(0,  true));
    assertEquals(false, evolution(1, false));
    assertEquals(false, evolution(1,  true));
    assertEquals(false, evolution(2, false));
    assertEquals( true, evolution(2,  true));
    assertEquals( true, evolution(3, false));
    assertEquals( true, evolution(3,  true));
    assertEquals(false, evolution(4, false));
    assertEquals(false, evolution(4,  true));
}
```

## Algorithme principal

Vous êtes maintenant prêt à écrire l’algorithme principal qui simulera le jeu de la vie, en complétant ce squelette:

```java
void algorithm() {
    boolean [][] monde = new boolean[20][20];
        int generation = 0;

        initialiser(monde, 0.1);
        // Ici, on pourra par la suite ajouter des motifs (carré, glisseur, barre, autres)
       
        do {
            // afficher le monde
            // calculer la génération suivante
            println("Génération "+generation);
            generation = generation + 1;
            println("Entrez \"stop\" pour arrêter, ou entrée pour continuer");
        } while(!equals("stop", readString()));
    }
}
```

## Peupler le monde ...

Vous allez maintenant écrire trois fonctions pour inclure des motifs, tester votre programme et mieux savourer le fruit de votre travail. Les fonctions en question vont générer comme motif :
— un carré de taille 2 × 2 cases, dont on sait qu’il vivra éternellement (s’il n’est pas perturbé, bien entendu) : `void ajouterCarre (boolean[][] monde, int lig, int col)`
— un « glisseur », qui en 4 générations effectue une translation en diagonale : `void ajouterGlisseur (boolean[][] monde, int lig, int col)`
— une barre horizontale de 3 cases dont on sait qu’elle oscille en alternant avec une barre verticale : `void ajouterBarre (boolean[][] monde, int lig, int col)`

Chacune des trois fonctions prend en paramètre les coordonnées où (le coin supérieur gauche du) motif doit être ajouté.
