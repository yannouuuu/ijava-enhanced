# Décomposition du jeu du Pendu

Maintenant que les structures de données sont définies, on peut s’intéresser à la décomposition du jeu.

Il y a plusieurs décompositions possibles, qui demanderont d’écrire des fonctions différentes. Dans tous les cas, chacune de ces fonctions doit être testée. 

::: question
**Est-il préférable d'écrire la procédure de test avant la fonction testée ou pas ?**
- [ ] D'abord le code fonctionnel et ensuite la procédure de test.
  > Hum, ce n'est pas l'idéal : si on écrit d'abord le code fonctionnel, cela nous biaise sur les tests à réaliser car l'on sait comment est implémenté la fonction.
- [x] D'abord la procédure de test ensuite le code fonctionnel
  > Oui, c'est préférable ! Ainsi, on se pose d'abord la question des cas limites, ce qui nous aide dans l'analyse des situations que doit traiter la fonction. C'est l'approche utilisée en *Test Driven Development* qui est maintenant bien utilisé dans l'industrie du logiciel.
:::

Les fonctions de saisie sont testées à l’aide de `void algorithm()`, toutes les
autres fonctions sont testées avec des fonctions de test dédiées.

Dans la suite vous serez guidés pour écrire les fonctions qui correspondent à une certaine décomposition. Si vous êtes aventureux, vous pouvez vous en inspirer et éventuellement les adapter à votre proposition de décomposition (mais l'on vous conseille fortement de suivre notre proposition ;)).

## Définition de la fonction `String toString (Lettre lettre)`

Une lettre est découverte ou pas ! Ainsi cette fonction retournera le caractère `*`si la lettre n'est pas encore découverte et le caractère associé à cette lettre sinon.

* Définissez la procédure `void test_toString_Lettre()` qui contient au moins deux assertions permettant de tester que le résultat retourné par `toString` est correct. Ces assertions devront tester les deux cas : un pour une lettre non découverte, et l'autre pour une lettre découverte.

* Définissez ensuite la fonction `String toString (Lettre lettre)` qui retourne la chaîne  `"*"` si lettre n’est pas découverte, et le caractère correspondant sinon.

## Définition de la fonction `String toString (Lettre[] chaîne)`

Cette fonction d'affichage se basera sur le fait que certaines lettres sont découvertes et d'autres pas. Ainsi, la chaîne retournée correspondra à l'état actuel du mot à deviner pour lequel les lettres non découvertes apparaissent sous la forme d'une étoile `*` et celles découvertes par leur caractère.

* Selon la même approche, définissez d'abord la procédure `void test_toString()` qui permettra de valider la fonction `String toString (Lettre[] chaîne)` que vous écrirez ensuite. Pour cela, soyez attentifs à réutiliser `toString(Lettre)`.

::: question
**Comment déterminer si un mot, représenté sous la forme de `Lettre[] mot`, est totalement découvert ?**
- [ ] En vérifiant qu'aucun des cases de `mot` n'est `null` ?
  > Heu, pas vraiment, après l'appel à `creerMot`, toutes les cases sont initialisisées normalement !
- [ ] En vérifiant simplement que `mot[0].decouvert` est à `true`. 
  > Alors, ceci indiquerait que la première lettre est découverte, mais pour que le mot soit découvert, il faudrait que **toutes** les lettres soient découvertes !
- [x] En vérifiant que tous les champs `decouvert` de chacune des `Lettre`valent bien `true`.
  > Bien vu ! Et c'est exactement ce qu'il faut faire juste après ;)
:::

## Définition de la fonction `boolean estDecouvert(Lettre[] mot)`

Nous allons maintenant nous intéresser au fait de déterminer si un mot à été totalement découvert. La fonction `estDecouvert` permet d'indiquer si la totalité du mot a été découvert, c'est-à-dire que toutes les lettres le composant sont découvertes.

* D'abord, commencez par définir la procédure `void test_estDecouvert()` qui doit au moins contenir trois assertions correspondant à ces cas:
— mot entièrement découvert,
— mot non entièrement découvert,
— mot de longueur 0.
* Définissez ensuite la fonction `boolean estDecouvert(Lettre[] mot)` qui permet de déterminer si le `mot` donné en paramètre est entièrement découvert.

## Définition de la fonction `boolean decouvrir(Lettre[] mot, char car)`

Dernière étape au niveau des fonctionnalités nécessaire avant de pouvoir s'attaquer à l'algorithme principal, la fonction permettant de découvrir toutes les lettres correspond à celle proposée par la joueuse ... si jamais cette lettre est présente dans le mot bien sûr !

* Définissez d'abord la procédure `void testDecouvrir()` qui vérifiera ces trois situations : 
- que le mot n'est pas modifié si la lettre proposée n'est pas présente,
- que le mot est mis à jour si la lettre est présente une fois dans le mot,
- que le mot est mis à jour si la lettre est présente plusieurs fois dans le mot.
* Définissez ensuite la fonction `boolean decouvrir(Lettre[] mot, char car)` qui découvre toutes les occurrences de la lettre `car` dans `mot`. La fonction retourne `true` si la lettre appartient au mot (càd au moins une occurrence a été découverte), et `false` sinon.

Ça y est : après tout ces travaux préparatoire, il est maintenant possible d'écrire notre programme principal (`void algorithm()`) puis finalement jouer à notre nouvelle version du jeu du Pendu utilisant un type spécifique !
