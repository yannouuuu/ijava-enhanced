# Calcul des fréquences pour le nuage de mots

Nous avons maintenant tous les ingrédients pour calculer les fréquences de chaque mot dans un texte. 

Recopiez les différentes fonctions que vous avez écrites dans les étapes précédentes et complétez la fonction `void algorithm()` ci-dessous :

```java
void algorithm() {
    String texte =
        "Keep it simple, stupid !" +
        "Keep it stupid simple !" +
        "Keep it simple and stupid !" +
        "Keep it small and simple !";
    println(texte);
    println();
    // Compléter ici en utilisant les fonctions écrites précédemment pour afficher les mots du texte et leur fréquence
}
```

afin qu’elle affiche la liste des mots de `texte` et la fréquence de chaque mot.

```bash
ijava execute NuageDeMots
Keep : 4
it : 4
simple : 4
stupid : 3
and : 2
small : 1
```

## Prolongements

### Ignorez les différences de casse

Actuellement, si l'on a le mot `Keep`et le mot `keep`, ils sont considérés comme différents. Améliorez votre programme afin que la casse (ie. les majuscules/minuscules) d'un mot soit ignorée.

### Triez les mots par ordre de fréquence

Même si cela sera l'objet d'un futur cours, sans vous aider d'une recherche web ou d'une quelconque IA, demandez vous comment il serait possible d'avoir un affichage permettant d'afficher par ordre décroissant de fréquence les différents mots.
