# Même longueur

Le programme `MemeLongueur` compare deux chaînes de caractères et indique si elles sont de même longueur ou non.

```java
class MemeLongueur extends Program{
  void algorithm(){
    ... mot1 = readString();
    ... mot2 = readString();
    ... longueurMot1 = ... ;
    ... longueurMot2 = ... ;
    ... memeLongueur = ... ;
    println("Les deux mots sont de même longueur : " + memeLongueur);
  }
}
```

Malheureusement, votre collègue n'a fait que la moitié du travail et il vous faut donc compléter ce programme incomplet afin qu'il compile et réalise la fonctionnalité demandée.

Complétez dans un premier temps les déclarations de variables actuellement incomplètes, puis réfléchissez aux expressions permettant de calculer la longeur des deux mots, puis de déterminer si elles sont identiques.

Une fois le programme complété, essayez-le avec deux mots de même longueur, vous devriez obtenir un affichage similaire à :
```bash
Les deux mots sont de la même longueur : true
```

Re-exécutez votre programme avec deux mots de longueur différentes et assurez vous que vous obtenez bien `false` dans ce cas.

N'oubliez pas de terminer l'exercice avec `ijava test MemeLongueur`.