package defpackage;

/* loaded from: ijava.jar:ANSI.class */
public interface ANSI extends ijava2.tools.ANSI {
    static String rgb(int i, int i2, int i3, boolean z) {
        return "\u001b[" + (z ? '&' : '0') + ";2;" + i + ";" + i2 + ";" + i3 + "m";
    }
}
