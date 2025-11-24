# Notion d'assertion


::: question
**Qu'est-ce qu'une assertion en programmation ?**

- [x] Un moyen d'affirmer quelque chose de vrai dans le code.
  > Exactement, une assertion vérifie qu'une condition est vraie à un certain moment, ce qui aide à déboguer le programme.
- [ ] Une condition.
  > Non, bien qu'une assertion puisse ressembler à une condition, elle n'est pas utilisée pour contrôler le flux du programme.
- [ ] Un type de boucle.
  > Non, non, non, une assertion n'est pas utilisée pour itérer mais pour vérifier qu'une situation est vérifié et produit une erreur sinon.
- [ ] Un moyen de documenter le code.
  > C'est vrai, la documentation est importante, mais une assertion n'est pas principalement utilisée à cette fin.
:::

::: question
**Quelle est la syntaxe correcte pour utiliser assertEquals en Java ?**

- [ ] assertEqual(expected, actual);
  > Incorrect, la méthode est assertEquals, avec un "s" à la fin.
- [ ] equalsAssert(expected, actual);
  > Aïe, la fonction  utilisée pour les assertions s’appelle assertEquals, et non equalsAssert.
- [x] assertEquals(expected, actual);
  > Exact, c'est la syntaxe correcte pour comparer deux valeurs en utilisant assertEquals.
- [ ] assertEquals(actual, expected);
  > Bien que cela soit syntaxiquement valide, l'ordre des paramètres n'est pas bon : il faut d'abord donner la valeur attendue et ensuite celle qui est calculée par la fonction testée.
:::

::: question
**Quel type de valeur assertEquals peut-il comparer ?**

- [ ] Seulement des entiers.
  > Non, assertEquals peut comparer différents types de valeurs, pas seulement des entiers.
- [x] Tous les types.
  > C'est vrai, assertEquals peut être utilisé pour comparer divers types.
- [ ] Uniquement des chaînes de caractères.
  > Faux, bien qu'il soit souvent utilisé pour les chaînes, il peut également comparer d'autres types.
- [ ] Uniquement des types primitifs (int, double, char, boolean ...).
  > Ce n'est pas correct, assertEquals peut aussi être utilisé avec des chaînes de caractères (String).
:::

::: question
**Qu'est-ce qu'une fonction de test ?**

- [ ] Une fonction dont le nom débute par test.
  > Presque, mais si l'on fait uniquement cela, ce n'est pas une fonction de test tant qu'elle ne contient pas d'assertion.
- [ ] Une fonction contenant des assertions.
  > Presque, mais si l'on fait uniquement cela et que le nom de la fonction ne débute pas par test, cette fonction ne sera jamais appelé automatiquement lorsque l'on utilise la commande test !
- [ ] Une fonction contenant des alternatives.
  > Alors là, aucun rapport, désolé !
- [x] Une fonction dont le nom débute par test et contenant des assertions.
  > Exactement, il faut que ces deux éléments soient présents pour avoir une fonction de test :)
:::
