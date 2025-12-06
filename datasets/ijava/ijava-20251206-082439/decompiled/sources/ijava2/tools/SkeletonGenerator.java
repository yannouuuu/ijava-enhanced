package ijava2.tools;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: ijava.jar:ijava2/tools/SkeletonGenerator.class */
public class SkeletonGenerator {
    public static void main(String[] strArr) {
        if (strArr.length != 2) {
            System.err.println("Usage: java ijava2.tools.SkeletonGenerator <source-dir> <output-dir>");
            System.err.println("Example: java ijava2.tools.SkeletonGenerator src/main/java/syllabus/tp1 build/skeletons/tp1");
            System.exit(1);
        }
        new SkeletonGenerator().generateSkeletons(Paths.get(strArr[0], new String[0]), Paths.get(strArr[1], new String[0]));
    }

    public void generateSkeletons(Path path, Path path2) {
        try {
            if (!Files.exists(path, new LinkOption[0])) {
                System.err.println("Source directory does not exist: " + String.valueOf(path));
                return;
            }
            Files.createDirectories(path2, new FileAttribute[0]);
            Files.list(path).filter(path3 -> {
                return path3.toString().endsWith(".java");
            }).filter(path4 -> {
                return !path4.getFileName().toString().endsWith("Test.java");
            }).forEach(path5 -> {
                try {
                    generateSkeleton(path5, path2);
                } catch (IOException e) {
                    System.err.println("Error processing " + String.valueOf(path5) + ": " + e.getMessage());
                }
            });
            System.out.println("Skeleton generation complete. Output in: " + String.valueOf(path2));
        } catch (IOException e) {
            System.err.println("Error generating skeletons: " + e.getMessage());
        }
    }

    private void generateSkeleton(Path path, Path path2) throws IOException {
        String createSkeletonContent = createSkeletonContent(Files.readString(path));
        Path resolve = path2.resolve(path.getFileName());
        Files.writeString(resolve, createSkeletonContent, new OpenOption[0]);
        System.out.println("Generated skeleton: " + String.valueOf(resolve));
    }

    /* JADX WARN: Code restructure failed: missing block: B:33:0x0048, code lost:
    
        r7 = (java.lang.String) r0.getMethod("value", new java.lang.Class[0]).invoke(r0, new java.lang.Object[0]);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private java.lang.String createSkeletonContent(java.lang.String r5) {
        /*
            Method dump skipped, instructions count: 268
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: ijava2.tools.SkeletonGenerator.createSkeletonContent(java.lang.String):java.lang.String");
    }

    private String extractClassName(String str) {
        Matcher matcher = Pattern.compile("class\\s+(\\w+)", 8).matcher(str);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private String processInjectedContent(String str) {
        if (str == null || str.trim().isEmpty()) {
            return "";
        }
        String trim = str.trim();
        StringBuilder sb = new StringBuilder();
        for (String str2 : trim.split("\n")) {
            if (!str2.trim().isEmpty()) {
                sb.append("    ").append(str2).append("\n");
            } else {
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}
