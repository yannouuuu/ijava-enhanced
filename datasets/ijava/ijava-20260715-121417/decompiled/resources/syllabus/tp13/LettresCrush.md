# Mini jeu d'élimination de zones contiguës

Vous allez implémenter un mini jeu vaguement inspiré du célèbre jeu Candy Crush.
On part d'une chaine de caractères, dans laquelle on a le droit d'éliminer des groupes d'au moins trois lettres consécutives. Le but est d'éliminer toute la chaine.

Voici deux exemples.

En partant de la chaine `"xxxooooxx"`, on peut commencer par éliminer les `o` du milieu pour obtenir `"xxxxx`, qu'on peut ensuite éliminer pour gagner.

En partant de la même chaine `"xxxooooxx"`, on peut commencer par éliminer les `x` du début pour obtenir `"ooooxx"`, puis éliminer les `o`, et arriver à la chaine `"xx"` qui est une configuration perdante.

Voici un exemple d'exécution, ainsi que l'algorithme principal correspondant.

```
xxxooooxx
Saisir indice à éliminer, ou 'fin' pour arrêter : 1
ooooxx
Saisir indice à éliminer, ou 'fin' pour arrêter : 4
ooooxx
Saisir indice à éliminer, ou 'fin' pour arrêter : 2
xx
Saisir indice à éliminer, ou 'fin' pour arrêter : fin
Perdu !
```

```
final int TAILLE_MIN_ZONE = 3;

String eliminer (String s, int indiceDepart) {
    int indiceAvant = explorer(s, indiceDepart, charAt(s, indiceDepart), -1);
    int indiceApres = explorer(s, indiceDepart, charAt(s, indiceDepart), 1);
    if (indiceApres - indiceAvant - 1 >= TAILLE_MIN_ZONE) {
        return substring(s, 0, indiceAvant+1) + substring(s, indiceApres, length(s));
   } else {
        return s;
   }
}

void algorithm() {
    String s = "xxxooooxx";
    boolean fin = false;

    while (! fin && length(s) != 0) {
        println(s);
        print("Saisir indice à éliminer, ou 'fin' pour arrêter : ");
        String saisie = readString();
        if (equals("fin", saisie)) {
            fin = true;
        } else {
            int indice = stringToInt(saisie);
            s = eliminer(s, indice);
        }
    }
    if (fin) {
        println("Perdu!");
    } else {
        println("Gagné!");
    }
}
```

Il vous reste alors à écrire la fonction **récursive** `int explorer (String s, int indiceDepart, char car, int direction)` qui part de `indiceDepart` et explore la chaine `s` de proche en proche pour trouver le premier indice sur son chemin dont la lettre est diffèrente de `car`. 
L'exploration se fait vers la gauche si `direction` est égal à `-1`, et vers la droite si `direction` est égal à `1`.

Voici le test que la fonction doit passer.
```
void test_explorer () {
	assertEquals(2, explorer("xxxooooxx", 4, 'o', -1));
	assertEquals(7, explorer("xxxooooxx", 4, 'o', 1));

	assertEquals(-1, explorer("xxxooooxx", 1, 'x', -1));
	assertEquals(9, explorer("xxxooooxx", 7, 'x', 1));

	assertEquals(2, explorer("xxxooooxx", 2, 'o', -1));
	assertEquals(2, explorer("xxxooooxx", 2, 'o', 1));
}
```

:::question
**Est-ce que votre fonction explorer est récursive terminale ?**
- [X] Oui
  > Très bien.
- [ ] Non
  > Alors, il faut la réécrire en utilisant une récursivité terminale.
- [ ] Je ne sais pas
  > Vous devriez revoir la notion de récursivité terminale.
:::