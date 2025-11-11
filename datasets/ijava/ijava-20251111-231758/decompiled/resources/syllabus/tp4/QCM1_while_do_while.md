# Répétitions à évènements : while et do while


::: question
**Quelle est la différence entre while et do while ?**

- [x] while test la condition en début de boucle et do while en fin
  > Oui, exactement !
- [ ] while test la condition en fin de boucle et do while en début
  > Incorrect, c'est exactement l'inverse :(
- [ ] aucune différence c'est le même type de boucle
  > Certes, ce sont toutes deux des répétitions à évènement, mais pourquoi en existe-t-il deux si elles n'ont aucune différence ?
- [ ] do while est beaucoup moins utilisée que while
  > Certes, mais ce n'est pas la différence la plus importante sur la nature de ces répétitions !
:::

::: question
**int cpt=0; while (cpt<5) { cpt=cpt+1;} Que vaut cpt à la fin de l'exécution ?**

- [ ] 6
  > Incorrect car lorsque cpt vaut 5, la condition bascule à faux et l'on sort de la boucle.
- [ ] 0
  > Incorrect au début cpt vaut zero, donc 0 < 5 est vrai et l'on entre dans la boucle.
- [ ] 4
  > Incorrect car lorsque cpt vaut 4, 4 < 5 est toujours vrai donc l'on fait un tour de plus !
- [ ] 5
  > Exactement, on itère sur 0, 1, 2, 3, 4 (soit 5 fois) et lorsque cpt vaut 5, on sort de la boucle.
:::

::: question
**int cpt=0; while (cpt<5) { cpt=cpt+1;} L'addition est exécutée combien de fois ?**

- [ ] 0
  > 0 < 5 est vrai donc on fait au moins un tour de boucle ...
- [ ] 6
  > 5 < 5 est faux et cela se produit avant que le compteur atteigne la valeur 6.
- [x] 5
  > Exactement, bien vu car lorsque cpt vaut 5, on sort de la boucle grâce à 5 < 5 qui est faux.
- [ ] 4
  > Hum, testons : 4 < 5 est vrai donc l'on fait un tour de plus !
:::

::: question
**int cpt=0; while (cpt<5) { cpt=cpt-1;} Combien y-a-t-il de tours de boucle ?**

- [ ] 0
  > 0<5 donc on rentre bien dans la boucle et réalisons au moins un tour !
- [x] une "infinité" ou presque
  > Tout à fait, comme on décrémente cpt, on "fuit" vers les valeurs négatives et c'est une boucle infinie qui s'enclenche :(
- [ ] 5
  > Non, non, bien plus que cela, regardez attentivement comment le compteur est mis à jour !
- [ ] 4
  > Non, non, bien plus que cela, regardez attentivement comment le compteur est mis à jour !
:::

::: question
**Est-il préférable d'utiliser while ou do while pour du contrôle de saisie ?**

- [ ] while
  > C'est possible d'utiliser while, mais il faut alors initialiser judicieusement la variable présente dans la condition avec une valeur ne perturbant pas le calcul ... c'est donc moins pratique que le do while pour ce type de traitement.
- [x] do while
  > Tout à fait car il faut réaliser une saisie avant de pouvoir la vérifier.
- [ ] peu importe
  > Incorrect, do while est plus appropriée.
- [ ] for 
  > ARGL !!! La boucle for est à utiliser lorsque l'on connaît à priori le nombre d'itérations !
:::
