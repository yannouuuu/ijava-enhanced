# Définition de la structure de données et fonctions associées

La première étape consiste à identifier les éléments du problème et la manière de les représenter. Concrètement:
* Quel type utiliser pour représenter un mouton? 
* Quel type utiliser pour représenter la prairie, càd l’espace dans lequel évoluent les moutons ?

Dans la solution proposée, un mouton sera représenté par un **caractère**, et la prairie par un **tableau de caractères**. Un caractère sera réservé pour représenter la case vide de la prairie.

Commençons par initialiser la prairie telle qu’elle est au début du jeu, et l’afficher.

## Déclaration des constantes constituant la prairie

Déclarer ces constantes globales pour représenter les moutons:
* une constante nommée `G` et contenant le caractère `<` pour un mouton allant vers la gauche
* une constante nommée `D` et contenant le caractère `<` pour un mouton allant vers la droite
* une constante nommée `V` et contenant le caractère `_` pour représenter l'espace vide dans la prairie.

Nous allons utiliser l'algorithme principal suivant pour débuter : 
```java 
void algorithm () {
    int[] praire = initialiser(7);  // Fonction à écrire
    println(toString(prairie));     // Fonction à écrire
}
```

## Fonction d'initialisation de la prairie

Définissez la fonction `initialiser` qui crée une configuration en fonction d'une taille donnée. **ATTENTION : si un nombre pair est donné, la fonction passera automatiquement au nombre impair lui succédant !** (en effet, pour avoir un nombre égal de moutons dans les deux troupeaux et un seul espace vide, une prairie doit nécessairement être impaire).

La fonction doit valider les tests suivants :
```java
    // test d'initialisation d'une prairie en situation initiale
    void test_initialiser_7() {
        assertArrayEquals(
            new char[]{DROITE, DROITE, DROITE, ESPACE, GAUCHE, GAUCHE, GAUCHE},
            initialiser(7));
    }
    void test_initialiser_3() {
        assertArrayEquals(
            new char[]{DROITE, ESPACE, GAUCHE},
            initialiser(3));
    }
    void test_initialiser_2() {
        assertArrayEquals(
            new char[]{DROITE, ESPACE, GAUCHE},
            initialiser(2));    
    }
```


## Fonction d'affichage de la prairie

Définissez la fonction `toString` qui crée une représentation sous forme de chaîne de caractères d'une prairie donnée en paramètre.

La fonction doit valider les tests suivants :
```java
    // test de la visualisation d'une prairie
    void test_toString_vide() {
        char[] prairie_vide = new char[]{ESPACE, ESPACE, ESPACE, ESPACE, ESPACE};
        assertEquals("     ", toString(prairie_vide));
    }
    void test_toString_prairie_initiale() {
        char[] prairie_vide = new char[]{
            DROITE, DROITE, DROITE, ESPACE, GAUCHE, GAUCHE, GAUCHE};
        assertEquals("     ", toString(prairie_vide));
    }
    void test_toString_prairie_inexistante() {
        char[] test_toString_prairie_inexistante = new char[]{};
        assertEquals("", toString(test_toString_prairie_inexistante));
    }
```

Vérifiez *de visu* que la prairie s'affiche comme prévu à l'aide de : `ijava execute StructureDeDonnees` !
