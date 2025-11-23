# Changement de représentation des images

Votre bosse vient vous voir car des clients ont critiqué la lourdeur des fichiers images.

Les développeurs et développeuses seniors ont creusé le problème et proposent de compresser la représentation des images. Le principe qu'ils proposent est de considérer que les images en *pixel art* des clients ont assez peu de couleurs (généralement 16 ou 32) par rapport au nombre de couleurs possibles en RGB (256 x 256 x 256 soit beaucoup !). Ainsi, ils proposent de compresser la représentation des images en introduisant la notion de palette de couleurs. 

Voici un exemple d'image pour comprendre le principe :
<table>
    <tr>
        <td style="background: #f00;"></td>
        <td style="background: #0f0;"></td>
        <td style="background: #00f;"></td>
    </tr>
    <tr>
        <td style="background: #000;"></td>
        <td style="background: #f00;"></td>
        <td style="background: #0f0;"></td>
    </tr>
    <tr>
        <td style="background: #fff;"></td>
        <td style="background: #000;"></td>
        <td style="background: #f00;"></td>
    </tr>
</table>

Cette image est constituée de 3x3 soit 9 pixels (**on ne travaille qu'avec des images carrées**), mais de seulement 5 couleurs (rouge, vert, bleu, noir, blanc) et est représentée ainsi avec notre codage actuel :
```java
final String IMAGE = "255000000"+"000255000"+"000000255"+
                     "000000000"+"255000000"+"000255000"+
                     "255255255"+"000000000"+"255000000";
```

Les responsables proposent donc de créer la notion de **palette** de couleurs, pour associer à chaque couleur un nombre :
- `0 = rouge`
- `1 = vert` 
- `2 = bleu` 
- `3 = noir`
- `4 = blanc`

Dit autrement, ils proposent de représenter la **palette** de couleurs d'une image par un tableau de chaînes de caractères :

<table style="width: 50%">
    <tr>
        <td style="background: #DDD; border: 1px solid">"255000000"</td>
        <td style="background: #DDD; border: 1px solid">"000255000"</td>
        <td style="background: #DDD; border: 1px solid">"000000255"</td>
        <td style="background: #DDD; border: 1px solid">"000000000"</td>
        <td style="background: #DDD; border: 1px solid">"255255255"</td>
    </tr>
    <tr style="text-align: center; font-style: italic">
        <td style="text-align: center">0</td>
        <td style="text-align: center">1</td>
        <td style="text-align: center">2</td>
        <td style="text-align: center">3</td>
        <td style="text-align: center">4</td>
    </tr>
</table>

Si l'on dispose de cette **palette**, l'image peut être représentée à l'aide d'un tableau d'entiers avec un code couleur de la **palette** pour chaque pixel de l'image. Ce qui pour cet exemple, correspond à ce tableau de 9 entiers : 

<table>
    <tr style="background: #DDD; border: 1px solid">
        <td style="border: 1px solid">0</td>
        <td style="border: 1px solid">1</td>
        <td style="border: 1px solid">2</td>
        <td style="border: 1px solid">3</td>
        <td style="border: 1px solid">0</td>
        <td style="border: 1px solid">1</td>
        <td style="border: 1px solid">4</td>
        <td style="border: 1px solid">3</td>
        <td style="border: 1px solid">0</td>
    </tr>
    <tr style="text-align: center; font-style: italic">
        <td style="text-align: center">0</td>
        <td style="text-align: center">1</td>
        <td style="text-align: center">2</td>
        <td style="text-align: center">3</td>
        <td style="text-align: center">4</td>
        <td style="text-align: center">5</td>
        <td style="text-align: center">6</td>
        <td style="text-align: center">7</td>
        <td style="text-align: center">8</td>
    </tr>
</table>

car la **palette** correspond à ceci : 

<table>
    <tr style="font-style: bold">
        <td style="background: #f00; border: 1px solid">"255000000"</td>
        <td style="background: #0f0; border: 1px solid">"000255000"</td>
        <td style="background: #00f; color: #fff; border: 1px solid">"000000255"</td>
        <td style="background: #000; color: #fff; border: 1px solid">"000000000"</td>
        <td style="background: #fff; border: 1px solid">"255255255"</td>
    </tr>
    <tr style="font-style: italic">
        <td style="text-align: center;">0</td>
        <td style="text-align: center;">1</td>
        <td style="text-align: center;">2</td>
        <td style="text-align: center;">3</td>
        <td style="text-align: center;">4</td>
    </tr>
</table>

## `int colorCode(String[] palette, String color)` (4 pts)

Définissez la fonction `colorCode` qui en fonction d'une couleur (`color`) représentée sous la forme d'une chaîne de 9 caractères retourne le code couleur correspondant dans la `palette`. Si la couleur n'est pas présente alors l'indice `-1` est retourné.

Votre fonction doit valider le test suivant : 
```java
void test_colorCode() {
    final String[] PALETTE = {
            "255000000", // rouge
            "000255000", // vert
            "000000255", // bleu
            "000000000", // noir
            "255255255"  // blanc
    };
    assertEquals( 0, colorCode(PALETTE, "255000000"));
    assertEquals( 4, colorCode(PALETTE, "255255255"));
    assertEquals(-1, colorCode(PALETTE, "032064032"));
}
```

## `int numberOfPixels(String image)` (1 pt)

Créez la fonction `numberOfPixels` qui retourne le nombre de pixels contenus dans l'`image` donnée en paramètre qui est une image selon l'ancien format..

Votre fonction doit valider le test suivant : 
```java
void test_numberOfPixels() {
    final String IMAGE_2x2 = "255255255"+"000255000"+
                                 "255255255"+"000255000";
        assertEquals(4, numberOfPixels(IMAGE_2x2));

        final String IMAGE_3x3 = "255000000"+"000255000"+"000000255"+
                                 "000000000"+"255000000"+"000255000"+
                                 "255255255"+"000000000"+"255000000";
        assertEquals(9, numberOfPixels(IMAGE_3x3));
}
```

## `int[] convertOldToNew(String image, String[] palette)` (4 pts)

Créez maintenant la fonction `convertOldToNew` qui convertit une `image` sous l'ancien format, vers le nouveau à l'aide de la `palette` de couleurs. On considère que la `palette` est bien construite et contient toutes les couleurs présentes dans `image`.

Votre fonction doit valider le test suivant : 
```java
void test_convertOldToNew() {
    final String IMAGE_2x2 = "255255255"+"000255000"+
                                 "255255255"+"000255000";
        final String[] PALETTE = {
            "255000000", // rouge
            "000255000", // vert
            "000000255", // bleu
            "000000000", // noir
            "255255255"  // blanc
        };
        assertArrayEquals(new int[]{4, 1, 4, 1}, 
                    convertOldToNew(IMAGE_2x2, PALETTE));
        
        final String IMAGE_3x3 = "255000000"+"000255000"+"000000255"+
                                 "000000000"+"255000000"+"000255000"+
                                 "255255255"+"000000000"+"255000000";
        assertArrayEquals(new int[]{0, 1, 2, 3, 0, 1, 4, 3, 0}, 
                    convertOldToNew(IMAGE_3x3, PALETTE));
}
```

## `String convertNewToOld(int[] image, String[] palette)` (4 pts)

Créez maintenant la fonction `convertNewToOld` qui convertit une `image` sous le nouveau format, vers l'ancien' à l'aide de la `palette` de couleurs. On considère que la `palette` est bien construite et contient toutes les couleurs présentes dans `image`.

Votre fonction doit valider le test suivant : 
```java
void test_convertNewToOld() {
    final String[] PALETTE = {
            "255000000", // rouge
            "000255000", // vert
            "000000255", // bleu
            "000000000", // noir
            "255255255"  // blanc
        };
        final String IMAGE_2x2 = "255255255"+"000255000"+
                                 "255255255"+"000255000";
        assertEquals(IMAGE_2x2, 
                    convertNewToOld(new int[]{4, 1, 4, 1}, PALETTE));
        
        final String IMAGE_3x3 = "255000000"+"000255000"+"000000255"+
                                 "000000000"+"255000000"+"000255000"+
                                 "255255255"+"000000000"+"255000000";
        assertEquals(IMAGE_3x3, 
                    convertNewToOld(new int[]{0, 1, 2, 3, 0, 1, 4, 3, 0}, PALETTE));
}
```

## `String[] shift(String[] palette, boolean right)` (2 pts)

Créez la fonction `shift` qui crée une nouvelle palette en effectuant un décalage des couleurs de la `palette` vers la droite si `right`vaut `true` et vers la droite sinon . Ceci permet de faire varier les palettes de couleurs indépendamment des images.

```java
void test_shift() {
    final String[] PALETTE = new String[]{"255000000", "000255000", "000000255"};
    assertArrayEquals(new String[]{"000000255", "255000000", "000255000"}, 
                      shift(PALETTE, true));
    assertArrayEquals(new String[]{"000255000", "000000255", "255000000"}, 
                      shift(PALETTE, false));
}
```

## `void show(int[] image, String[] palette)` (3 pts)

On souhaite maintenant définir la procédure `show` qui affiche une `image` avec le nouvel encodage en fonction d'une `palette` de couleurs. Vous pouvez vous inspirer de la fonction `show` que vous avez écrite dans les semaines précédentes et pouvez aussi copier les éventuelles fonctions qui vous semblent utiles pour définir cette nouvelle version.

Ici on n'a pas de fonction de test associé, mais vous pouvez vérifier que lorsque vous exécutez le programme, vous obtenez bien l'image donnée en exemple au début de l'énoncé avec ce code : 
```java
void algorithm() {
    final String IMAGE_3x3 = "255000000"+"000255000"+"000000255"+
                             "000000000"+"255000000"+"000255000"+
                             "255255255"+"000000000"+"255000000";
    final String[] PALETTE = {
        "255000000", // rouge
        "000255000", // vert
        "000000255", // bleu
        "000000000", // noir
        "255255255"  // blanc
    };
    show(convertOldToNew(IMAGE_3x3, PALETTE), PALETTE);
}
```

