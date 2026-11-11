# Affichage verlan

## À l'envers ?

On souhaite créer un programme `Verlan`, qui inverse la deuxième partie du mot avec la première. Par exemple, avec la chaîne `louche`, le programme affichera `chelou`. Lisez attentivement le programme suivant :

```java
class Verlan extends Program {
    void algorithm(){
        print("Saisissez un mot : ");
        ... mot = readString();
        ... tailleMot = ... ;
        ... indiceMilieu = ... ;
        ... debut = ... ;
        ... fin = ... ;
        println(fin+debut);
    }
}
```

- Complétez ce programme, puis compilez le et exécutez le afin de vérifier qu'il fonctionne comme souhaité lorsque l'on saisit `louche`.
- Une fois que votre programme marche avec cet exemple, vérifiez qu'il fonctionne bien dans tous les cas de figure. Par exemple, en saisissant la chaîne `"cheval"` ou `"malin"` dans votre programme, après compilation et exécution, celui-ci devrait afficher \textit{linma} si il est valide. Si tel n'est pas le cas, modifiez votre programme pour qu'il soit suffisamment général pour couvrir ces 2 cas.
- Lorsque vous êtes sûr de votre programme lancer la commande : `ijava test Verlan`

## Étapes à suivre
- *complétez le code au niveau des ... et sauvez bien votre fichier !*
- `ijava compile Verlan.java`
- `ijava execute Verlan`
- `ijava test Verlan`

---
