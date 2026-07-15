# Modélisation des dates de début et fin de mandat

Comme vous l'avez remarqué dans le fichier CSV, nous avons des dates qui sont présentes pour identifier les débuts et fin de mandats. 

Lors d'un TD précédent, nous avions travaillé la modélisation d'une date. Pour notre contexte, nous n'allons pas faire le même choix concernant les mois (énumération) et simplifier un peu en utilisant trois champs entiers.

Définissez le type `Date` avec les champs *jour*, *mois* et *année*.

::: question
**Quelle signature choisiriez-vous pour le constructeur de `Date` ?**
- [ ] Date newDate(int jour, int mois, int annee)
  > Alors cela correspond à l'intuition et l'implémentation habituelle, mais dans le fichier CSV les dates sont représentées différemment et il serait plus pratique d'avoir un constructeur capable des les interpréter (mais l'on pourrait définir aussi celui-ci en plus).
- [x] Date newDate(String date)
  >  Bien vu ! Effectivement, les dates étant représentées sous la forme `jour/mois/année` dans le fichier CSV, il est plus pratique d'avoir ce constructeur.
- [ ] Date newDate(int jour, Mois mois, int annee)
  > Heu, on vient de dire que pour notre contexte, on ne remobilise pas la piste de l'énumération ... il faut bien LIRE L'ÉNONCÉ !
:::