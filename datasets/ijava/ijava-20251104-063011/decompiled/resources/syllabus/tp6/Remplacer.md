# Remplacer dans une chaine

On souhaite créer la fonctionnalité *Rechercher-remplacer* dans un éditeur de texte. On vous demande pour cela d’écrire une fonction qui prend en paramètre trois chaînes de caractères (`phrase`, `avant`, `apres`) et retourne en résultat une nouvelle chaîne dans laquelle toutes les occurrences de `avant` ont été remplacées par `apres`.

Créez le programme `Remplacer` validant la fonction de test suivante :
```java
void testCopieEnRemplacant () {
    assertEquals("15x35"  , copieEnRemplacant("15*35"     , "*"   , "x"));
    assertEquals("15 + 35", copieEnRemplacant("15 plus 35", "plus", "+"));
    assertEquals("abcd"   , copieEnRemplacant("abcd"      , "cb"  , "xy"));
    assertEquals("abcd"   , copieEnRemplacant("abcd"      , ""    , "x"));
    assertEquals("abcd"   , copieEnRemplacant("-ab-cd-"   , "-"   , ""));
    assertEquals("xx"     , copieEnRemplacant("aaaa"      , "aa"  , "x"));
    assertEquals("xxa"    , copieEnRemplacant("aaaaa"     , "aa"  , "x"));
    assertEquals("9 plus 3 plus 3", copieEnRemplacant("9 moins 3 moins 3", "moins", "plus"));
}
```
