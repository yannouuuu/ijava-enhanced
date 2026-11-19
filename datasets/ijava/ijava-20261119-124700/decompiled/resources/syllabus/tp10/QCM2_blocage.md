# Jeu du Saute-Mouton : situations de blocage


Rappelons d'abord la situation initiale du casse-tête avec les deux troupeaux de faisant face :
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

Pour réussir le casse-tête, il faut parvenir à cette une situation finale où les troupeaux se tournent le dos :
<table>
  <tr style=>
    <td style="text-align: center">&lt;</td>
    <td style="text-align: center">&lt;</td>
    <td style="text-align: center">&lt;</td>
    <td style="text-align: center">_</td>
    <td style="text-align: center">&gt;</td>
    <td style="text-align: center">&gt;</td>
    <td style="text-align: center">&gt;</td>
  </tr>
</table>

::: question
**Quels sont les déplacements possibles dans cette situation finale ?**

- [ ] 3 et 5
  > les moutons ne maîtrisent pas la marche arrière malheureusement :p
- [x] aucun !
  > Tout a fait ! La situation finale est une situation de blocage, c'est-à-dire sans plus aucun déplacement possible.
- [ ] 2 et 6
  > Ah non, les moutons ne savent sauter que devant eux, trop périlleux le saut arrière !
- [ ] je ne sais pas trop
  > relisez attentivement les règles de déplacements ...
:::

Observez attentivement la situation suivante :
<table>
  <tr style="background-color: lightblue">
    <td style="text-align: center">&gt;</td>
    <td style="text-align: center">&gt;</td>
    <td style="text-align: center">&lt;</td>
    <td style="text-align: center">&gt;</td>
    <td style="text-align: center">&lt;</td>
    <td style="text-align: center">&lt;</td>
    <td style="text-align: center">_</td>
  </tr>
  <tr style="border: none">
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
**Quels sont les déplacements possibles dans cette situation ?**

- [ ] 4
  > Nop, un mouton ne peut sauter qu'un mouton à la fois, pas deux !
- [ ] 6
  > Hum, les moutons ne peuvent se déplacer que dans la direction dans laquelle ils regardent, pas de marche arrière pour les moutons !
- [ ] 7
  > Il n'y a pas de mouton à cet endroit ...
- [x] aucun
  > Effectivement, c'est une situation de blocage aussi !
:::
