# Actions de base de la commande ijava


::: question
**Quelle action permet d'initialiser un exercice dans ijava ?**

- [ ] ijava start
  > Incorrect : start doit juste être utilisée au début de chaque séance de TP !
- [x] ijava init
  > Oui : init permet d'initialiser un exercice préciser en paramètre le nom de l'exercice. Si c'est un QCM, il faut y répondre dans le navigateur, sinon un squelette de code source est généré automatiquement.
- [ ] ijava compile
  > Incorrect : cette commande lance la compilation d'un programme.
- [ ] ijava execute
  > Incorrect : cette commande lance les tests liés à un programme.
:::

::: question
**Comment voir l'état d'avancement des exercices dans une session ?**

- [ ] ijava progress
  > Cette action n'existe pas :)
- [x] ijava status
  > Tout à fait ! Si vous êtes dans ~/ijava cela vous affiche votre progression sur les différentes séances et si vous êtes dans un TP comme ~/ijava/tp1 cela votre progression sur les différents exercice de la séance en cours.
- [ ] ijava statut
  > Cette action n'existe plus ! Toutes les actions sont maintenant en anglais.
- [ ] ijava overview
  > Cette action n'existe pas, mais cela serait plutôt test pour vérifier un programme éventuellement.
:::

::: question
**Quelle action permet de compiler un fichier Java ?**

- [ ] ijava run
  > Non, cette action n'existe pas ... on utilise execute pour lancer un programme
- [ ] ijava build
  > Non, cette action n'existe pas ...
- [ ] ijava exec
  > Non, cette action n'existe pas ... même si elle ressemble à execute pour exécuter un programme, mais ici, on souhaite d'abord compiler !
- [x] ijava compile
  > Ah, action qui n'existe pas, mais effectivement l'action execute utilise cette commande ijavac qui est fourni dans le Java Development Kit ...
:::

::: question
**Comment exécuter un programme avec ijava ?**

- [ ] ijava start
  > Ah non, ça c'est la commande à utiliser en début de TP ...
- [ ] ijava run
  > On aurait pu choisir cela, mais c'est un autre verbe qui a été préféré. Après tout, un programme ne court pas :o)
- [ ] ijava launch
  > On aurait pu choisir cela, mais c'est un autre verbe qui a été préféré. Après tout, on ne lance pas un programme :o)
- [ ] ijava go
  > CORRECT : cette action exécute le programme précisé en paramètre
:::

::: question
**Quelle commande exécute les tests d'un exercice ?**

- [ ] ijava validate
  > On aurait pu choisir cela, mais c'est un autre verbe qui a été préféré.
- [ ] ijava check
  > On aurait pu choisir cela, mais c'est un autre verbe qui a été préféré.
- [ ] ijava verify
  > On aurait pu choisir cela, mais c'est un autre verbe qui a été préféré.
- [x] ijava test
  > On aurait pu choisir cela, mais c'est un autre verbe qui a été préféré.
:::

::: question
**Quelle action faut-il exécuter en début de TP ?**

- [ ] ijava workspace
  > Cette action n'existe pas ...
- [x] ijava start
  > Oui : cette action marque le commencement d'une nouvelle séance de TP !
- [ ] ijava init
  > Ah non, cette commande est utilisé pour lancer les QCM ou générer les squelettes de code source des exercices.
- [ ] ijava new
  > Cette action n'existe pas non plus ...
:::
