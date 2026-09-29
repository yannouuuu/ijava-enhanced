# Une histoire de saisons

**RAPPEL AVANT DE COMMENCER : UN SEUL RETURN EN DERNIÈRE INSTRUCTION D'UNE FONCTION (ET INTERDIT POUR LES PROCÉDURES)**

On souhaite créer le programme `Saisons` définissant quelques fonctions utiles pour travailler avec des dates.

## Saison météorologique

Commençons par la fonction `String saisonMeteorologique (int mois)` qui détermine la saison à partir du numéro du mois.
Rappelons qu’en France métropolitaine le printemps météorologique commence le 1 mars, l’été météorologique
commence le 1 juin, etc.

Copiez ensuite cette fonction pour vérifier manuellement votre fonction :
```java
void algorithm () {
    for (int mois = 0; mois <=13; mois = mois + 1){
        println("mois " + mois + " : " + saisonMeteorologique(m));
    }
}
```

Voici les affichages que vous devriez avoir :
```bash
mois 0 : erreur
mois 1 : hiver
mois 2 : hiver
mois 3 : printemps
mois 4 : printemps
mois 5 : printemps
mois 6 : été
mois 7 : été
mois 8 : été
mois 9 : automne
mois 10 : automne
mois 11 : automne
mois 12 : hiver
mois 13 : erreur
```

## Nombre de jours dans un mois

Continuons avec la fonction `int nombreJoursMois(int numeroMois)` qui retourne le nombre de jours en fonction du numéro du
mois. Voici un peu de poésie pour vous aider :

---
« Trente jours ont novembre,
Avril, juin et septembre.
De vingt-huit, il y en a un,
Tous les autres ont trente et un. »
---

Pour simplifier, nous ignorons pour le moment les années bissextiles et considérons donc que le mois de février a toujours 
28 jours.

Ajouter à votre programme cette fonction `nombreJoursMois` et modifier l’algorithme principal pour qu’il affiche en plus le bon nombre de jours pour chaque mois valide et qui compte en plus le nombre de jours total dans une année (non bissextile). 

Ainsi vous devrez produire ces affichages :
```bash
mois 0 : erreur, 0 jours
mois 1 : hiver, 31 jours
mois 2 : hiver, 28 jours
...
mois 9 : automne, 30 jours
...
nombre de jours total : 365
```

## Saison astronomique

Dans le calendrier, on utilise plutôt la saison astronomique que météorologique :
- le printemps astronomique commence aux alentours du 21 mars (cette date varie d’une année à l’autre),
- l’été astronomique commence aux alentours du 21 juin, etc. 
Ici, nous prendrons les dates 21/03, 21/06, 21/09 et 21/12 comme début des différentes saisons. 

Remarquez-vous un lien entre saison astronomique et saison météorologique ? 
Si oui, vous en servir peut rendre votre code moins redondant, plus clair et plus concis. 

Ajouter la fonction de signature `String saisonAstronomique (int jour, int mois)` qui calcule la saison astronomique à partir du numéro du jour et du mois.

## 365 affichages !

Modifiez votre fonction principale afin qu’elle affiche pour chaque jour de l’année sa saison astronomique sous
la forme suivante :
``` bash
1/1 : hiver
2/1 : hiver
...
20/6 : printemps
...
31/12 : hiver
```

Vérifiez notamment que la saison affichée est la bonne pour les dates suivantes.

<table>
  <tr><td>Date</td><td>Saison astronomique</td></tr>
  <tr><td>11/11</td><td>automne</td></tr>
  <tr><td>20/03</td><td>hiver</td></tr>
  <tr><td>31/03</td><td>printemps</td></tr>
  <tr><td>20/06</td><td>printemps</td></tr>
  <tr><td>20/09</td><td>été</td></tr>
  <tr><td>20/12</td><td>automne</td></tr>
  <tr><td>21/12</td><td>hiver</td></tr>
</table>

