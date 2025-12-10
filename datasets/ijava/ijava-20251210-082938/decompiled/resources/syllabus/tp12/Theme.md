# Créer des catégories à partir de fichier pour le jeu de mot en TD

Nous allons dans cet exercice travailler sur la foncionnalité d'initialisation des catégories par des fichiers textes pour le jeu de mots vu en TD.

## Création du type `Theme`

On souhaite pouvoir diposer de différents thèmes pour notre jeu de mots. Par exemple, biologie, géographie, maths, physique, sport ... Au début, on reste modeste et on se limite à deux thèmes : biologie et géographie.

::: question
**Quel outil proposez vous de mobiliser ?**
- [ ] La création d'un type avec `class`
  > Que contiendrait-il ? Deux constantes globales ? C'est possible, mais ce n'est pas l'approche la plus pertinente.
- [x] La création d'un type avec `enum`
  > Tout à fait car la liste des thèmes correspond finalement à un ensemble de constantes de même nature. L'énumération est la plus appropriée pour cette situation !
:::

Créez maintenant le nouveau type `Theme`.