# Passages de programmes à des fonctions

Nous avons dans les étapes précédentes définis deux programmes réalisant deux dessins différents : `Plein` qui dessine des triangles remplis et `Creux` qui dessine des triangles creux.

On souhaite maintenant créer un programme `Dessins` qui regroupe ces deux fonctionnalités et facilite leur réutilisation. Pour cela, on souhaite transformer chacun de ces programmes en une fonction. 

Ainsi, dans le programme `Dessins` :
- ce qu'affichait le programme `Plein` devient la fonction `trianglePlein`,
- de même ce qu'affichait le programme `Creux` devient la fonction `triangleCreux`.

Afin de se faciliter un peu la tâche, vous introduirez la fonction `String nbFois(char c, int n)` qui comme son nom l'indique apporte une facilité existante en python ('c' * 5 !) mais pas en Java.

<p class="flip" onclick="show()">(Après avoir fini tous les exercices, CLIQUEZ ICI !)<br/>Avez-vous la solution la plus simple ?</p>
<div id="hint2" style="display: none">
  <p>Avez-vous bien simplifié vos fontions `trianglePlein` et `triangleCreux` en utilisant `nbFois` ?</p>
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