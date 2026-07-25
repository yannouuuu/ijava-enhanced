# Notion de tableau : manipulations


::: question
**Quelle proposition indique la bonne séquence d'étape d'usage d'un tableau ?**

- [ ] déclaration, initiatilisation, allocation, lecture/écriture
  > Non, non, non, il faut d'abord allouer la mémoire avant d'initialiser le tableau.
- [ ] déclaration et initiatilisation, allocation, lecture/écriture
  > Sans avoir réserver la mémoire avant, impossible d'initialiser quoique ce soit.
- [x] déclaration, allocation, initiatilisation, lecture/écriture
  > Exactement : bien joué !
- [ ] allocation, déclaration, initiatilisation, lecture/écriture
  > Hum, avant de demander de la mémoire, il faut expliquer pour quoi on en a besoin.
:::

::: question
**Que se passe-t-il si vous tentez d'accéder à un indice en dehors des limites d'un tableau ?**

- [ ] Le programme s'exécute normalement.
  > Incorrect. Accéder à un indice invalide entraînera une erreur.
- [x] Une exception ArrayIndexOutOfBoundsException est lancée.
  > Exact. Cela se produit lorsque l'indice est inférieur à 0 ou supérieur ou égal à la longueur du tableau.
- [ ] L'indice est automatiquement ajusté à la limite du tableau.
  > Faux, cela ne se produit pas en Java. Un indice invalide n'est pas corrigé.
- [ ] Le tableau retourne une valeur par défaut.
  > Ceci est incorrect, car aucun accès ne peut être fait à un indice invalide.
:::

::: question
**Comment déclarer et allouer un tableau d'entiers de taille 5 en Java ?**

- [ ] int tableau[5];
  > Incorrect. En Java, la syntaxe de déclaration ne permet pas de spécifier la taille ainsi à la déclaration et surtout les crochets doivent être avec le type !
- [ ] int tableau = new int[5];
  > Faux, la déclaration doit inclure les crochets pour déclarer un tableau d'entiers et pas juste un entier.
- [ ] int tableau[0] = new int[5];
  > Cela est incorrect, cette syntaxe n'existe pas en Java.
- [x] int[] tableau = new int[5];
  > Exact, c'est la bonne syntaxe pour déclarer et allouer un tableau d'entiers grâce à l'opérateur new.
:::

::: question
**Quel est le format correct pour déclarer un tableau à deux dimensions ?**

- [x] int[][] tableau;
  > Correct. C'est la bonne syntaxe pour déclarer un tableau à deux dimensions.
- [ ] int tableau[][];
  > Bien que cela fonctionne également, c'est à déconseiller fortement car cela éclate la définition du type à deux endroites différents de l'expression.
- [ ] int[2][2] tableau;
  > Incorrect, car il ne s'agit pas de la syntaxe de déclaration en Java.
- [ ] tableau[2][2] int;
  > Ce format est incorrect ; la déclaration doit être faite avant la définition des dimensions.
:::

::: question
**Lorsque l'on calcule le nombre d'occurences d'un caractère dans une chaîne, on utilise ...**

- [ ] un parcours partiel, une alternative et un accumulateur
  > Non, il faut parcourir tous les caractères !
- [x] un parcours complet, une alternative et un accumulateur
  > Oui, bien vu !
- [ ] un parcours partiel, une cascade d'alternative et un accumulateur
  > Déjà si l'on n'étudie pas tous les caractères, c'est mal parti ...
- [ ] un parcours complet, une cascade alternative et un accumulateur
  > Non ... pourquoi plusieurs alternatives, il n'y a qu'une cas à vérifier !
:::
