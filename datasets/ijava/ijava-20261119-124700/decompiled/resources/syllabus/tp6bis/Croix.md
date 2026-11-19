# Dessinons une croix

On souhaite dessiner des croix en fonction d'une `taille` et d'un `caractere` tous deux saisis auprès de l'utilisateur ou l'utilisatrice.

Voici un exemple d'exécution du programme `Croix`.

```java
Taille : 5
Caractere : o
o   o
 o o
  o
 o o
o   o
```

Il est judicieux de reprendre la fonction nbFois afin de réaliser deux versions du dessin de la croix :
1. `croix_qui_brule_la_planete` qui parcourt violemment toutes les lignes et colonnes et affiche ce qui doit l'être,
1. 
Définissez la fonction `void algorithm()` produisant une sortie similaire (bien sûr dépendante de la `taille` et du `caractere` ...).

**ATTENTION : IL NE FAUT DESSINER QUE LES CARACTÈRES NÉCESSAIRES POUR LE DESSIN !**

<p class="flip" onclick="show()">(Après avoir fini tous les exercices, CLIQUEZ ICI !)<br/>Avez-vous la solution la plus simple ?</p>
<div id="hint2" style="display: none">
  <p>Après avoir fait la version simple utilisant deux boucles et une alternative, raitement ...</p>
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