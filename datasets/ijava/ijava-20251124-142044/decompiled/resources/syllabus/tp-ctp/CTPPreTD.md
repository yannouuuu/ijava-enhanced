# Images et tableau à 2 dimensions

On souhaite changer la représentation de nos images en utilisant un tableau à deux dimensions d'entiers. Cette représentation permet de supprimer la gestion des conversions entre chaînes de caractères et nombres entiers.

Voici la description du nouvel encodage d'une image : un tableau à deux dimensions d'entiers constitué de 3 lignes (une pour chaque couleur primaire) et de `n x n` colonnes pour une image de taille `n`.

Ainsi, une image de taille 3 avec un damier noir et blanch est représentée par :
```bash
0 255 0 255 0 255 0 255 0
0 255 0 255 0 255 0 255 0
0 255 0 255 0 255 0 255 0
``` 
La première ligne correspond à la composante `RED`, la deuxième à `GREEN` et la troisième à `BLUE`. La première colonne correspond au premier pixel de coordonnée (0, 0), la deuxième colonne au pixel de coordonnées (0, 1), la troisième colonne au pixel de coordonnées (0, 2) ce qui correspond donc à la première ligne du damier.

## `int[][] creer(int taille, int r, int g, int b)`

Définissez la fonction `creer` qui fabrique une nouvelle image de taille `taille` dont les pixels sont initialisés avec les composantes `r`, `g`et `b`. Ainsi, l'appel `creer(2, 0, 32, 64)` produira ce tableau d'entier :
```bash
 0  0  0  0
32 32 32 32
64 64 64 64
```

## `String toString(int nb)` et `String debug(int[][] image)`

Afin de déboguer plus facilement, on souhaite disposer d'une fonction `debug` qui permet de visaliser l'état de chacun des pixels d'une image. Voici le type de visualisation souhaité : 
```bash
(000,000,000) (215,123,123) (255,255,255)
(215,123,123) (255,255,255) (000,000,000)
(255,255,255) (000,000,000) (215,123,123) 
```
Pour obtenir cet affichage, il faut d'abord définir la fonction `String toString(int nb)` permettant d'afficher un entier représentant une composante primaire sous la forme d'une chaîne de caractère faisant toujours 3 caractères.

Créez la fonction `String toString(int nb)` réalisant cela.

Une fois le test validé pour cette fonction, définissez la fonction `String debug(int[][] image)` qui réalise un affichage similaire à celui donné ci-dessus. Vous pourrez tester votre fonction à l'aide d'un court `void algorithm()` comme celul-ci :
```java
void algorithm() {
    int[][] image2x2 = new int[][]{
        {0, 32, 64, 128},
        {0, 32, 64, 128},
        {0, 32, 64, 128}};
    println(debug(image2x2));
}
```
qui devrait produire cet affichage:
```bash
(000,000,000) (032,032,032) 
(064,064,064) (128,128,128) 
```

## `void show(int[][] image)`

Maintenant, on souhaite disposer de la fonction `show` qui à l'aidé de la fonction prédéfinie `rgb` affiche sur la console l'image en couleurs.

