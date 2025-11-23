# Des minuscules aux majuscules et vice-versa

## Jouer avec les décalages dans la table ASCII

On souhaite disposer d'un programme qui transforme une lettre de l'alphabet des minuscules vers les majuscules et réciproquement. N'oubliez pas qu'avec le code ASCII les différents caractères sont représentés par des nombres. Il est donc possible en appliquant un certain décalage sur un caractère (en lui ajoutant un entier) de le transformer en un autre caractère. Pas besoin de connaître la table ASCII pour réaliser un tel exercice, juste de savoir comment elle est strucutrée c'est-à-dire que les lettres majuscules se trouvent avant les minuscules `[...,A-Z,...,a-z,...]`.

Voici le programme que vous avez à compléter : 
```java
class MajMin extends Program {
    void algorithm(){
        print("Entrez une lettre en minuscule");
        char lettreMin = readChar();
        char enMaj = ; // <-- À COMPLÉTER
        println("La lettre " + lettreMin + " en majuscule donne : " + enMaj );
        print("Entrez une lettre en majuscule");
        char lettreMaj = readChar();
        char enMin = ; // <-- À COMPLÉTER
        println("La lettre " + lettreMaj + " en minuscule donne : " + enMin );
    }
}
```
- Générez le squelette puis complétez le code source avant de compiler.
- Exécutez le programme en réalisant les saisies permettant d'obtenir l'affichage suivant lors de l'exécution :
```bash
> Entrez une minuscule : **a** 
La lettre a en majuscule donne : A 
Entrez une majuscule : **Z**
La lettre Z en minuscule donne : z
```
*Rappel: ce qui est **en gras** correspond aux entrées de l'usager*
- Essayez avec au moins une autre minuscule et une autre majuscule.
- Lancez les tests pour finir et valider cet exercice !

## Étapes à suivre
- `ijava init MajMin`
-  *complétez le code source avec un éditeur de texte sans modifier ce qui est fourni !*
- `ijava compile MajMin.java`
- `ijava execute MajMin`
- `ijava test MajMin`

---
