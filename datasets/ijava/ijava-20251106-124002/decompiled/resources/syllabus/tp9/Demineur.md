# Jeu du démineur

Dans ce jeu le joueur ou la joueuse va découvrir progressivement des cases du plateau jusqu’à ce qu’il ou elle tombe sur une mine ! C’est un jeu de hasard dont l’objectif est de découvrir le plus grand nombre de cases avant de tomber sur une mine.

On choisit d'utiliser deux structures de données afin de représenter le plateau et ses différents états. Pour cela, 
 représenterNous avons besoin de deux tableaux à deux dimensions de booléens pour représenter l’état actuel du jeu:
— un `champs` qui contient des mines représenté par un tableau de booléens à deux dimensions dont chaque case indique si elle contient une mine (`true`) ou pas (`false`),
— une `carte` représentée par un tableau de booléens à deux dimensions dont chaque case peut indique si la case a été découverte (`true`) ou pas (`false`)

## Affichage du plateau et des mines

Écrire la fonction `String afficher(boolean[][] champs, boolean[][] carte)` qui affiche l’état actuel du jeu du point de vue de la joueuse ou du joueur, à savoir:
— un point d’interrogation pour les cases non encore explorées,
— un point pour les cases explorées qui ne contiennent pas de mine,
— le symbole ’@’ pour les cases explorées qui contiennent une mine.

De plus, comme le joueur ou la joueuse devront saisir des coordonnées, il serait utile d’afficher les coordonnées des lignes et des colonnes en plus du contenu des cases. Voici donc ce à quoi l’affichage doit correspondre pour un champ de taille `3 × 5`.
```bash
  ABCDE
1 ??.?.
2 .?..?
3 ?..@?
```

## Validation automatique de l'affichage du plateau

Afin d'automatiser la vérification de la validité de votre fonction d'affichage, vous devez écrire la fonction de test `void test_afficher()`. Pour cela, utilisez simplement l'exemple donné précédemment, en supposant qu'il n'y avait que deux mines, l'autre se trouvant dans la première case du tableau (en `(0,0)`).

## Initialisation aléatoire du plateau

Écrire la procédure `void initialiserChamps(boolean[][] champ, double proba)` qui pose des
mines dans les cases de champ avec la probabilité donnée en paramètre. Cette fonction est quasiment identique à celle écrite pour le jeu de la vie, sauf qu’ici les cases sur la bordure ne sont pas forcément vides.

On peut ensuite tester la fonction avec un simple algorithme principal qui crée un tout petit plateau et l'affiche en faisant varier les cartes découvertes :

```java
void test_initialiserChamps() {
    boolean[][] champs = new boolean[1][4]; 
    initialiserChamp(champs, 0.0);
    assertArrayEquals(new new boolean[][]{{false, false, false, false}}, champs);
    initialiserChamp(champs, 1.0);
    assertArrayEquals(new new boolean[][]{{true, true, true, true}}, champs);
}
```

## Contrôle de saisie 

Nous allons maintenant concevoir les deux fonctions de saisie des coordonnées. La coordonnée de la ligne est un nombre compris entre `1` et le nombre de lignes du plateau, et la coordonnée de la colonne est une lettre entre `A` et la lettre qui correspond au nombre de colonnes.

Chacune de ces deux fonctions prend en paramètre le nombre de lignes (ou colonnes) et effectue le contrôle de saisie en redemandant une saisie tant que le joueur saisit une coordonnée invalide.
- `int saisirLigne(int nombreLignes)`
- `int saisirColonne(int nombreColonne)`

Comme ces fonctions font des saisies, il faut les tester manuellement dans `void algorithm()`, par exemple en faisant des saisie et en affichant le résultat retourné par la fonction pour s’assurer qu’elles sont valides:
```java
void algorithm () {
    int l = saisirLigne(5);
    println(l); // doit afficher 3 si la joueuse a saisi 4
    int c = saisirColonne(10);
    println(c); // doit afficher 5 si la joueuse a saisi F
}
```

Lors de l'exécution l'affichage doit correspondre à ceci:
```bash
Ligne (1-5) : 0
Ligne (1-5) : 6
Ligne (1-5) : 4
3
Colonne (A-J) : K
Colonne (A-J) : Z
Colonne (A-J) : F
5
```

## Algorithme principal

Vous êtes maintenant prêt à écrire l’algorithme principal, en complétant ce squelette.

```java
void algorithm () {
    boolean[][] champs = new boolean[5][7];
    boolean[][] carte  = new boolean[5][7];
    boolean perdu = false;
    int score = 0;

    initialiserChamp(champ, 0.1);

    while (! perdu) {
        println("Score: " + score);
        // Afficher le plateau 
        println("Où veux-tu tenter ta chance ?");
        // Saisir les coordonnées et découvrir la case correspondante
        // sur la carte
        ...
        // Mettre à jour le score ou perdu
        ...
    }
    // Afficher le plateau 
    println(ANSI_RED + "BOUM !" + ANSI_RESET); // Dans ce jeu on perd toujours ...
    println("Ton score final est " + score);
}
```

Voici un exemple de partie : 
```bash
Score: 0
  ABCD
1 ????
2 ????
3 ????

Où veux-tu tenter ta chance ?
Ligne (1-3) : 1
Colonne (A-D) : A
  ABCD
1 ????
2 ????
3 ????

BOUM !
Ton score final est de 0 point(s).
```

Amélioration possible : changer l'initiatlisation afin de préciser le nombre de mines souhaitées sur le plateau et les disposer aléatoirement.
