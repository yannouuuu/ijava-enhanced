# Notion de tableau : bases


::: question
**Quelle est la différence entre la déclaration et l'allocation d'un tableau ?**

> Exact, la déclaration définit un tableau sans l'initialiser, tandis que l'allocation réserve de la mémoire pour celui-ci.
- [ ] Il n'y a pas de différence, ce sont des synonymes.
  > C'est faux, ces termes ont des significations distinctes dans le contexte de la programmation.
- [ ] La déclaration et l'allocation se font en une seule étape.
  > Ce n'est pas vrai, on doit d'abord déclarer puis ensuite il faut allouer le tableau.
- [x] La déclaration définit la variable de type tableau alors que l'allocation réserve la mémoire nécessaire pour stocker l'ensemble des éléments souhaités.
- [ ] L'allocation définit les valeurs des éléments.
  > Non, l'allocation ne fait que allouer de la mémoire, et l'initialisation est ce qui définit les valeurs.
:::

::: question
**Comment déclarer un tableau d’entiers en Java ?**

- [x] int[] tableau;
  > Correct, c'est la syntaxe appropriée pour déclarer un tableau d'entiers.
- [ ] tableau int[];
  > C'est incorrect, comme pour les variables "classiques", on donne d'abord le type et ensuite le nom de la variable.
- [ ] int tableau[];
  > Bien que ça fonctionne, la convention préférée est d'utiliser int[] tableau;. DONC INTERDICTION D'UTILISER CETTE SYNTAXE !
- [ ] def int tableau[];
  > Ce n'est pas du tout une syntaxe valide en Java ...
:::

::: question
**Quelle est la bonne façon d'allouer un tableau de chaînes de caractères de 5 éléments ?**

- [ ] String[] tableau = new String(5);
  > Incorrect, cette syntaxe est erronée pour l'allocation d'un tableau, les parenthèses devraient être des crochets ...
- [x] String[] tableau = new String[5];
  > C'est exact, cette syntaxe alloue correctement un tableau de 5 chaînes.
- [ ] String tableau[] = new String[5];
  > Bien que cela fonctionne, la première syntaxe est plus conventionnelle. NE PAS UTILISER CETTE SYNTAXE QUI EXPLOSE L'INFORMATION CONCERNANT LE TYPE DE LA VARIABLE !
- [ ] String tableau = new String[5];
  > Ce n'est pas correct car il manque les crochets dans la déclaration de la variable !
:::

::: question
**Que se passe-t-il si vous essayez d’accéder à un indice qui n’existe pas dans un tableau ?**

- [ ] Le tableau renvoie une valeur par défaut.
  > pas du tout et cela serait très embêtant comme comportement par défaut !
- [ ] Le programme fait une erreur sans lancer d'exception.
  > Faux, une exception est levée ...
- [x] Une exception ArrayIndexOutOfBoundsException est lancée.
  > Correct, c'est l'exception qui est lancée lorsque l'on tente d'accéder à un indice inválide.
- [ ] Une exception de type NullPointerException est lancée.
  > Incorrect, une NullPointerException n'est pas liée à l'accès d'un indice hors limites, c'est un autre type d'erreur liée à l'oubli d'allocation d'une variable.
:::

::: question
**Quelle est l'intervalle des valeurs des indices d'un tableau en Java ?**

- [x] Entre 0 et le nombre d'élèments du tableau moins 1.
  > Exact, les indices d’un tableau en Java commencent à 0.
- [ ] Entre 0 et le nombre d'élèments du tableau.
  > Faux, car l'on commence à compter à partir de 0, du coup le dernier indice valide est le nombre d'élément moins 1.
- [ ] Entre 1 et le nombre d'élèments du tableau moins 1.
  > Incorrect, on commence à compter à partir de 0 !
- [ ] Entre 1 et le nombre d'élèments du tableau.
  > Incorrect, on commence à compter à partir de 0 !
:::

::: question
**Quelle est la différence entre un indice et une valeur dans un tableau ?**

- [ ] La valeur est toujours numérique.
  > Incorrect, la valeur peut être d’un type quelconque, pas seulement numérique.
- [ ] Les valeurs ne peuvent pas être modifiées.
  > Faux, les valeurs dans le tableau peuvent être modifiées, c'est tout l'intérêt !
- [ ] Les indices sont des variables locales uniquement.
  > Ce n'est pas vrai, les indices sont des entiers et peuvent être utilisés dans divers contextes.
- [x] L'indice est utilisé pour accéder à la valeur stockée à cet emplacement dans le tableau.
  > Correct, l'indice permet de référencer la valeur dans le tableau.
:::
