# Menu

On vous demande de réaliser un menu en mode texte pour une application des années 80 (1980 !).

Avant cela votre collègue vous provoque en vous demandant si vous vous rappellez comment afficher un guillement ...

::: question
**Quelle proposition permet d'afficher "YES" à l'écran ?**

- [ ] print("YES");
  > Alors, YES va s'afficher à l'écran, mais sans les guillements demandés ...
- [ ] print(""YES"");
  > Incorrect : ceci va surtout générer une erreur de compilation !
- [x] print("\"YES\"");
  > Bravo, pas évident car à peine évoqué en amphi ;)
- [ ] c'est impossible
  > Cela serait très embêtant que l'on ne puisse afficher de guillements, non ? ...
:::

Le client vous donne cette maquette écran :
```bash
Bienvenue dans le SuperLogicielDeLanTroisMille

1. Ouvrir un document existant.
2. Créer un nouveau document.
3. Enregistrer le document courant.
4. Quitter ce magnifique logiciel.

Veuillez entrer votre choix: 4
Vous avez choisi: "Quitter ce magnifique logiciel."
```
Le client vous demande de respecter *exactement* cette mise en page (à l'espace et retour à la ligne prêt !). Votre collègue vous conseille *fortement* de copier/coller la maquette et ne pas saisir le texte manuellement dans votre programme (au risque d'avoir des coquilles et tests invalides laborieux à déboguer).

Connaissant la malice des usagers, à votre interrogation sur que faire si un nombre non présent dans la liste est saisi, le client vous indique qu'il faudra alors afficher `"Un mauvais chiffre !"`.

Comme d'habitude, testez d'abord manuellement votre programme avant d'utiliser la commande d'ijava.