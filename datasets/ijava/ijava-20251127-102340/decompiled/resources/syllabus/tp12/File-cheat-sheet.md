# Guide de manipulation des fichiers

Ce document présente les fonctions disponibles pour manipuler des fichiers texte dans vos programmes *ijava*.

Avant de pouvoir utiliser ce type, il est important d'ajouter la ligne d'importation au tout début de votre fichier, car `File` n'est pas un type disponible par défaut :
```java
import extensions.File;
```

On indique ainsi au compilateur que pour trouver la déclaration du type `File`, il faut la rechercher dans la librairie `extensions`.

## Lister les fichiers d'un répertoire

Afin de trouver les fichiers disponibles, il existe deux fonctions permettant de lister le contenu d'un répertoire. Ces deux fonctions retournent des tableaux de chaînes contenant les noms de l'ensemble des fichiers présents dans un répertoire.

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

Afin de lire un fichier texte, il faut d'abord créer une variable de ce type grâce au constructeur `newFile`. Ensuite, ce fichier est géré avec les deux fonctions `ready`et `readLine` qui permettent respectivement de déterminer si il y a encore des lignes à lire dans le fichier et d'obtenir la prochaine ligne disponible.

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

::: question
**Si l'on souhaite lire un fichier texte, il faut ... **
- [ ] appeller le constructeur et lire les lignes.
  > C'est un peu dangereux : si jamais le fichier est vide, alors une exception se produira !
- [x] appeler le constructeur, vérifier qu'il y a des lignes et lire les lignes.
  > Tout à fait, on crée d'abord une valeur de type `File`, puis on l'utilise pour vérifier d'abord qu'elle contient au moins une ligne et finalement on la lit.
- [ ] appeller le constructeur, allouer le fichier, vérifier qu'il y a des lignes et les lire.
  > Heu ... allouer un fichier ?! Ce n'est pas un tableau ! Toute l'allocation est gérée par le constructeur `newFile` de la même manière que vous le faîtes lorsque vous créez vos propres types.
:::
---
