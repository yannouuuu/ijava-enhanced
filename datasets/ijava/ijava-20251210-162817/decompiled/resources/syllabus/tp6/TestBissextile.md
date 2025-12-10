# Années bissextiles et tests

Dans le TP précédent, vous deviez créer une fonction bissextile capable de dire si une année est bissextile ou non. 

On souhaite automatiser la vérification de la validité de cette fonction en créant des fonctions de test.

Créez le programme `TestBissextile` (cf. commande init) et recopiez le code de la fonction `bissextile` réalisée lors du TP précédent (ou définissez là si ça n’est pas déjà fait !) et ajoutez lui deux fonctions de tests : `test_bissextile_true` et `test_bissextile_false`.

- La fonction `test_bissextile_true` s'assurera que la fonction `bissextile` retourne bien `true`pour ces années qui sont bissextiles : 2000, 2012, 2024, 1600.
- La fonction `test_bissextile_false` s'assurera que la fonction `bissextile` retourne bien `false`pour ces années qui ne sont pas bissextiles : 2013, 2006, 1999, 1000.

Rappel : `void assertEquals(<typeX> valeurAttendue, <typeX> appelDeLaFonctionTestée)` est incontournable pour cet exercice ...