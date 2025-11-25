# Analyse des mandats présidentiels des USA

Un collègue a récupéré un fichier de données contenant des détails sur les différents présidents américains depuis la création des USA. Ce fichier est encodé sous la forme d'un fichier CSV dont la première ligne indique la nature des différentes colonnes et les suivantes les données des différentes présidences : 
```bash
Presidency,President,Wikipedia Entry,Took office,Left office,Party,Portrait,Thumbnail,Home State
1,George Washington,http://en.wikipedia.org/wiki/George_Washington,30/04/1789,4/03/1797,Independent,GeorgeWashington.jpg,thmb_GeorgeWashington.jpg,Virginia
2,John Adams,http://en.wikipedia.org/wiki/John_Adams,4/03/1797,4/03/1801,Federalist ,JohnAdams.jpg,thmb_JohnAdams.jpg,Massachusetts
3,Thomas Jefferson,http://en.wikipedia.org/wiki/Thomas_Jefferson,4/03/1801,4/03/1809,Democratic-Republican,Thomasjefferson.gif,thmb_Thomasjefferson.gif,Virginia
4,James Madison,http://en.wikipedia.org/wiki/James_Madison,4/03/1809,4/03/1817,Democratic-Republican,JamesMadison.gif,thmb_JamesMadison.gif,Virginia
5,James Monroe,http://en.wikipedia.org/wiki/James_Monroe,4/03/1817,4/03/1825,Democratic-Republican,JamesMonroe.gif,thmb_JamesMonroe.gif,Virginia
6,John Quincy Adams,http://en.wikipedia.org/wiki/John_Quincy_Adams,4/03/1825,4/03/1829,Democratic-Republican/National Republican,JohnQuincyAdams.gif,thmb_JohnQuincyAdams.gif,Massachusetts
```

::: question
**Quel parti a été le plus au pouvoir au début de la démocratie américaine ?**
- [ ] Les indépendants
  > Seul George Washington est listé comme indépendant, donc un seul mandat.
- [ ] Les fédéralistes
  > Un peu comme G. Washington, il n'y a un qu'un représentant des fédéralistes ...
- [x] Les démocrates-républicains
  > Effectivement, sur les 6 premiers mandats, ils ont été au pouvoir durant 4 !
:::

Nous allons nous familiariser avec cette structuration tabulaire des données en mobilisant le type `CSVFile` et en réalisant quelques manipulations classiques de fichiers CSV, puis quelques analyses sur cette source de données en particulier.

Commencez par charger le fichier `USPresident.csv` disponible sur Moodle et sauvegardez-le dans le répertoire du TP actuel. Tapez la commande suivante pour avoir une vue globale sur le contenu de ce fichier :
```bash
cat USPresident.csv 
```
