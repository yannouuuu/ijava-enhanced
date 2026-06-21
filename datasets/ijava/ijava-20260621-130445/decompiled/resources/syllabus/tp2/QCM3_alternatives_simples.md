# Alternatives simples ...


::: question
**En ijava, quel mot-clé permet de définir une alternative ?**

- [ ] si
  > Incorrect : en (i)java tous les mots-clés du langage sont en anglais !
- [ ] IF
  > Incorrect : généralement, lorsque tout est écrit en majuscule, c'est plutôt une constante.
- [ ] If
  > Presque, mais les mots-clés du langage sont toujours en minuscules.
- [x] if
  > Parfait, bien joué !
:::

::: question
**En ijava, quelle est la seule l'expression valide ci-dessous ?**

- [ ] if a == 5 { print(5); } else { print("zut");}
  > Incorrect : les parenthèses autour de la condition sont obligatoires !
- [x] if (a == 5) { print(5); } else { print("zut");}
  > Parfait, bien joué !
- [ ] if (a = 5) { print(5); } else { print("zut");}
  > Aïe, aïe, aïe : NE PAS CONFONDRE l'opérateur d'AFFECTATION (=) pour modifier le contenu d'une variable et l'opérateur d'ÉGALITÉÉ (==) !!!
- [ ] if {a == 5} { print(5); } else { print("zut");}
  > Incorrect : la condition doit être entre parenthèses, pas entre accolades. Ces dernières délimitent des blocs d'instructions.
:::

::: question
**Quelle condition permet de déterminer si un nombre est pair ?**

- [ ] a / 2 == 0
  > Incorrect : on demande ici si a divisé par 2 vaut 0, ce qui n'est vrai que pour 1.
- [ ] a - 2 * a / 2 == 0
  > Incorrect : même si cela fonctionnerait, cela fait 4 opérations au lieu de deux ...
- [x] a % 2 == 0
  > Exactement : avec l'opérateur du reste de la division entière (modulo).
- [ ] a / 2 == int
  > Incorrect :  l'expression a / 2 retourne une valeur entière. On ne peut tester l'égalité entre une valeur entière et le type représentant les entiers car ces données ne sont pas de même type.
:::

::: question
**Si a vaut 5 qu'affiche ce programme : if (a <6) { print("a"); if (a==5) { print("b"); }} ?**

- [x] ab
  > Exactement car la première condition est vérifié et le a est affiché, puis la seconde donc le b aussi.
- [ ] a
  > Incorrect : après l'affichage du a, la seconde condition est aussi vérifiée du coup le b s'affiche ...
- [ ] b
  > Incorrect : avec ce programme, il est impossible que seul b s'affiche. Pour atteindre l'affichage du b, on exécute obligatoirement l'affichage du a avant.
- [ ] rien ne s'affiche
  > Incorrect : la première condition étant vérifiée, il y a au moins l'affichage du a ... et même un peu plus !
:::
