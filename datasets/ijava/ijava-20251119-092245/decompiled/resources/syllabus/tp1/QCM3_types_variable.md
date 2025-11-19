# Notions de type, variable et affectation


::: question
**Quelle instruction déclare une variable entière nommée age et l'initialise à 30 ?**

- [ ] age = 30; 
  > age n'est pas déclarée, juste affecté !
- [x] int age = 30; 
  > Déclaration et initialisation correctes : le type, le nom et la valeur.
- [ ] int age; age = 30; 
  > Presque, mais là, il y séparation entre l'étape de déclaration et ensuite une affectation.
- [ ] var age = 30; 
  > Correct en Java 10+ ... mais nécessite une inférence de type : nous ne ferons pas cela au S1 !
:::

::: question
**Quel type de données utiliseriez-vous pour représenter une seule lettre ?**

- [ ] String 
  > Ce type est pertinent pour représenter plusieurs lettres ...
- [ ] int 
  > int = integer, on l'utilise pour représenter des nombres entiers.
- [x] char 
  > Effectivement, le type char permet de représenter un seul caractère.
- [ ] boolean 
  > boolean = booléen, on l'utilise pour reprénter des valeurs vrai (true) ou fausse (false).
:::

::: question
**Quel est le type de données du résultat de length("Bonjour") ?**

- [ ] String 
  > length() retourne un nombre pas une chaîne de caractères.
- [ ] char 
  > char représente un caractère unique et non un nombre.
- [x] int 
  > effectivement car length() retourne la longueur de la chaîne, donc un entier.
- [ ] boolean 
  > boolean représente vrai/faux et non un nombre
:::

::: question
**Laquelle de ces proposition est un nom de variable valide ?**

- [x] maVariable 
  > Tout à fait, est le nommage respecte la convention Java (Camel case).
- [ ] 42variable 
  > Non car un identificateur ne peux commencer par un nombre.
- [ ] $variable 
  > Non car un identificateur ne peux commencer par un caractère spécial (si ce n'est _).
- [ ] ma-variable 
  > Non car les tirets '-' ne sont pas autorisés, contrairement au '_' (underscore).
:::

::: question
**Quel est le résultat de "Bonjour" + " le monde" ?**

- [ ] "Bonjour" + " le monde" 
  > Non, l'expression est évaluée et produit une nouvelle chaîne !
- [x] "Bonjour le monde" 
  > Oui c'est une nouvelle chaîne construite par concaténation de deux autres chaînes.
- [ ] "Bonjourle monde"
  > Attention : il y a un espace avant 'le' dans la seconde chaîne ...
- [ ] L'expression n'est pas valide et produit une erreur. 
  > Ah non, aucune erreur ici, l'expression est valide.
:::

::: question
**Quelle expression teste l'égalité entre deux nombres entiers x et y ?**

- [x] x == y 
  > Correct, l'opérateur d'égalité pour les types primitifs.
- [ ] x = y 
  > Aïe ! ATTENTION : un seul '=' correspond à l'affectation, ce n'est pas un opérateur de comparaison !
- [ ] equals(x, y) 
  > ATTENTION : equals() n'est valide que pour les chaînes de caractères (String) et pas les autres types.
:::
