#  À la bonne heure (américaine) ! 

Alors qu’en France l’heure de la journée est généralement affichée sur 24h, aux États-Unis, elle l’est sur 12h.

En France, l’heure peut prendre une valeur entre 0 et 23.

Aux États-Unis (mais pas seulement), l’heure prend une valeur entre 1 et 12 compris. Les heures avant midi sont représentées par l’heure, suivie de "AM", sauf 0h qui s’écrait 12h00 AM. Les heures de l’après midi sont représentées par un nombre entre 1 et 12, suivi de "PM".

Écrivez le programme `HeuresUsVersEu` transformant une heure américaine (sur 12h) à la mode française (sur 24h). On supposera dans tout cet exercice que l’heure et les minutes saisies sont toujours correctes (i.e. pas de nombres négatifs, pas de minutes >= 60).

```bash
Saisir heures: 3
Saisir minutes: 25
Saisir AM/PM: AM
3:25
```

Voici une série de valeurs pour vérifier votre programme :
<table>
  <tr><td>Saisie</td><td>Affichage</td></tr>
  <tr><td>5 20 AM</td><td>5:20</td></tr>
  <tr><td>5 20 PM</td><td>17:20</td></tr>
  <tr><td>10 49 PM</td><td>22:49</td></tr>
  <tr><td>12 10 AM</td><td>0:10</td></tr>
  <tr><td>12 10 PM</td><td>12:10</td></tr>
  <tr><td>1 05 AM</td><td>1:05</td></tr>
</table>

Le dernier scénario est optionnel car un peu plus complexe : il faut prendre en compte l’éventuel 0 des dizaines sur les minutes dans le formatage.
