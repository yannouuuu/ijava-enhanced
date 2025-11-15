# Changement de représentation des couleurs et des images    

Pour limiter un peu la taille de nos images, notre bosse nous informe que la société souhaite passer à une représentation hexadécimale des couleurs. 

Pour l'instant, chaque couleur primaire (rouge, vert, bleu) est représentée par 3 chiffres décimaux (allant de `000` à `255`) concaténés les uns aux autres. 

Ainsi, le rouge est codé par la chaîne `"255000000"`, le vert par `"000255000"` et le bleu par `"000000255"`, le blanc par `"255255255"` et le noir par `"000000000"`.

En hexadécimal, c'est une base 16 qui est utilisée, avec les chiffres `0` à `9` puis les lettres `A` à `F` pour représenter les valeurs de `10` (`A`) à `15` (`F`). Ainsi, chaque couleur primaire est représentée par seulement deux chiffres hexadécimaux (allant de `00` à `FF`) concaténés les uns aux autres. 

Par exemple, le rouge est codé par la chaîne `"FF0000"`, le vert par `"00FF00"`, le bleu par `"0000FF"`, le blanc par `"FFFFFF"` et le noir par `"000000"`.

Au lieu d'utiliser 3 caractères pour chaque couleur primaire, on n'en utilise plus que 2, ce qui implique un gain de place non négligeable sur la taille des images !

Le but du contrôle TP est de refaire les fonctions du projet, mais en utilisant cette nouvelle représentation hexadécimale des couleurs.

## Q1 (3) : `char numberToHex(int digit)`
Créez la fonction `char numberToHex(int digit)` qui convertit un nombre (entre `0` et `15`) en caractère hexadécimal (entre `'0'` et `'F'`). On suppose que le nombre passé en paramètre est toujours valide, c'est-à-dire compris entre `0` et `15`.

**Rappel :** le forçage de type (*cast*) permet de convertir un entier en caractère en utilisant la table ASCII et réciproquement.

```java
    void test_numbertoHex() {
        assertEquals('0', numberToHex(0));
        assertEquals('5', numberToHex(5));
        assertEquals('9', numberToHex(9));
        assertEquals('A', numberToHex(10));
        assertEquals('C', numberToHex(12));
        assertEquals('F', numberToHex(15));
    }
```

## Q2 (3): `String intToHex(int primaryColor)`

Créez une fonction `String intToHex(int primaryColor)` qui convertit une couleur primaire (rouge, vert ou bleu) donnée en décimal (entre `0` et `255`) en hexadécimal (entre `00` et `FF`).

Pour trouver l'écriture hexadécimale d'un nombre, il faut diviser ce nombre par 16 pour obtenir le chiffre de poids fort (le premier) et prendre le reste de cette division pour obtenir le chiffre de poids faible (le second).
Par exemple, pour 254,  on a `254 / 16 = 15`, donc `F` pour le chiffre de poids fort, et `254 % 16 = 14`, donc `E` pour le chiffre de poids faible. Ainsi, l'écriture hexadécimale de `254` est `FE`.

```java
    void test_intToHex() {
        assertEquals("00", intToHex(0));
        assertEquals("05", intToHex(5));
        assertEquals("0A", intToHex(10));
        assertEquals("1F", intToHex(31));
        assertEquals("7F", intToHex(127));
        assertEquals("FF", intToHex(255));
    }  
```

**Conseil :** pensez à ré-utiliser la fonction définie précédemment ...

## Q3 (2) : `String colorToHex(int red, int green, int blue)`
Créez une fonction `String colorToHex(int red, int green, int blue)` qui convertit une couleur donnée en décimal (avec ses trois composantes rouge, vert et bleu entre `0` et `255`) en hexadécimal (avec ses trois composantes rouge, vert et bleu entre `00` et `FF`).

**Conseil :** pensez à ré-utiliser les fonctions définies précédemment !

Afin d'alléger la lecture du code et des tests, on définit quelques constantes pour les couleurs de base.

```java
    final String HEX_RED   = "FF0000";
    final String HEX_GREEN = "00FF00";
    final String HEX_BLUE  = "0000FF";
    final String HEX_WHITE = "FFFFFF";
    final String HEX_BLACK = "000000";

    void test_colorToHex() {
        assertEquals(HEX_RED,   colorToHex(255, 0, 0));     // rouge 
        assertEquals(HEX_GREEN, colorToHex(0, 255, 0));     // vert 
        assertEquals(HEX_BLUE,  colorToHex(0, 0, 255));     // bleu 
        assertEquals(HEX_WHITE, colorToHex(255, 255, 255)); // blanc
        assertEquals(HEX_BLACK, colorToHex(0, 0, 0));       // noir
        assertEquals("7F7F7F",  colorToHex(127, 127, 127)); // gris moyen
        assertEquals("123ABC",  colorToHex(18, 58, 188));   // couleur quelconque
    }
```

## Q4 (2) : `int size(String[] image)`

On souhaite maintenant représenter une grille carrée de pixels de couleurs en utilisant cette nouvelle représentation hexadécimale des couleurs. Ainsi, chaque pixel est représenté par une chaîne de 6 caractères (2 pour le rouge, 2 pour le vert et 2 pour le bleu) et nous allons utiliser un tableau à une dimension de pixels (chaque pixel étant représenté par une chaîne de 6 caractères).

Par exemple, une image 2x2 avec un pixel rouge en (0,0), un pixel vert en (0,1), un pixel bleu en (1,0) et un pixel blanc en (1,1) sera représentée par le tableau de chaîne de caractères `["FF0000", 00FF00", 0000FF", "FFFFFF"]`. 

Avec cette nouvelle représentation, adaptez la fonction `int size(String[] image)` réalisée en *Semaine2* pour qu'elle utilise cette nouvelle représentation.

```java
    void test_size() {
        // 2x2 rouge et vert :
        // ROUGE,     VERT,     VERT,    ROUGE 
        // {"FF0000", "00FF00", "00FF00", "FF0000"}
        assertEquals(2, size(new String[]{
            HEX_RED, HEX_GREEN, HEX_RED, GREEN}));
        // 3x3 noir et bleu :
        // NOIR, BLEU, NOIR, BLEU, NOIR, BLEU, NOIR, BLEU, NOIR
        // "0000000000FF0000000000FF0000000000FF0000000000FF000000"
        assertEquals(3, size(new String[]{
            HEX_BLACK, HEX_BLUE,  HEX_BLACK, 
            HEX_BLUE,  HEX_BLACK, HEX_BLUE, 
            HEX_BLACK, HEX_BLUE,  HEX_BLACK}));
        // 4x4 blanc : 16 fois WHITE
        // "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF"
        assertEquals(4, size(new String[]{
            HEX_WHITE, HEX_WHITE, HEX_WHITE, HEX_WHITE, 
            HEX_WHITE, HEX_WHITE, HEX_WHITE, HEX_WHITE, 
            HEX_WHITE, HEX_WHITE, HEX_WHITE, HEX_WHITE, 
            HEX_WHITE, HEX_WHITE, HEX_WHITE, HEX_WHITE}));
    }
```

## Q5 (2): `String get(String[] image, int line, int column)`

Adaptez la fonction `String get(String[] image, int line, int column)` de *Semaine2* pour qu'elle utilise la nouvelle représentation hexadécimale des couleurs au lieu de la représentation décimale actuelle.

**Rappel :** l'image est maintenant représentée par un tableau à une dimension de chaînes de caractères, chaque chaîne représentant la couleur d'un pixel en hexadécimal (6 caractères par pixel).

```java
    void test_get() {
        // image2x2 = "FF0000" "00FF00" 
        //            "00FF00" "FF0000";
        String[] image2x2 = new String[]{  HEX_RED, HEX_GREEN, 
                                         HEX_GREEN,   HEX_RED};  
        assertEquals(HEX_RED,   get(image2x2, 0, 0)); // rouge
        assertEquals(HEX_GREEN, get(image2x2, 0, 1)); // vert
        assertEquals(HEX_GREEN, get(image2x2, 1, 0)); // vert
        assertEquals(HEX_RED,   get(image2x2, 1, 1)); // rouge
        // image3x3 = "000000" "0000FF" "000000"
        //            "0000FF" "000000" "0000FF"
        //            "000000" "0000FF" "000000"
        String[] image3x3 = new String[]{HEX_BLACK,  HEX_BLUE, HEX_BLACK, 
                                          HEX_BLUE, HEX_GREEN,  HEX_BLUE,
                                         HEX_BLACK,  HEX_BLUE, HEX_BLACK};
        assertEquals(HEX_BLACK, get(image3x3, 0, 0)); // noir
        assertEquals(HEX_BLUE,  get(image3x3, 0, 1)); // bleu
        assertEquals(HEX_BLACK, get(image3x3, 0, 2)); // noir
        assertEquals(HEX_BLUE,  get(image3x3, 1, 0)); // bleu
        assertEquals(HEX_GREEN, get(image3x3, 1, 1)); // vert
        assertEquals(HEX_BLUE,  get(image3x3, 1, 2)); // bleu
        assertEquals(HEX_BLACK, get(image3x3, 2, 0)); // noir
        assertEquals(HEX_BLUE,  get(image3x3, 2, 1)); // bleu
        assertEquals(HEX_BLACK, get(image3x3, 2, 2)); // noir
    }
```

## Q6 (4) : `String[] generate(int size, int r, int g, int b, int stepR, int stepG, int stepB)`
Modifiez la fonction `generate` de *Semaine2* pour qu'elle utilise la nouvelle représentation hexadécimale des couleurs au lieu de la représentation décimale actuelle.

```java
    void test_generate_5_200_255_155_moins20_moins30_moins15(Program student) {
        String[] generatedImage = new String[]{
            "B4E18C", "B4E18C", "B4E18C", "B4E18C", "B4E18C", 
            "A0C37D", "A0C37D", "A0C37D", "A0C37D", "A0C37D", 
            "8CA56E", "8CA56E", "8CA56E", "8CA56E", "8CA56E", 
            "78875F", "78875F", "78875F", "78875F", "78875F", 
            "646950", "646950", "646950", "646950", "646950"};
        assertArrayEquals(generatedImage, generate(5, 200, 255, 155, -20, -30, -15));
    }
```

## Q7 (4) : `String[] miroir(String[] image)`
Créez la fonction `String[] miroir(String[] image)` qui prend en paramètre une image représentée par un tableau à une dimension de chaînes de caractères (chaque chaîne représentant la couleur d'un pixel en hexadécimal) et qui retourne une nouvelle image qui est le miroir horizontal de l'image passée en paramètre.

```java
void test_miroir() {
    String[] image2x2 = new String[]{  
        HEX_RED,   HEX_GREEN, 
        HEX_GREEN, HEX_RED};  
    String[] mirrored2x2 = new String[]{
        HEX_GREEN, HEX_RED,
        HEX_RED,   HEX_GREEN};
    assertArrayEquals(mirrored2x2, mirroir(image2x2));

    String[] image3x3 = new String[]{
        HEX_BLACK, HEX_BLUE,  HEX_BLACK, 
        HEX_BLUE,  HEX_GREEN, HEX_BLUE,
        HEX_BLACK, HEX_BLUE,  HEX_BLACK};
    String[] mirrored3x3 = new String[]{
        HEX_BLACK, HEX_BLUE,  HEX_BLACK, 
        HEX_BLUE,  HEX_GREEN, HEX_BLUE,
        HEX_BLACK, HEX_BLUE,  HEX_BLACK};
    assertArrayEquals(mirrored3x3, mirroir(image3x3));
}
```
