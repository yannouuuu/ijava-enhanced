# L’odyssée de Pi
Voici un programme proposant quelques fonctions

```java
class Cercle extends Program{
    double circonference(double rayon) {
        return 3.14 * diametre(rayon);
    }
    double diametre(double rayon) {
        return 2 * rayon;
    }
    double aire(double rayon) {
        return 3.14 * rayon * rayon;
    }
    double volume(double rayon) {
        return 4.0/3.0 * 3.14 * rayon * rayon *rayon;
    }
}
```

- Améliorez ce code en y intégrant deux constantes globales, l’une pour éviter la redondance d’une même valeur (`PI`), l’autre pour éviter un calcul à chaque appel de la fonction `volume` (`QUATRE_TIERS_DE_PI`).
- Pour des calculs plus précis, on souhaite une valeur de PI avec 5 décimales après la virgule, modifiez votre programme dans ce sens.
- Écrivez un `void algorithm()` qui pour des rayons allant de 1 à 15 affiche la circonférence, le diamètre et le volume, puis indique le nombre de multiplications effectuées au total. Pour ce faire, vous utiliserez une variable globale `nombreDeMultiplications` que vous mettrez à jour dans chaque fonction réalisant des multiplications.

```java
circonference(1) = 6.28318, diametre(1) = 2.0, volume(1) = 4.188786666666666.
circonference(2) = 12.56636, diametre(2) = 4.0, volume(2) = 33.51029333333333.
circonference(3) = 18.849539999999998, diametre(3) = 6.0, volume(3) = 113.09723999999999.
circonference(4) = 25.13272, diametre(4) = 8.0, volume(4) = 268.08234666666664.
circonference(5) = 31.4159, diametre(5) = 10.0, volume(5) = 523.5983333333332.
circonference(6) = 37.699079999999995, diametre(6) = 12.0, volume(6) = 904.7779199999999.
circonference(7) = 43.98226, diametre(7) = 14.0, volume(7) = 1436.7538266666666.
circonference(8) = 50.26544, diametre(8) = 16.0, volume(8) = 2144.658773333333.
circonference(9) = 56.54862, diametre(9) = 18.0, volume(9) = 3053.6254799999997.
circonference(10) = 62.8318, diametre(10) = 20.0, volume(10) = 4188.786666666666.
circonference(11) = 69.11498, diametre(11) = 22.0, volume(11) = 5575.275053333333.
circonference(12) = 75.39815999999999, diametre(12) = 24.0, volume(12) = 7238.223359999999.
circonference(13) = 81.68133999999999, diametre(13) = 26.0, volume(13) = 9202.764306666666.
circonference(14) = 87.96452, diametre(14) = 28.0, volume(14) = 11494.030613333332.
circonference(15) = 94.2477, diametre(15) = 30.0, volume(15) = 14137.154999999999.
Soit un total de 90 multiplications.
```

N'oubliez pas de tester votre programme à l'aide de la commande `test` d'`ijava` !
