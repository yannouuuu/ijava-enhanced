# Gestion de parc de véhicules

On s’intéresse maintenant à la gestion (très simplifiée) de parcs de véhicules.

## Représentation d’un parc de véhicules

Considérons un parc de véhicules. Définissez le type `Parc` constitué de deux champs :
* `vehicules` qui est un tableau à une dimension de véhicules;
* `nbVehicules` de type entier qui permet de stocker le nombre effectif de véhicules dans le parc.

## Constructeur pour un parc de véhicules

Créez maintenant un fichier `GestionParc.java` dans laquelle vous allez créer les fonctions de gestion de parcs de voitures.

Pour simplifier les choses, déclarer une constante globale NB_MAX initialisée à 100 qui correspondra à la capacité du parc.

* Définissez la fonction `Parc newParc()` qui crée un parc de véhicules pouvant contenir le nombre maximal de véhicules, mais qui est pour l’instant vide. 
* Définissez une fonction `test_newParc()` ... effectuant les assertions pertinentes !

## Ajout d’un véhicule

* Définissez la fonction `boolean ajouter(Parc parking, Vehicule v)` qui ajoute un nouveau véhicule au parc de véhicules. Si le parking est déjà plein, cette fonction n’ajoute pas le véhicule, laisse donc le parc inchangé et retourne la valeur `false`. A l’inverse, si le véhicule a été ajouté, la fonction retourne `true`.
* Définissez une fonction `test_ajouter()` ... effectuant les assertions pertinentes !

## Représentation d’un parc de véhicules

* Définissez la fonction `String toString (Parc p)` qui crée une chaîne de caractères permettant de représenter un parc de véhicules. La fonction devra valider le test suivant :
```java
void testToStringParc(){
    Parc p = newParc();
    Vehicule v1 = newVehicule("309DF21", Gabarit.CITADINE);
    Vehicule v2 = newVehicule("204XZ99", Gabarit.SUV);
    ajouterVehicule(p, v1);
    ajouterVehicule(p, v2);
    assertEquals("CITADINE (309DF21)\nSUV (204XZ99)\n", toString(p));
}
```

## Nombre de voitures d’un certain gabarit

* Définissez la fonction `int nbVoitures(Parc p, Gabarit g) qui retourne le nombre de véhicules d’un certain gabarit présents dans le parc de voitures.

## Fusion de deux parcs de véhicules

* Définissez la fonction `boolean fusionParcs (Parc p1, Parc p2)` qui déplace un maximum de véhicules du parc `p2` au parc `p1` dans la limite des places disponibles de `p1`. C’est-à-dire que si `p2` contient plus de véhicules que ne peut
en contenir `p1`, on déplace le maximum de véhicule dans `p1` et on s’arrange pour que `p2` reste bien organisé (ie. sans trou).

## Suppression d’un véhicule

* Définissez la fonction `boolean enlever(Parc p, String immatriculation)` qui enlève du parc `p` le véhicule ayant la plaque d'`immatriculation`. Si le véhicule n'est pas présent dans le parc alors la fonction retourne `false`, sinon `true`.

**Attention, les véhicules doivent toujours être sur le début du tableau (pas de trou dans le tableau).**