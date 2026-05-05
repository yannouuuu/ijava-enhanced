# Types manquants

## Ces types ont disparu !

Dans le programme ci-dessous, vous devez compléter par le bon type partout où les ... apparaîssent.

```java
class JeuxDeType extends Program {
    void algorithm(){
        ... prenom = "Alan";
        ... nom = "Turing";
        ... naissance = 1912;
        ... annee = 2022;
        ... age = annee - naissance;
        ... initiale = charAt(prenom,0);
        println(initiale + ". " + nom + " aurait eu " + age + "ans en " + annee);
    }
}
```
Pour débuter l'exercice, utilisez la commande : `ijava init JeuxDeType`.
Éditez ensuite le squelette qui a été généré afin de compléter avec les types appropriés. 

Ensuite, compilez votre programme avec la commande : `ijava compile JeuxDeType.java`.

Finalement, exécutez votre programme avec : `ijava execute JeuxDeType`
Vous devriez obtenir l'affichage suivant :

```bash
> A. Turing aurait eu 110 ans en 2022
```

Afin de vérifier cela automatiquement, utilisez la commande : `ijava test JeuxDeType`

Après cette première étape, modifiez les valeurs des variables de manière à ce qu'il réalise le même comportement mais cette fois concernant Ada Lovelace née en 1815.

## Étapes à suivre
- `ijava init JeuxDeType`
-  *compléter le code source avec un éditeur de texte*
- `ijava compile JeuxDeType.java`
- `ijava execute JeuxDeType`
- `ijava test JeuxDeType`

---

