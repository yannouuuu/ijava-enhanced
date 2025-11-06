# Extraire les mots d’un texte et créer un tableau de mots

Le dernier ingrédient nécessaire pour calculer les fréquences des mots est de pouvoir stocker dans un tableau les mots contenus dans un texte (ici,
une chaîne de caractères).

Dans la suite, une lettre signifie un caractère alphabétique. Un mot est une suite consécutive de lettres précédée et suivie d’un caractère qui n’est pas une lettre. 


Nous allons maintenant écrire la fonction `String[] extraireMots(String texte)` qui passe ce test.
```
void test_extraireMots () {
    assertArrayEquals(new String[]{"je", "tu", "elle", "je", "tu"}, 
        extraireMots("je tu elle je tu"));
    assertArrayEquals(new String[]{"je", "tu", "elle", "je", "tu"}, 
        extraireMots(" je tu, elle . je tu ! "));
}
```

Voici quelques indications pour écrire cette fonction:
- Comme pour la fonction `motsDifferents`, nous ne pouvons pas connaitre le taille du tableau à produire en résultat avant d’avoir visité le texte entier. Pour gérer ce problème on peut utiliser la même technique que dans `motsDifferents`.
- Remarquez que pour détecter la fin d’un mot dans texte, il suffit de détecter quand on passe d’une lettre à un autre caractère et pour détecter le début d’un mot, quand on passe d’autre chose qu’une lettre à une lettre.
- L’algorithme est plus facile à écrire si on suppose que la dernier caractère de texte n’est pas alphabétique. En première intention, ajoutez un espace à la fin du texte, ce qui vous facilitera le travail (vous reviendrez là dessus plus tard si vous le souhaitez).
- Il peut être utile de disposer d’une fonction `boolean estLettre(char c)` qui teste si un caractère est une lettre. Vous avez déjà écrit cela il y a longtemps ;)
- Vous pouvez commencer par écrire une fonction qui affiche les mots un à un, et la modifier ensuite pour que les mots soient stockés dans un tableau. Toujours penser à développer de manière itérative, petits pas à petits pas !
