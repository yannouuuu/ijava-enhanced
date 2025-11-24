# Guide de manipulation des fichiers

Ce document présente les fonctions disponibles pour manipuler des fichiers texte et CSV dans vos programmes Java.

## Lister les fichiers d'un répertoire

| Retour | Fonction | Paramètres | Description |
|--------|----------|------------|-------------|
| `String[]` | `getAllFilesFromDirectory(directory)` | `String directory` : chemin du répertoire | Liste les fichiers d'un répertoire spécifié |
| `String[]` | `getAllFilesFromCurrentDirectory()` | aucun | Liste les fichiers du répertoire courant |

**Exemple d'utilisation :**
```java
void algorithm() {
    String[] fichiers = getAllFilesFromDirectory("data");
    for (int idx = 0; idx < length(fichiers); idx++) {
        println(fichiers[idx]);
    }
}
```
---
## Lecture de fichiers texte (type `File`)

| Retour | Fonction | Paramètres | Description |
|--------|----------|------------|-------------|
| `File` | `newFile(filename)` | `String filename` : nom du fichier | Ouvre un fichier texte en lecture |
| `boolean` | `ready(file)` | `File file` : fichier à tester | Indique s'il reste des lignes à lire |
| `String` | `readLine(file)` | `File file` : fichier à lire | Lit la ligne courante et avance |

**Exemple d'utilisation :**
```java
void algorithm() {
    File fichier = newFile("texte.txt");
    while (ready(fichier)) {
        println(readLine(fichier));
    }
}
```
---

## Manipulation de fichiers CSV (type `CSVFile`)

Le type `CSVFile` représente un fichier CSV chargé en mémoire. **⚠️ Les indices commencent à 0.**

| Retour | Fonction | Paramètres | Description |
|--------|----------|------------|-------------|
| `CSVFile` | `loadCSV(filename)` | `String filename` | Charge un CSV (séparateur `,` par défaut) |
| `CSVFile` | `loadCSV(filename, separator)` | `String filename`, `char separator` | Charge un CSV avec séparateur personnalisé |
| `int` | `rowCount(table)` | `CSVFile table` | Nombre de lignes du CSV |
| `int` | `columnCount(table)` | `CSVFile table` | Nombre de colonnes (toutes lignes) |
| `int` | `columnCount(table, idxLine)` | `CSVFile table`, `int idxLine` | Nombre de colonnes d'une ligne |
| `String` | `getCell(table, idxLine, idxColumn)` | `CSVFile table`, `int idxLine`, `int idxColumn` | Valeur d'une cellule |
| `void` | `saveCSV(content, filename)` | `String[][] content`, `String filename` | Sauvegarde un CSV (séparateur `,`) |
| `void` | `saveCSV(content, filename, separator)` | `String[][] content`, `String filename`, `char separator` | Sauvegarde un CSV avec séparateur |

**Exemple d'utilisation :**
```java
void algorithm() {
    CSVFile donnees = loadCSV("notes.csv");
    for (int idxLigne = 0; idxLigne < rowCount(donnees); idxLigne++) {
        String nom  = getCell(donnees, idxLigne, 0);
        String note = getCell(donnees, idxLigne, 1);
        println(nom + " : " + note);
    }
}
```
---

## 💡 Points importants

* **Indices CSV** : commencent à 0 (première ligne = 0, première colonne = 0)
* **Séparateurs CSV** : virgule `,` (défaut), point-virgule `;` (France), tabulation `\t`
* **Types de données** : `getCell()` retourne toujours une `String`.
* **Gestion d'erreurs** : si le fichier n'existe pas, une erreur sera levée ...

::: question
**Pour intégrer de l'ASCII Art dans votre projet, quel type est le plus approprié ?**
- [x] `extensions.File`
  > Oui, a priori c'est le plus pertinent car l'ASCII Art est un ensemble de caractères contenus dans un simple fichier texte.
- [ ] `extensions.CSVFile`
  > Hum peu probable, on utilise plutôt les fichiers pour des données structurées et mobilisant généralement plusieurs types d'information
:::
