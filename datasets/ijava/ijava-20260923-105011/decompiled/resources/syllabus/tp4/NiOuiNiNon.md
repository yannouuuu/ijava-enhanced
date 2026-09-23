# Ni oui, Ni non

::: question
**Connaissez-vous le jeu ni oui, ni non ?**

- [ ] oui
  > Ah c'est dommage car dans ce cas, vous avez perdu :p
- [ ] non
  > Hé bien, il ne faut ni dire oui, ni dire non ... donc vous avez perdu !
- [x] je crois
  > Effectivement, vous y avez déjà joué :^)
:::

Concevez le programme `NiOuiNiNon` en étant attentif à ce qu'il fonctionne aussi bien pour une réponse entièrement en lettre majuscules (OUI, NON) qu'en lettres minuscules (oui, non).

```bash
~ijava2/tp?> ijava execute NiOuiNiNon
hello
zut
oui
Perdu !
```

<p class="flip" onclick="show()">Avez-vous la solution la plus simple ?</p>
<div id="hint" style="display: none">
  <p>Ce programme ne doit contenir qu'une boucle à évènement (while) et trois connecteurs logiques. </p>
</div>

<script>
function show() {
  var x = document.getElementById("hint");
  if (x.style.display === "none") {
    x.style.display = "block";
  } else {
    x.style.display = "none";
  }
}
</script>