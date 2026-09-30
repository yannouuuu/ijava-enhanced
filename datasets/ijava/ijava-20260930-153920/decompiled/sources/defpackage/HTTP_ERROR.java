package defpackage;

/* loaded from: ijava.jar:HTTP_ERROR.class */
enum HTTP_ERROR {
    NOT_FOUND(404),
    METHOD_NOT_ALLOWED(405);

    private int code;

    HTTP_ERROR(int i) {
        this.code = i;
    }

    public int getCode() {
        return this.code;
    }
}
