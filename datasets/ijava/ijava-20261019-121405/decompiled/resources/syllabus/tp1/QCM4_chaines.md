# Type String et fonctions associées


::: question
**Quel est le résultat de l'exécution de println(15 / length("papa") + 2) ?**

- [ ] 5.75
  > Non car c'est une division entière et une addition donc le résultat est entier.
- [x] 5
  > Oui car 15 / 4 donne 3 et on ajoute ensuite 2 (division prioritaire).
- [ ] 6
  > Non, pour cela il aurait fallu 16 / 4 + 2.
- [ ] L'expression n'est pas valide et produit une erreur.
  > Pas du tout, l'expression est valide.
:::

::: question
**Que retourne charAt("Java", 2) ?**

- [ ] 'J' 
  > Non : il aurait fallu passer 0 comme indice pour avoir 'J'.
- [ ] 'a' 
  > Non : il aurait fallu passer 1 ou 3 comme indice pour avoir 'a'.
- [x] 'v' 
  > Tout à fait, car la première lettre a pour indice 0.
- [ ] L'expression n'est pas valide et produit une erreur. 
  > Si elle est valide, l'index est dans la chaîne. Si il valait 0 ou 7, là il y aurait eu une erreur.
:::

::: question
**Quel est le résultat de substring("exemple", 0, 3) ?**

- [ ] ple 
  > Non car 0 correspond à l'indice de début et 3 à celui de fin exclus.
- [ ] ex 
  > Non : pour obtenir ce résultat il aurait fallu donner les indices 0 et 2.
- [x] exe 
  > Correct ! On copie les caractères depuis l'indice 0 jusqu'à l'indice 3 exclus.
- [ ] exam 
  > Non : pour obtenir ce résultat il aurait fallu donner les indices 0 et 4.
:::

::: question
**Quel est le résultat de equals("test", "Test") ?**

- [ ] true 
  > Ah non : pour être égales deux chaînes doivent avoir exactement les mêmes caractères (et dans le même ordre) !
- [x] false 
  > Tout à fait : les chaînes ne sont pas identiques à cause de leur premier caractère.
- [ ] L'expression n'est pas valide et produit une erreur.  
  > Non, non, cette expression est valide.
- [ ] null 
  > Non car equals retourne un booléen.
:::

::: question
**Que retourne equals("abc" "abc") ?**

- [ ] false 
  > Non car il y a une erreur de syntaxe ...
- [ ] true 
  > Non car il y a une erreur de syntaxe ... mais sans cela cela aurait effectivement retourné true ;)
- [ ] null 
  > Non car equals renvoie un booléen ... mais il y a de toute façon une erreur de syntaxe.
- [x] Une erreur 
  > Effectivement ! Il manque la virgule pour séparer les deux arguments : equals("abc", "abc").
:::
