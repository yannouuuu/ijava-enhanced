# Un peu plus loin sur les fonctions


::: question
**Quelle est la meilleure pratique concernant les noms des fonctions en ijava ?**

- [ ] Ils doivent être en majuscules.
  > Non, la convention est d'utiliser la casse camel (commeCeciParExemple).
- [x] Ils doivent commencer par une minuscule et suivre la casse camel.
  > Correct, c'est la convention standard en ijava.
- [ ] Ils peuvent être appelés n'importe comment sans aucune règle.
  > Non, il est recommandé de suivre les conventions de nommage.
- [ ] Ils doivent toujours inclure un préfixe "fn".
  > Non, il n'y a pas de tel préfixe requis.
:::

::: question
**Que signifie surcharger (overloading) une fonction ?**

- [ ] Définir plusieurs fonctions avec le même nom mais des types de retour différents.
  > Non, cela dépend des paramètres, pas du type de retour.
- [x] Définir plusieurs fonctions avec le même nom mais des paramètres différents.
  > Effectivement, la surcharge se base sur des signatures différentes.
- [ ] Modifier le corps d'une fonction existante.
  > Non, cela ne définit pas la surcharge.
- [ ] Appeler une fonction de manière récurrente.
  > Non, ce n'est pas du tout la définition de la surcharge.
:::

::: question
**Que se passe-t-il lorsqu'une fonction appelée se termine par une boucle infinie ?**

- [ ] Le programme continue sans problème.
  > Non, la boucle infinie bloque l'exécution de la fonction.
- [ ] La fonction s'arrête et retourne une valeur.
  > Non, elle ne s'arrête pas si elle est infinie ...
- [x] Le programme se bloque ou génère une exception.
  > Effectivement, cela peut entraîner le gel du programme.
:::
