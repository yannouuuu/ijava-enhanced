# Usages sur les fonctions


::: question
**Quelle syntaxe est correcte pour appeler la fonction  void afficherMenu() ?**

- [ ] afficherMenu;
  > Non, il manque les parenthèses. Même lorsque la fonction n'a pas de paramètre c'est obligatoire !
- [x] afficherMenu();
  > Correct, cette syntaxe appelle la fonction afficherMenu.
- [ ] void afficherMenu()
  > Non, ceci correspond à sa signature, c'est-à-dire à sa définition !
- [ ] println(afficherMenu());
  > ATTENTION : afficherMenu indique un résultat de type void, c'est-à-dire vide, dit autrement cette fonction ne calcule pas de résultat. Impossible de l'appeler au sein de println qui a besoin d'une valeur à afficher.
:::

::: question
**Quelle est la portée des variables définies à l'intérieur d'une fonction ?**

- [ ] Elles sont accessibles dans tout le programme.
  > Non, leur portée est limitée à la fonction elle-même.
- [x] Elles sont accessibles uniquement dans la fonction où elles sont déclarées.
  > Effectivement, on dit qu'elles sont locales à la fonction.
- [ ] Elles sont accessibles uniquement à partir d'autres fonctions.
  > Non, il est impossible d'accéder à une variable définit dans une autre fonction.
:::

::: question
**Que se passe-t-il si une fonction a une déclaration de type de retour, mais n'inclut pas d'instruction return ?**

- [ ] Le programme compile normalement.
  > Non, cela engendrera une erreur de compilation.
- [ ] La fonction retournera null.
  > Non, cela engendrera une erreur de compilation.
- [x] La fonction ne compilera pas.
  > Correct, il est nécessaire d'avoir une instruction return si le type de retour n'est pas void.
- [ ] La fonction retournera une valeur par défaut.
  > Non, cela engendrera une erreur de compilation. (i)Java n'a pas ce type de facilité (source de nombreux bugs) où la dernière instruction évaluée correspond au résultat.
:::

::: question
**Quelle est la manière correcte de définir une procédure qui retourne un entier ?**

- [x] int maFonction() { ... }
  > Correct, cette déclaration est valide : pas de paramètre et un type de retour entier.
- [ ] void maFonction() { ... }
  > Non, cette fonction est définie pour ne pas retourner de valeur car le type de retour indiqué est void !
- [ ] int maFonction { ... }
  > Syntaxe incorrecte ! Même lorsqu'il n'y a pas de paramètre, il faut le signaler explicitement avec des parenthèses n'en contenant aucun.
- [ ] maFonction(int) { ... }
  > Syntaxe incorrecte et erreur de raisonnement : d'abord il manque le type de retour et l'entier entre les parenthèses indique que la procédure nécessite un entier pour réaliser son calcul (et ce n'est pas ce qui est demandé).
:::
