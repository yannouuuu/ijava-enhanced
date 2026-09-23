# Plus grand commun diviseur

L’algorithme d’Euclide permet de calculer le *Plus Grand Commun Diviseur* (PGCD) de deux entiers `a` et `b` à l’aide
de divisions successives.

On suppose que `a ≥ b` et que `a` et `b` sont tous deux positifs ou nuls. À chaque itération, on divise `a` par `b`, soit `r` le reste de cette division. Si `r` vaut 0, alors on arrête et `b` contient le PGCD de `a` et `b`, sinon, on continue l’itération en donnant à `a` la valeur de `b` et à `b` celle de `r`.

Le tableau ci-dessous présente les valeurs successives de `a`, `b` et `r` lors du calcul du PGCD de `80` et `62`. La dernière valeur de `a` est le PGCD.
<table>
  <tr><td>a</td> <td>b</td> <td>r = a % b</td></td>
  <tr><td>21</td><td>15</td><td>6</td></td>
  <tr><td>15</td><td> 6</td><td>3</td></td>
  <tr><td> 6</td><td> 3</td><td>0</td></td>
  <tr><td> 3</td><td> 0</td><td>-</td></td>
</table>

Écrivez le programme `PGCD` qui calcule le PGCD de deux nombres saisis au clavier, en supposant que ces nombres sont
positifs. Voici deux exemples d’exécution illustrant l'affichage attendu :
```bash
~java2/tp4> ijava execute PGCD
80
70
Le pgcd est 10
~java2/tp4> ijava execute PGCD
5
3
Le pgcd est 1
```

<p class="flip" onclick="show()">Avez-vous la solution la plus simple ?</p>
<div id="hint" style="display: none">
  <p>Ce programme ne doit contenir qu'une unique boucle à évènement (ni alternative, ni connecteur logique).</p>
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
