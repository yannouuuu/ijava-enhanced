# Toutes les sous-chaînes

On souhaite réaliser un algorithme qui énumère toutes les sous-chaînes possibles d’un chaine donnée. 

Voici un exemple d'affichage produit par cet algorithme avec la chaîne `"Hello"`:
```java
Mot : Hello
Hello
Hell
Hel
He
H
ello
ell
el
e
llo
ll
l
lo
l
o
```

Pour vous aider à trouver l’algorithme, répondez à la série de questions ci-dessous qui devraient vous permettre d’identifier les éléments clés de ce problème:
- Avec la chaîne initiale, `"ABC"` écrivez toutes les sous-chaînes possibles. Combien en trouvez vous ?
- Avec la chaîne initiale `"abcdefghijk"`, pouvez vous caractériser la chaîne `"defg"` au moyen de deux entiers ? Quels sont-t-ils ?
- Quels sont les valeurs possibles de ces deux entiers sur cet exemple ?
- Existe-t-il une ou plusieurs contraintes entre ces deux entiers ?
- Écrivez la boucle qui parcourt toutes les valeurs possibles pour le premier entier.
- Écrivez la boucle qui, étant donné la valeur du premier entier, parcourt toutes les valeurs possibles pour le second
entier.
- Écrivez l’expression qui retourne la sous-chaîne à partir des deux entiers et de la chaîne.

Muni de tous ces éléments, vous devriez pouvoir maintenant écrire le programme `SousChaines` qui saisit un mot auprès de l’utilisateur et affiche toutes les sous-chaînes de ce mot.
