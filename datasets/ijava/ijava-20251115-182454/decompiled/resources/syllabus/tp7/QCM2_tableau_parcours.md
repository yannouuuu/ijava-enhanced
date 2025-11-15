# Notion de tableau : parcours


::: question
**Comment parcourir entièrement un tableau d'entiers ?**

- [ ] for (int i = 0; i > length(tableau); i = i+1) { ... }
  > Incorrect, la condition doit utiliser < au lieu de >.
- [x] for (int i = 0; i < length(tableau); i = i+1) { ... }
  > Exact, c'est la bonne syntaxe pour parcourir complètement le tableau.
- [ ] for (int i = 1; i <= length(tableau); i = i+1) { ... }
  > Faux, cela causerait une exception car l'indice va dépasser la taille du tableau.
- [ ] for (int i = 0; i < length(tableau); i = i-1) { ... }
  > Ce n'est pas correct, car i = i-1 va décrémenter le compteur et entraîner une boucle infinie !
:::

::: question
**Que contiennent les cases d'un tableau de chaînes de caractères lorsqu'il n'est pas initialisé ?**

- [ ] Des valeurs aléatoires.
  > Incorrect, ceci serait vrai avec le langage C, mais pas en Java.
- [x] Des valeurs null.
  > Exactement, cette constante indique qu'il n'y a pas (encore) de valeurs stockée.
- [ ] Un tableau vide.
  > Non, un tableau vide est un tableau dont la taille vaut 0, parfois cela peut servir.
- [ ] La chaîne vide.
  > Cela aurait pu être un choix de conception du langage ... mais ce n'est pas le cas !
:::

::: question
**Comment récupérer la longueur d'un tableau de chaînes en Java ?**

- [ ] lenght(tableau);
  > Ah, elle est classique celle là ! Non c'est length() avec TH à la fin pas lenght() ...
- [ ] size(tableau);
  > Perdu : cela aurait pu, mais c'est length qui a été choisi.
- [x] length(tableau);
  > Correct : retourne la taille d'un tableau, c'est-à-dire son nombre de cases (et donc le nombre de valeurs qu'il peut contenir au maximum).
- [ ] longueur(tableau);
  > Ah, si des français·e·s avaient conçu Java peut-être, mais les anglo-saxons ont gagné de ce côté (quoique ADA, c'était nous ;o))
:::

::: question
**Quel est le résultat de tableau[0] si le tableau de chaînes à une taille de 0 ?**

- [ ] null
  > Perdu, un tableau vide par définition ne peut contenir quoique ce soit !
- [ ] ""
  > Perdu aussi, toujours car un tableau vide par définition ne peut contenir quoique ce soit !
- [x] Une exception ArrayIndexOutOfBoundsException.
  > Exactement : puisque le tableau ne contient aucune case, quelque soit l'indice cette exception sera levée.
- [ ] undefined
  > Ceci n'existe pas en Java.
:::

::: question
**Quel est le but de l'initialisation d'un tableau ?**

- [ ] D'assigner une valeur par défaut aux éléments du tableau.
  > Incorrect, on peut initialiser les différentes cases avec différentes valeurs.
- [x] De remplir le tableau avec des valeurs spécifiques.
  > Correct : l'initialisation permet d'assigner des valeurs aux différents éléments.
- [ ] D'allouer de la mémoire.
  > Faux, l'allocation (réservation de la mémoire) est distincte de l'initialisation (définition des valeurs de chacune des cases du tableau).
- [ ] De le définir.
  > Non, la déclaration est le moment où l'on indique le type et le nom de la variable ... il faut faire cela avant d'allouer le tableau, puis finalement de l'initialiser.
:::

::: question
**Quel type de boucle est la plus appropriée pour parcourir entièrement un tableau ?**

- [ ] La boucle while.
  > Non, même si il on peut utiliser cela, mais ce n'est pas le type de boucle le plus approprié.
- [ ] La boucle do while.
  > Non, même si il on peut utiliser cela, mais ce n'est pas le type de boucle le plus approprié.
- [x] La boucle for.
  > Exactement ! Parcours complet d'un ensemble (quel qu'il soit!) = boucle FOR.
- [ ] On n'a pas besoin de boucle.
  > Alors, voilà qui est surprenant ! Comment vous y prendriez-vous ? ...
:::

::: question
**Quel type de boucle est la plus appropriée pour parcourir partiellement un tableau ?**

- [x] while
  > Exactement, ainsi on s'arrête dès que l'on a trouvé ce que l'on cherchait. Parcours partiel d'un ensemble (quel qu'il soit!) = boucle WHILE.
- [ ] On n'a pas besoin de boucle
  > Étonnant et du coup comment feriez-vous sans boucle ? Bon courage !
- [ ] for
  > Aïe, aïe, aïe, c'est à cause de vous que la planète brûle :( Évidemment que non : un parcours partiel, veut dire que l'on s'arrête dès que l'on peut. Avec une boucle for, on parcourt la totalité du tableau et l'on effectue plein de calculs inutiles !
- [ ] foreach
  > Avez-vous entendu parler de cela en cours ? Non ? Donc pourquoi choisir cela ...
:::
