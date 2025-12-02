# Trouver une solution pour le jeu LettresCrush

Il s'agit maintenant d'écrire une fonction (récursive) qui détermine si pour une chaine de caractères donnée, il existe une solution pour élimination de toutes les lettres avec les règles du jeu `LettresCrush`.
La fonction doit passer ce test :
```
void test_solutionExiste () {
    assertEquals(3, TAILLE_MIN_ZONE);
    assertTrue(solutionExiste("xxxooooxx"));
    assertFalse(solutionExiste("abcde"));
    assertTrue(solutionExiste(""));
    assertFalse(solutionExiste("ab"));
    assertFalse(solutionExiste("aa"));
    assertFalse(solutionExiste("aabbbaaabb"));
    assertTrue(solutionExiste("oaabbbaoo"));
}
```
Par exemple, comme on l'a vu, pour la chaine `"xxxooooxx"` une solution possible est d'éliminer d'abord à partir de l'indice 3 (les `'o'` du milieu), pour obtenir `"xxxxx"`, et d'éliminer ensuite les lettres restantes à partir de n'importe quel indice.

Pour la chaine `"aabbbaaabb"` par contre, il n'existe aucune solution. En effet, qu'on commence par éliminer les `'a'` ou les `'b'` du milieu, on va toujours se retrouver avec des lettres qui restent à la fin. 

L'algorithme de la fonction `boolean solutionExiste(String s)` est simple à énoncer :
> Pour chacun des indices de la chaine `s`, tester si on peut éliminer la lettre se trouvant à cet indice. Si oui, vérifier si une solution existe pour la chaine obtenue après élimination. On peut s'arrêter dès qu'on trouve un indice qui convient (récursivement).

Commencez par réfléchir aux cas d'arrêt :

- Quelle condition sur la chaine `s` permet d'être sûr·e qu'une solution existe ?
- Quelle condition sur la chaine `s` permet d'être sûr·e qu'il n'existe pas de solution ?

Ensuite, il faut écrire le cas général décrit ci-dessous qui essaie de trouver récursivement une solution à partir de chacune des positions de la chaine `s`.