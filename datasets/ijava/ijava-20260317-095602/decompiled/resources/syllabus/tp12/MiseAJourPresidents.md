# Programme principal pour l'analyse des présidences

Maintenant que nous disposons de nos types `Date` et `President`, nous allons pouvoir nous concentrer sur les traitements associés. A commencer par les fonctions classiques : 
* les constructeurs `newDate` et `newPresident`,
* les fonctions de représentation des valeurs sous forme de chaînes `toString`.

## Fonctions liées au type `Date`

Définissez les fonctions de création et représentation du type `Date`:
* `Date newDate(String date)` avec la chaîne `date` formatée ainsi `jj/mm/aaaa` (et on supposera cette donnée correctement formatée),
* `String toString(Date d)` qui affichera une date sous la forme `jj/mm/aaaa`. 

## Fonctions liées au type `President` 

Définissez les fonctions de création et représentation du type `President`:
* `President newPresident(String nom, String parti, Date debut, Date fin)`,
* `String toString(Presient p)` qui affichera un president sous la forme `jj/mm/aaaa`.

## Fonction d'initialisation de la structure de données des présidents

Afin de réaliser des traitements plus facilement sur les données, il est intéressant de créer une structure de données dédiées. De plus, le fichier est un peu daté et il serait souhaitable de pouvoir le mettre à jour. 

Nous allons donc ajouter deux fonctions : la première pour charger les données du CSV dans un tableau de présidents et à l'inverse et la seconde pour sauvegarder un tableau de présidents sous la forme d'un fichier CSV.

### `President[] load(String nomFichier, int presidentsEnPlus)`

Pour pouvoir mettre à jour les données, il est nécessaire de disposer d'une structure de données plus volumineuse que notre source de données initiales. On se propose donc de créer la fonction `load` qui prend en paramètre le nombre de présidents que l'on souhaiterait ajouter afin d'allouer un tableau plus grand que celui présent dans le fichier CSV.

Définissez la fonction `load` et décommentez ensuite le corps d'`algorithm()` afin de vérifier que le chargement s'est bien déroulé et que la structure de données est bien initialisée (enfin, au moins sa première ligne, mais n'hésitez pas à remplacer par une boucle ;)).

### `void save(President[] presidents, String nomFichier)`

Ajoutez dans l'algorithme principal quelques lignes afin d'ajouter un nouveau président dans la structure de données (à la main, en dur). Ainsi, nous pourrons vérifier que la fonction `save` fonctionne correctement.

Cette fonction prend en paramètre un tableau de président et le nom du fichier CSV à créer (ou écraser si il existe). La fonction doit d'abord convertir la structure de données en un tableau à 2 dimensions de chaînes de caractères afin d'utiliser la procédure de sauvegarde proposée pour les CSV (`void saveCSV(String[][] content, String filename)`).

Définissez la fonction `save` et vérifiez que le nouveau fichier créé contient bien le président que vous avez ajouté.

## Fonctions d'analyse de notre structure de données

Maintenant que nous avons une structure de données évolutives et persistante grâce aux fichiers, nous allons pouvoir passer à une phase d'analyse des données.

### Déterminer le nombre de présidents issus d'un parti donné

Écrivez une fonction `int nombreDe(President[] presidents, String parti)` qui calcule le nombre de présidents d’un parti donné.

### Déterminer la durée maximale durant laquelle un parti est resté au pouvoir

Écrivez une fonction `int regneMaximal(President[] presidents, String parti)` qui calcule la durée maximale en années pendant laquelle un même parti est resté au pouvoir.
