# Remplacer dans une chaine

On souhaite créer la fonctionnalité *Rechercher-remplacer* dans un éditeur de texte. On vous demande pour cela d’écrire une fonction qui prend en paramètre trois chaînes de caractères (`phrase`, `avant`, `apres`) et retourne en résultat une nouvelle chaîne dans laquelle toutes les occurrences de `avant` ont été remplacées par `apres`.

Créez le programme `Remplacer` validant la fonction de test suivante :
```java
void test_copieEnRemplacant_avec_differents_arguments () {
    assertEquals("15x35"  , copieEnRemplacant("15*35"     , "*"   , "x"));
    assertEquals("15 + 35", copieEnRemplacant("15 plus 35", "plus", "+"));
    assertEquals("abcd"   , copieEnRemplacant("abcd"      , "cb"  , "xy"));
    assertEquals("abcd"   , copieEnRemplacant("abcd"      , ""    , "x"));
    assertEquals("abcd"   , copieEnRemplacant("-ab-cd-"   , "-"   , ""));
    assertEquals("xx"     , copieEnRemplacant("aaaa"      , "aa"  , "x"));
    assertEquals("xxa"    , copieEnRemplacant("aaaaa"     , "aa"  , "x"));
}
```

Une fois les tests au vert, ajoutez une dernière assertion dans la fonction de test qui vérifie le remplacement de `moins`par `plus` dans la phrase `"9 moins 3 moins 3"`. Vérifiez que les tests sont toujours bien au vert !

<p class="flip" onclick="show()">(Après avoir fini tous les exercices, CLIQUEZ ICI !)<br/>Avez-vous la solution la plus simple ?</p>
<div id="hint2" style="display: none">
  <p>Une boucle et une alternative : pas plus !</p>
</div>

<script>
function show() {
  var x = document.getElementById("hint2");
  if (x.style.display === "none") {
    x.style.display = "block";
  } else {
    x.style.display = "none";
  }
}
</script>