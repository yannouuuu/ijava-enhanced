# Prise de température

Le programme `Temperature` demande à l’usager de saisir des températures afin d'afficher la valeur maximale.
La saisie des températures doit s’interromptre lorsque l’utilisateur entre la valeur `-273`, qui correspond au zéro absolu. Cette température ne doit donc pas être prise en compte dans le calcul de la valeur maximale. On supposera aussi que l'usager saisit toujours au moins un entier avant `-273`. 

Avant d’écrire l’algorithme, répondez aux questions suivantes, qui devraient vous aider à décomposer la résolution de ce problème.

On suppose que les premières températures saisies sont : `5 -9 23 98 58 -11 89 8`

::: question
**Quelle sera la valeur maximale calculée si l'usager terminer la saisie maintenant ?**

- [ ] 89
  > Hum, un peu d'attention : 98 est un peu plus grand que 89, non ? :p
- [x] 98
  > Exactement, 98 est la valeur maximale de cette liste de températures.
- [ ] 105
  > C'est certes la plus grande valeur ... mais non présente dans les températures saisies !
- [ ] -11 
  > On cherche à calculer la valeur maximale, pas minimale ...
:::

::: question
**Si l'usager saisit ensuite la valeur 6, quelle est la valeur maximale maintenant ?**
- [ ] 6
  > Sérieusement ? 6 est plus grand que 98 selon vous ?
- [x] la même qu'avant
  > Oui, car 6 < 98 donc le maximum ne change pas.
:::

::: question
**Si l'usager saisit ensuite la valeur 105, quelle est la valeur maximale maintenant ?**
- [x] 105
  > Oui et même si cela semble elevé, méfiez-vous du réchauffement climatique ...
- [ ] la même qu'avant
  > Heu, pas vraiment car 105 > 98 !
:::

::: question
**Suite à ces observations, combien de variables sont nécessaires pour calculer le maximum ?**
- [ ] 1
  > Comme il en faut au moins une pour saisir un nombre auprès de l'usager, où mémorise-t-on le maximum dans ce cas ?
- [x] 2
  > Exactement : une pour le nombre saisit et une autre pour la température maximale actuelle
- [ ] autant que de températures saisies
  > Cela serait bien embêtant puisque l'on ne sait pas combien de températures seront saisies par l'usager ...
:::

Sur un papier ou en commentaire dans votre programme (en utilisant /* et */ afin de pouvoir écrire sur plusieurs lignes), décrivez le traitement qui doit être effectué à chaque nouvel entier saisi pour déterminer la valeur maximale.

Ecrivez maintenant le programme `Temperature` qui saisit les températures et calculant le maximum au fur et à mesure des saisies et l'affiche à la fin des saisies. Votre programme produira un affichage similaire à ceux donnés en exemple ci-dessous.

```bash
~ijava2/tp4> ijava execute Temperature
Saisir une suite de valeurs entières terminée par -273.
2
7
5
-273
Le maximum est 7.
~ijava2/tp4> ijava execute Temperature
6. Et en voici un second :
-2
-7
-5
-273
Le maximum est -2.
```

<p class="flip" onclick="show()">Avez-vous la solution la plus simple ?</p>
<div id="hint" style="display: none">
  <p>Ce programme ne doit contenir qu'une boucle à évènement (while), une alternative et aucun connecteur logique. </p>
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
