# Opérations de base sur les fichiers textes

Nous allons dans cet exercice réaliser quelques opérations de base sur des fichiers textes : 
* lister les fichiers présents dans un répertoire donné, 
* calculer le nombre de lignes que contient un fichier,
* afficher l'ensemble des lignes d'un fichier texte.

::: question
**Quel type allons-nous utiliser dans cet exercice ?**
- [ ] extensions.CSVFile
  > Le type CSV est effectivement un fichier textuel, mais avec une structure bien particulière. Or ici, on souhaite travailler avec n'importe quel fichier texte.
- [x] extensions.File
  >  Exactement ! C'est le type dédié à la manipulation de fichiers textuels
- [ ] extension.Fichier
  > A part les types que nous créons parfois, tous les types prédéfinis en (i)java sont en anglais ...
:::

Afin de pouvoir réaliser ces opérations de base sur les fichiers, commencez par créer un répertoire `categories` dans le répertoire actuel. Faîtes un simple copier coller des données et après avoir validé la commande `cat`, faîtes un `CONTROL-C` pour copier le contenu et terminez par un `CONTROL-D` pour finir la saisie ce qui créera le fichier.

Puis créez dans ce répertoire les fichiers suivants : 
```bash 
> mkdir categories
> cat > categories/animaux.txt
BIOLOGIE
Noms d'animaux
brebis
aigle
perroquet
lion
mouche
souris
tigre
chat
loup
koala
kiwi
pie
(appuyez sur les touches Control-D pour terminer la saisie)
> cat > categories/capitales.txt
GEOGRAPHIE
Capitales du monde
paris
londres
rome
amsterdam
tokyo
alger
athenes
berlin
brasilia
bruxelles
(appuyez sur les touches Control-D pour terminer la saisie)
> cat categories/pays.txt
GEOGRAPHIE
Pays européens
suisse
france
italie
belgique
espagne
portugal
allemagne
autriche
serbie
croatie
(appuyez sur les touches Control-D pour terminer la saisie)
```

Normalement, vous devriez avoir ceci :
```bash
> tree
.
├── Fichiers.java
├── Fichiers.md
├── Files-cheat-sheet.md
└── categories
    ├── animaux.txt
    ├── capitales.txt
    └── pays.txt

2 directories, 6 files>
```

## Lister les fichiers présents dans un répertoire donné

A l'aide de la fonction `String[] getAllFilesFromDirectory(String repertoire)`, créez la fonction `String choixFichier(String repertoire)` qui affiche la liste des fichiers du `repertoire` donné en les préfixant de leur indice et effectue une saisie **sans contrôle** au niveau de l'usager puis retourne le nom du fichier correspondant à l'indice.

Par exemple, en supposant que la fonction `choixFichier` est appellée avec le répertoire `categories`, on obtiendrait un affichage similaire à celui-ci:
```bash
0 : animaux.txt
1 : capitales.txt
2 : pays.txt
Choisir un fichier en indiquant son numéro : 0
```
Et dans ce cas, la valeur de retour de la fonction aurait été `animaux.txt`.

## Calculer le nombre de lignes que contient un fichier

On souhaite maintenant déterminer le nombre de lignes contenues dans un fichier quelconque. A l'aide des fonctions :
* `File newFile(String filename)`
* `boolean ready(File file)`
* `String readLine(File file)`
créez la fonction `int nbLignes(String nomFichier)` qui décompte le nombre de lignes de texte présentes dans le fichier nommé `nomFichier`.

Définissez ensuite la procédure `test_nbLignes_animaux_txt` qui s'assure que le fichier `categories/animaux.txt` contient bien 14 lignes.

## Afficher l'ensemble des lignes d'un fichier texte

Finalement, on souhaite pouvoir afficher le contenu d'un fichier donné. A l'aide des mêmes fonctions proposées pour `nbLignes`, définissez la procédure `void dump(File file)` qui affiche le contenu du fichier donné en paramètre.

```bash
ijava execute Fichiers
0 : capitales.txt
1 : animaux.txt
2 : pays.txt
Choisir un fichier en indiquant son numéro : 0
Vous avez choisi le fichier : capitales.txt
Ce fichier contient 12 lignes.
GEOGRAPHIE
Capitales du monde
paris
londres
rome
amsterdam
tokyo
alger
athenes
berlin
brasilia
bruxelles
```
