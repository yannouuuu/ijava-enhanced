# Initialisation d'un tableau

1. Écrivez la fonction `int[] creerTableau()` permettant de créer un tableau d'entiers de 10 cases dont les 5 premières cases contiennent la valeur 1 et les 5 suivantes la valeur 2. **Vous n'avez pas le droit d'utiliser une déclaration en extension.**

La fonction doit passer le test fourni `test_creerTableau_taille_fixe_10`. Il est normal que les tests professeurs ne passent pas encore, car il vous manque des fonctions.

2. Enlever le commentaire autour de la fonction de test `test_creerTableau_taille_en_parametre`. Puis, écrivez la fonction concernée par ce test. Elle prend en paramètre une taille de tableau et retourne un tableau avec la première moitié de cases qui contiennent 1, et la seconde moitié contiennent 2.

La fonction doit passer le test fourni.

3. Simplifiez la première fonction que vous avez écrite (`int[] creerTableau()`) en faisant un appel à la seconde (`int[] creerTableau(int)`). La nouvelle fonction doit faire une ligne. Assurez-vous que les tests passent toujours.

4. Intéressons-nous mainteannt aux deux fonctions de test `test_creerTableauAleatoire_valeurs_entre_0_et_20` et `test_creerTableauAleatoire_toutes_les_valeurs_entre_0_et_20_sont_presentes`. Définissez la fonction testée qui retourne un tableau de valeurs tirées aléatoirement entre 0 et 20 inclus selon une taille donnée. Rappel: vous pouvez utiliser la fonction `double random()` qui retourne un nombre à virgule dans l'intervalle [0.0, 1.0[, ainsi que l'instruction de forçage de type `(int)` qui convertit un nombre à virgule en entier.

<p class="flip" onclick="show()">(Après avoir fini tous les exercices, CLIQUEZ ICI !)<br/>Avez-vous la solution la plus simple ?</p>
<div id="hint2" style="display: none">
  <p>Nous sommes bien d'accord : aucune alternative n'est nécessaire dans les fonctions creerTableau, n'est-ce pas ?</p>
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