#  À la bonne heure (française) ! 

Alors qu’en France l’heure de la journée est généralement affichée sur 24h, aux États-Unis, elle l’est sur 12h.

En France, l’heure peut prendre une valeur entre 0 et 23.

Aux États-Unis (mais pas seulement), l’heure prend une valeur entre 1 et 12 compris. Les heures avant midi sont représentées par l’heure, suivie de "AM", sauf 0h qui s’écrait 12h00 AM. Les heures de l’après midi sont représentées par un nombre entre 1 et 12, suivi de "PM".

Écrivez le programme `HeuresEuVersUs` transformant une heure française (sur 24h) à la mode  américaine (sur 12h). On supposera dans tout cet exercice que l’heure et les minutes saisies sont toujours correctes (i.e. pas de nombres négatifs, pas de minutes >= 60).

```bash
Saisir heures: 3
Saisir minutes: 25
3:25AM
```

Voici une série de valeurs pour vérifier votre programme :
<table>
  <tr><td>Saisie</td><td>Affichage</td></tr>
  <tr><td>5 20</td><td>5:20AM</td></tr>
  <tr><td>17 22 </td><td>5:20PM</td></tr>
  <tr><td>0 12</td><td>10:49PM</td></tr>
  <tr><td>20 49</td><td>12:10AM</td></tr>
  <tr><td>10 10</td><td>12:10PM</td></tr>
</table>

