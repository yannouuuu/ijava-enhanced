# Pendu : reconception avec les types

Vous allez refaire un programme qui permet de jouer au jeu du pendu, mais cette fois vous allez décomposer le problème en sous-fonctions, et définir deux nouveaux types pour représenter les données manipulées.

Le joueur doit découvrir un mot caché. Au début, toutes les lettres du mot sont cachées et représentée à l’écran par des étoiles, indiquant la longueur du mot à découvrir. À chaque tour du jeu le joueur propose une lettre de l’alphabet. Si la lettre est présente dans le mot, elle est découverte. Sinon, le joueur perd un essai. Généralement le joueur a droit à 5 essais pour découvrir la totalité du mot.

## Choix de la structure de données et définition des nouveaux types

Dans le jeu du pendu, nous devons manipuler des lettres et des mots. Ce sont donc deux types assez naturels à définir. Le type `Lettre` est défini par un caractère `caractere`, et un booléen `decouverte` qui indique si la lettre est découverte ou non.

**Rappelons que chaque nouveau type doit être défini dans un fichier séparé.**

* Définir le type `Lettre` contenant les champs décrits ci-dessus.

::: question
**Dans quel fichier doit être créé le type `Lettre`?**
- [ ] Pendu.java
  > Ah non, ce fichier sera pour le programme principal, pas pour le nouveau type.
- [x] Lettre.java
  > Exactement : il faut créer un fichier nommé avec le nom du type que l'on souhaite définir.
- [ ] Il n'est pas nécessaire de créer un fichier spécifique.
  > Hé bien si, un type = un fichier.
- [ ] Je ne sais pas
  > Hum, il est tant de relire les supports de cours ...
:::

### Création de la fonction `Lettre newLettre(char car)`

Cette fonction sera notre constructeur permettant de créer de nouvelles valeurs de type `Lettre` en fonction d'un caractère donné.

* Dans le fichier `Pendu.java`, définissez la fonction `void test_newLettre()` qui contiendra 2 assertions. Chaque assertion va créer une lettre, la stocker dans une variable, puis tester que le champ `caractere` de cette lettre est bien celui donné à la création, et que le champs `decouvert` est faux. Ajoutez la signature de la fonction testée avec une valeur de retour par défaut, par exemple `null`, puis exécutez le test : il est censé être au rouge !
* Définissez maintenant la fonction `Lettre newLettre(char car)` qui crée une **lettre non découverte**. Le test devrait être maintenant au vert.

### Est-il intéressant ou pas de créer un type `Mot` ?

Nous allons maintenant nous intéresser à la représentation d'un mot à l'aide du type `Lettre`. Le mot à deviner est constitué d'un ensemble de `Lettre`. La structure de donnée dont on dispose étant le tableau, un mot pourrait être représenté par un tableau de `Lettre`: `Lettre[] mot`. Du coup, est-ce qu'il serait judicieux d'un nouveau type `Mot` qui aurait pour champs ce tableau de lettres, ou est-ce préférable de travailler directement avec `Lettre[]` ?

::: question
**La création d'un type `Mot` est-il pertinent ?**
- [ ] Oui, tout à fait.
  > Que pourrait contenir ce type, à part un tableau de `Lettre` ? Il serait possible de créer ce type, mais à part pour la lisibilité, il apporterait assez peu car il ne contiendrait que le champs `lettres`.
- [x] Non, pas du tout.
  > Effectivement, lorsqu'un type ne contient qu'un seul champs, on peut s'interroger sur l'intérêt de l'introduire.
:::

### Création de la fonction `Lettre[] creerMot(String mot)`

Puisque que l'on ne crée pas un type spécifique pour le mot, on propose l'usage d'un autre nom de fonction n'utilisant pas la convention de nommage (ie. `creerMot` plutôt que `newMot` qui induirait qu'un type `Mot` existe).

* Définissez d'abord la fonction `void test_creerMot()` qui teste la validité de la fonction `Lettre[] creerMot(String mot)` qui retourne le tableau de lettres correspondant au `mot`. Pour cela, vous allez déclarer une variable `mot` de type `Lettre[]` initialisée avec `"unix"`, puis tester que `mot` a bien une longueur de 4, que `mot[0].caractere` est bien le caractère `’u’`, et ainsi de suite pour tous les caractères et que l'ensemble des champs `decouvert` des différentes `Lettre` est bien à `false`.
* Définissez maintenant la fonction `Lettre[] creerMot(String mot)` qui crée un tableau qui contient les lettres de la chaine `mot`. Pensez bien à utiliser `newLettre`.
