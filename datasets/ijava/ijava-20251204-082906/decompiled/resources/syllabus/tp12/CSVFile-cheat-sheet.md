# Manipulation de fichiers CSV (type `CSVFile`)

Le type `CSVFile` représente un fichier CSV chargé en mémoire.
On accède à une valeur de manière similaire à un tableau à deux dimensions en précisant des indices de lignes et de colonnes.

Lorsqu’un fichier CSV est chargé, il est stocké dans une variable de type `CSVFile` sous la forme d’un tableau à deux dimensions de chaînes de caractères. Il est possible de connaître le nombre de lignes grâce à l’instruction `rowCount(CSVFile)` et le nombre de colonnes avec `columnCount(CSVFile)`. 

Pour accéder à une case, il suffit d’appeler `getCell` en précisant les indices de ligne et de colonne (sachant que les indices commencent à 0). 

Finalement, si vous souhaitez créer un fichier CSV, vous pouvez sauver un tableau à deux dimensions de chaînes de caractères à l’aide de la fonction `saveCSV`.

Comme pour le type `File`, le type `CSVFile` est défini dans la librairie `extensions`, du coup, il est nécessaire d'indiquer cela au compilateur au tout début des programmes nécessitant la manipulation de fichiers CSV :
```java
import extensions.CSVFile;
``` 

**⚠️ Les indices commencent à 0, comme pour les tableaux !**

| Retour | Fonction | Paramètres | Description |
|--------|----------|------------|-------------|
| `CSVFile` | `loadCSV(String filename)` | `filename` : le nom du fichier à charger | Charge un CSV (séparateur `,` par défaut) |
| `CSVFile` | `loadCSV(String filename, char separator)` | `filename` : le nom du fichier à charger et `separator` le séparateur à utiliser | Charge un CSV avec séparateur personnalisé |
| `int` | `rowCount(CSVFile table)` | `table` la source de données à utiliser| Nombre de lignes du CSV |
| `int` | `columnCount(CSVFile table)` | `table` la source de données à utiliser | Nombre de colonnes (toutes lignes) |
| `int` | `columnCount(CSV table, int idxLine)` | `table` la source de données et  `idxLine` la ligne dont on souhaite connaître le nombre de colonnes  | Nombre de colonnes d'une ligne donnée |
| `String` | `getCell(CSVFile table, int idxLine, int idxColumn)` | `table` la source de donnée à utiliser et `idxLine` l'indice de ligne et `idxColumn` de colonne de la valeur souhaitée| La valeur de la cellule de coordonnées ( `idxLine`,  `idxColumn`) |
| `void` | `saveCSV(String[][] content, String filename)` | `content` la table de chaîne de caractères à sauver sous la forme d'un fichier CSV, `filename` le nom du fichier à créer ou écraser | Sauvegarde des données dans un fichier CSV avec le séparateur `,`) |
| `void` | `saveCSV(String[][] content, String filename, char separator)` | `content` la table de chaîne de caractères à sauver sous la forme d'un fichier CSV, `filename` le nom du fichier à créer ou écraser et `separator` le séparateur à utiliser | Sauvegarde les données dans un fichier CSV avec le séparateur indiqué) |

**Exemple d'utilisation :**
```java
void algorithm() {
    CSVFile donnees = loadCSV("notes.csv");
    for (int idxLigne = 0; idxLigne < rowCount(donnees); idxLigne++) {
        // Ici on n'a pas de boucle sur les colonnes car il n'y a que 2 colonnes
        String nom  = getCell(donnees, idxLigne, 0);
        String note = getCell(donnees, idxLigne, 1);
        println(nom + " : " + note);
    }
}
```
---

## 💡 Points importants

* **Indices CSV** : commencent à 0 (première ligne = 0, première colonne = 0)
* **Séparateurs CSV** : virgule `,` (défaut), point-virgule `;` (France), tabulation `\t` pour les plus courants
* **Types de données** : `getCell()` retourne toujours une `String`.
* **Gestion d'erreurs** : si le fichier n'existe pas, une erreur sera levée ...

::: question
**Pour intégrer de l'ASCII Art dans votre projet, quel type est le plus approprié ?**
- [x] `extensions.File`
  > Oui, a priori c'est le plus pertinent car l'ASCII Art est un ensemble de caractères contenus dans un simple fichier texte.
- [ ] `extensions.CSVFile`
  > Hum peu probable, on utilise plutôt les fichiers pour des données structurées et mobilisant généralement plusieurs types d'information
:::
