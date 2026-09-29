# Maquiller un caractère dans une chaîne

Cet exercice consiste à maquiller certains caractères d'une chaînes de caractères ...

```java
class Maquillage extends Program {
    void algorithm(){
        String texte;
        char ancien, nouveau;
        print("Veuillez saisir votre texte : ");
        texte = readString();
        print("Caractère recherché : ");
        ancien = readChar();
        print("Nouveau caractère : ");
        nouveau = readChar();
```

::: question
**Existe-t-il un opérateur ou une fonction permettant de modifier un caractère dans une chaîne de caractères?**

- [x] Non
  > Effectivement, une chaîne est comme un nombre : on peut lire sa valeur, mais impossible de la modifier.
- [ ] Oui avec l'opérateur d'affectation
  > Non, l'affectation permet d'affecteur une nouvelle chaîne à une variable mais ne permet pas de modifier une chaîne existante !
- [ ] Oui avec la fonction `chartAt`
  > La fonction `charAt`permet de *copier* un caractère à une position donnée mais ne modifie en rien la chaîne initiale.
- [ ] Aucune idée
  > ATTENTION : il est urgent de relire le cours, toutes les informations importantes sont présentes dedans !
:::


Compléter le programme `Maquillage` afin qu’il affiche lettre par lettre le `texte` initial en maquillant la lettre recherchée par la nouvelle.

Voici quelques scénarios à vérifier :
```bash
~/ijava2/tp2> ijava execute Maquillage
Veuillez saisir votre texte : HELLo
Caractère recherché : o
Nouveau caractère : O
HELLO
~/ijava2/tp2> ijava execute Maquillage
Veuillez saisir votre texte : Blabla
Caractère recherché : i
Nouveau caractère : u
Blabla
~/ijava2/tp2> ijava execute Maquillage
Veuillez saisir votre texte : oooTITREooo
Caractère recherché : o
Nouveau caractère : x
xxxTITRExxx
```
