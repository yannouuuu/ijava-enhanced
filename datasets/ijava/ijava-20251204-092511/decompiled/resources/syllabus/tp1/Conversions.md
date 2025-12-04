# D'un type à l'autre

## Opérateur de forçage de type ou *cast*

Cet exercice vous aidera à comprendre le fonctionnement de l'opérateur de conversion.

Attention : il ne faut ni compiler et ni exécuter ce programme !

```java
class Conversions extends Program {
    void algorithm(){                    // Indiquez ci-dessous l'affichage produit
        print( "(int) 4.6 ->" ); 
        println( (int) 4.6 );            // => 
        print( "(double) 4 ->" );
        println( (double) 4 );           // => 
        print("2.1 + 3 -> ");
        println(2.1 + 3);                // => 
        print("(int) 'A' -> ");
        println( (int) 'A' );            // => 
        print(" (char) 66 -> ");
        println( (char) 66);             // => 
        print(" (int) 3.7 * 2 -> ");
        println( (int) 3.7 * 2);         // => 
        print("(int) (3.7 * 2) -> ");
        println((int) (3.7 * 2));        // => 
        print(" \"ABC\" + (char) 65 ");
        println( "ABC" + (char) 65 );    // => 
    }
}
```
- Essayez dans un premier temps de prédire le résultat du programme `Conversions` ci-dessus en écrivant en commentaire (rappel : `// => ceci est un commentaire`) sur les lignes `print` les affichages se produisant lors de l'exécution.
- Appelez votre enseignant·e une fois que c'est terminé pour vérification.

## Étapes
- `ijava init Conversions`
-  *complétez **UNIQUEMENT LES AFFICHAGES PRODUIT AU NIVEAU DES COMMENTAIRES !**
- `ijava compile Conversions.java`
- `ijava execute Conversions` **=> NE PAS LANCER AVANT D'AVOIR VU VOTRE ENSEIGNANT·E !**
- `ijava test Conversions`
---
