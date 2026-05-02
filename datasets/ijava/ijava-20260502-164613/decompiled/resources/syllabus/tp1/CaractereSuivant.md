# Au suivant !

## Appréhender le type `char` et la table ASCII

On souhaite disposer d'un programme qui nous indique quel est le caractère suivant dans la table ASCII pour un caractère donné. On souhaite que l'usager choississe le caractère ce qui implique de réaliser une saisie au clavier. Pour ce faire, on fait un appel à la fonction `readChar()`, qui ne prend pas de paramètre et retourne le premier caractère saisi par l'utilisateur au clavier. 

## Programme à compléter

Voici le programme presque complet si ce n'est qu'il manque la partie concernant le passage au caractère suivant.
```java
class CaractereSuivant extends Program {
    void algorithm(){
        print("Entrez un caractère : ");
        char c = readChar();
        char suivant = ... ; // <-- À COMPLÉTER
        println("Le caractère après " + c + " est " + suivant );        
    }
}
```
- Générez le squelette à l'aide de l'action dédiée de la commande `ijava`
- Complétez le code source puis compilez le.
- Exécutez le programme en l'essayant avec les deux scénarios suivant :

```
    > Entrez un caractère : **a** 
    Le caractère après a est b

    > Entrez un caractère : **Z** 
    Le caractère après Z est [
```

Et du texte normal après.
Les éléments **en gras** correspondent aux entrées de l'usager au cours de l'exécution du programme.
- Essayez avec au moins un autre caractère que les deux demandés.
- Lancez les tests associés à ce programme pour terminer.

## Étapes à suivre
- `ijava init CaractereSuivant`
-  *complétez le code source avec un éditeur de texte*
- `ijava compile CaractereSuivant.java`
- `ijava execute CaractereSuivant`
- `ijava test CaractereSuivant`
---
