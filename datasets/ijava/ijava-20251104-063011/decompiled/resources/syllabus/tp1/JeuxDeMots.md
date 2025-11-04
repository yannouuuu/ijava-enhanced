# D'une chaîne à une autre

## Où sont passés ces types ?!

Étudiez le programme donné ci-dessous :
```java
class JeuxDeMots extends Program {
    void algorithm(){
        ... mot = "etat";
        ... resultat;
        ... premiereLettre = charAt(mot, 0);
        ... resteDuMot = substring(mot, 1, length(mot));
        resultat = resteDuMot + premiereLettre;
        println(resultat);
    }
}
```
- A votre avis, que réalise ce programme ? Notez cela sur votre brouillon.
- Générez le squelette corrrespondant : `ijava init JeuxDeMots`
- Éditez le code source pour compléter les déclarations de type manquantes.
- Exécutez le programme pour vérifier votre hypothèse : `ijava execute JeuxDeMots`
- Testez votre programme : `ijava test JeuxDeMots`
- Choississez un autre mot de départ et ré-exécutez votre programme pour être sûr du traitement réalisé par cet algorithme.

## Étapes à suivre
- `ijava init JeuxDeMots`
-  *complétez le code source avec un éditeur de texte sans modifier ce qui est donné*
- `ijava compile JeuxDeMots.java`
- `ijava execute JeuxDeMots`
- `ijava test JeuxDeMots`

---
*Time: 10 minutes*
