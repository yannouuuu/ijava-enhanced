# Borne automatique au cinéma

Le gérant d’un cinéma a besoin d’un programme de gestion de sa billetterie devant prendre en compte ces règles commerciales :
* le prix de base d’une place de cinéma est de 12C,
* les enfants de moins de 10 ans bénéficient d’un demi-tarif,
* les personnes âgées de moins de 16 ans ainsi que celles ayant plus de 60 ans bénéficient elles d’une réduction de 3C,
* pour les séances bénéficiant de la 3D, il y a un supplément de 2C,
* finalement, si le client est un abonné, alors il bénéficie d’une réduction de 20% sur le prix final (ie. 3D incluse le cas échéant).

Voici deux exemples de traces d’exécutions que doit permettre le programme Cinema que vous demande le gérant.

```bash
> ijava execute Cinema
Age du spectateur : 9
Option 3D ? (1 si oui, autre chiffre si non) : 1
Abonné ? (1 si oui, autre chiffre si non) : 0
Coût du billet : 8.0 euros
> ijava execute Cinema
Age du spectateur : 64
Option 3D ? (1 si oui, autre chiffre si non) 0
Abonné ? (1 si oui, autre chiffre si non) 1
Coût du billet : 7.2 euros
```

Assurez-vous qu’il fonctionne aussi dans d’autres cas de figure (on considérera que l’utilisateur saisit toujours des valeurs correctes).
