# Remplissage par diffusion

Vous connaissez sans doute l'outil "seau" des logiciels de dessin, qui permet de modifier la couleur d'une zone contiguë. Cette fonctionnalité utilise un algorithme récursif qui propage la nouvelle couleur de proche en proche depuis un pixel initial, comme c'est illustré par [cette animation](https://fr.wikipedia.org/wiki/Algorithme_de_remplissage_par_diffusion#/media/Fichier:Recursive_Flood_Fill_4_(aka).gif).

Afin de nous concentrer sur l'algorithme récursif lui-même et non sur la représentation des images, nous allons considérer qu'une image est un tableau à deux dimensions `char[][]`.
Voici deux traces d'exécution de l'algorithme que vous devez écrire.

```
Image initiale
XXOXX
XOOXO
OOXOX
XOXXX
XOOXX

Saisir les indices du point de départ, ainsi que la nouvelle couleur
Indice ligne : 2
Indice colonne : 1
Nouvelle couleur : ·
XX·XX
X··XO
··XOX
X·XXX
X··XX
```

```
Image initiale
XXOXX
XOOXO
OOXOX
XOXXX
XOOXX

Saisir les indices du point de départ, ainsi que la nouvelle couleur
Indice ligne : 2
Indice colonne : 3
Nouvelle couleur : ·
XXOXX
XOOXO
OOX·X
XOXXX
XOOXX
```



Et voici l'algorithme principal qui devra vous permettre de tester la fonction récursive.
```
void algorithm() {	
	char[][] image = new char [][]{{'X', 'X', 'O', 'X', 'X'},
	                               {'X', 'O', 'O', 'X', 'O'},
                                    {'O', 'O', 'X', 'O', 'X'},
                                    {'X', 'O', 'X', 'X', 'X'},
                                    {'X', 'O', 'O', 'X', 'X'}};
	
	println("Image initiale");
	println(toString(image));
	println("Saisir les indices du point de départ, ainsi que la nouvelle couleur");
	print("Indice ligne : ");
	int ligne = readInt();
	print("Indice colonne : ");
	int colonne = readInt();
	print("Nouvelle couleur : ");
	char couleur = readChar();
	
	diffuser(image, ligne, colonne, image[ligne][colonne], couleur);
	println(toString(image));
}
```

## Affichage d'une image sur le terminal

Pour vous échauffer, écrire la fonction `String toString(char[][] image)` permettant d'afficher le tableau de caractères comme dans l'exemple.

## Algorithme de diffusion

Écrire la fonction récursive `void diffuser(char[][] image, int indiceLigne, int indiceColonne, char ancienneCouleur, char nouvelleCouleur)` qui doit colorier avec `nouvelleCouleur` toute la zone autour de `indiceLigne` et `indiceColonne` ayant `ancienneCouleur`.
Le principe est simple : colorier le pixel aux indices donnés, et faire la même chose récursivement sur ses voisins.
Vous devrez trouver les conditions d'arrêt adéquates pour que votre fonction s'écrive le plus simplement possible.
Si votre fonction fait plus de 10 lignes de code, elle est sans doute plus compliquée que nécessaire.

:::question
**Est-ce que votre fonction `diffuser` est récursive terminale ?**
- [ ] Oui
  > C'est étonnant. Ou bien vous ne comprenez pas encore bien la récursivité terminale, ou bien votre fonction n'est pas correcte. En effet, elle doit faire plusieurs appels récursifs, donc ne peut pas être récursive terminale.
- [X] Non
  > C'est normal, cet algorithme ne peut pas s'écrire avec une fonction récursive terminale, car il nécessite plusieurs appels récursifs.
:::

## Voir la trace d'exécution de l'algorithme récursif

Vous pouvez ajouter les trois lignes ci-dessous au début de votre fonction `diffuser` pour voir les appels récursifs qui sont faits, et comment l'image change.

```
println("indiceLigne = " + indiceLigne + ", indiceColonne = " + indiceColonne +
        ", ancienneCouleur = " + ancienneCouleur + ", nouvelleCouleur = " + nouvelleCouleur);
println(toString(image));
readString();                   // Sert à arrêter l'exécution pour qu'on puisse lire le résultat
                                // Il suffit d'appuyer sur Entrée pour relancer l'exécution
```