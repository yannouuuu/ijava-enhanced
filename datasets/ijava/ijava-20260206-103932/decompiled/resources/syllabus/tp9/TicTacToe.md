# Jeu du Tic-Tac-Toe 

Dans ce jeu, deux joueurs ou joueuses s'affrontent en cherchant à créer un alignement de 3 symboles sur une grille de 3x3 cases. Les alignements peuvent se produire selon les lignes, les colonnes ou les diagonales. Par convention, le premier joueur est représenté par le symbole `X` et la seconde joueuse par `O`.

Voici un exemple de partie :
```bash
1 2 3
4 5 6
7 8 9
Joueur X : 5
1 2 3
4 X 6
7 8 9
Joueur O : 1
O 2 3
4 X 6
7 8 9
Joueur X : 2
O X 3
4 X 6
7 8 9
Joueur O : 4
O X 3
O X 6
7 8 9
Joueur X : 8
O X 3
O X 6
7 X 9
Bravo X a gagné !
```

## Représentation des joueurs et joueuses et du plateau

Choisissez une représentation adaptée afin de représenter les deux participants et la grille sur laquelle les symboles sont placés. Au début, la grille ne doit contenir aucun symbole.

## Affichage de la grille

Créez la fonction `afficher` permettant de visualiser la grille ainsi que la fonction de test associée (`test_afficher`).

## Saisie d'une coordonnée

Créez la fonction `saisir` saissant au clavier un nombre compris entre 1 et 9 en effectuant un contrôle de saisie et vérifiant aussi qu'il n'y a pas déjà un symbole présent dans la case désignée. 

Assurez-vous de sa validité à l'aide d'un court `void algorithm()`.

## Mise à jour du plateau

Créez la fonction `jouer` qui à l'aide d'un nombre et d'un symbole met à jour le plateau. N'oubliez pas de créer la fonction de test correspondante, sachant que le coup est tjours valide car cela a été vérifié par la fonction `saisir`. 

**Avant d'écrire le code de cette fonction, créez auparavant la procédure `test_jouer()`** qui permettra de s'assurer de la validité de la fonction `jouer`.

## Détection d'un alignement

Créez la fonction booléenne `alignement` détectant si un alignement est présent dans une grille, quelque soit le symbole provoquant l'alignement. Créez aussi la fonction `test_alignement()` s'assurant de la validité de votre fonction.

## Algorithme principal

Maintenant que nous disposons des principales fonctionnalités, modifiez la procédure `void algorithm()` afin de pouvoir jouer au jeu de *Tic-Tac-Toe*.
