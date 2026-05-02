# Jeu du Saute-Mouton : Introduction

Nous allons nous intéresser aujourd’hui à un petit casse-tête, c’est-à-dire un jeu à un seul joueur. Celui-ci consiste à déplacer des moutons pour passer d’une situation initiale de troupeaux face à face (les **&gt;** représentent des moutons allant vers la droite et les **&lt;** des moutons allant vers la gauche):
<table>
  <tr>
    <td style="text-align: center">&gt;</td>
    <td style="text-align: center">&gt;</td>
    <td style="text-align: center">&gt;</td>
    <td style="text-align: center">_</td>
    <td style="text-align: center">&lt;</td>
    <td style="text-align: center">&lt;</td>
    <td style="text-align: center">&lt;</td>
  </tr>
</table>
à une situation finale où les troupeaux se tournent le dos :
<table>
  <tr>
    <td style="text-align: center">&lt;</td>
    <td style="text-align: center">&lt;</td>
    <td style="text-align: center">&lt;</td>
    <td style="text-align: center">_</td>
    <td style="text-align: center">&gt;</td>
    <td style="text-align: center">&gt;</td>
    <td style="text-align: center">&gt;</td>
  </tr>
</table>

Les règles du jeu sont très simples et ne reposent que sur les mouvements possibles des moutons:
* un mouton ne peut pas changer de direction,
* un mouton ne peut jamais reculer,
* un mouton peut avancer d’une case si il n’y a personne devant lui,
* un mouton peut sauter au dessus d’un autre mouton si la case suivant le mouton qu’il saute est libre.

Observez attentivement la situation suivante :
<table>
  <tr>
    <td style="text-align: center">&gt;</td>
    <td style="text-align: center">&gt;</td>
    <td style="text-align: center">_</td>
    <td style="text-align: center">&gt;</td>
    <td style="text-align: center">&lt;</td>
    <td style="text-align: center">&lt;</td>
    <td style="text-align: center">&lt;</td>
  </tr>
  <tr>
    <td style="text-align: center">1</td>
    <td style="text-align: center">2</td>
    <td style="text-align: center">3</td>
    <td style="text-align: center">4</td>
    <td style="text-align: center">5</td>
    <td style="text-align: center">6</td>
    <td style="text-align: center">7</td>
  </tr>
</table>

::: question
**Quels sont les déplacements possibles ?**

- [ ] 1, 2, 6
  > Le mouton 1 peut effectivement sauter, le 2 peut avancer, mais par contre le 6 ne peut pas sauter deux moutons !
- [ ] 2, 3, 5
  > Le mouton 2 peut avancer et le mouton 5 peut sauter, mais voyez-vous un mouton en 3 ?!
- [x] 1, 2, 5
  > Effectivement, bien vu ! Les moutons 1 et 5 peuvent sauter et le 3 peut avancer. Aucun autre déplacement n'est possible dans cette situation.
- [ ] 1, 2, 3, 5
  > Hum, presque, les moutons 1, 2 et 5 peuvent se déplacer. Par contre, le mouton 3 est bloqué : il ne peut ni avancer, ni sauter (et impossible de reculer dans les règles !)
:::
