# DessineIUT

## Un peu d'ASCII Art

On souhaite réaliser un programme `DessineIUT` permettant de produire les affichages suivant en fonction d'une `taille` saisie auprès de l'usager:

```bash
taille : **5**

IIIII
  I
  I
  I
IIIII

U   U
U   U
U   U
U   U
UUUUU

TTTTT
  T
  T
  T
  T
```

```bash
taille : **3**

III
 I
III

U U
U U
UUU

TTT
 T
 T
```

Chacune des lettres I, U et T est ici dessinée dans un carré dont la taille est le nombre saisi par l'usager.

Pour y parvenir, on va découper le programme en plusieurs procédures {rappel : ce sont des fonctions qui ne retournent rien, c'est-à-dire que leur type de retour est `void`, comme pour `algorithm`}.  

Vous devez tester séparément chacune des procédures. C'est à dire, après avoir écrit une procédure, appelez-la plusieurs fois dans la fonction principale `algorithm` avec des paramètres différents pour vous assurer qu'elle fait bien ce qu'on attend.

Si on analyse chacune des lignes de chacune des lettres, on remarque qu'il existe en fait 3 types de lignes :
- les lignes pleines, par exemple
```bash
TTTTT
```
On peut ici reprendre la procédure dessineLigne vue en TD :
```java
void dessineLigne(int n, char c) {
    for (int i=0; i<n; i=i+1) {
        print(c);
    }
    print("\n');
}      
```
- les lignes avec un caractère au milieu du carré, par exemple
```bash
 I
```
Réalisez une procédure `void dessineMilieu(int n, char c)` qui dessine le caractère reçu `c` au milieu d'une ligne de taille `n`. On peut remarquer que dans ce cas, il n'est pas nécessaire d'afficher les espaces en fin de ligne.
- les lignes avec des caractères aux extrêmités, par exemple
```bash
U   U
```
Réalisez une procédure qui correspont à cela.    
- Maintenant que l'on a nos briques de base, on va pouvoir dessiner chacune de nos lettres en les assemblant. Créez trois procédures, `dessinerI`,  `dessinerU`,  `dessinerT`, paramétrées par un caractère et une taille. 
- Il ne nous reste plus qu'à définir `void algorithm()` en saisissant auprès de l'utilisateur la taille souhaitée et à appeler nos procédures de manière à obtenir l'affichage successif des 3 lettres I, U et T en utilisant le caractère correspondant à la lettre qu'on dessine ('I' pour dessiner le I, etc.).

## Étapes à suibre
- `ijava init Iut`
- *complétez le corps des fonctions du fichier `Iut.java`*
- `ijava compile Iut.java`
- `ijava execute Iut`
- `ijava test Iut`

---
