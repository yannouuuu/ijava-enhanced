# Usages sur les fonctions


::: question
**Quelle syntaxe est correcte pour appeler une fonction nommée calculerSomme ?**

- [ ] calculerSomme;
  > Non, il manque les parenthèses.
- [x] calculerSomme();
  > Correct, cette syntaxe appelle la fonction.
- [ ] int calculerSomme()
  > Non, ceci correspond à sa signature
- [ ] exec(calculerSomme);
  > Non, cette syntaxe n'existe pas en ijava.
:::

::: question
**Quelle est la portée des variables définies à l'intérieur d'une fonction ?**

- [ ] Elles sont accessibles dans tout le programme.
  > Non, leur portée est limitée à la fonction elle-même.
- [x] Elles sont accessibles uniquement dans la fonction où elles sont déclarées.
  > Effectivement, elles sont locales à la fonction.
- [ ] Elles sont accessibles uniquement à partir d'autres fonctions.
  > Non, il est impossible d'accéder à une variable définit dans une autre fonction.
:::

::: question
**Que se passe-t-il si une fonction a une déclaration de type de retour, mais n'inclut pas d'instruction return ?**

- [ ] Le programme se compile normalement.
  > Non, cela engendrera une erreur de compilation.
- [ ] La fonction retournera null.
  > Non, cela engendrera une erreur de compilation.
- [x] La fonction ne compilera pas.
  > Correct, il est nécessaire d'avoir une instruction return si le type de retour n'est pas void.
- [ ] La fonction retournera une valeur par défaut.
  > Non, cela engendrera une erreur de compilation.
:::

::: question
**Quelle est la manière correcte de définir une fonction qui retourne un entier ?**

- [x] int maFonction() {}
  > Correct, la déclaration est valide.
- [ ] void maFonction() { return 0; }
  > Non, cette fonction est définie pour ne pas retourner de valeur (cf. void).
- [ ] int maFonction { return 0; }
  > Non, la syntaxe est incorrecte.
- [ ] maFonction(int) {}
  > Non, cela n'est pas une déclaration valide.
:::
