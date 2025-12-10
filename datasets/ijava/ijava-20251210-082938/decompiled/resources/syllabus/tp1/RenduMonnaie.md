# Rendre efficacement la monnaie

## ... sans utiliser `if` !

Sachant que l’on dispose de coupures de 20, 10, 5, 2 et 1 euro, comment procéder pour rendre la monnaie de la manière la plus efficace possible, c'est-à-dire en rendant le moins de coupure possible ?

On supposera que l’on a en entrée un nombre représentant la somme à rendre et que l’on affiche en sortie le nombre minimum de billets de 20 euros, de 10 euros, de 5 euros et de pièces de 2 et de 1 euro à rendre. 

**ATTENTION : comme nous n'avons pas encore vu cela en cours, il est bien sûr interdit d'utiliser le mot-clé `if` ...

Voici le squelette qu'il vous faut compléter :
```java
/**
* Ce programme détermine le nombre minimal de coupures
* à restituer pour une somme donnée. Les coupures utilisables
* sont les billets de 20, 10, 5 et les pièces de 2 et 1 euros.
*
*/
class RenduMonnaie extends Program {
    void algorithm(){
        int somme, nb20, nb10, nb5, nb2, nb1, reste;
        print("Quelle est le montant que vous souhaitez rendre en monnaie ?");
        somme = readInt();
        // à vous de compléter ce qui suit par les calculs permettant de 
        // déterminer le nombre minimal de chaque coupure nécessaire.
        // ne pas modifier les lignes ci-dessous, ni les déplacer 
        println("Nombre de billets de 20 : " + nb20);
        println("Nombre de billets de 10 : " + nb10);
        println("Nombre de billets de  5 : " + nb5);
        println("Nombre de pièces  de  2 : " + nb2);
        println("Nombre de pièces  de  1 : " + nb1);
    }
}
```

- Ecrivez le programme permettant de rendre la monnaie en un minimum de coupure. Voici 2 exemples d'exécutions attendues :

```bash
Quelle est le montant que vous souhaitez rendre en monnaie ? 4 
Nombre de billets de 20 : 0 
Nombre de billets de 10 : 0 
Nombre de billets de \  5 : 0 
Nombre de billets de \  2 : 2 
Nombre de billets de \  1 : 0
```

```bash
Quelle est le montant que vous souhaitez rendre en monnaie ? 38\
Nombre de billets de 20 : 1 
Nombre de billets de 10 : 1 
Nombre de billets de \  5 : 1 
Nombre de billets de \  2 : 1 
Nombre de billets de \  1 : 1
```

- Combien de variables avez-vous utilisé ? Est-il possible d’utiliser moins de variables ? Si c’est le cas, quel est le nombre minimal de variables nécessaire ! :)
- Essayer votre programme avec au moins 2 autres exemples de votre choix.
- Tester votre programme à l'aide de la commande dédiée de ijava !

## Étapes à suivre
- `ijava init RenduMonnaie`
- *complétez le code source, sans modifier ce qui est fourni !*
- `ijava compile RenduMonnaie.java`
- `ijava execute RenduMonnaie`
- `ijava test RenduMonnaie`
---
