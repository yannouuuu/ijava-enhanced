# Initialisations à partir de données externalisées dans un fichier

Afin d'automatiser et faciliter la mise à jour des données de notre jeu de mots, nous allons externaliser les données dans des fichiers. 

On ne souhaite pas réaliser l'ensemble des fonctionnalités du jeu modélisé en TD, mais se concentrer uniquement sur la partie externalisation et le chargement des données à partir d'un fichier. L'objectif est donc que ce court programme soit fonctionnel :

```java
    void algorithm(){
        String nomFichier = choixFichier(NOM_REPERTOIRE);
	    Categorie categorie = chargerCategorie(NOM_REPERTOIRE+"/"+nomFichier);
	    affichageCategorie(categorie);
    }
```

La première fonction (`choixFichier`) demande à l'usager le fichier qu'il ou elle souhaite charger. Suite à cela la fonction `chargerCategorie` charge le fichier correspondant pour créer une nouvelle categorie. Finalement, la procédure `affichageCategorie` permet de visualiser que la variable `categorie` a été correctement initialisée à partir des données présentes dans le fichier choisit.

## `String choixFichier(String nomRepertoire)`

Vous avez déjà écrit cette fonction dans l'exercice `Fichiers`! 
Copiez tout simplement cette fonction dans `JeuDeMots` :)

## `Categorie newCategorie(Theme theme, String sujet, String[] mots)`

On souhaite maintenant disposer d'**un constructeur** de valeur de type `Categorie`.
Définissez le constructeur `newCategorie` qui suivra le schéma classique d'affectation des arguments aux champs de la variable de type `Categorie`.

## `Categorie chargerCategorie(String cheminFichier)`

Maintenant que nous disposons du constructeur pour des `Categorie`, nous allons créer la focntion `chargerCategorie` qui va charger les données d'une catégorie dans un fichier donné et utiliser `newCategorie` (ainsi que `nbLignes` à recopier depuis `Fichiers`) pour créer une nouvelle `Categorie` initialisé grâce aux données présentes dans le fichier.

## `void affichageCategorie(Categorie categorie)`

Finalement, pour vérifier que tout fonctionne correctement, la procédure `affichageCategorie` permet de visualiser le contenu d'une variable de type `Categorie`. Cette procédure doit produire un affichage similaire à celui ci-dessous :

```bash
> ijava execute JeuDeMots
0 : capitales.txt
1 : animaux.txt
2 : pays.txt
Choisir un fichier en indiquant son numéro : 1
[BIOLOGIE] Noms d'animaux : 
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
> ijava execute JeuDeMots
0 : capitales.txt
1 : animaux.txt
2 : pays.txt
Choisir un fichier en indiquant son numéro : 0
[GEOGRAPHIE] Capitales du monde : 
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