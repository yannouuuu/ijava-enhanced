# Semaine 3 : à la mémoire de Seymour !

**Cette semaine, petit cadeau, la solution des `Semaine1` et `Semaine2` est présente dans le squelette :) A la fin de l'énoncé, il vous faudra donc changer `void algorithm()` qui correspond actuellement à la `Semaine2` avec le dessin de la `Semaine3`**

Nous allons réimplementer le fameux mode tortue inventé par Seymour Papert il y a quelques décennies. Sans aller malheureusement jusqu'à implémenter l'interprète LOGO cependant. 

Notre objectif sera d'avoir cette métaphore de la tortue se déplaçant sur une image et tenant un crayon qu'elle peut baisser ou lever et qui laisse une trace le cas échéant.

Pour cela nous avons besoin de maintenir un certain nombre d'information concernant la tortue comme :
- sa position sur l'image avec un indice de ligne et de colonne,
- la position du crayon : est-il levé ou baissé (car si baissé, cela laisse une trace lors des déplacements). 

Il nous faudra aussi l'image sur laquelle elle se trouve !

Nous allons donc **exceptionnellement** mobiliser quelques variables globales ... afin de maintenir ces états pour les différentes fonctions permettant de gérer la tortue.

## Définition des variables globales 

La première étape consiste donc à définir ces variables globales : 
- deux variables entières pour `lineTurtle` et `columnTurtle`, représentant respectivement l'indice de ligne et de colonne de la tortue (initialement à 0 tous les deux),
- une variable booléenne `penDown`, qui comme son nom l'indique nous informe de la position du crayon (initialement baissé),
- une variable de type chaîne de caractère `colorPen` pour représenter la couleur du crayon (initalement à bleu),
- et finalement une chaîne de caractère `currentImage` pour représenter l'image sur laquelle se trouve la tortue.

Définissez l'ensemble des ces variables globales avec des initialisations à la déclaration.

## Fonctions *one liner*

Pour s'échauffer, voici quelques fonctions qui peuvent toutes s'écrire en une seule ligne, tout en restant parfaitement lisibles !

*Rappel : il n'est pas toujours nécessaire de dégaîner une alternative lorsqu'une condition suffit ...*

<table>
<tr><th>Type de retour </th><th>Nom de la fonction</th><th>Paramètres</th></tr>
<tr><td><code>void
</code></td><td><code>togglePen
</code></td><td><code>()
</code></td></tr>
<tr><td colspan="3">Bascule l'état du crayon : si il est actuellement baissé, cela le relève et réciproquement, si il est levé, cela l'abaisse.
</td></tr>
</table>

<table>
<tr><th>Type de retour </th><th>Nom de la fonction</th><th>Paramètres</th></tr>
<tr><td><code>boolean</code></td>
<td><code>within</code></td>
<td><code>(int min, int max, int value)</code></td></tr>
<tr><td colspan="3">Vérifie que la valeur <code>value</code> est bien comprise dans l'intervalle <code>[min, max]</code>.<br/>
Exemples: <ul>
<li><code>within(0, 10, 10)</code> retourne <code>true</code>,</li>
<li><code>within(0, 10, 11)</code> retourne <code>false</code>.</li></ul> 
</td></tr>
</table>

<table>
<tr><th>Type de retour </th><th>Nom de la fonction</th><th>Paramètres</th></tr>
<tr><td><code>boolean
</code></td><td><code>inside
</code></td><td><code>(int imageSize, int line, int column)
</code></td></tr>
<tr><td colspan="3">Vérifie si les coordonnées <code>(line, column)</code> sont valide pour une image de taille <code>imageSize x imageSize</code>.<br/>
Exemples: <ul>
<li>si <code>inside(5, 3, 3)</code> retourne <code>true</code>,</li>
<li>si <code>inside(5, 3, 5)</code> retourne <code>false</code>,</li>
<li>si <code>inside(5, -1, 3)</code> retourne <code>false</code>,</li>
</ul> 
</td></tr>
</table>

<table>
<tr><th>Type de retour </th><th>Nom de la fonction</th><th>Paramètres</th></tr>
<tr><td><code>void
</code></td><td><code>setCurrentImage
</code></td><td><code>(String image)
</code></td></tr>
<tr><td colspan="3">Initialise l'image actuelle <code>currentImage</code> avec <code>image</code>.
</ul> 
</td></tr>
</table>

## Fonctions de gestion du crayon et de ses effets ...

La semaine dernière nous avons oublié d'écrire une fonction pratique pour modifier une image ... et nous allons en avoir besoin afin de pouvoir modifier le pixel sur lequel se trouve la tortue (si le crayon est baissé bien sûr). Votre boss étant sympa, il vous donne le pseudo-code de la fonction permettant de modifier le pixel d'une image donnée en fonction de coordonnées `(line, column)` d'une couleur `color`.

```java 
    String set(String image, int line, int column, String color) {
        String newImage = "";
        int imageSize = size(image);
        int idxStart  = line * imageSize * 9 + column * 9;
        // on copie ce qui est avant le pixel (éventuellement rien)
        // on ajoute la nouvelle couleur
        // on copie ce qui est après le pixel
        return newImage;
    }
```

Il nous reste maintenant à écrire la procédure `void trace()` qui en fonction de l'état du crayon modifie la couleur du pixel de l'image courante sur laquelle se trouve la tortue.

<table>
<tr><th>Type de retour </th><th>Nom de la fonction</th><th>Paramètres</th></tr>
<tr><td><code>void
</code></td><td><code>trace
</code></td><td><code>()
</code></td></tr>
<tr><td colspan="3">Si le crayon est baissé alors la couleur du pixel où se trouve actuellement la tortue est mis à jour avec la valeur de <code>colorPen</code>, sinon rien ne se passe. Cette procédure dépend uniquement des variables globales et ne modifie <code>currentImage</code> que si <code>penDown</code> est à vrai, c'est-à-dire si le crayon est baissé.
</ul> 
</td></tr>
</table>

## Fonctions de déplacement de la tortue

Nous avons maintenant les outils nécessaires pour définir nos deux fonctions de déplacement pour notre tortue :
- `boolean go(int line, int column)` : ceci correspond à un déplacement dans l'absolu, un peu comme si la tortue se téléportait directement à la position `(line, column)` dans `currentImage`.
- `boolean move(int[] directions)` :  ceci correspond à un déplacement relatif d'une case vers le `NORTH`, le `SOUTH`, l'`EAST` ou l'`WEST`.
Pour ces deux fonctions, si le crayon est baissé, une trace apparaitra sur l'image ...

<table>
<tr><th>Type de retour </th><th>Nom de la fonction</th><th>Paramètres</th></tr>
<tr><td><code>boolean
</code></td><td><code>go
</code></td><td><code>(int line, int column)
</code></td></tr>
<tr><td colspan="3">Téléporte la tortue à <code>(line, column)</code> ... si cela correspond à une position valide pour l'image courante (<code>currentImage</code>). Si le déplacement est possible, <code>true</code> est retourné et <code>false</code> sinon.<br/>
Exemples (en supposant une image de taille 5): 
<ul>
<li><code>go(3, 4)</code> retourne <code>true</code> car la tortue a pu se déplacer et <code>lineTurtle</code> vaudra <code>3</code> et <code>columnTurtle</code> vaudra <code>4</code>,</li>
<li><code>go(3, 7)</code> retourne <code>false</code> et <code>lineTurtle</code> et <code>columnTurtle</code> restent inchangés.</li></ul> 
</td></tr>
</table>

Définissez dans un premier temps des constantes globales permettant de représenter les 4 directions possibles (`NORTH`, `SOUTH`, `EAST` ou `WEST`), sachant que leur codage correspond à un tableau d'entiers de 2 cases dont la première correspond au décalage sur la ligne et le second au décalage sur la colonne. On ne se décale à chaque fois que d'une seule case et dans une seule direction (`NORTH_EAST` n'existe pas pour notre tortue !).

<table>
<tr><th>Type de retour </th><th>Nom de la fonction</th><th>Paramètres</th></tr>
<tr><td><code>boolean
</code></td><td><code>move
</code></td><td><code>(int[] direction)
</code></td></tr>
<tr><td colspan="3">La tortue effectue un déplacement relatif dans la direction indiquée, si c'est possible, sinon elle ne bouge pas. La <code>direction</code> correspond à l'une des constantes <code>NORTH</code>, <code>SOUTH</code>, <code>EAST</code>, <code>WEST</code>.<br/>
Exemples (en supposant la tortue en <code>(0,0)</code>): <ul>
<li><code>move(NORTH)</code> retourne <code>false</code> et la tortue reste en <code>(0,0)</code>,</li>
<li><code>move(EAST)</code> retourne <code>true</code> et la tortue se trouve en <code>(0,1)</code> et si le crayon est baissé la case d'arrivée est coloriée avec la couleur <code>colorPen</code>.</li>
</ul> 
</td></tr>
</table>

Maintenant, nous pouvons changer `void algorithm()` faire un petit dessin à l'aide de notre tortue :
```java
    void algorithm() {
        String image = generate(5, 128, 128, 128, 0, 0, 0);
        setCurrentImage(image);
        penDown = true;
        colorPen = color(0,0,0);
        go(3, 3);
        show(currentImage);
        move(NORD);
        show(currentImage);
        move(SUD);
        show(currentImage);
        move(EST);
        show(currentImage);
        move(OUEST);
        show(currentImage);
    }
````

A vous de jouer pour produire bien mieux que cet exemple minimaliste et un peu provocant pour ceux ayant fait un choix de système d'exploitation les forçant à changer de machine ...

<center>
<img src="turtle_ms.png" alt="Affichages du programme d'exemple" width="50" height="auto">
</center>