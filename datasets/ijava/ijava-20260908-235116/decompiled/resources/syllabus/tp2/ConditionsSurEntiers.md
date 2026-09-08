# Connecteurs logiques et entiers

Dans cet exercice, votre objectif est de déterminer les conditions exprimant certaines propriétés sur des entiers.

Au début du programme, trois entiers `a`,  `b` et `c` sont saisis auprès de l'usager et vous avez ensuite différentes variables booléennes qui sont définies et que vous devez initialiser à l'aide d'une condition.

```java
    void algorithm(){
        int a = readInt();
        int b = readInt();
        int c = readInt();
        // Écrire les conditions à la place des valeurs false
        // Condition a est strictement supérieure à 5.
        boolean a_superieur_a_5 = false ;
```

Ainsi, la première condition que vous devez déterminer devra vérifier la propriété que a est strictement supérieure à 5. Il faut donc effacer la valeur `false` et la remplacer par la condition indiquée.

::: question
**A votre avis, la condition qu'il faut trouver est ... ?**

- [ ] a >= 5
  > Incorrect : on demande à ce que cela soit *strictement* supérieur à 5 !
- [ ] a < 5
  > Incorrect : c'est l'inverse, ici on exprime que a doit être strictement inférieur à 5 :(
  - [x] a > 5
  > Parfait : bien joué !
- [ ] a >= 6
  > Hum, cela fonctionnerait mais n'est pas lisible et plus difficile à interpréter : à éviter !
:::

Complétez l'ensemble des conditions permettant d'initialiser toutes les variables et vérifiez ensuite si c'est valide en exécutant votre programme avec ces différentes entrées :
<table>
  <tr>
    <td style="text-align: center">0, 0, 0</td>
    <td style="text-align: center">1, 0, 5</td>
    <td style="text-align: center">2, 4, 6</td>
    <td style="text-align: center">0, 0, 34</td>
    <td style="text-align: center">2, 4, -6</td>
    <td style="text-align: center">0, 0, 34</td>
    <td style="text-align: center">2, 4, 2</td>
  </tr>
</table>

Si vous n'avez pas trouvé d'erreur, n'oubliez pas de terminer par `ijava test ConditionsSurEntiers`.
