# Gestion d'un parc de voitures

Le responsable de l'entreprise *EasyPark* vous contacte car il souhaite informatisé la gestion des parkings avec lesquels il est partenaire. Pour l'instant, il souhaite juste la création d'un *POC* (*Proof Of Concept* vous explique-t-il parce qu'en *english*, c'est *better* paraît-il ...) et du coup, il simplifie sa demande.

L'objectif principal est de pouvoir gérer des voitures présentes dans différents parkings et parfois d'essayer de faire un peu de place en remplissant totalement un des parkings en déplaçant le maximum de voitures présentes du parking le plus proche.

Ainsi, il vous indique qu'il n'a pas besoin de plus pour l'instant que de repérer les voitures par leur plaque d'immatriculation et leur gabarit (citadine, SUV, camionnette). Pour ce qui est des parkings, on s'intéresse juste au nombre de place en supposant que quelque soit la taille de la voiture, cela rentre dans une place quelconque du parking.

::: question
**Quel outil vous semble le plus judicieux pour représenter le gabarit des voitures ?**

- [ ] Une classe définissant un nouveau type `Gabarit` avec trois champs booléens pour chaque gabarit
  > Si on fait cela, on va avoir un champs `Gabarit` dans notre voiture et pour accéder à sa valeur, on aura une notation complexe : voiture.gabarit.citadine et surtout, on ne peut accéder facilement au gabarit sans devoir tester 3 champs !
- [x] Une énumération `Gabarit` avec trois valeurs pour chacun des gabarits
  > Exactement, c'est le choix le plus simple permettant de nommer précisément cette information et de garantir qu'il ne sera pas possible autre chose que ces 3 valeurs.
- [ ] Une simple chaîne de caractères prenant les valeurs "citadine", "SUV" ou "camionette"
  > Ok, et du coup, si on se trompe et que l'on met d'autres chaînes de caractères que les 3 prévues, comment allons-nous gérer cela ? En protégeant tout le code accédant à ces chaînes ?!
- [ ] Un simple entier, ainsi si on a 0=citadine, 1=SUV et 2=camionette, on peut facilement les comparer.
  > C'est un peu comme la solution précédente et pour l'instant, personne n'a demandé à comparer les gabarits donc pourquoi le faîtes-vous ?!
:::

Passons maintenant au type représentant une voiture !

::: question
**Combien de champs sont suffisant pour le type `Voiture`?**

- [ ] La plaque d'immatriculation (String), le gabarit (Gabarit) et le parking (String).
  > Hum, le parking n'est pas une propriété de la voiture. C'est plutôt le parkling qui va contenir les voitures.
- [x] La plaque d'immatriculation (String) et le gabarit (Gabarit).
  > Tout à fait : ni plus, ni moins !
- [ ] La plaque d'immatriculation (int) et le gabarit (Gabarit).
  > La plaque d'immatriculation comme entier ? Toutes les plaques actuelles contiennent des lettres, on ne peut la représenter avec un simple entier.
- [ ] La plaque d'immatriculation (énumération) et le gabarit (Gabarit).
  > Oups, une énumération pour la plaque d'immatriculation ?! Voilà qui risque de faire un fichier très conséquent. Et comment fera-t-on lorsque nous aurons de nouvelles immatriculations ?! Non, les énumérations sont utilisés lorsque l'on a un ensemble restreint de constantes de même nature ...
:::

Maintenant que nous avons une vision plus claire de la représentation d'une voiture :
* créez l'énumération `Gabarit` contenant les 3 valeurs évoquées précédemment.
* créez le nouveau type `Voiture` avec deux champs `immatriculation` et `gabarit`.
