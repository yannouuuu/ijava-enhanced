class ExtraireMots extends Program {

    void test_extraireMots () {
            assertArrayEquals(new String[]{"je", "tu", "elle", "je", "tu"}, extraireMots("je tu elle je tu "));
            assertArrayEquals(new String[]{"je", "tu", "elle", "je", "tu"}, extraireMots(" je tu, elle . je tu ! "));
        }

        void algorithm() {
            // ne rien mettre ici !
        }

}
