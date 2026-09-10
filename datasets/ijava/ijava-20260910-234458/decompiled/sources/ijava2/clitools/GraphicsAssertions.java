package ijava2.clitools;

/* loaded from: ijava.jar:ijava2/clitools/GraphicsAssertions.class */
public class GraphicsAssertions {
    public static void assertPenState(boolean z, boolean z2, String str, int i) {
        String str2;
        String str3 = z ? "down (drawing)" : "up (not drawing)";
        String str4 = z2 ? "down (drawing)" : "up (not drawing)";
        if (z) {
            str2 = "Make sure you called penDown() to start drawing";
        } else {
            str2 = "Make sure you called penUp() to stop drawing";
        }
        TestAssertions.assertEqual(str3, str4, str, i, str2);
    }

    public static void assertPixelColor(int i, int i2, int i3, int i4, String str, int i5) {
        TestAssertions.assertEqual(Integer.valueOf(i), Integer.valueOf(i2), str, i5, String.format("Pixel at position (%d,%d) should be %s but was %s. This might indicate an issue with your drawing algorithm or pen state.", Integer.valueOf(i3), Integer.valueOf(i4), colorToString(i), colorToString(i2)));
    }

    public static void assertCanvasOutput(String str, String str2, String str3, int i) {
        TestAssertions.assertEqual(str, str2, str3, i, analyzeCanvasDifference(str, str2));
    }

    public static void assertDrawingResult(String str, String str2, String str3, String str4, int i) {
        TestAssertions.assertEqual(str, str2, str4, i, String.format("After %s, the drawing should look different. Check if your %s method is correctly implemented and if the pen is in the right state.", str3, str3));
    }

    public static void assertPosition(int i, int i2, int i3, int i4, String str, int i5) {
        if (i != i3 || i2 != i4) {
            throw new AssertionTestException(str, "assertPosition", String.format("(%d,%d)", Integer.valueOf(i), Integer.valueOf(i2)), String.format("(%d,%d)", Integer.valueOf(i3), Integer.valueOf(i4)), i5, null, String.format("Position should be (%d,%d) but was (%d,%d). Check your movement methods (forward, backward, turn, etc.)", Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3), Integer.valueOf(i4)));
        }
    }

    private static String analyzeCanvasDifference(String str, String str2) {
        if (str == null || str2 == null) {
            return "One of the canvas outputs is null. Check if your drawing methods are being called.";
        }
        if (str.length() != str2.length()) {
            return String.format("Canvas size mismatch: expected %d characters but got %d. This might indicate missing or extra drawing operations.", Integer.valueOf(str.length()), Integer.valueOf(str2.length()));
        }
        int i = 0;
        for (int i2 = 0; i2 < Math.min(str.length(), str2.length()); i2++) {
            if (str.charAt(i2) != str2.charAt(i2)) {
                i++;
            }
        }
        if (i == 0) {
            return "Canvas outputs are identical but test failed - this might be a test framework issue.";
        }
        if (str.contains("000") && str2.contains("255")) {
            return "Expected black pixels (0) but found white pixels (255). This suggests the pen might be up when it should be down, or vice versa.";
        }
        if (str.contains("255") && str2.contains("000")) {
            return "Expected white pixels (255) but found black pixels (0). This suggests the pen might be down when it should be up, or drawing in wrong areas.";
        }
        return String.format("Found %d pixel differences out of %d total pixels. Check your drawing logic, pen state, and movement calculations.", Integer.valueOf(i), Integer.valueOf(str.length()));
    }

    private static String colorToString(int i) {
        switch (i) {
            case 0:
                return "black (0)";
            case 255:
                return "white (255)";
            default:
                return String.format("color(%d)", Integer.valueOf(i));
        }
    }
}
