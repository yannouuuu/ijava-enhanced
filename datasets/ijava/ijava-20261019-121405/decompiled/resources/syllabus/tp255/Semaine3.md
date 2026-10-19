# Nouveau codage plus lisible pour un humain :)

Nous disposons maintenant de fonctions facilitant le traitement d'images (création et affichage), mais avec une représentation peu intuitive. Impossible d'imaginer l'image en lisant cette interminable suite de nombres ...

Cette semaine, on va introduire un nouveau codage qui s'approchera un peu du principe du Markown pour les textes structurés mais pour une image. Pour cela, nous allons avoir besoin de la notion de palette de couleurs qui associera un caractère à une couleur. Ainsi, il devient possible de décrire une image sous la forme d'une chaine de caractères.

Voici un exemple de palette de couleurs, réduite à 5 couleurs :

<table>
   <tr><td>Noir</td><td>Blanc</td><td>Rouge</td><td>Vert</td><td>Bleu</td>></tr>
   <tr><td>0,0,0</td><td>255,255,255</td><td>255,0,0</td><td>0,255,0</td><td>0,0,255</td>></tr>
   <tr><td>N</td><td>B</td><td>r</td><td>v</td><td>b</td>></tr>
</table>

Ne pouvant créer nos propres types, nous allons contourner la difficulté en travaillant avec plusieurs chaînes de caractères pour représenter une palette de couleurs. Une première, très courte, est constituée des lettres associées aux couleurs et une seconde plus longue contient nos couleurs représentées en RGB.

Ainsi, pour la palette donnée en exemple ci-dessus, nous aurons cette représentation :
```java
  final String CODES  = "NBrvb";
  final String COLORS = "000000000255255255255000000000255000000000255";
```

Une image peut maintenant se représenter un peu plus intuitivement par une chaîne de caractères utilisant les `CODES` des `COULEURS` de la palette et utilisant la concaténation pour mieux visualiser la grille de pixels:
```bash
"NNNNNNNN"+
"NbbbbbbN"+
"NbbbbbbN"+
"NbbrrbbN"+
"NbrrrrbN"+
"NvrrrrvN"+
"NvvvvvvN"+
"NNNNNNNN"
```

Nous allons maintenant créer les fonctions nous permettant de convertir une image représentée à l'aide d'une palette de couleurs, vers la représentation des semaines précédentes afin de bénéficier des outils de visualisation déjà développés.

### Définition de `int colorIndex(String codes, char letter)`.

<table>
<tr><th>Type de retour </th><th>Nom de la fonction</th><th>Paramètres</th></tr>
<tr><td><code>int</code></td>
    <td><code>colorIndex</code></td>
    <td><code>(String codes, char lettre)</code></td>
</tr>
<tr><td colspan="3">Retourne l'indice de la <code>lettre</code> dans <code>codes</code> si elle est présente dans la palette de couleurs et <code>-1</code> sinon. Exemples:
<ul>
<li><code>colorIndex(CODES, 'B')</code> retourne l'indice <code>1</code> car <code>B</code> est bien présent dans la constante <code>CODES</code> définie précédemment.</li>
<li><code>colorIndex(CODES, 'z')</code> retourne l'indice' <code>-1</code> car <code>z</code> n'est pas présent dans <code>CODES</code>.</li>
</ul>
</td></tr>
</table>

### Définition de `String getColorForCode(String codes, String colors, char letter)`.

<table>
<tr><th>Type de retour </th><th>Nom de la fonction</th><th>Paramètres</th></tr>
<tr><td><code>String</code></td>
    <td><code>getColorForCode</code></td>
    <td><code>(String codes, String colors, char letter)</code></td>
</tr>
<tr><td colspan="3">Retourne la couleur correspond à la <code>letter</code> donnée en la recherchant dans la palette (définie par <code>codes</code> et <code>colors</code>). Si la <code>letter</code> n'est pas présente dans les <code>codes</code> de la palette de couleurs alors la chaîne vide est retournée. Exemples:
<ul>
<li><code>getColorFor(CODES, COLORS, 'B')</code> retourne la chaîne <code>"255255255"</code> car le caractère <code>B</code> est bien présent dans la constante <code>CODES</code> et correspond à la couleurs blanches définie dans <code>COLORS</code> comme <code>255255255</code>.</li>
<li><code>getColorFor(CODES, COLORS, 'z')</code> retourne la chaîne vide <code>""</code> car le caractère <code>z</code> n'est pas présent dans <code>CODES</code>.</li>
</ul>
</td></tr>
</table>

### Définition de `String convert(String codes, String colors, String imageAsCharColors)`.

<table>
<tr><th>Type de retour </th><th>Nom de la fonction</th><th>Paramètres</th></tr>
<tr><td><code>String</code></td>
    <td><code>convert</code></td>
    <td><code>(String codes, String colors, String imageAsCharColors)</code></td>
</tr>
<tr><td colspan="3">Convertit l'image représentée sous la forme d'une chaîne de caractères contenant les lettres de la palette (représentée par <code>codes</code> et <code>colors</code>) en une chaîne contenant les composantes RGB de chacun des pixels de l'image. Si il y a une erreur dans <code>imageAsCharColors</code> la fonction retournera une chaîne contenant l'indice de la lettre fautive.
</td></tr>
</table>

### Un peu de créativité !

Maintenant que vous disposez d'une représentation un peu plus intuitive, à vous de créer votre propre image pour la battle de fin de projet : les plus belles seront imprimées et exposées quelque part à l'IUT ;)
