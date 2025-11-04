package ijava2.tools;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.List;
import java.util.stream.Stream;

/* loaded from: ijava.jar:ijava2/tools/MarkdownToHtmlConverter.class */
public class MarkdownToHtmlConverter {
    private static final Parser parser;
    private static final HtmlRenderer renderer;
    private static final String HTML_TEMPLATE = "<!DOCTYPE html>\n<html>\n<head>\n    <meta charset=\"UTF-8\">\n    <title>%s - %s</title>\n    <link rel=\"stylesheet\" href=\"/static/style.css\">\n</head>\n<body>\n    <header>\n        <h1>%s</h1>\n        <p class=\"session-info\">%s / %s.java</p>\n    </header>\n    <main>\n%s\n    </main>\n</body>\n</html>";

    static {
        MutableDataSet mutableDataSet = new MutableDataSet();
        parser = Parser.builder(mutableDataSet).build();
        renderer = HtmlRenderer.builder(mutableDataSet).build();
    }

    private static String extractTitle(String str) {
        for (String str2 : str.split("\\n")) {
            if (str2.startsWith("# ")) {
                return str2.substring(2).trim();
            }
        }
        return "Exercise";
    }

    private static String removeFirstH1(String str) {
        int indexOf = str.indexOf("<h1>");
        if (indexOf >= 0) {
            return str.substring(0, indexOf) + str.substring(str.indexOf("</h1>", indexOf) + 5);
        }
        return str;
    }

    public static boolean convertFile(Path path, Path path2) {
        try {
            String readString = Files.readString(path, StandardCharsets.UTF_8);
            String removeFirstH1 = removeFirstH1(renderer.render(parser.parse(readString)));
            String path3 = path.getParent().getFileName().toString();
            String replaceFirst = path.getFileName().toString().replaceFirst("\\.md$", "");
            String format = String.format(HTML_TEMPLATE, replaceFirst, path3, extractTitle(readString), path3, replaceFirst, removeFirstH1);
            Path resolve = path2.resolve("syllabus").resolve(path3).resolve(replaceFirst + ".html");
            Files.createDirectories(resolve.getParent(), new FileAttribute[0]);
            Files.writeString(resolve, format, StandardCharsets.UTF_8, new OpenOption[0]);
            return true;
        } catch (IOException e) {
            System.err.println("Error converting " + String.valueOf(path) + ": " + e.getMessage());
            return false;
        }
    }

    public static boolean processDirectory(Path path, Path path2) {
        String str;
        try {
            if (!Files.exists(path, new LinkOption[0])) {
                System.err.println("Syllabus directory not found: " + String.valueOf(path));
                return false;
            }
            Stream<Path> walk = Files.walk(path, new FileVisitOption[0]);
            try {
                List<Path> list = walk.filter(path3 -> {
                    return Files.isRegularFile(path3, new LinkOption[0]);
                }).filter(path4 -> {
                    return path4.toString().endsWith(".md");
                }).toList();
                if (walk != null) {
                    walk.close();
                }
                if (list.isEmpty()) {
                    System.out.println("No .md files found in syllabus directory");
                    return true;
                }
                System.out.println("Found " + list.size() + " Markdown files to convert");
                int i = 0;
                for (Path path5 : list) {
                    String path6 = path5.getParent().getFileName().toString();
                    String replaceFirst = path5.getFileName().toString().replaceFirst("\\.md$", "");
                    if (path2.getFileName().toString().equals("todo")) {
                        str = "todo/" + path6 + "/" + replaceFirst + ".html";
                    } else {
                        str = "build/syllabus/" + path6 + "/" + replaceFirst + ".html";
                    }
                    System.out.println("Converting " + path6 + "/" + replaceFirst + ".md -> " + str);
                    if (convertFile(path5, path2)) {
                        i++;
                    } else {
                        System.err.println("  ❌ Failed to convert " + String.valueOf(path5));
                    }
                }
                System.out.println("Successfully converted " + i + "/" + list.size() + " files");
                return i == list.size();
            } finally {
            }
        } catch (IOException e) {
            System.err.println("Error processing directory: " + e.getMessage());
            return false;
        }
    }

    public static void main(String[] strArr) {
        if (strArr.length != 2) {
            System.err.println("Usage: java -cp production/ijava2.jar ijava2.tools.MarkdownToHtmlConverter <syllabus-directory> <output-directory>");
            System.err.println("Professor workflow:");
            System.err.println("  java -cp production/ijava2.jar ijava2.tools.MarkdownToHtmlConverter src/main/java/syllabus todo");
            System.err.println("This generates HTML files in todo/ for review before including in JAR");
            System.exit(1);
        }
        if (processDirectory(Paths.get(strArr[0], new String[0]), Paths.get(strArr[1], new String[0]))) {
            System.out.println("✅ All Markdown files converted to HTML successfully");
            System.exit(0);
        } else {
            System.err.println("❌ Some files failed to convert");
            System.exit(1);
        }
    }
}
