# Modélisation d'un président

Il n'est pas judicieux de travailler directement sur une variable de type `CSVFile`, ne serait-ce que parce que c'est une source de données non modifiable. Et puis, pour la lisibilité du code, ce n'est pas idéal non plus.

Ainsi, on crée généralement des types faisant sens pour notre problème et on initialise leurs valeurs grâce à des données parfois externalisées dans des fichiers.

Pour cette raison, nous allons maintenant créer le type `President` pour ensuite pouvoir faire quelques traitements sur ces présidents américains.

## Création du type `President`

::: question
**Si l'on s'intéresse aux durées de mandats des présidents et à leur parti, quelles sont les informations suffisantes pour le type President ?**
- [ ] le nom, le parti, la date de début de mandat, le rang
  > Presque ! Mais à quoi nous servirait le rang du président ? L'information est de toute façon déjà présente implicitement dans le fichier avec l'odre des lignes.
- [x] le nom, le parti, la date de début et la date de fin de mandat (on ne s'intéresse pas au cas de Grover qui a eu la drôle d'idée de réaliser 2 mandats !)
  > Yep ! A priori, cela suffit pour les traitements envisagés.
- [ ] le nom, le parti, la durée du mandat
  > Cela pourrait éventuellement convenir, mais cela fait déjà une étape de synthèse d'information qui rajoute du traitement. Même si cette piste est pertinente, conserver l'information exacte est plus prudent.
:::

Définissez un type `President` contenant un champs pour son *nom*, un autre pour son *parti* et deux champs pour les dates de *debut* et *fin* de mandat.
