# Remplacer un caractère dans une chaîne

Cet exercice consiste à manipuler des chaînes de caractères.

```java
class Remplacement extends Program {
    void algorithm(){
        String texte;
        char ancien, nouveau;
        print("Veuillez saisir votre texte : ");
        texte = readString();
        print("Caractère à remplacer : ");
        ancien = readChar();
        print("Caractère de remplacement : ");
        nouveau = readChar();
```

::: question
**Que fait le début de ce programme ?**

- [x] La saisie des données à traiter
  > Effectivement, quelques affichages et la saisie du texte et des caractères nécessaires pour traiter ensuite le problème
- [ ] Le remplacement du caractère dans le texte
  > Où voyez-vous une modification sur le `texte` saisit dans le code ci-dessus ?!
- [ ] Des affichages
  > Certes, mais aussi des saisies. Celles sur lesquels porteront le traitement à écrire.
- [ ] Aucune idée
  > Hum, il est urgent de relire le cours, retravaillez les TD et finir les TP précédents !
:::


Compléter le programme `Remplacement` afin qu’il affiche le `texte` initial dans lequelle toutes les occurrences de l'`ancien` caractère sont remplacées par le `nouveau`. 

Voici quelques scénarios à vérifier :
```bash
~/ijava2/tp2> ijava execute Remplacement
Veuillez saisir votre texte : HELLo
Caractère à remplacer : o
Caractère de remplacement : O
HELLO
~/ijava2/tp2> ijava execute Remplacement
Veuillez saisir votre texte : Blabla
Caractère à remplacer : i
Caractère de remplacement : u
Blabla
~/ijava2/tp2> ijava execute Remplacement
Veuillez saisir votre texte : oooTITREooo
Caractère à remplacer : o
Caractère de remplacement : x
xxxTITRExxx
```
