class ValeursMinMax extends Program {

    void test_minMax() {
        assertArrayEquals(new int[]{1,3}, minMax(new int[]{2,1,3}));
        assertArrayEquals(new int[]{2,2}, minMax(new int[]{2,2,2}));
        assertArrayEquals(new int[]{-1,3}, minMax(new int[]{3,-1,2}));
    }
    /*
    void test_indicesMinMax() {
        assertArrayEquals(new int[]{1,2}, indicesMinMax(new double[]{2.1, 1.5, 3.0}));
        assertArrayEquals(new int[]{0,0}, indicesMinMax(new double[]{2.1, 2.1, 2.1}));
        assertArrayEquals(new int[]{1,0}, indicesMinMax(new double[]{3.0, -1.9, 2.5}));
    }
    */
    void algorithm() {
    }

}
