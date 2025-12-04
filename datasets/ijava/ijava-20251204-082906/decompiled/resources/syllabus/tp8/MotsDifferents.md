# Extraire les mots différents d’un tableau de mots

Étant donné un tableau `mots` de `String`, dans lequel certains mots peuvent être répétés, on voudrait produire un autre tableau dans lequel chaque mot de `mots` apparait exactement une fois. Vous devez écrire la fonction `String[] motsDifferents (String[] mots)` qui passe ce test.
```java
void test_motsDifferents () {
    String[] mots = new String[]{"je", "tu", "tu", "elle", "je", "je"};
    String[] motsDiff = motsDifferents(mots);
    for (int idx = 0; idx < length(mots); idx = idx+1){
        assertEquals(1, nbFois(motsDiff,mots[idx]));
        //on s’assure que chaque mot apparaît une et une seule fois dans motsDiff
    }
    //on vérifie que les mots sont bien à la bonne place
    assertEquals("je", motsDifferents[0]);
    assertEquals("tu", motsDifferents[1]);
    assertEquals("elle", motsDifferents[2]);
    for (int idx = 3; idx < length(motsDifferents); idx = idx+1){
        assertEquals("", motsDifferents[idx]);
    }
}
```

La difficulté vient du fait qu’on ne peut pas savoir par avance quel est le nombre de mots différents qui apparaîssent dans le tableau `mots` donné en paramètre. Par conséquent, on ne sait pas quelle va être la taille du tableau de résultat à créer.

Pour contourner ce problème, vous allez d’abord créer un tableau temporaire `motsDiff` suffisamment grand. Pour cela, pensez que le cas le plus large est celui où mots contient des mots tous différents. Commencez par initialiser chaque case par le mot vide `""`, pour vous assurez qu’aucune ne restera sans mot. Ajoutez à présent les nouveaux mots rencontrés au fur et à mesure. Pour savoir si un mot donné est déjà contenu dans `motsDiff`, on peut utiliser la fonction `nbFois` en la recopiant de l'exercice précédent (ou écrire une nouvelle fonction `estPresent` qui serait plus efficace).

Voici une illustration des calculs qui doivent être effectués par la fonction `motsDifferents` si le paramètre `mots` est le tableau 

<table>
  <tr><td>"je"</td><td>"tu"</td><td>"tu"</td><td>"elle"</td><td>"je"</td><td>"je"</td></tr>
  <tr><td>   0</td><td>   1</td><td>   2</td><td>     3</td><td>   4</td><td>   5</td></tr>
</table>

Au départ, on initialise l’ensemble des cases de `motsDiff` avec le mot `""`.

<table>
  <tr><td>""</td><td>""</td><td>""</td><td>""</td><td>""</td><td>""</td></tr>
  <tr><td> 0</td><td> 1</td><td> 2</td><td> 3</td><td> 4</td><td> 5</td></tr>
</table>

Après avoir visité `mots[0]` (càd `"je"`), le tableau `motsDiff` devient

<table>
  <tr><td>"je"</td><td>""</td><td>""</td><td>""</td><td>""</td><td>""</td></tr>
  <tr><td>   0</td><td> 1</td><td> 2</td><td> 3</td><td> 4</td><td> 5</td></tr>
</table>

Après avoir visité `mots[1]` (càd `"tu"`), le tableau `motsDiff` devient

<table>
  <tr><td>"je"</td><td>"tu"</td><td>""</td><td>""</td><td>""</td><td>""</td></tr>
  <tr><td>   0</td><td>  1</td><td> 2</td><td> 3</td><td> 4</td><td> 5</td></tr>
</table>

Après avoir visité `mots[2]` (càd la deuxième occurrence de `"tu"`), le tableau `motsDiff` reste inchangé car le mot `"tu"` apparaît déjà dans la partie significative de `motsDiff`. 

Et ainsi de suite. Après avoir visité tout le tableau `mots`, le tableau `motsDiff` sera égal à 

<table>
  <tr><td>"je"</td><td>"tu"</td><td>"elle"</td><td>""</td><td>""</td><td>""</td></tr>
  <tr><td>   0</td><td>  1</td><td>      2</td><td> 3</td><td> 4</td><td> 5</td></tr>
</table>

Ainsi, à la fin, seules les `n` premières cases de `motsDiff` contiendront des informations utiles, où `n` est le nombre de mots différents qui apparaîssent dans `motsDiff`, les suivantes contiendront le mot vide.

Testez déjà cette première version de votre fonction à l'aide de la fonction de test donné au début de l'énoncé (et présente dans le squelette).

Une fois cette étape validée, nous allons améliorer `motsDiff` afin de retourner **exactement** les mots uniques et ne pas conserver les cases contenant les chaînes vides. Pour cela, il est sûrement judicieux de se rappeler l'existence de la fonction `sousTableau` écrite précédemment ...

Modifier votre fonction en appelant `sousTableau` (après l'avoir recopié de l'exercice précédent) et modifiez aussi `test_motsDifferents` en commentant les trois dernières lignes qui vérifiaient les cases avec les chaînes vides afin de vous assurer que tout est valide.
