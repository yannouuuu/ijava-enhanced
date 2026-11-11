package defpackage;

/* loaded from: ijava.jar:GridColor.class */
class GridColor extends WebApp {
    GridColor() {
    }

    String home(Request request) {
        if (request.verb == HTTP_VERB.GET) {
            return "<html>\n  <body>\n    <h1>Bienvenue sur la grille colorée !</h1>\n    <form method='POST' action='/generate'>\n      <input type='text' name='lignes'>\n      <input type='text' name='colonnes'>\n      <input type='text' name='couleur_depart'>\n      <input type='text' name='couleur_arrivee'>\n      <input type='submit'>\n    </form>\n  </body>\n</html>\n";
        }
        return "<html><body>Désolé, je ne comprends pas ce que vous demandez ...</body></html>";
    }

    String generate(Request request) {
        if (request.verb == HTTP_VERB.POST) {
            return String.format("<html>\n  <body>\n    <h1>Description de votre grille</h1>\n    <table>\n      <tr><td>Lignes</td><td>%s</td></tr>\n      <tr><td>Colonnes</td><td>%S</td></tr>\n    </table>\n  </body>\n</html>\n", request.postData.get("lignes"), request.postData.get("colonnes"));
        }
        return "<html><body>Désolé, je ne comprends pas ce que vous demandez ...</body></html>";
    }
}
