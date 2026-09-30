# Un peu plus loin sur les fonctions

::: question
**Quelle est la meilleure pratique concernant les noms des fonctions en ijava ?**

- [ ] Ils doivent être en majuscules.
  > Non, la convention est d'utiliser la casse camel (`commeCeciParExemple`).
- [x] Ils doivent commencer par une minuscule et suivre la casse camel.
  > Correct, c'est la convention standard en (i)java.
- [ ] Ils peuvent être choisis comme l'on souhaite.
  > Non, il est recommandé de suivre les conventions de nommage recommandées pour le langage utilisé !
- [ ] Ils doivent toujours inclure un préfixe "fn".
  > Non, ce préfixe n'est pas requis.
:::

::: question
**Que signifie surcharger une fonction ?**

- [ ] Définir plusieurs fonctions avec le même nom, mais des types de retour différents.
  > Non, car dans ce cas, comment la machine pourrait déterminer quelle fonction appeler ?
- [x] Définir plusieurs fonctions avec le même nom, mais des paramètres différents.
  > Effectivement, la surcharge se base sur des signatures différentes au niveau des paramètres.
- [ ] Modifier le corps d'une fonction existante.
  > Non, cela ne définit pas la surcharge cela (en tout cas, pas tant que l'on ne bascule pas sur la programmation orientée objets ...)
- [ ] Appeler une fonction de manière récurrente.
  > Non, ce n'est pas du tout la définition de la surcharge.
:::

::: question
**Que se passe-t-il lorsqu'une fonction appelée se termine par une boucle infinie ?**

- [ ] Le programme continue sans problème.
  > Non, la boucle infinie bloque l'exécution de la fonction et les instructions situées ensuite ne peuvent jamais être atteintes.
- [ ] La fonction s'arrête et retourne une valeur.
  > Non, elle ne s'arrête pas puisque la boucle est infinie ...
- [x] Le programme ne s'arrête pas ou génère une erreur.
  > Effectivement, le programme poursuit son exécution ou parfois génère une exception liée à la saturation de la mémoire.
:::
