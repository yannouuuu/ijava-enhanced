class AffichageTableau extends Program {

    void algorithm() {
        int[] tabInt = new int[] { 1, 2, 3 };
        String[] tabString = new String[] { "un", "deux", "trois" };
        println(tabInt);
        println(tabString);
    }

    void test_toString_tab_int() {
        //assertEquals("1 2 3 ", toString(new int[]{1, 2, 3}));
        //assertEquals("", toString(new int[]{}));
    }

    void test_toString_tab_string() {
        //assertEquals("un deux trois ", toString(new String[]{"un", "deux", "trois"}));
        //assertEquals("", toString(new String[0]));
    }

    String toString(int[] tabInt) {
        return "";
    }


    String toString(String[] tabString) {
        return "";
    }


}