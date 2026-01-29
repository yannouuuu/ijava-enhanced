# Simulation Wa-Tor

On va débuter en créant les quelques types nécessaires pour représenter d'abord les espèces :

```java
enum Espece {
    PLANCTON, THON, REQUIN
}
```

et ensuite les différents types de poissons :

```java
class Poisson {
    Espece espece;
    int gestation;
    int famine;
}
```

Créez ces deux types, puis, parcourez une première fois le code source de `Wator` en essayant d’en comprendre la logique et de vous l’approprier. Vous pouvez remarquer qu’il s’agit d’une version partielle de la simulation, dans laquelle ni la famine ni la reproduction ne sont encore implémenteés (voire quelques bugs !).

Conseil : Pour votre CTP début janvier, vous aurez à étendre l’archive de ConjuGator, déjà disponible sur moodle dans la SAÉ1.2. Nous vous conseillons donc fortement d’effectuer cette étape de familiarisation avec ce code d’ici là (mais pas maintenant!).

## Généralités

Il va maintenant s’agir d’ajouter des fonctionnalités à ce logiciel. Pour ce faire, réfléchissez-bien aux endroits auxquels le code va être modifié et essayez de garder une approche modulaire (notamment en ajoutant des fonctions quand c’est pertinent).

## Amélioration de l’affichage
Améliorer l’affichage en indiquant le nombre de poissons de chaque espèce. Une approche consiste à mettre à jour ces valeurs dynamiquement après chaque ajout ou suppression d’un animal (c’est plus efficace que de tout recompter à chaque fois).

## Ajout de fonctionnalités de comportement

### Famine
Ajouter la fonctionnalité de famine, pour rappel :
— les requins dépensent 1 nourriture à chaque fois que c’est leur tour
— on considère que les thons ne dépensent jamais de nourriture
— tout individu qui arrive à court de nourriture meurt
On fera intervenir la règle de famine avant celle de déplacement. Autrement dit, l’animal ne pourra pas se déplacer
s’il arrive à court de nourriture avant.

### Reproduction
Ajouter la reproduction, pour rappel un poisson se reproduit si sa gestation est arrivée à 0 et qu’il a pu se déplacer.
Un nouveau poisson de son espèce est alors créé sur la case d’où il vient et sa gestation revient alors à la valeur d’origine
de son espèce.
Conseil : appliquer la règle de reproduction après le déplacement (et seulement s’il a eu lieu)

## Une nouvelle espèce
Ajouter une nouvelle espèce : les orques
Les orques se comportent exactement comme les requins avec une différence : en plus des thons, ils attaquent aussi
les requins. Si un orque tente d’aller sur la case d’un requin, il a 50% de chances d’y parvenir et de le manger et 50% de
chances d’échouer, dans ce cas il reste sur sa case.

## Sauvegarde
Ajouter la possibilité tous les 100 tours de sauvegarder la simulation pour la recharger ultérieurement (on essaiera de sauvegarder directement les poissons, leur état et leur localisation plutôt que tout le plateau). On partira ici du principe que la configuration de la simulation (NOMRE_COLS, NOMBRE_LIGNES ...) est celle par défaut et qu’il n’est pas la peine de la sauvegarder.

## Prolongements

* Proposer à l’utilisateur les caractéristiques par défaut de la simulation ou de saisir celles qu’il souhaite
* Faire un environnement torique (les bords se rejoignent)
* Permettez à l’utisateureur de choisir sa configuration de la similuation et incluez la dans la sauvegarde
