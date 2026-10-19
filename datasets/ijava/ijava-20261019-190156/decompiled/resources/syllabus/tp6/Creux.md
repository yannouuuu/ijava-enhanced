# Dessinons un triangle creux

On souhaite dessiner des triangles creux en fonction d'une `taille` et d'un `caractere` tous deux saisis auprès de l'utilisateur ou l'utilisatrice.

Voici un exemple d'exécution du programme `Creux`.

```java
Taille : 5
Caractere : o
o
oo
o o
o  o
ooooo
```
Définissez la fonction `void algorithm()` produisant une sortie similaire (bien sûr dépendante de la `taille` et du `caractere` ...).

**ATTENTION : IL NE FAUT DESSINER QUE LES CARACTÈRES NÉCESSAIRES POUR LE DESSIN !**

<p class="flip" onclick="show()">(Après avoir fini tous les exercices, CLIQUEZ ICI !)<br/>Avez-vous la solution la plus simple ?</p>
<div id="hint2" style="display: none">
  <p>Avez-vous bien 2 boucles imbriquées et une troisième ensuite ?</p>
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