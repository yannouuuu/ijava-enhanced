# Répétitions à compteur : for


::: question
**Quelle est la syntaxe correcte d'une boucle for de base en Java ?**

- [x] for (initialisation; condition; accumulation) { }
  > Parfait : trois expressions, l'initialisation exécutée une seule fois, la condition en début de chaque tour de boucle et à l'inverse l'accumulation en fin de chaque tour de boucle 
- [ ] for [initialisation; condition; incrémentation] { }
  > Ah non, les étapes importantes sont entourées de parenthèses, pas d'accolades ! Ces dernières sont réservées aux blocs d'instructions.
- [ ] for (condition) { }
  > Heu, c'est un peu court ... ceci correspondra à un autre type de boucle ;)
- [ ] for (initialisation, condition, incrémentation) { }
  > Attention : ce qui sépare vos expressions ce ne sont pas des virgules, mais des points-virgules (car chaque expression correspond à une instruction).
:::

::: question
**Que va afficher le code suivant : for (int i = 0; i < 5; i=i+1) { print(i + " "); }**

- [ ] 1 2 3 4 5
  > Le compteur étant initialisé à 0, sa première valeur sera donc 0 ...
- [ ] 0 1 2 3 4 5
  > Attention : la condition indique que le compteur doit être *strictement* inférieur à 5, du coup, lorsque i vaudra 5, la condition sera fausse le l'on sortira de la boucle. Le 5 ne sera donc pas affiché
- [x] 0 1 2 3 4
  > Exactement, on part de 0 inclus à 5 exclus :)
- [ ] 1 2 3 4
  > Aïe, aïe, aïe ... inattention : on initialise le compteur à 0, pas à 1 !
:::

::: question
**Quelle est la valeur de i à la fin de cette boucle ? for (int i = 10; i > 0; i=i-1) { }**

- [ ] -1
  > Nop, la condition indique que 0 est exclus, donc impossible d'atteindre -1.
- [ ] 10
  > Heu ... le compteur est initialisé à 10 et décrémenté à chaque tour de boucle. Comme 10 > 0, on fait au moins un tour et le compteur passe donc à 9. 
- [ ] 1
  > Si i vaut 1 et que la condition i > 0 est évaluée, elle vaudra vrai. On fera donc un nouveau tour de boucle et l'on décrémentera une fois le compteur l'amenant à 0 !
- [x] 0
  > Parfaitement : le dernier tour de boucle i vaut 1, la condition est validée et à la fin de ce tour, i est décrémenté et tombe à 0, du coup, la condition est fausse et l'on sort de la boucle.
:::

::: question
**Que fait cette boucle : for (int i = 0; i < 10; i = i + 2) {print(i + " "); }**

- [ ] Affiche tous les nombres de 0 à 10
  > Attention : la mise à jour du compteur est effectué par pas de 2 et pas 1 !
- [x] Affiche les nombres pairs de 0 à 8
  > Tout à fait : on part de 0, et on incrémente de deux en deux, donc : 0, 2, 4, 6 et 8 (car 10 est exclus !)
- [ ] Affiche les nombres impairs de 0 à 9
  > Perdu : dès le premier tour de boucle 0 s'affiche (et 0 n'est pas impair).
- [ ] Affiche les nombres pairs de 0 à 10
  > Presque ... mais attention la condition est i < 10, donc lorsque i vaut 10, la condition sera fausse, on sort donc de la boucle et impossible d'afficher 10.
:::
