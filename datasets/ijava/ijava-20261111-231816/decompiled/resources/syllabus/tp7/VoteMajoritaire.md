# Vote majoritaire

On considère rangés dans un tableau les votes sur une proposition. La valeur `true` correspond à un vote pour, la valeur `false` à un vote contre. Pour qu’une proposition soit adoptée, il faut qu’il y ait plus de votes pour que de votes contre. 

Écrivez la fonction `estAdopte` qui passe le test fourni.

<p class="flip" onclick="show()">(Après avoir fini tous les exercices, CLIQUEZ ICI !)<br/>Avez-vous la solution la plus simple ?</p>
<div id="hint2" style="display: none">
  <p>Rassure-moi, vous n'avez quand même pas contribué à brûler la planète en utilisant une boucle for ?</p>
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