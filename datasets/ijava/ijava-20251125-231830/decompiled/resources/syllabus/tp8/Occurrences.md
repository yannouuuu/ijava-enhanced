# Nombre d’occurrences d’un mot dans un tableau

Écrire la fonction `int nbFois(String[] tab, String mot)` qui calcule le nombre de fois que `mot` apparait dans le tableau `tab`. Lisez attentivement le test ci-dessous afin de trouver la signature de `nbFois`.

```java
void testNbFois () {
    String[] tab = new String[]{"je", "tu", "tu", "elle", "je", "je"};
    // "je" apparait 3 fois dans tab
    assertEquals(3, nbFois(tab, "je"));
    assertEquals(2, nbFois(tab, "tu"));
    assertEquals(0, nbFois(tab, "il"));
}
```
