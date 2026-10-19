# Dessinons un caractère au milieu d'une ligne

Maintenant que l'on dispose d'une fonction (dessineLigne) pour dessiner un caractère donné un certain nombre de fois, on souhaiterai disposer d'une autre fonction pour dessiner un caractère au milieu d'une ligne d'une taille donnée.

::: question
**Est-ce que dessineLigne pourrait nous servir pour dessineMilieu ?**

- [x] Oui !
  > Effectivement cela serait judicieux :)
- [ ] Non !
  > Techniquement, on peut s'en passer, mais vu qu'elle existe, autant la réutiliser ...
- [ ] Difficile à dire ...
  > Certes, mais si l'on étudie le traitement à réaliser, on se rend compte qu'il faut afficher quelques espaces avant le caractère placé au milieu en fonction de la taille ... pourquoi ne pas utiliser dessineLigne pour cela ?!
:::

Voici quelques exemples d'appels à la fonction `dessineMilieu` et de l'affichage produit
```java
dessineMilieu('a', 5) affichera "  a  "
dessineMilieu('i', 4) affichera "  i " (compliqué le "vrai" milieu avec les tailles paires ...)
dessineMilieu('%', 0) affichera "" (rien ... mais il ne doit pas y avoir d'erreur)
```

Complétez le squelette fourni en **NE MODIFIANT PAS ALGORITHM** et en recopiant le code de `dessineLigne` de l'exercice précédent.

<p class="flip" onclick="show()">(Après avoir fini tous les exercices, CLIQUEZ ICI !)<br/>Avez-vous la solution la plus simple ?</p>
<div id="hint2" style="display: none">
  <p>Si vous n'avez pas appelé <code>dessineLigne</code>, il faut retravailler votre code !</p>
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
