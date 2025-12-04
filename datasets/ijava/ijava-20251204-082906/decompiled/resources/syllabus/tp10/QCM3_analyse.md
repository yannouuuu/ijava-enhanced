# Jeu du Saute-Mouton : analyse de l'algorithme principal

Nous devons maintenant identifier quels sont les problèmes simples qu’il faut résoudre et pour construire une solution complète au Saute-Mouton. 

Commençons par faire une liste des sous-tâches que le programme devra pouvoir effectuer:
* initialiser la prairie (**déjà fait !**)
* afficher la prairie (**déjà fait !**)
* saisir le coup (valide!) du joueur, càd le déplacement que le joueur veut faire
* faire le déplacement qui correspond au coup saisi
* détecter la fin de la partie, soit parce que la configuration finale est atteinte, soit parce qu’il y a blocage
* afficher le résultat du jeu: réussite ou non

Cette réflexion nous permet déjà d'ébaucher la forme générale de l'algorithme principal. Écrivons le en langage naturel:
```bash
initialiser la prairie
tant que le jeu n’est pas fini
   afficher l’état actuel de la prairie
   saisir le coup du joueur
   effectuer le déplacement
```

Nous allons raffiner ce début de solution en déterminant comment chaque sous-tâche sera implémentée: par une fonction, ou par du code à écrire directement dans l'algorithme principal. C’est la partie difficile, qu’on arrive à faire en accumulant de l’expérience ...

En règle générale, on essaye de s'attacher à ceci :
* chaque tâche a sa fonction dédiée
* les noms des fonctions sont choisies de telle manière qu’en lisant le programme on comprend ce qu’il fait
* le code à écrire directement dans le programme se limite à des affectations de variables qui servent à transmettre de l’information entre les différentes fonctions, ou à des calculs très simples

Remplaçons chaque sous-tâche identifiée par une fonction qui lui sera dédiée. Pour l’instant nous donnons uniquement les noms des fonctions. Les signatures des fonctions (càd paramètres et valeurs retournées) seront précisées plus tard, quand on se posera la question des informations qui doivent circuler entre les différentes sous-tâches.

```java
void algorithm() {
    // initialiser la prairie (FAIT !)
    // tant que le jeu n’est pas fini
    //    afficher la prairie (FAIT !)
    //    saisir le coup (valide) du joueur
    //    appliquer le déplacement
}
```

::: question
**Quelle signature vous semble pertinente pour `fini` ?**

- [ ] `boolean fini()`
  > Sans accès à la prairie, comment vérifier si la partie est finie ?!
- [x] `boolean fini(char[] prairie)`
  > Oui bien vu : en ayant la prairie, il est possible de s'assurer que la situation finale est atteinte ... ou que l'on est dans une situation de blocage.
- [ ] `void fini(char[] prairie, boolean oui)`
  > Aïe, aïe, aïe : il faut réviser les modes de passage des paramètres ! Le booléen transmis peut être lu mais si on le modifie dans le corps de la fonction, cela n'affecte que le calcul dans la fonction et pas le code ayant appelé cette fonction !
- [ ] `boolean fini(char[] prairie, int[] coup)`
  > Pourquoi aurait-on besoin de connaître le coup pour déterminer si la partie est finie ? Toujours transmettre uniquement l'information nécessaire pour réaliser le traitement et rien de plus.
:::

::: question
**En supposant qu'un coup est représenté par un indice de départ et un indice d'arrivée, quelle signature vous semble pertinente pour `saisir` ?**

- [ ] `int[] saisir()`
  > Comment vérifier la validité du coup saisit si l'on n'a pas accès à la prairie ?!
- [ ] `int[] saisir(String message)`
  > Certes on peut afficher un gentil message au joueur ou la joueuse, mais comment vérifier que le coup est valide sans la prairie en paramètre ?
- [x] `int[] saisir(char[] prairie)`
  > Bien vu ! Il est crucial d'avoir accès à la prairie pour vérifier la validité du coup saisit. 
- [ ] `char[] saisir(char[] prairie)`
  > Le type du retour de la fonction est surprenant ! La fonction saisir a juste pour responsabilité de saisir un coup, pas de l'appliquer. Et quand bien même, il n'y aurait aucune nécessité de retourner le tableau qui nous a été transmis, celui nous l'ayant transmis l'ayant déjà !! 
:::

::: question
**Quelle signature vous semble pertinente pour `appliquer` ?**

- [ ] `char[] appliquer(int[] coup)`
  > Hum, et où ce coup va-t-t-il s'appliquer si l'on n'a pas accès à la prairie ?
- [ ] `char[] appliquer(char[] prairie, int[] coup)`
  > Il est incohérent d'avoir en paramètre la prairie et de la retourner. Quel sens y aurait-il à retourner une copie ? Lorsqu'il y a un tableau en paramètre, la fonction peut modifier son contenu directement ...
- [ ] `boolean appliquer(char[] prairie, int[] coup)`
  > C'est la fonction saisir qui a la responsabilité de vérifier la validité d'un coup. A quoi pourrait correspondre ce booléen comme valeur de retour ?
- [x] `void appliquer(char[] prairie, int[] coup)`
  > Exactement : on a besoin du coup et de la prairie sur laquelle l'appliquer. Et comme l'on peut modifier directement le contenu du tableau passé en paramètre, pas besoin de retourner un quelconque résultat dans ce cas.
:::
