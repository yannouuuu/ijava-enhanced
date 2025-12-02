# Notion de fonction


::: question
**Une fonction permet de ...**

- [x] d'enrichir le langage en lui ajoutant une nouvelle fonctionnalité
  > Tout à fait, c'est ce qui permet de modéliser le problème que l'on traite et donner du sens aux algorithmes.
- [ ] d'exprimer des calculs mathématiques
  > Alors en mathématiques oui, mais là en informatique, ce n'est pas ce qui est le plus utile (même si c'est bien sûr possible)
- [ ] disposer de nouvelles responsabilités
  > Certes, si l'on parle d'une fonction politique, mais la notion de responsabilité aura un autre sens en informatique.
- [ ] déplacer du code ailleurs
  > D'une certaine manière oui, mais ce n'est pas la réponse la plus précise parmi celles proposées.
:::

::: question
**Quel est le type de retour de : `int présent(String texte, char lettre)`**

- [ ] texte
  > Heu ... ceci n'est pas un type !
- [x] int
  > Tout à fait, cela correspond à la première information indiquée.
- [ ] boolean
  > Ce n'est pas ce qui est déclaré, même si cela aurait peut-être pu faire sens.
- [ ] char
  > Ah non, ça c'est le type d'un des paramètres de la fonction.
:::

::: question
**Combien de paramètre(s) a cette fontion : `int présent(String texte, char lettre)`**

- [ ] 0
  > Hum, il faudrait que cela soit `présent()` pour que cette réponse soit acceptable.
- [ ] 1
  > Hum, il faudrait que cela soit `présent(<type>)` pour que cette réponse soit acceptable.
- [x] 2
  > Exactement ! Les paramètres sont : `String texte` et `char lettre`.
- [ ] 3
  > Hum, il faudrait que cela soit `présent(<type>, <type>, <type>)` pour que cette réponse soit acceptable.
:::

::: question
**Une signature de fonction est ...**

- [ ] la description du traitement réalisée par la fonction.
  > Incorrect : ceci correspondrait plutôt au corps de la fonction !
- [ ] la documentation technique détaillant ce que fait la fonction.
  > Non, ceci correspondrait plutôt à la Javadoc que nous verrons plus tard ...
- [x] le type de retour, le nom de la fonction et la liste des paramètres.
  > Tout a fait !
- [ ] une trace laissée par la fonction lors de son appel.
  > Ah, cela pourrait être amusant, mais cela n'existe pas :)
:::

::: question
**Le corps d'une fonction est ... **

- [ ] ce qui l'a fait vivre.
  > Amusant, mais soyons sérieux 2mn :p
- [x] le programme exprimant le traitement réalisé.
  > Tout à fait : bien joué !
- [ ] tout ce qu'il faut écrire pour la définir.
  > Non : le corps de la fonction ne contient pas sa signature ...
- [ ] le nom de la fonction et la liste des valeurs qu'elle doit traiter.
  > Non, ceci correspondrait à l'appel de la fonction.
:::

::: question
**En supposant que la fonction `void saluer(String messsage)` existe, quel appel est valide ?**

- [ ] saluer{"Bonjour"};
  > Non, les accolades permettent de définir un bloc d'instruction : elles ne sont pas utilisées pour identifier les paramètres d'une fonction.
- [ ] saluer(String "Bonjour");
  > Argl ... pourquoi repréciser le type alors qu'il est déjà défini au niveau de la signature ? C'est totalement inutile. Et faux !
- [ ] println(saluer("Bonjour"));
  > Alors `void` cela veut dire `vide`, donc ici il n'y a pas de résultat qui est retourné, donc on ne peut réaliser un appel à un endroit nécessitant d'avoir une donnée.
- [x] saluer("Bonjour");
  > Exactement : juste le nom de la fonction et la donnée qu'elle doit traiter.
:::
