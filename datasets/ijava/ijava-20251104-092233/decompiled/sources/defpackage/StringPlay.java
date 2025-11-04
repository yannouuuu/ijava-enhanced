package defpackage;

/* loaded from: ijava.jar:StringPlay.class */
class StringPlay extends Program {
    StringPlay() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // defpackage.Program
    @skill({"variable-declaration", "io-basic", "string-manipulation"})
    public void algorithm() {
        print("Message length: ");
        println(length("Hello, World!"));
        print("Is name 'Alice'? ");
        println(equals("Alice", "Alice"));
        print("Is name 'Bob'? ");
        println(equals("Alice", "Bob"));
        print("First 5 characters: ");
        println(substring("Hello, World!", 0, 5));
        print("Characters 7-12: ");
        println(substring("Hello, World!", 7, 5));
    }

    void testStringOperations() {
        algorithm();
        String capturedOutput = getCapturedOutput();
        assertTrue(contains(capturedOutput, "Message length: 13"));
        assertTrue(contains(capturedOutput, "Is name 'Alice'? true"));
        assertTrue(contains(capturedOutput, "Is name 'Bob'? false"));
        assertTrue(contains(capturedOutput, "First 5 characters: Hello"));
        assertTrue(contains(capturedOutput, "Characters 7-12: World"));
        clearCapturedOutput();
    }

    void testMessageLength() {
    }

    void testNameComparison() {
    }

    void testSubstringOperations() {
    }

    void testStringBoundaries() {
        algorithm();
        String capturedOutput = getCapturedOutput();
        assertTrue(contains(capturedOutput, "Hello") && contains(capturedOutput, "World"));
        assertTrue(contains(capturedOutput, "13"));
        assertTrue(contains(capturedOutput, "true") && contains(capturedOutput, "false"));
        clearCapturedOutput();
    }

    void testPreciseStringOperations() {
        algorithm();
        String capturedOutput = getCapturedOutput();
        assertTrue(matches(capturedOutput, ".*First 5 characters: Hello.*"));
        assertTrue(matches(capturedOutput, ".*Characters 7-12: World.*"));
        clearCapturedOutput();
    }
}
