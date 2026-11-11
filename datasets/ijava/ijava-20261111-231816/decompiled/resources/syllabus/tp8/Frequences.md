# Calcul des fréquences

On suppose que l'on dispose de deux tableaux de mots (`String[]`): un pour les `motsCles` et l'autre pour les `mots`. Pour chaque `mot` dans `motsCles`, on voudrait calculer combien de fois il apparaît dans le tableau `mots`.

Écrire la fonction `int[] frequences (String[] motsCles, String[] mots)` qui produit en résultat un tableau d’entiers `freqences` ayant la même taille que `motsCles` et tel que pour chaque indice `idx`, la valeur `frequence[idx]` est égale au nombre de fois où le mot `motsCles[idx]` apparaît dans le tableau `mots`. 

Il peut être utile de recopier la fonction `nbFois` écrite précédemment ;)

```java
void testFrequences () {
    String[] motsCles = new String[]{"je", "tu", "elle"};
    int[] fréquences = new int[]{3, 2, 1};
    String[] mots = new String[]{"je", "tu", "tu", "elle", "je", "je"};
    assertArrayEquals(fréquences, frequences(motsCles, mots));
}
```
