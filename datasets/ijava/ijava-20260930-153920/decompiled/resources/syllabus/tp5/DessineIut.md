# Dessine moi des lettres

On souhaite réaliser un programme `DessineIUT` permettant de produire les affichages suivant en fonction d'une `taille` (nécessairement impaire) saisie auprès de l'usager:

```bash
Taille : 5

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
Taille : 3

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

Comme nous avons déjà quelques primitives pour 

Si on analyse les différentes lignes de chacune des lettres, on remarque qu'il existe en fait seulement 3 types de lignes :
- **les lignes pleines**
```java
void dessineLigne(char symbole, int taille)
```
- **les lignes milieux**
```java
void dessineMilieu(char symbole, int taille)
```
- **les lignes bordures** (avec des caractères aux extrêmités, comme `U   U` par exemple)
```java
void dessineExtremites(char symbole, int taille)
```

Commencez par compléter la procédure `dessineExtremites` selon la même approche que `dessineMilieu`.


Maintenant que l'on a toutes nos briques de base, on va pouvoir dessiner chacune de nos lettres en utilisant ces fonctions.

Créez trois procédures, `dessinerI`,  `dessinerU`,  `dessinerT`, paramétrées par un caractère et une taille. Vous veillerez à ce qu'il y ait un retour sur la ligne finale pour chacune de ces procédures.

Finalement, il ne nous reste plus qu'à essayer tout cela en exécutant manuellement le programme avant de lancer les tests !

**NOTE :** il est possible d'appeler la procédure `println()` dans paramètre si l'on souhaite juste afficher un retour à la ligne.

<p class="flip" onclick="show()">(Après avoir fini tous les exercices, CLIQUEZ ICI !)<br/>Avez-vous la solution la plus simple ?</p>
<div id="hint2" style="display: none">
  <p>Si vous n'avez pas appelé <code>dessineLigne/Milieu/Extremités</code> dans les fonctions dessinant le I, le U et le T ... il faut retravailler votre code !</p>
</div>

<script>
function show() {
  var x = document.getElementById("hint2");
  if (x.style.display === "none") {
    x.style.display = "block";
  } else {
    x.style.display = "none";
  }
}
</script>

---
