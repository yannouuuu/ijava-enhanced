package defpackage;

/* loaded from: ijava.jar:Semaine1_old.class */
class Semaine1_old extends Program {
    String pageHTML = "    <!DOCTYPE html>\n    <html lang=\"fr\">\n        <head>\n            <meta charset=\"UTF-8\">\n            <title>Ma super page</title>\n        </head>\n        <body>\n            <div class=\"main\">\n                <h1>Bienvenue sur mon site</h1>\n                <p>Mes langages préférées sont les suivants :</p>\n                <ul>\n                    <li>ijava</li>\n                    <li>java</li>\n                    <li>LISP</li>\n                </ul>\n            </div>\n        </body>\n    </html>\n";

    Semaine1_old() {
    }

    String insert(String str, String str2, int i) {
        String str3 = str;
        if (i >= 0 && i < length(str)) {
            str3 = substring(str, 0, i) + str2 + substring(str, i, length(str));
        }
        return str3;
    }

    String delete(String str, int i, int i2) {
        String str2 = str;
        if (i < i2 && i >= 0 && i2 < length(str)) {
            str2 = substring(str, 0, i) + substring(str, i2 + 1, length(str));
        }
        return str2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // defpackage.Program
    public void algorithm() {
        println(this.pageHTML);
    }
}
