# Affichage du contenu d'un fichier CSV

Pour nous échauffer, commençons par ré-écrire la commande `cat` que vous veznez de réaliser en *bash*, mais cette fois ci en *ijava*.

## `void dump(String nomFichier)`

Créer la procédure `dump` qui en affiche le contenu d'un fichier CSV dont le nom lui est passé en paramètre. Chacun des éléments sera séparé du suivant par une tabulation (`'\t'`) afin de produire un affichage similaire à celui-ci :

```bash
ijava execute DumpCSV
Presidency	President	Wikipedia Entry	Took office	Left office	Party	Portrait	Thumbnail	Home State	
1	George Washington	http://en.wikipedia.org/wiki/George_Washington	30/04/1789	4/03/1797	Independent	GeorgeWashington.jpg	thmb_GeorgeWashington.jpg	Virginia	
2	John Adams	http://en.wikipedia.org/wiki/John_Adams	4/03/1797	4/03/1801	Federalist 	JohnAdams.jpg	thmb_JohnAdams.jpg	Massachusetts	
3	Thomas Jefferson	http://en.wikipedia.org/wiki/Thomas_Jefferson	4/03/1801	4/03/1809	Democratic-Republican	Thomasjefferson.gif	thmb_Thomasjefferson.gif	Virginia	
4	James Madison	http://en.wikipedia.org/wiki/James_Madison	4/03/1809	4/03/1817	Democratic-Republican	JamesMadison.gif	thmb_JamesMadison.gif	irginia	
5	James Monroe	http://en.wikipedia.org/wiki/James_Monroe	4/03/1817	4/03/1825	Democratic-Republican	JamesMonroe.gif	thmb_JamesMonroe.gif	Virginia	
6	John Quincy Adams	http://en.wikipedia.org/wiki/John_Quincy_Adams	4/03/1825	4/03/1829	Democratic-Republican/National Republican	JohnQuincyAdams.gif	thmb_JohnQuincyAdams.gif	Massachusetts	
7	Andrew Jackson	http://en.wikipedia.org/wiki/Andrew_Jackson	4/03/1829	4/03/1837	Democratic	Andrew_jackson_head.gif	thmb_Andrew_jackson_head.gif	ennessee	
(...)
```
