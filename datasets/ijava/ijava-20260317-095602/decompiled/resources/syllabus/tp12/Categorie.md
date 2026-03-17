# Création du type `Categorie`

Une catégorie est caractérisée par **un thème, le sujet dans ce thème et un ensemble de mots** correspondant à ce sujet. Par exemple, dans le thème `BIOLOGIE`, on pourrait avoir `"Noms d'animaux"` et dans le thème `GEOGRAPHIE`, `"Capitales du monde"`.

::: question
**Comment proposez-vous de modéliser cette notion de catégorie ?**
- [ ] Un champs de type `String` pour le thème, un autre champs de même type pour le sujet et une chaîne de caractères contenant les mots séparés par des virgules
  > Pourquoi s'être embêté à créer le type `Theme` dans ce cas ?!
- [ ] Un champs de type `Theme`, un autre champs de même type pour le sujet et une chaîne de caractères contenant les mots séparés par des virgules
  >  Il ne va pas être évident de parcourir les différents mots avec cette représentation ...
- [x] Un champs de type `Theme`, un champs de type `String` pour le sujet et un tableau à une dimension de chaînes de caractères
  > Exactement ! Choix très judicieux :)
:::

Créez maintenant le nouveau type `Categorie`.
