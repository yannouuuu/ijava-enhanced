# Tableau d'un tournoi

Vous êtes contacté par une association qui organise un tournoi de fléchettes et a besoin de gérer un tableau de scores pour l'ensemble des participants.

L'organisatrice t'explique qu'ils utilisent depuis toujours la bonne vieille méthode du papier et crayon ... en notant manuellement le résultat de chaque partie (avec un 1 quand une partie est gagnée et un 0 lorsque c'est perdu), et décompte ensuite le nombre total de victoires pour chaque joueurs. Elle te temps une feuille de l'an passé :

<table>
  <tr>
    <td>Nom</td>    <td>Parties</td>  <td>Total</td>
  </tr>
  <tr>
    <td>Jeanne</td> <td>O1O1OO1O</td> <td>3</td>
  </tr>
  <tr>
    <td>Pierre</td> <td>11OOO11O</td> <td>4</td>
  </tr>
  <tr>
    <td>Aksil</td>  <td>O1O1OO1O</td> <td>3</td>
  </tr>
  <tr>
    <td>Paloma</td> <td>11O1OO1O</td> <td>4</td>
  </tr>
  <tr>
    <td>Victor</td> <td>OOO1O111</td> <td>4</td>
  </tr>
  <tr>
    <td>Salwa</td>  <td>11O11O1O</td> <td>5</td>
  </tr>
  <tr>
    <td>...</td>
  </tr>
</table<>>

L'organisatrice : *"Alors qu'en penses-tu ? Tu peux me faire une app' pour gérer tout ça plus facilement ?".*

Dans ta tête, différentes pistes se bousculent et avant de lui répondre, tu réfléchis d'abord à la structure de données qui te semble la plus appropriée.

::: question
**Quel type permettrait de représenter un joueuse ou une joueur ?**

- [ ] Un type `Joueuse` avec deux champs : une chaîne pour son nom, une chaîne pour les parties et une chaîne pour le total
  > Hum, cela pourrait être une piste, mais une chaîne de caractères pour représenter le total qui est un nombre n'est pas très judicieux
- [x] Un type `Joueuse` avec deux champs : une chaîne pour son nom et un un tableau de booléens pour les parties
  > Intéressant ! Effectivement, ce choix permet de représenter de manière concise les informations concernant un joueur. Comme une partie est soit gagnée, soit perdue, le booléen est le plus approprié. Et puis, le total n'est pas vraiment nécessaire puisqu'avec les parties il est possible de le calculer !
- [ ] Un type `Joueuse` avec trois champs : une chaîne pour son nom, une autre pour les parties et un entier pour le total
  > Cela pourrait être un choix possible, il y a une redondance d'information entre les parties et le score et du coup, il faudrait être attentif à ce que le score soit toujours bien synchronisé avec les parties. Possible, mais proposition pouvant amener à des erreurs en cas de mauvaise mises à jour des informations.
- [ ] Pas besoin de type : une seule chaîne avec toutes les informations dedans, en les séparant juste par une virgule pour s'y retrouver !
  > Alors dans l'absolu, on pourrait toujours tout représenter avec des chaînes, mais êtes-vous si fan des parcours de chaînes et des conversions de type ?! ;)
:::

Vous : *"Après quelques réflexions, oui pas de soucis : je peux m'occuper de ça pour ton tournoi. Tu peux compter sur moi !"*.

Définissez maintenant le type `Joueuse` avec le type le plus approprié proposé en réponse aux questions ci-dessus. 
