# Manipulations de base pour les tableaux

On souhaite créer des tableaux d'entiers initialisés de différentes manières. Pour cela, le programme `BaseTableaux` définit plusieurs fonctions facilitant ces créations, ainsi que des fonctions de test correspondantes.


- Écrivez, sans utiliser de déclaration en extension, une fonction `int[] creerTableau()` permettant de créer un tableau d’entiers de 10 cases dont les 5 premières cases contiennent la valeur 1 et les 5 suivantes la valeur 2.
```java
void testCreerTableau() {
    assertArrayEquals(new int[]{1,1,1,1,1,2,2,2,2,2}, creerTableau());
}
```

- Que faut-il modifier dans votre fonction pour que l’initialisation se réalise avec une taille de tableau précisée en paramètre (ie. première moitié du tableau ne contenant que des 1 et seconde moitié des 2 ? Écrire cette nouvelle fonction qui prend en paramètre la taille, et qui passe ce test.
```java
void testCreerTableau2() {
    assertArrayEquals(new int[]{1,1,1,1,1,2,2,2,2,2}, creerTableau(10));
    assertArrayEquals(new int[]{1,1,2,2,2}, creerTableau(5));
    assertArrayEquals(new int[]{}, creerTableau(0));
}
```
- Écrivez la fonction `int[] creerTableauAleatoire(int taille)` qui crée un tableau de `taille` cases contenant des valeurs tirées aléatoirement entre 0 et 20 inclus (utilisez pour cela la fonction de signature `double random()` qui retourne un nombre réel entre [0.0, 1.0[ et l’instruction de forçage de type `(int)` qui convertit un réel en entier).
- Définissez la fonction `testCreerTableauAleatoire` qui appelle dans un premier temps la fonction de création d’un tableau aléatoire (`creerTableauAleatoire`) et qui parcourt ensuite chacune des cases en testant l’assertion vérifiant que le contenu de la case est compris entre 0 et 20 inclus (pour cela, utilisez simplement un `assertEquals(true, ...)` prenant en paramètre une expression booléenne devant être vraie).
- **Optionnel** Tester l’aléatoire n’est pas facile et le test réalisé ici a d’ailleurs ses limites. Par exemple, si toutes les valeurs de votre tableau sont à 0, le test passerait. Ajoutez les instructions nécessaires pour que votre test vérifie que toutes les valeurs de 0 à 20 sont bien sorties sur la création d’un tableau de taille 10000 (ça n’est pas certain, mais très fortement probable).