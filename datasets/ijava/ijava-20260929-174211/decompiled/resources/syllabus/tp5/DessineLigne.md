# Dessinons une ligne

On souhaite créer des dessins (simples) en mode texte. Comme ces dessins contiennent souvent des lignes, on se dit que créer une fonction pour afficher facilement une ligne avec un certain caractère et la taille de la ligne serait plutôt pratique.

::: question
**Quelle pourrait être la signature de cette fonction ?**

- [ ] int dessineLigne(char symbole)
  > Non, non : il faut effectivement avoir le caractère en paramètre, mais aussi l'entier représentant le nombre de symboles à afficher. Le type int placé à cet endroit signifie que la fonction calcule un résultat entier, ce qui n'est pas le cas.
- [ ] String dessineLigne(char symbole, int taille)
  > Ah, ce n'est pas ce qui est demandé ! Plus tard, on utilisera plutôt cette technique : fabriquer une chaîne plutôt que réaliser un affichage. Donc minute papillon et soyez attentifs aux consignes !
- [x] void dessineLigne(char symbole, int taille)
  > Exactement ! Cette fonction a besoin du caractère et de l'entier pour réaliser son calcul, mais elle ne produit pas de résultat exploitable ensuite ... elle se contente de changer la couleur de certains pixels à l'écran !
- [ ] int char dessineLigne()
  > Hum, étrange syntaxe ou les données nécessaires semblent placer avant le nom de la fonction :( Il faut apprendre le cours ...
:::

Voici quelques exemples d'appels à la fonction `dessineLigne` et de l'affichage produit
```java
dessineLigne('a', 4) affichera "aaaa"
dessineLigne(' ', 5) affichera "     " (donc rien de visible pour un humain !)
dessineLigne('%', 0) affichera "" (rien de nouveau, aucune erreur ne doit arriver)
```

Complétez le squelette fourni en **NE MODIFIANT PAS ALGORITHM** !

**ATTENTION :** pour une fois, on ne souhaite pas un retour à la ligne après avoir dessiné les caractères.
