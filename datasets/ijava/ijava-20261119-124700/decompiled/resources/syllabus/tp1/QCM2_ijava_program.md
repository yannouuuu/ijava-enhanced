# Principales composantes d'un programme ijava


::: question
**Quel est le but de algorithm() ?**

- [ ] C'est ce qui permet de réaliser des sorties.  
  > Non, ce sont print(...), println(...)etc., qui affichent des données à l'écran.
- [x] C'est là que l'on définit l'algorithme principal du programme. 
  > Effectivement, algorithm() est le point d'entrée de l'exécution.
- [ ] C'est ce qui permet de réaliser des entrées utilisateur. 
  > Non, ce sont readInt(), readString()etc., qui saisissent des données au clavier.
- [ ] Aucun, il n'est pas nécessaire d'écrire cela. 
  > Argl ! Au contraire, sans cela pas de programme !
:::

::: question
**print("Hello") sert à ...**

- [ ] saisir Hello depuis le clavier. 
  > Non pas du tout print est lié à l'affichage. 
- [ ] afficher bonjour à l'écran. 
  > Pas vraiment, la machine ne fait pas de traduction automatique lorsque l'on lui demande d'afficher Hello.
- [x] afficher Hello à l'écran. 
  > Tout à fait, println() affiche la donnée entre les parenthèse à l'écran et ajoute un retour à la ligne (contrairement à print).
- [ ] à rien du tout car cela produit une erreur. 
  > Ah non, c'est une expression tout à fait valide.
:::

::: question
**readInt() sert à ...**

- [ ] saisir un caractère depuis le clavier. 
  > Non pas du tout car Int = entier et pas caractère ! C'est readChar() qui saisit un caractère.
- [ ] saisir une ligne de texte depuis le clavier. 
  > Non pas du tout car String = chaîne et pas caractère ! C'est readString() qui saisit une ligne.
- [ ] afficher un entier sur la console. 
  > Pas du tout, c'est println() qui réalise un affiche sur la sortie standard.
- [x] saisir un entier depuis le clavier. 
  > Oui tout à fait, d'ailleurs cela se voit dans sa définition : int readInt().
:::

::: question
**Quel sera le résultat de l'exécution de println(15 / 4 + 2) ?**

- [x] 5
  > Oui car 15 / 4 donne 3 et on ajoute ensuite 2 (division prioritaire).
- [ ] 5.75
  > Non car c'sest une division entière et une addition donc le résultat est entier.
- [ ] 6
  > Non, pour cela il aurait fallu 16 / 4 + 2.
- [ ] L'expression n'est pas valide et produit une erreur.
  > Pas du tout, l'expression est valide.
:::

::: question
**Juste après avoir edité le code source d'un programme Hello.java, quelle action faut-il avant de l'exécuter ?**

- [ ] ijava execute Hello
  > Avant de pouvoir exécuter un programme (Java), il faut le compiler !
- [ ] ijava test Hello
  > Avant de pouvoir tester un programme (Java), il faut le compiler !
- [ ] ijava init Hello
  > init sert à initialiser un exercice, pas à compiler un programme ...
- [x] ijava compile Hello.java
  > Tout à fait, on demande au compilateur de faire son travail de vérification :)
:::

::: question
**Quelle est la différence entre print(String s) et println(String s) ?**

- [ ] print() ajoute un retour à la ligne, println() non. 
  > Dommage, c'est l'inverse :)
- [x] println() ajoute un retour à la ligne, print() non. 
  > Oui tout à fait, c'est la différence clé.
- [ ] Elles sont fonctionnellement identiques. 
  > Ah non, si non cela serait embêtant d'avoir deux fonctions avec des noms différents ...
- [ ] println() est utilisée pour les nombres, print() pour les chaînes. 
  > Argl, pas du tout, ces deux fonctionnent sur tous les types de base.
:::
