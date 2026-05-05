# Masque d’une chaîne
 
Écrivez un programme `MasqueChaine` contenant la fonction `masque` qui prend en paramètre une chaîne `phrase` et un caractère `lettre`, et retourne une nouvelle chaine qui ne contient que les occurrence de `lettre` à leur emplacement. Les autres caractères sont remplacés par des points. 

**Attention** : soyez bien attentif au fait qu’ici c’est une fonction qui retourne la chaîne créée, pas d’affichage donc.

Réalisez la fonction devant passer le test qui suit. Vous pouvez vous inspirez de la fonction `copieSans` de votre cours (mais essayez par vous même avant d'aller jeter un oeil au cours !).

```java
void testMasque () {
    assertEquals("a...a...a....",    masque("au bal masqué",    'a'));
    assertEquals("................", masque("Tonari no Totoro", 'u'));
    assertEquals(".o......o..o.o.o", masque("Tonari no Totoro", 'o'));
    assertEquals("",                 masque("",                 'z'));
}
```

Il est important de laisser la fonction `void algorithm()`, même si elle n'est pas utilisée dans ce programme.
