# Jeu du pendu

Vous connaissez sûrement ce jeu à deux joueurs dans lequel un joueur `A` fait deviner un mot à un joueur `B`. Le joueur `B` doit proposer des lettres et a droit à un nombre d'erreurs limité. Le joueur `A` décompte les erreurs et place les lettres trouvées. Vous devez écrire un algorithme qui **remplace le joueur `A`**.

Pour simplifier le problème, nous supposerons dans cet exercice que le mot à deviner est représenté sous la forme d'une chaîne de caractères initialisée en début d'algorithme. On suppose aussi que le joueur `B` a le droit à cinq erreurs avant de perdre la partie. 

Lorsque la partie débute, le joueur `B` ne voit que le nombre de lettres à deviner, chacune d'elles étant symbolisée par une étoile. À chaque tour, il ou elle propose une lettre. Si la lettre est présente dans le mot, l'étoile est remplacée par cette lettre. Sinon, on comptabilise une erreur supplémentaire. Le joueur B gagne s'il découvre le mot avant d'atteindre le nombre d'erreurs autorisées.

Voici un exemple de déroulement de partie dans laquelle le joueur `B` perd, après avoir proposé les lettres `p`, `p`, `e`, `i`, `a`, `u`, `o`, `s`, `m`, `x`.

```
Il vous reste 5 erreurs: * * * * * * * * * 
Entrez un caractère: p
Il vous reste 5 erreurs: p * * * * * * * * 
Entrez un caractère: p
Il vous reste 4 erreurs: p * * * * * * * * 
Entrez un caractère: e
Il vous reste 4 erreurs: p * * * * * * * e 
Entrez un caractère: i
Il vous reste 3 erreurs: p * * * * * * * e 
Entrez un caractère: a
Il vous reste 3 erreurs: p * * * * a * * e 
Entrez un caractère: u
Il vous reste 2 erreurs: p * * * * a * * e 
Entrez un caractère: o
Il vous reste 2 erreurs: p * o * * a * * e 
Entrez un caractère: s
Il vous reste 1 erreurs: p * o * * a * * e 
Entrez un caractère: m
Il vous reste 1 erreurs: p * o * * a m m e 
Entrez un caractère: x
Vous avez perdu ! Il fallait trouver: programme
```

Voici maintenant une partie dans laquelle le joueur `B` gagne.

```
Il vous reste 5 erreurs: * * * * * * * * * 
Entrez un caractère: o
Il vous reste 5 erreurs: * * o * * * * * * 
Entrez un caractère: a
Il vous reste 5 erreurs: * * o * * a * * * 
Entrez un caractère: e
Il vous reste 5 erreurs: * * o * * a * * e 
Entrez un caractère: i
Il vous reste 4 erreurs: * * o * * a * * e 
Entrez un caractère: u
Il vous reste 3 erreurs: * * o * * a * * e 
Entrez un caractère: m
Il vous reste 3 erreurs: * * o * * a m m e 
Entrez un caractère: p
Il vous reste 3 erreurs: p * o * * a m m e 
Entrez un caractère: r
Il vous reste 3 erreurs: p r o * r a m m e 
Entrez un caractère: g
Vous avez gagné ! Il fallait trouver: programme
```

Maintenant que vous avez une bonne compréhension du jeu, répondez aux questions suivantes avant de vous lancer dans la conception de votre algorithme.

1. Faites une partie avec votre voisin ou voisine.
2. Réfléchissez aux éléments de l'algorithme et à la représentation des informations :
- Comment procédez-vous pour trouver les endroits où il faut afficher la lettre déjà trouvée à la place de l'étoile ?
- Comment faire pour garder trace, entre deux tours du jeu, des positions déjà découvertes par rapport à celles encore à trouver ? 
3. Quand la partie s'arrête-t-elle ? Quel type de boucle semble le plus approprié ? 

Vous pouvez maintenant vous lancer dans l'écriture de l'algorithme (en utilisant, si vous le souhaitez, les indications ci-dessous). Une fois que vous êtes confiant ou confiante pour le résultat, vous pouvez lancer les tests professeur en **utilisant `programme` comme mot à deviner**.

**Indications**

N'hésitez pas à procéder par étapes. Exécutez et tester votre programme à chaque étape. C'est ce que l'on appelle développer de manière itérative.

Par exemple :
- D'abord faire juste trois tours de boucle au total et, à chaque tour de boucle, afficher le mot caché et lire la proposition du joueur.
- Ajouter le code nécessaire pour prendre en compte la proposition du joueur en remplaçant les étoiles aux bonnes positions.
- Ajouter le décompte du nombre d'erreurs.
- Faire le bon nombre de tours, en ajoutant les bonnes conditions de boucle (gagné ou perdu).
