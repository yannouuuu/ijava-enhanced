# Nombre d'occurrences d'un caractère dans une chaîne : version récursive !

Nous avons déjà réalisé un programme calculant le nombre d'occurrences dans une chaîne de caractère, mais de manière classique avec une boucle à compteur pour parcourir les indices de la chaîne et un accumulateur pour mémoriser les occurrences rencontrées.

Nous souhaitons maintenant résoudre ce même problème, mais en tirant partie d'une approche récursive. Il est donc important d'identifier les cas de bases et de trouver ensuite le ou les appels récursifs.

Nous allons réaliser cela de deux manières différentes : 
* la première, plus intuitive, mais moins performante (récursivité non terminale),
* la seconde, plus complexe, mais plus efficiente (récursivité terminale).

# Version intuitive non récursive terminale

Pour la première version, on souhaite résoudre ce problème en ne définissant qu'une seule fonction `int nbOccurrences(String mot, char lettre)`.

::: question
**A votre avis, sur lequel des deux paramètres porte la récursivité ?**
- [ ] char lettre
  > Voilà qui est étonnant ! La donnée sur laquelle porte la récursivité évolue lors appels récursif, or le caractère étant celui que l'on recherche, si il évolue, le calcul du nombre d'occurrences ne ferait plus aucun sens :(
- [x] String mot
  >  Exactement : c'est cette information que l'on va faire évoluer d'appel récursif en appel récursif (généralement en la faisant diminuer).
- [ ] Les deux !
  > Intéressant, on change donc le mot et la lettre recherchée à chaque étape ? Cela va être difficile de calculer un nombre d'occurrence **d'un** caractère dans une chaîne dans ces conditions !
:::

Tout à coup, cette phrase surgit de votre mémoire : *"concentre-toi sur les cas les plus simples, sachant qu'on travaille sur une chaîne de caractères ... "*.

Sur ce problème, avec la chaîne la plus simple, le nombre d'occurrence est assez évident, non ?

Par contre, lorsque l'on traite un caractère, deux situations se produisent :
* soit c'est celui que l'on recherche et cela indique que l'on a trouvé une nouvelle occurrence, 
* soit ce n'est pas le cas, et du coup, le nombre d'occurrences restent le même.

Néanmoins, que l'on soit dans l'une ou l'autre de ces situations, il faut étudier le reste de la chaîne ...

Rappelez-vous la démarche pour l'écriture d'une fonction récursive :
* identifiez d'abord les cas d'arrêts, qui correspondent généralement aux situations où la donnée sur laquelle porte la récursivité est réduite à sa plus simple expression ou presque.
* intéressez-vous ensuite à où aux appels récursifs, c'est-à-dire lorsque l'on rappelle la fonction que l'on est en train de définir, mais en effectuant juste une petite étape du calcul et au fait de faire "diminuer" la donnée sur laquelle porte la récursivité (ainsi on se rapproche du ou des cas d'arrêt).

Définissez maintenant la fonction `int nbOccurrences(String mot, char lettre)`.

# Version plus efficiente en récursive terminale

La version précédente s'écrit en quelques lignes, mais n'est pas très performante (rappel : dessinez l'arbre d'évaluation pour vous en convaincre !). Nous allons donc écrire une nouvelle version plus efficace. Comme il est nécessaire de maintenir un état (le nombre d'occurrences actuellement rencontré) d'appel récursif en appel récursif ...

::: question
** ... quelle technique est-il nécessaire de mobiliser pour ces situations ?**
- [ ] une boucle `for` et une alternative imbriquée dedans
  > Hum, certes si l'on résoud ce problème de manière impérative, sauf que là on souhaite le faire de manière récursive.
- [ ] introduire une variable globale pour accéder à l'état.
  > Techniquement, cela serait possible, mais on souhaite limiter au maximum l'usage des variables globales car cela rend plus difficile de tester les programmes.
- [x] introduire une fonction auxiliaire pour transmettre l'état d'appel en appel.
  > Tout à fait ! Cette fonction aura un paramètre en plus afin de transmettre le nombre d'occurrences d'appel récursif en appel récursif : `int nbOccurrencesTerminale(String mot, char lettre, int compteur)`.
:::

Définissez la(les) fonction(s) `int nbOccurencesTerminale(String mot, char lettre, int compteur)`.
