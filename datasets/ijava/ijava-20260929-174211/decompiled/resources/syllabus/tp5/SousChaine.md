# Détecter la présence d’une sous-chaine

Créz le programme `SousChaine` qui définit la fonction `boolean contient(String chaine, String souschaine)` qui prend en paramètre deux chaines de caractères et détermine si la seconde est sous-chaîne de la première.

Vérifiez d'abord manuellement votre fonction avec ces exemples avant de lancer les tests automatisés.

<table>
  <tr><th>Chaîne</th><th>Sous-chaîne</th><th>Affichage</th></tr>
  <tr><td>"blablabla"</td><td>"lab"</td><td>trouvé</td></tr>
  <tr><td>"blablabla"</td><td>"bal"</td><td>pas trouvé</td></tr>
  <tr><td>"abc"</td><td>""</td><td>trouvé</td></tr>
  <tr><td>""</td><td>""</td><td>trouvé</td></tr>
  <tr><td>"abcdef"</td><td>"defg"</td><td>pas trouvé</td></tr>
  <tr><td>"abc"</td><td>"abcdef"</td><td>pas trouvé</td></tr>
</table>
