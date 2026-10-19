# Enlever l'espace en début et fin d'une chaîne

Écrire le programme `NettoyerChaine` qui à partir d’une variable (`chaine`) contenant une chaîne de caractères retourne la même chaîne privée de l’éventuel caractère espace qui se trouverait en début et/ou en fin de la chaîne.

```java
class NettoyerChaine extends Program {
    void algorithm () {
        String chaine = readString();
        println("Avant nettoyage :");
        println(">"+chaine+"<");

        //À COMPLÉTER

        println("Après nettoyage :");
        println(">"+chaine+"<");
    }
}
```
Vous ne devez pas modifier les lignes 7-10 et 14-15 ! Vous devez juste insérer votre code à la place de la ligne 12 et les suivantes pour réaliser cet exercice.

Voici quelques chaînes qu'il est intéressant de tester :
* avant `> Bonjour <` après `>Bonjour<`
* avant `> Hello <` après `>Hello<`
* avant `> bye<` après `>bye<`
* avant `> x <` après  `>x<`
* avant `> <` après `><`
* avant la chaîne vide après `><`

Après avoir testé manuellement, n'oubliez pas le test automatisé d'ijava !
