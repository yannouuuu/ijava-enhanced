# La suite de Syracuse

Définition de la suite de Syracuse : prenez un entier positif ; s’il est pair, divisez-le par 2; s’il est impair, multipliez-le par 3 et ajoutez lui 1 ... Réitérez ce processus sur plusieurs exemples : que semble-t-il se passer ?

Partons de l’entier `7` et regardons la suite : `7, 22, 11, 34, 17, 52, 26, 13, 40, 20, 10, 5, 16, 8, 4, 2, 1, 4, 2, 1, 4, 2, 1 ...`

Cette suite devient cyclique, puisque l’obtention de la valeur 1 fait “boucler” indéfiniment l’algorithme.

Quelque soit le nombre choisit, on finit toujours par trouver la valeur 1 ... c’est la conjecture de Syracuse (encore appelée “problème 3n + 1”) ... qui attend toujours une preuve !

Sur l’exemple de l’entier 7, on appellera **vol** la suite (7, 22, 11, 34, 17, 52, 26, 13, 40, 20, 10, 5, 16, 8,
4, 2, 1). 

Concevez le programme Syracuse permettant d’expérimenter la conjecture de Syracuse.

```bash
~ijava2/tp4> ijava execute Syracuse
Entrez un nombre: 7
Vol: 7, 22, 11, 34, 17, 52, 26, 13, 40, 20, 10, 5, 16, 8, 4, 2, 1.
```

<p class="flip" onclick="show()">Avez-vous la solution la plus simple ?</p>
<div id="hint" style="display: none">
  <p>Ce programme ne doit contenir qu'une boucle à évènement et une alternative.</p>
</div>

<script>
function show() {
  var x = document.getElementById("hint");
  if (x.style.display === "none") {
    x.style.display = "block";
  } else {
    x.style.display = "none";
  }
}
</script>