# Affichage de tableaux

En java, la fonction `print` n'affiche pas les valeurs contenues dans un tableau, mais la _référence_ (i.e. l'adresse mémoire) du tableau.
Exécutez le programme fourni pour voir à quoi ressemble la référence d'un tableau.
```java
void algorithm() {
    int[] tabInt = new int[]{1,2,3};
    String[] tabString = new String[]{"un", "deux", "trois"};
    println(tabInt);
    println(tabString);
}
```

Dans la suite vous allez écrire des fonctions permettant d'afficher les valeurs contenues dans un tableau.

### Affichage d'un tableau d'entiers


- Modifiez la fonction `void algorithm()` comme ceci.
```java
void algorithm() {
    int[] tabInt = new int[]{1,2,3};
    String[] tabString = new String[]{"un", "deux", "trois"};
    println(toString(tabInt));  // <- appel d'une foncion qu'il faudra écrire
    println(tabString);
}
```
- Enlevez le commentaire de la fonction de test `test_toString_tab_int`, puis implémentez la fonction concernée par ce test.
- Exécutez le programme avec `ijava execute` pour vérifier si l'affichage correspond à ce qui est attendu. Il est normal que tous les tests automatiques ne passent pas encore, car il vous manque des fonctions.

### Affichage d'un tableau de chaines de caractère

Vous allez procéder de la même manière.

- Modifiez la fonction `void algorithm()` comme ceci
```java
void algorithm() {
    int[] tabInt = new int[]{1,2,3};
    String[] tabString = new String[]{"un", "deux", "trois"};
    println(toString(tabInt));  
    println(toString(tabString)); // <- appel d'une foncion qu'il faudra écrire
}
```
- Enlevez le commentaire de la fonction de test `test_toString_tab_string`, puis implémentez la fonction concernée par ce test.
- Exécutez le programme avec `ijava execute` pour vérifier que l'affichage correspond à ce qui est attendu.

**Surcharge de fonctions** Vous avez remarqué que votre programme contient maintenant deux fonctions ayant le même nom `toString`. C'est possible car elles n'attendent pas les mêmes paramètres. Le compilateur peut reconnaitre laquelle de ces deux fonctions doit être appelées grâce aux arguments d'appel: `toString(new String[]{"un", "deux", "trois"})` appelle la fonction toString qui prend en paramètre un tableau de String, tandis que `toString(new int[]{1,2,3})` appelle la fonction toString qui prend en paramètre un tableau de int.

### Tests automatiques

Maintenant que les deux fonctions sont implémentées, vous pouvez exécuter les tests automatiques avec `ijava test`.