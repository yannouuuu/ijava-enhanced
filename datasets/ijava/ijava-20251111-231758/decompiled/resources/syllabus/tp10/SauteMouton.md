# Implémentation des fonctionnalités constituant SauteMouton

Nous allons maintenant implémenter les différents fonctions décomposant le jeu du Saute-Mouton, l'une après l'autre, en testant évidemment la validité de chaque fonction de manière unitaire.

Dans un premier temps, nous nous intéressons à la saisie **contrôlée** d'un coup, à la manière de représenter un coup, et à la séparation de la saisie et de l'application d'un coup.

Pour saisir un coup, le joueur doit indiquer quel mouton il souhaite déplacer. A priori, il n'est pas nécessaire qu'il indique aussi où le mouton est censé aller car avec un seul espace, un mouton n'a pas trop le choix : soit la case vide est devant lui et il *avance*, soit il y a un mouton devant lui et la case vide juste après et alors il *saute*

**Comment représenter le plus simplement possible un coup ?**

Si l'on représente un coup avec un entier, la position du mouton se déplaçant, cela signifie que lorsque l'on devra effectuer le déplacement, il faudra vérifier si c'est un déplacement de type *avancer* ou *sauter*. Ceci signifie que nous allons faire les vérifications une première fois au niveau de la saisie pour s'assurer que le mouton indiqué peut se déplacer, et il faudra recommencer ensuite lorsque l'on effectuera le déplacement.

Pour éviter cela, on propose de représenter un déplacement par *un couple d'entiers représentant l'indice de départ et d'arrivée* du mouton à déplacer (ce dernier sera toujours l'indice de l'`ESPACE` !). Avec cette représentation, tous les contrôles s'effectuent au niveau de la saisie et ensuite il ne restera plus qu'à appliquer le coup (nécessairement valide).

Nous choisissons donc de représenter un coup par la position de départ et d'arrivée du mouton à déplacer, càd un tableau à une dimension de deux cases contenant des entiers compris entre 0 et 6. Ainsi, la fonction `saisir` va retourner ce coup qui sera ensuite transmis à la fonction `appliquer`. Comme pour effectuer un déplacement il faut mettre à jour la prairie, `appliquer` aura en paramètre non seulement le coup, mais aussi la prairie.

## Implémentation de la fonction `appliquer`

Avant d’écrire la fonction `void appliquer(char[] prairie, int[] coup)`, il faudra s’assurer qu’elle se comporte comme attendu. Le comportement de notre programme final en dépend, et il est beaucoup plus facile de trouver une erreur dans une seule fonction que dans un programme entier. Donc, on commence par écrire des tests pour cette fonction.

```java
    void test_appliquer_avancer_droite() {
        char[] prairie = new char[]{DROITE, ESPACE, GAUCHE};
        appliquer(prairie, new int[]{0, 1});
        assertArrayEquals(new char[]{ESPACE, DROITE, GAUCHE}, prairie);
    }
    void test_appliquer_avancer_gauche() {
        char[] prairie = new char[]{DROITE, ESPACE, GAUCHE};
        appliquer(prairie, new int[]{2, 1});
        assertArrayEquals(new char[]{DROITE, GAUCHE, ESPACE}, prairie);
    }
```

Inspirez-vous des tests ci-dessus pour ajouter les fonctions de test `test_appliquer_sauter_droite` et `test_appliquer_sauter_gauche`.

Ensuite, implémentez la signature de la fonction `appliquer` et ne réalisez aucune opération dans le corps de cette fonction, puis compilez et lancez les tests.

Normalement, ils doivent être tous au rouge, vous pouvez maintenant réfléchir à l'algorithme à mettre dans le corps de la fonction `appliquer` !

## Implémentation de la fonction `saisir`

Cette fonction a pour responsabilité de faire une **saisie contrôlée** d'un coup auprès de l'usager. Les contrôles doivent porter sur différents aspects :
* l'indice doit être à l'intérieur du tableau,
* l'indice doit correspondre à un mouton,
* l'indice doit correspondre à un mouton pouvant se déplacer (en avançant ou sautant)

Tant que tous ces éléments ne sont pas vérifiés, il faut re-saisir un coup auprès de l'usager.

Comme nous allons devoir vérifier la validité de l'indice donné par l'usager, mais aussi de ce dernier avec des décalages de `-2`, `-1` (moutons se déplaçant vers la gauche) ou `+1`, `+2` (moutons se déplaçant vers la droite), il serait pratique de disposer d'une fonction `corriger` s'assurant que l'indice reste toujours dans le tableau.

Définissez la fonction `corriger` qui retourne un indice toujours valide, soit la valeur initiale si elle est déjà dans le tableau, soit `0` si l'indice est négatif, soit `taille-1` si l'indice est supérieur ou égal à `taille`. Cette fonction doit vérifier ces tests :
```java
    void test_corriger_negatif() {
        assertEquals(0, corriger(-3, 5));
    }
    void test_corriger_trop_grand() {
        assertEquals(4, corriger(10, 5));
    }
    void test_corriger_valide() {
        assertEquals(2, corriger(2, 5));
    }
```

Nous pouvons maintenant implémenter plus facilement la fonction `saisir` qui doit retourner un couple d'entiers valides, c'est-à-dire un tableau d'entier à une dimension dont le premier indice indique bien le mouton à déplacer et le second indice une position d'arrivée pertinente pour ce mouton, c'est-à-dire l'amenant à la case `ESPACE` en respectant les règles de déplacement.

Il y a des tests automatisés côté profs, mais comme il est plus délicat de tester automatiquement des fonctions de saisies, copiez cet algorithme principal et exécutez le si vous souhaitez visualiser que la saisie est bien valide (et que la prairie est bien mise à jour, mais normalement les tests précédents étant au vert à ce point, cela ne doit poser aucun soucis) :

```java
    void algorithm() {
        char[] prairie = initialiser(7);
        println(toString(prairie));
        int[] coup = saisir(prairie);
        appliquer(prairie, coup);
        println(toString(prairie));
    }   
```

## Implémentation de la fonction `bloque`

Lorsque l'on observe l'algorithme ci-dessus, il correspond au corps de la boucle principal de ce casse-tête, dont on sortirai lorsque le jeu est bloqué. 

Implémentez la fonction `bloque` qui retourne `true` si l'on est bloqué, c'est-à-dire si plus aucun déplacement n'est possible et `false` sinon. 

Cette fonction devra valider les assertions suivantes : 

```java
    void test_bloque() {
        assertFalse(bloque(new char[]{ESPACE, GAUCHE, DROITE, GAUCHE, DROITE}));
        assertFalse(bloque(new char[]{DROITE, GAUCHE, DROITE, GAUCHE, ESPACE}));
        assertTrue (bloque(new char[]{DROITE, DROITE, GAUCHE, GAUCHE, ESPACE}));
        assertTrue (bloque(new char[]{ESPACE, DROITE, DROITE, GAUCHE, GAUCHE}));
        assertTrue (bloque(new char[]{GAUCHE, GAUCHE, ESPACE, DROITE, DROITE}));
    }
```

## Implémentation de la fonction `gagne`

Il ne nous manque plus qu'à déterminer si lorsque l'on est bloqué, c'est parce que l'on a gagné et atteint la configuration finale où les deux troupeaux se sont croisés et l'espace se retrouve de nouveau au milieu de la prairie ... ou pas !

Définissez la fonction `gagne` qui vérifie cela et doit valider le test suivant :
```java
    void test_gagne() {
        assertTrue(
            gagne(new char[]{GAUCHE, GAUCHE, GAUCHE, ESPACE, DROITE, DROITE, DROITE}));
        assertFalse(
            gagne(new char[]{GAUCHE, GAUCHE, GAUCHE, DROITE, ESPACE, DROITE, DROITE}));
    }
```

## Définition de l'algorithme principal

Maintenant que vous disposez de toutes les fonctionnalités nécessaire, ajoute la boucle de jeu dans l'algorithme principal et l'affichage du résultat en sortie de boucle et profitez de votre nouveau casse-tête !