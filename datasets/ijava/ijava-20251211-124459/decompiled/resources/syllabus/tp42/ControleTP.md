# Contrôle TP SaÉ1

Lisez attentivement ces consignes avant de débuter :
- vous pouvez utiliser les fichiers présents sur votre compte pour vous aider,
- vous pouvez consulter le cours sur Moodle, 
- aucun accès extérieur n'est autorisé,
- aucun usage de votre portable qui doit être rangé dans votre sac.

Vous avez un ensemble de fonctions à créer durant le contrôle, selon les mêmes procédures que lorsque vous êtes en TP. L'énoncé est disponible à la fois dans le navigateur ou en prévisualisant le fichier Markdown. Des squelettes plus ou moins complets sont potentiellement généré et vous utilisez les commandes habituelles d'`ijava` pour développer
- `start` au début, 
- puis `status` pour savoir en vous en êtes et ce qu'il reste à faire,
- ensuite `init` et après avoir créé vos foontions `compile`,
- éventuellement `execute` si vous voulez tester à la main et, 
- finalement `test` pour vérifier que votre fonction est valide.

**ATTENTION : ce n'est pas parce que vous avez tous les tests au VERT que vous aurez 20/20 ... la lisibilité du code et le fait de ne pas faire de calculs inutiles sont aussi évalués**

## Révolution dans le langage `ijava` : naissance des tableaux !

Vous avez la chance d'être la première génération a bénéficié d'une nouvelle fonctionnalité du langage `ijava`: les tableaux ! 

Cet outil fantastique va permettre de simplifier le programme de dessin sur lequel vous travaillez depuis plusieurs semaines. Fini de s'embêter avec cet encodage de couleurs sous la forme d'entiers, avec les soucis d'avoir toujours exactement 9 caractères pour retrouver les différentes composantes primaires de couleurs ...

Afin d'évaluer la pertinence de ce nouvel outil, votre boss*e* vous demande de refaire votre projet en utilisant ce concept révolutionnaire appelé "structure de donnée de type tableau" !

### Encodage d'un pixel de couleur et d'une image

Un pixel est toujours constitué de ses 3 composantes `RED`, `GREEN` et `BLUE` et chacune d'elle est toujours représentée par un entier dans l'intervalle `[0, 255]`. Une image est un ensemble de pixels, que l'on préférerait représenter sous la forme d'une grille à deux dimensions pour faciliter la gestion des indices de lignes et de colonnes.

Votre boss*e* vous demnde donc de représeter un pixel par trois entiers et une image par un tableau à 2 dimensions avec pour chaque ligne 3 colonnes correspondant à chaque composante primaire pour un pixel, et une ligne représentant une ligne de l'image.

Voici un exemple, **attention** les `|` sont là uniquement pour simplifier la lecture !
```
RVBR   -> 255 0 0 | 0 255 0 | 0 0 255 | 255 0 0     
BBBR   -> 0 0 255 | 0 0 255 | 0 0 255 | 255 0 0
RRRR   -> 255 0 0 | 255 0 0 | 255 0 0 | 255 0 0
```
On voit ainsi qu'une image de `3 lignes x 4 colonnes` sera représentée par un tableau d'entiers de `3 lignes x 12 colonnes (4 pixels x 3 composantes)`.

#### Définition de `generate` pour créer une nouvelle image

On souhaite définir la fonction `int[][] generate(int nbLines, int nbColumns, int r, int g, int b)` pour créer facilement une image rectangulaire dont chaque pixel est initialisée avec une couleur par défaut.

<table>
<tr><th>Type de retour </th><th>Nom de la fonction</th><th>Paramètres</th></tr>
<tr><td><code>int[][]
</code></td><td><code>generate
</code></td><td><code>(int nbLines, int nbColumns, int r, int g, int b)
</code></td></tr>
<tr><td colspan="3">Crée une nouvelle image de <code>nbLines x nbColonnes</code> pixels avec la couleur précisée par les paramètres <code>(r,g,b)</code>.
</td></tr>
</table>

#### Définition de `get` pour obtenir les 3 composantes d'un pixel

On souhaite maintenant disposer de la fonction `int[] get(int[][] image, int line, int column)` facilitant l'accès à un pixel dans une image. Cette fonction retourne un tableau de 3 entiers correspondant respectivement à la valeur de `RED`, `GREEN` et `BLUE` du pixel aux coordonnées `(line, colum)` dans l'`image`.

<table>
<tr><th>Type de retour </th><th>Nom de la fonction</th><th>Paramètres</th></tr>
<tr><td><code>int[]
</code></td><td><code>get
</code></td><td><code>(int[][] image, int line, int column)
</code></td></tr>
<tr><td colspan="3">Retourne les composantes de couleur du pixel de coordonnées <code>(line, column)</code> dans l'<code>image</code> et un tableau vide si les coordonnées sont invalides.
</td></tr>
</table>

#### Définition de `set` pour modifier l'une des 3 composantes d'un pixel

Réciproquement à la fonction précédente, on souhaite pouvoir facilement modifier une composante de couleur d'un pixel dans une image donnée, on souhaite donc disposer de la fonction `boolean set(int[][] image, int line, int column, int component, int value)` pour réaliser cela.

<table>
<tr><th>Type de retour </th><th>Nom de la fonction</th><th>Paramètres</th></tr>
<tr><td><code>boolean
</code></td><td><code>set
</code></td><td><code>(int[][] image, int line, int column, int component, int value)
</code></td></tr>
<tr><td colspan="3">Modifie la composante de couleur <code>component</code> du pixel situé en <code>(line, column)</code> dans l'<code>image</code> et indique si l'opération a réussi. Cette fonction ne retourne faux que si la valeur n'est pas dans le bon intervalle, ou si la composante ou la position du pixel sont invalides.
</td></tr>
</table>

#### Définition de `show` pour afficher une image dans le terminal

Maintenant que nous disposons des outils permettant de créer et manipuler une image, bien plus simplement qu'avec notre implémentation précédente avec les chaînes (!), on souhaiterait pouvoir visualier une image. 

Pour cela, on vous demande de créer la fonction `void show(int[][] image)`, qui nécessitera d'utiliser la fonction `rgb` que vous aviez mobilisé durant votre projet.

<table>
<tr><th>Type de retour </th><th>Nom de la fonction</th><th>Paramètres</th></tr>
<tr><td><code>void
</code></td><td><code>show
</code></td><td><code>(int[][] image)
</code></td></tr>
<tr><td colspan="3">Affiche l'<code>image</code> sur le terminal.
</td></tr>
</table>
