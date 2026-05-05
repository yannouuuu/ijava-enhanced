# Fonctions associées à un nouveau type

Maintenant que vous avez créé votre nouveau type `Joueuse`, il est nécessaire de définir quelques fonctions permettant de le manipuler plus facilement.

::: question
**Quelles sont les fonctions indispensables à définir lors de la création d'un nouveau type ?**

- [ ] une fonction pour créer une nouvelle variable de ce type et une fonction représentant ses valeurs comme une chaîne
  > Une fonction qui créee une variable ?!! **NON !** une fonction ne peut créer de nouvelle variable mais peut créer une valeur comme résultat de son traitement !
- [ ] une fonction pour créer une valeur de ce type et une fonction d'affichage des valeurs sur la console
  > Hum, une fonction d'affichage des valeurs sur la console ne peut être testée facilement. On pourrait faire cela, mais une autre approche est préférable.
- [ ] une fonction pour créer une nouvelle variable de ce type et une fonction représentant ses valeurs comme une chaîne
  > Une fonction qui créee une variable ?!! **NON !** une fonction ne peut créer de nouvelle variable mais peut créer une valeur comme résultat de son traitement !
- [x] une fonction pour créer une valeur de ce type et une fonction représentant ses valeurs comme une chaîne
  > Bien joué :) Effectivement, lors de la création d'un type, il faut **toujours** définir une fonction **newXXX** et **toString(XXX)** afin de faciliter la création de nouvelles valeurs de ce type et de pouvoir déboguer plus facilement nos programmes.
:::

::: question
**Lorsque l'on crée un nouveau joueur, quelles informations sont nécessaires ?**

- [x] Juste son nom.
  > Bravo ! Effectivement au début du tournoi, les joueurs et joueuses ont un nom, mais n'ayant pas encore joué, n'ont pas de résultat pour leurs parties et leur score ...
- [ ] Son nom et ses parties.
  >  Alors le nom, oui, mais les parties ? Que pourrait-on bien stocker dans notre tableau de parties au début du tournoi ? Que des victoires ?! Que des défaites ?!
- [ ] Son nom, ses parties et son score.
  > Le nom, ok, les parties ... qui n'existent pas encore ?! Et que dire du score ? N'avait-on pas conclut précédemment que cette information peut être calculée à partir des parties et n'a donc pas besoin d'être mémorisée ?
- [ ] Son nom, ses victoires et ses défaites.
  > Oui pour le nom, par contre, quelles victoires et quelles défaites ? Au début du tournoi, aucune partie n'a encore débutée !
:::

::: question
**Concernant la gestion de l'affichage, la fonction a créer devrait avoir quelle signature ?**

- [ ] void afficher(Joueuse j)
  > Attention : il y a des conventions à respecter et avec cette signature, on ne crée pas une chaîne, mais on réalise directement un affichage ... non testable !
- [ ] void toString(Joueuse j)
  > Argl, on a donc une fonction dont le but est de créer une représentation sous forme de chaîne, mais qui ne retourne rien ... impossible !
- [x] String toString(Joueuse j)
  > Exactement ! On suit toujours la même convention : String toString(XXX) avec XXX le nom de notre nouveau type.
- [ ] String toStringJoueuse()
  > Hum, les informations de quelle joueuse est-on censé affiché si on ne nous transmet par la joueuse concernée en paramètre ?
:::

Passez maintenant à la création du `Tournoi` pour créer l'ensemble de ces fonctions dans cet autre fichier.
