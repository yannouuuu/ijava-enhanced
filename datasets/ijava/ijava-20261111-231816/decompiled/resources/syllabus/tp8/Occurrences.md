# Nombre d’occurrences d’un mot dans un tableau

Écrivez la fonction qui calcule le nombre de fois où `mot` apparait dans le tableau `mots`. Pour cela, lisez attentivement le test ci-dessous afin de déterminer la signature de la fonction `nbFois` avant de l'implémenter.

```java
void testNbFois () {
    String[] mots = new String[]{"je", "tu", "tu", "elle", "je", "je"};
    // "je" apparait 3 fois dans tab
    assertEquals(3, nbFois(mots, "je"));
    assertEquals(2, nbFois(mots, "tu"));
    assertEquals(0, nbFois(mots, "il"));
}
```
