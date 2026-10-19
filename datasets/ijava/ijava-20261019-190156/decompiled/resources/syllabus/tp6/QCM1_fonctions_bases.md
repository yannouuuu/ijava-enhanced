# Notion de fonction


::: question
**Une fonction permet de ...**

- [x] d'enrichir le langage en lui ajoutant une nouvelle fonctionnalité
  > Tout à fait, c'est ce qui permet de modéliser le problème que l'on traite et donner du sens aux algorithmes.
- [ ] d'exprimer des calculs mathématiques
  > Alors en mathématiques oui, mais là en informatique, ce n'est pas ce qui est le plus utile (même si c'est bien sûr possible).
- [ ] disposer de nouvelles responsabilités
  > Certes, si l'on parle d'une fonction politique, mais la notion de responsabilité aura un autre sens en informatique.
- [ ] déplacer du code ailleurs
  > D'une certaine manière oui, mais ce n'est pas la réponse la plus précise parmi celles proposées.
:::

::: question
**Quel est le type de retour de : <code>int présent(String texte, char lettre)</code>**

- [ ] <code>texte</code>
  > Heu ... ceci n'est pas un type !
- [x] <code>int</code>
  > Tout à fait, cela correspond à la première information indiquée.
- [ ] <code>boolean</code>
  > Ce n'est pas ce qui est déclaré, même si cela aurait pu faire sens (si l'on ne s'intéresse pas à la position de la lettre dans le texte).
- [ ] <code>char</code>
  > Ah non, ça c'est le type d'un des paramètres de la fonction.
:::

::: question
**Combien de paramètre(s) a cette fontion : <code>int présent(String texte, char lettre)</code>**

- [ ] 0
  > Hum, il faudrait que cela soit <code>présent()</code> pour que cette réponse soit acceptable.
- [ ] 1
  > Hum, il faudrait que cela soit <code>présent(<paramètre>)</code> pour que cette réponse soit acceptable.
- [x] 2
  > Exactement ! Les paramètres sont : <code>String texte</code> et <code>char lettre</code>.
- [ ] 3
  > Hum, il faudrait que cela soit <code>présent(<paramètre>, <parametre>, <paramettre>)</code> pour que cette réponse soit acceptable.
:::

::: question
**Une signature de fonction est ...**

- [ ] la description du traitement réalisée par la fonction.
  > Incorrect : ceci correspondrait plutôt au corps de la fonction !
- [ ] la documentation technique détaillant ce que fait la fonction.
  > Non, ceci correspondrait plutôt à la Javadoc que nous verrons plus tard ...
- [x] son type de retour, son nom et sa liste de paramètres.
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
**En supposant que la fonction <code>void saluer(String messsage)</code> existe, quel appel est valide ?**

- [ ] <code>saluer{"Bonjour"};</code>
  > Non, les accolades permettent de définir un bloc d'instruction : elles ne sont pas utilisées pour identifier les paramètres d'une fonction.
- [ ] <code>saluer(String "Bonjour");</code>
  > Argl ... pourquoi repréciser le type alors qu'il est déjà défini au niveau de la signature ? C'est totalement inutile. Et faux !
- [ ] <code>println(saluer("Bonjour"));</code>
  > Alors <code>void</code> cela veut dire vide, donc ici il n'y a pas de résultat qui est retourné, donc on ne peut réaliser un appel à un endroit nécessitant d'avoir une donnée.
- [x] <code>saluer("Bonjour");</code>
  > Exactement : juste le nom de la fonction et la donnée qu'elle doit traiter.
:::
