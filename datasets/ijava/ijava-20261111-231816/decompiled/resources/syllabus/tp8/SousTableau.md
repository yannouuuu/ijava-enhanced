# Copie d'un sous-tableau

Autant il est pratique de disposer d'une fonction comme `substring`sur les chaînes de caractères, autant il serait très pratique de disposer de l'équivalent mais sur des tableaux. Pour remédier à cela, et cela nous servira, nous allons créer une fonction `String[] sousTableau(String[] tab, int idxD, int idxF)` permettant de copier le sous-tableau de `tab` compris entre l'indice de début `idxD` inclus et l'indice de fin `ìdxF` exclus (pour suivre l'exemple de `substring`). Si jamais les indices données en paramètres sont invalides alors la fonction va retourner **une copie exacte** de `tab`.

```java
    void testSousTableau () {
        assertArrayEquals(new String[]{"a", "b", "c"}, 
            sousTableau(new String[]{"a", "b", "c", "d", "e"}, 0, 3));
        assertArrayEquals(new String[]{"a", "b"},
            sousTableau(new String[]{"a", "b"}, 0, 5));
        assertArrayEquals(new String[]{},   
            sousTableau(new String[]{"a", "b", "c"}, 0, 0));
        assertArrayEquals(new String[]{"a", "b"},
            sousTableau(new String[]{"a", "b"}, 0, -5));

        // L’assertion suivante sert à tester qu’on retourne bien une copie du tableau
        // et non le tableau lui-même
        String[] tab = new String[]{"a", "b"};
        assertNotEquals(tab, sousTableau(tab,  2, 1));
        assertNotEquals(tab, sousTableau(tab, -1, 2));
        assertNotEquals(tab, sousTableau(tab,  1, 7));
    }
```

Voici l'algorithme (non trivial) à implémenter pour réaliser cette fonction :
```
déclarer le tableau pour le résultat de la fonction
si les indices sont invalides
  allouer le tableau à la taille du tableau donné en paramètre
  initialiser l'indice de début à 0
sinon
  allouer le tableau à la taille (idxF-idxD)
finsi
parcourir entièrement le tableau résultat
  initialiser la idxième case du tableau par la (idxD+idxième) case du tableau source
retourner le (sous)tableau copié
```
