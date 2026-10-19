package defpackage;

/* loaded from: ijava.jar:CalculatorTest.class */
class CalculatorTest extends HiddenTest {
    CalculatorTest() {
    }

    void testNegativeNumbers(Program program) {
        program.addInput(-3);
        program.addInput(-4);
        program.algorithm();
        String capturedOutput = getCapturedOutput();
        program.assertTrue(contains(capturedOutput, "Sum: -7"));
        program.assertTrue(contains(capturedOutput, "Product: 12"));
    }

    void testLargeNumbers(Program program) {
        program.addInput(999999);
        program.addInput(1);
        program.algorithm();
        String capturedOutput = getCapturedOutput();
        program.assertTrue(contains(capturedOutput, "Sum: 1000000"));
        program.assertTrue(contains(capturedOutput, "Product: 999999"));
        program.clearCapturedOutput();
    }
}
