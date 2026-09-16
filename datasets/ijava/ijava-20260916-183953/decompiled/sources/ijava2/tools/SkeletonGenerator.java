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
                    e.printStackTrace();
                }
            });
            System.out.println("Skeleton generation complete. Output in: " + String.valueOf(path2));
        } catch (IOException e) {
            System.err.println("Error generating skeletons: " + e.getMessage());
        }
    }

    private void generateSkeleton(Path path, Path path2) throws IOException {
        String readString = Files.readString(path);
        if (readString.contains("@Student")) {
            System.out.println("Processing " + String.valueOf(path.getFileName()) + " (New System - @Student)");
            Files.writeString(path2.resolve(path.getFileName()), generateFromSourceParser(readString), new OpenOption[0]);
        } else {
            System.out.println("Processing " + String.valueOf(path.getFileName()) + " (Legacy System - @inject)");
            Files.writeString(path2.resolve(path.getFileName()), generateFromLegacyReflection(readString), new OpenOption[0]);
        }
    }

    private String generateFromSourceParser(String str) {
        String trim;
        String trim2;
        StringBuilder sb = new StringBuilder();
        String[] split = str.split("\n");
        boolean z = false;
        int i = 0;
        int i2 = 0;
        while (i2 < split.length) {
            String str2 = split[i2];
            String trim3 = str2.trim();
            if (!z) {
                if (trim3.startsWith("package ") || trim3.startsWith("import ")) {
                    if (!trim3.contains("Student")) {
                        sb.append(str2).append("\n");
                    }
                } else if (trim3.startsWith("public class ") || trim3.startsWith("class ") || trim3.startsWith("abstract class ")) {
                    sb.append(str2).append("\n");
                    if (trim3.contains("{")) {
                        i++;
                    }
                    z = true;
                } else if (trim3.isEmpty() || trim3.startsWith("//") || trim3.startsWith("/*")) {
                    sb.append(str2).append("\n");
                }
                i2++;
            } else if (trim3.startsWith("@Student")) {
                Object obj = "STUB";
                if (trim3.contains("Mode.COPY")) {
                    obj = "COPY";
                }
                if (trim3.contains("Mode.IGNORE")) {
                    obj = "IGNORE";
                }
                i2++;
                StringBuilder sb2 = new StringBuilder();
                while (i2 < split.length && !split[i2].trim().endsWith("{")) {
                    if (!split[i2].trim().isEmpty()) {
                        sb2.append(split[i2]).append("\n");
                    }
                    i2++;
                }
                if (i2 < split.length) {
                    sb2.append(split[i2]);
                }
                if ("IGNORE".equals(obj)) {
                    skipMethodBody(split, i2);
                    i2 = findEndOfMethod(split, i2) + 1;
                } else if ("STUB".equals(obj)) {
                    sb.append((CharSequence) sb2).append("\n");
                    sb.append("        // TODO: Complete this method\n");
                    sb.append("    }\n\n");
                    i2 = findEndOfMethod(split, i2) + 1;
                } else if ("COPY".equals(obj)) {
                    sb.append((CharSequence) sb2).append("\n");
                    int findEndOfMethod = findEndOfMethod(split, i2);
                    int i3 = i2 + 1;
                    while (i3 < findEndOfMethod) {
                        String str3 = split[i3];
                        String trim4 = str3.trim();
                        if (trim4.startsWith("// @replace-start")) {
                            int indexOf = trim4.indexOf(":");
                            if (indexOf > 0) {
                                trim2 = trim4.substring(indexOf + 1).trim();
                            } else {
                                trim2 = trim4.substring("// @replace-start".length()).trim();
                            }
                            String str4 = "";
                            int indexOf2 = str3.indexOf("//");
                            if (indexOf2 > 0) {
                                str4 = str3.substring(0, indexOf2);
                            }
                            if (!trim2.isEmpty()) {
                                sb.append(str4).append(trim2).append("\n");
                            }
                            do {
                                i3++;
                                if (i3 < findEndOfMethod) {
                                }
                            } while (!split[i3].trim().startsWith("// @replace-end"));
                        } else if (trim4.startsWith("// @replace")) {
                            int indexOf3 = trim4.indexOf(":");
                            if (indexOf3 > 0) {
                                trim = trim4.substring(indexOf3 + 1).trim();
                            } else {
                                trim = trim4.substring("// @replace".length()).trim();
                            }
                            String str5 = "";
                            int indexOf4 = str3.indexOf("//");
                            if (indexOf4 > 0) {
                                str5 = str3.substring(0, indexOf4);
                            }
                            if (!trim.isEmpty()) {
                                sb.append(str5).append(trim).append("\n");
                            }
                            i3++;
                        } else {
                            sb.append(str3).append("\n");
                        }
                        i3++;
                    }
                    sb.append("    }\n\n");
                    i2 = findEndOfMethod + 1;
                }
            } else if (trim3.equals("}")) {
                sb.append("}");
                i2++;
            } else if (trim3.startsWith("@skill") || trim3.startsWith("@test")) {
                i2++;
            } else {
                if (!trim3.isEmpty() && !trim3.equals("}")) {
                    if (!trim3.endsWith("{")) {
                        sb.append(str2).append("\n");
                    } else {
                        i2 = findEndOfMethod(split, i2) + 1;
                    }
                } else {
                    sb.append(str2).append("\n");
                }
                i2++;
            }
        }
        return sb.toString();
    }

    private void skipMethodBody(String[] strArr, int i) {
    }

    private int findEndOfMethod(String[] strArr, int i) {
        int i2 = 0;
        boolean z = false;
        for (int i3 = i; i3 < strArr.length; i3++) {
            for (char c : strArr[i3].toCharArray()) {
                if (c == '{') {
                    i2++;
                    z = true;
                } else if (c == '}') {
                    i2--;
                }
            }
            if (z && i2 == 0) {
                return i3;
            }
        }
        return strArr.length - 1;
    }

    /* JADX WARN: Code restructure failed: missing block: B:33:0x0048, code lost:
    
        r7 = (java.lang.String) r0.getMethod("value", new java.lang.Class[0]).invoke(r0, new java.lang.Object[0]);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private java.lang.String generateFromLegacyReflection(java.lang.String r5) {
        /*
            Method dump skipped, instructions count: 298
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: ijava2.tools.SkeletonGenerator.generateFromLegacyReflection(java.lang.String):java.lang.String");
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
