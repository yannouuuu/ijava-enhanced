# Connecteurs logiques et chaînes de caractères

Dans cet exercice, votre objectif est de déterminer les conditions exprimant certaines propriétés sur des chaînes de caractères.

Au début du programme, trois chaînes de caractères `a`,  `b` et `c` sont saisies auprès de l'usager et vous avez ensuite différentes variables booléennes qui sont définies et que vous devez initialiser à l'aide d'une condition.

```java
    void algorithm(){
        String a = readInt();
        String b = readInt();
        String c = readInt();
        // Écrire les conditions à la place des valeurs false
        // Condition la longueur de a est inférieure à 5
        boolean condLongAinf5 = false ;
```

Ainsi, la première condition que vous devez déterminer devra vérifier la propriété que la longueur de la chaîne a est strictement inférieur à 5. Il faut donc effacer la valeur `false` et la remplacer par la condition indiquée.

::: question
**A votre avis, la condition qu'il faut trouver est ... ?**

- [ ] length(a) > 5
  > Incorrect : on demande à ce que cela soit inférieur à 5, pas supérieur !
- [x] length(a) < 5
  > Parfait : bien joué !
- [ ] length(a < 5)
  > Attention : a < 5 va s'évaluer à un booléen, et du coup, la fonction `length` ne peut pas prendre un booléen pour réaliser son traitement ...
- [ ] a < 5
  > Alors, a est une chaîne de caractères et 5 un entier, que signifierait la comparaison entre une chaîne de caractères et un entier ?!
:::

Complétez l'ensemble des conditions permettant d'initialiser toutes les variables et vérifiez ensuite si c'est valide en exécutant votre programme avec ces différentes entrées :
<table>
  <tr>
    <td style="text-align: center">"bonjour", "jour", "bon"</td>
    <td style="text-align: center">"", "xx", "xxx"</td>
    <td style="text-align: center">"yyy", "yyy", "x"</td>
  </tr>
</table>

Si vous n'avez pas trouvé d'erreur, n'oubliez pas de terminer par `ijava test ConditionsSurChaines`.

<p class="flip" onclick="show()">Si vous ne comprenez pas pourquoi le test d'ijava est au rouge, cliquez ici (après avoir essayé de déboguer par vous même !) ...</p>

<div id="hint" style="display: none">
  <p>A votre avis, que peut-il se passer si l'on doit traiter une chaîne vide avec `charAt` ou `substring` ? ;)</p>
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
