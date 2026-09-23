# Syracuse le retour

Maintenant que l'on sait calculer la suite de Syracuse, nous allons nous intéresser à certaines propriétés.

Reprenons l’exemple de l’entier `7` qui produit cette suite : `7, 22, 11, 34, 17, 52, 26, 13, 40, 20, 10, 5, 16, 8, 4, 2, 1`

Voici la définitions des différentes propriétés : 
- la **vol** de 7 est la suite des nombres données ci-dessus (déjà fait !)
- on considère que chaque entier est une **étape** du vol
- l'**altitude maximale** est le plus grand nombre atteint (`52`)
- la **durée du vol** est le nombre d'étapes nécessaires avant l'apparition de `1`
- la **durée du vol en altitude** est le nombre d'étapes entre le début du vol et le moment où le nombre courant passe en dessous de la valeur de départ (`11`)
- le **facteur d'expansion** est l'altitude maximale divisée par l'entier de départ (`52/7`)

En vous basant sur ce que vous avez déjà réalisé pour `Syracuse`, ajoutez les variables et structures de contrôles suffisantes pour produire un affichage similaire à celui donné en exemple ci-dessous :
```bash
Entrez un nombre: 7
Vol: 7, 22, 11, 34, 17, 52, 26, 13, 40, 20, 10, 5, 16, 8, 4, 2, 1.
Altitude max: 52
Durée de vol: 16
Durée de vol en altitude: 11
Facteur d'expansion: 7
```

<p class="flip" onclick="show()">Avez-vous la solution la plus simple ?</p>
<div id="hint" style="display: none">
  <p>Ce programme ne doit contenir qu'une boucle à évènement et trois alternatives.</p>
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
