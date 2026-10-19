# The final countdown

Sachant qu'il existe une fonction `long getTime()` qui retourne un entier long correspondant au nombre de millisecondes écoulées depuis le 1er janvier 1970, on vous demande de concevoir le programme `FinalCountDown` qui compte de 5 en 5 en partant de 1000 jusqu’à ce que 5 millisecondes se soient écoulées.

Cette fonction `getTime` est généralement utilisée pour calculer des durées en prenant une mesure avant et après un traitement afin de déterminer sa durée d'exécution. Voici un exemple :
```java
   long debut = getTime();
   // ... faire quelque chose ...
   long fin = getTime();
   long duree = fin - debut;    // → durée en millisecondes
   println("Opération terminée en " + duree + " ms");
```

Le programme `FinalCountDown` devra produire un affichage similaire à cela :

```bash
~/ijava2/tp4bis> ijava execute FinalCountDown
1000 995 990 985 980 975 970 965 960 955 950 945 940 935 930 925 920 915 910 905 900 895 890 885 880 875 870 865 860 855 850 845 840 835 830 825 820 815 810 805 800 795 790 785 780 775 770 765 760 755 750 745 740 735 730 725 720 715 710 705 700 695 690 685 680 675 670 665 660 655 650 645 640 635 630 625 620 615 610 605 600 595 590 585 580 575 570 565 560 555 550 545 540 535      
...
```

Lancez plusieurs fois votre programme ... que constatez-vous ?

PS : Toute référence à [un très ancien tube du millénaire](https://fr.wikipedia.org/wiki/The_Final_Countdown_(chanson)) précédent serait tout à fait fortuit :o)
