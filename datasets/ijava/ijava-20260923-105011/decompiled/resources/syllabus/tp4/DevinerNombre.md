# Deviner (efficacement !) un nombre

Le programme `DevinerNombre` demande à l'usager de choisir un nombre entre 1 et 100 et le trouve de manière efficace en un petit nombre de tentatives. A chaque proposition du programme, la joueuse indique si c'est la bonne valeur (en saisissant `=`), si la valeur à trouver est plus grande (`+`) ou plus petite (`-`).

Voici un exemple d’exécution, où la joueuse a choisi le nombre 28 :
```bash
Est-ce que le nombre est 50 ?
-
Est-ce que le nombre est 25 ?
+
Est-ce que le nombre est 37 ?
-
Est-ce que le nombre est 31 ?
-
Est-ce que le nombre est 28 ?
=
Il fallait trouver 28 !
```

::: question
**A votre avis, quelle technique a été utilisée par la machine ?**

- [ ] en choisissant les nombres au hasard
  > Pensez-vous que cela serait une méthode efficace permettant de trouver le nombre en un petit nombre d'essais ?
- [ ] cela me rappelle un truc avec la récursivité
  > Hum, cela aurait pu être une possibilité, mais c'est secondaire comme aspect.
- [x] cela me rappelle un truc avec la dichotomie
  > Oui, bien joué. C'est exactement cela qu'il faut faire !
- [ ] franchement, aucune idée
  > C'est vrai que ce n'était pas simple à trouver avec un seul exemple.
:::

<p class="flip" onclick="show1()">Si vous n'avez aucune idée de la technique à utiliser, CLIQUEZ ICI ! Sinon écrivez votre programme directement.</p>
<div id="hint" style="display: none">
  <p>
La manière la plus efficace pour trouver le nombre utilise la dichotomie. Pour cela, l’algorithme maintient à jour l’intervalle dans lequel peut se trouver le nombre, au début [0, 100] et propose toujours la valeur au milieu.
Au début, c'est donc la valeur 50 qui est proposée. En fonction de la réponse de l'utilisateur, l’intervalle est mis à jour soit au niveau de son début ou de sa fin.

Pour l’exemple ci-dessus, voici les valeurs successives de l’intervalle et la proposition de l’algorithme.
<table>
  <tr><td>Étape</td>      <td>1</td>       <td>2</td>      <td>3</td>       <td>4</td>       <td>5</td></tr>
  <tr><td>Intervalle</td> <td>[1, 100]</td><td>[1, 49]</td><td>[26, 49]</td><td>[26, 36]</td><td>[26, 30]</td></tr>
  <tr><td>Proposition</td><td>50</td>      <td>25</td>     <td>37</td>      <td>31</td>      <td>28</td></tr>
</table>
  </p>
</div>

<script>
function show1() {
  var x = document.getElementById("hint");
  if (x.style.display === "none") {
    x.style.display = "block";
  } else {
    x.style.display = "none";
  }
}
</script>

<p class="flip" onclick="show()">Avez-vous la solution la plus simple ?</p>
<div id="hint2" style="display: none">
  <p>Ce programme ne doit contenir qu'une boucle à évènement, deux alternatives et aucun connecteur logique. </p>
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
