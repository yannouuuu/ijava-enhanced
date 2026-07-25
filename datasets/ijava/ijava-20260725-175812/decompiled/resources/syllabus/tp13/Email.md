# Abonnement à une newsletter

L'entreprise *ProchainNuage* vous contacte car elle a constaté que sa liste de diffusion est polluée par des adresses mails ... qui ne sont pas des adresses mails valides. Le problème le plus courant vient de personnes entrant un faux email afin d'obtenir la réduction pour une première commande. Ces pseudo emails sont tellement faux qu'ils ne contiennent même pas le caractère `@` dans la majorité des cas !

La directrice vous demande donc d'écrire une fonction déterminant si un "mail" donné contient au moins le caractère arrobas ou pas. Par contre, la directrice ayant passé ses années d'études à programmer en LISP, elle tient absolument à ce que vous écriviez cela de manière récursive : *"c'est tellement plus beau !"* vous dit-elle ...

## Leçon 0 de programmation fonctionnelle

*"Non seulement c'est beau, mais en plus c'est concis ! Quoi de mieux que le [rasoir d'Ockham](https://fr.wikipedia.org/wiki/Rasoir_d'Ockham) ?"*, vous lance-t-elle. Vous n'avez pas le temps d'esquisser une réponse qu'elle vous donne les conseils qu'elles avaient reçu de son enseignante de TD à l'époque :

**Règles d'or pour penser de manière récursive :**
* **aux cas d'arrêt, d'abord s'intéresser** : identifiez d'abord les cas d'arrêts, car ce sont les situations où la donnée sur laquelle porte la récursivité est réduite à sa plus simple expression ou presque. Du coup, la réponse à apporter est évidente !
* **aux cas d'arrêt, toujours se ramener** : intéressez-vous ensuite à l'appel récursif, c'est-à-dire au moment où l'on rappelle la fonction que l'on est en train de définir, mais en effectuant juste une petite étape du calcul et au fait de faire "diminuer" la donnée sur laquelle porte la récursivité (ainsi on se rapproche du ou des cas d'arrêt).

Alors que vous comenciez à perdre le fil en pensant à votre chef qui vous a encoyé sur cette mission un peu tordue, elle vous prend de cours en vous demandant :

::: question
**Combien de cas d'arrêt à votre avis sur cette fonction ?**
- [ ] 0
  > Sérieusement ? Donc, l'idée est d'écrire une boucle infinie  ?! ...
- [ ] 1
  >  Comment ? Une fonction booléene qui retournerait toujours la même chose ? Comme oui c'est toujours un email valide ou au contraire faux, quel que soit le paramètre ? Ah j'ai compris ... c'était une blague ?!
- [x] 2
  > Effectivement : pour une fonction booléenne il est nécessaire d'avoir au moins 2 cas d'arrêt, après selon le problème, il est possible d'en avoir plus, mais pour celui-ci cela sera déjà bien suffisant.
:::

Poursuivant son interrogatoire, elle enchaîne en vous disant "concentre-toi sur les cas les plus simples, sachant qu'on travaille sur une chaîne de caractères ... "

::: question
**... quelle est la chaîne la plus simple ?**
- [ ] Une chaîne non initialisée
  > Alors certes, `null` veut dire qu'une chaîne n'est pas initialisée, mais pensez-tu vraiment que le compilateur autorise à donner en argument une valeur `null`? (c'est une question rhétorique :p)
- [x] Une chaîne vide
  >  Oui, c'est cela : quoi de plus simple qu'une chaînes de caractères qui n'en contient aucun ? :)
- [ ] Une chaîne en ASCII
  > Heu, est-on vraiment sur les questions d'encodage des caractères là ?
:::

Cette fonction étant booléene, il y des situations ou elle retourne `true`, lorsque le caractère `@` est présent, et `false` sinon. Les cas d'arrêts vont donc refléter ces deux situations. 

::: question
**Alors, que faut-il vérifier au niveau des cas d'arrêts ?**
- [ ] lorsqu'il n'y a qu'un caractère, si c'est celui cherché alors `true` sinon `false`.
  > Cette approche fonctionnerait si l'on ne devait pas traiter la chaîne vide, et cela n'applique pas totalement le rasoir d'Ockham permettant d'affiner encore les deux cas d'arrêt.
- [x] si la chaine est vide, alors `false` (quoi de plus simpel ?), et sinon (c'est que l'on a au moins un caractère dans la chaîne !) on vérifie que le premier caractère est celui cherché et si c'est le cas, alors `true`.
  > Exactement, et cela sera très souvent ce type de raisonnement pour les récursivité portant sur des chaînes de caractères. 
:::

A l'aide de tout cela, écrivez maintenant `boolean estPeutEtreUnEmail(String email)`.

