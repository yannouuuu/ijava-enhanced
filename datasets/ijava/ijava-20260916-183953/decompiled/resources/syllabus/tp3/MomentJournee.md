# Vous avez un moment ?

Le programme `MomentJournee` indique si c’est la nuit, le matin, l’après midi ou la soirée, en fonction d’une heure reçue. On considère que le matin commence à 6h, l’après-midi commence à 12h, la soirée commence à 18h, et la nuit commence à 22h. Si l’heure donnée en paramètre n’est pas comprise dans l’intervalle [0, 23], la fonction affichera erreur.

```bash
~/ijava2/tp2> ijava execute MomentJournee
Saisir une heure: 3
nuit
~/ijava2/tp2> ijava execute MomentJournee
Saisir une heure: 12
après-midi
```

Voici une série de valeurs avec lesquelles mettre à l’épreuve votre programme :
<table>
  <tr><td>Saisie</td><td>Affichage</td></tr>
  <tr><td>0</td><td>nuit</td></tr>
  <tr><td>4</td><td>nuit</td></tr>
  <tr><td>6</td><td>matinée</td></tr>
  <tr><td>11</td><td>matinée</td></tr>
  <tr><td>14</td><td>après-midi</td></tr>
  <tr><td>19</td><td>soirée</td></tr>
  <tr><td>23</td><td>nuit</td></tr>
  <tr><td>24</td><td>erreur</td></tr>
  <tr><td></td><td>erreur</td></tr>
</table>



