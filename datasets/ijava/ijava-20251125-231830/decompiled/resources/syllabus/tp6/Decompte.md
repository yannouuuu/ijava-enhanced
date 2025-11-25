# Chaînes de nombres décroissants

On désire concevoir une fonction générant une chaîne de caractères contenant les nombres pairs décroissants entre `n` (dont on supposera qu’il est pair et supérieur ou égal à 2) et `1`, sans espace entre les différents nombres.

Créez le programme `Decompte` dans lequel vous veillerez à ne pas utiliser d’alternative (oui, oui, c’est possible !).

- Écrire la fonction genereNombresPairs qui passe ce test.
```java
void testGenereNombresPairs1() {
    assertEquals("8642", genereNombresPairs1(8));
    assertEquals("12108642", genereNombresPairs1(12));
    assertEquals("2", genereNombresPairs1(2));
}
```
- Écrire une fonction similaire mais qui produit une étoile avant et après la suite de nombres:
```java
void testGenereNombresPairs2() {
    assertEquals("*8642*", genereNombresPairs2(8));
    assertEquals("*12108642*", genereNombresPairs2(12));
    assertEquals("*2*", genereNombresPairs2(2));
}
```
- Écrire une troisième version de la fonction, qui produit une étoile entre chaque paire de nombres consécutifs.
```java
void testGenereNombresPairs3() {
    assertEquals("8*6*4*2", genereNombresPairs3(8));
    assertEquals("12*10*8*6*4*2", genereNombresPairs3(12));
    assertEquals("2", genereNombresPairs3(2));
}
```