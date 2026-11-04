# Trions trois caractères

Concevez le programme `Tri` permettant d’ordonner, selon l’ordre ASCII, trois caractères quelconques en complétant
le squelette suivant.

```java
class Tri extends Program {
   void algorithm(){
      char c1, c2, c3;
      c1 = readChar();
      c2 = readChar();
      c3 = readChar();
    // à compléter pour que c1, c2 et c3 contiennent les caractères saisis dans l’ordre ASCII


      println("" + c1 + c2 + c3); // NE RIEN MODIFIER ICI !
   }
}
```

Voici une série de valeurs avec lesquelles mettre à l’épreuve votre programme :
<table style="text-align: center;">
  <tr><td>Saisie 1</td><td>Saisie 2</td><td>Saisie 3</td><td>Affichages</td></tr>
  <tr><td>a</td><td>b</td><td>c</td><td>abc</td></tr>
  <tr><td>b</td><td>a</td><td>c</td><td>abc</td></tr>
  <tr><td>c</td><td>b</td><td>a</td><td>abc</td></tr>
  <tr><td>b</td><td>c</td><td>a</td><td>abc</td></tr>
  <tr><td>c</td><td>a</td><td>b</td><td>abc</td></tr>
  <tr><td>e</td><td>d</td><td>b</td><td>bde</td></tr>
</table>

<p class="flip" onclick="show()">Question subsidiaire : combien de comparaisons de nombres contient votre fonction ? (Cliquez ici pour un challenge !)</p>
<div id="hint" style="display: none">
  <p>Il est possible de résoudre ce problème en n’utilisant que trois fois un opérateur de comparaison. Si ce n'est déjà fait, cherchez comment améliorer votre programme ;)</p>
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


