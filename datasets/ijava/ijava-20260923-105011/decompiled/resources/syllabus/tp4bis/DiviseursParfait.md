# Diviseurs et nombre parfait

Après les exercices sur les diviseurs d'un nombre, un autre exercice donne la définition d'un nombre parfait.

Un nombre `n` est dit parfait si la somme de ses diviseurs stricts (sans n!) vaut `2 × n`.

Par exemple, `6` est un nombre parfait car `1 + 2 + 3 + 6 = 12 = 2 × 6`.

En partant de votre programme `Diviseurs`, concevez le programme `DiviseursParfait` ajoutant juste l'affichage `Nombre parfait !` lorsque c'est le cas.

```bash
~ijava2/tp4bis> ijava execute DiviseursParfait
7
Diviseurs : 7 1.
~ijava2/tp4bis> ijava execute DiviseursParfait
6
Diviseurs : 6 3 2 1.
Nombre parfait !
```

<p class="flip" onclick="show()">Avez-vous la solution la plus simple ?</p>
<div id="hint" style="display: none">
  <p>Ce programme ne doit contenir qu'une boucle à compteur et deux alternatives. </p>
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