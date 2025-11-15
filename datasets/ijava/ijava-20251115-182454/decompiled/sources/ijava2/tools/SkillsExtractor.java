package ijava2.tools;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* loaded from: ijava.jar:ijava2/tools/SkillsExtractor.class */
public class SkillsExtractor {
    public static void main(String[] strArr) {
        if (strArr.length != 3) {
            System.err.println("Usage: java ijava2.tools.SkillsExtractor <source-dir> <progression-file> <output-file>");
            System.err.println("Example: java ijava2.tools.SkillsExtractor src/main/java/syllabus/tp1 src/main/java/syllabus/tp1/tp1.progression build/tp1.skills");
            System.exit(1);
        }
        new SkillsExtractor().extractSkills(Paths.get(strArr[0], new String[0]), Paths.get(strArr[1], new String[0]), Paths.get(strArr[2], new String[0]));
    }

    public void extractSkills(Path path, Path path2, Path path3) {
        try {
            if (!Files.exists(path, new LinkOption[0])) {
                System.err.println("Source directory does not exist: " + String.valueOf(path));
                return;
            }
            if (!Files.exists(path2, new LinkOption[0])) {
                System.err.println("Progression file does not exist: " + String.valueOf(path2));
                return;
            }
            List<String> list = Files.readAllLines(path2).stream().map((v0) -> {
                return v0.trim();
            }).filter(str -> {
                return !str.isEmpty();
            }).toList();
            Files.createDirectories(path3.getParent(), new FileAttribute[0]);
            ClassLoader createClassLoader = createClassLoader(path);
            ArrayList arrayList = new ArrayList();
            for (String str2 : list) {
                Path resolve = path.resolve(str2 + ".java");
                if (!Files.exists(resolve, new LinkOption[0])) {
                    System.err.println("Warning: Exercise file not found: " + String.valueOf(resolve));
                    arrayList.add(str2 + " : []");
                } else {
                    String formatSkillsAsJson = formatSkillsAsJson(extractSkillsFromClass(str2, path, createClassLoader));
                    arrayList.add(str2 + " : " + formatSkillsAsJson);
                    System.out.println("Extracted skills for " + str2 + ": " + formatSkillsAsJson);
                }
            }
            Files.write(path3, arrayList, new OpenOption[0]);
            System.out.println("Skills file generated: " + String.valueOf(path3));
            Path resolve2 = path.resolve(String.valueOf(path.getFileName()) + ".skills");
            Files.write(resolve2, arrayList, new OpenOption[0]);
            System.out.println("Skills file also saved in source directory: " + String.valueOf(resolve2));
        } catch (IOException e) {
            System.err.println("Error extracting skills: " + e.getMessage());
        }
    }

    private ClassLoader createClassLoader(Path path) {
        try {
            Path absolutePath = path.toAbsolutePath();
            Path path2 = null;
            while (true) {
                if (absolutePath == null) {
                    break;
                }
                if (Files.exists(absolutePath.resolve("build"), new LinkOption[0])) {
                    path2 = absolutePath;
                    break;
                }
                absolutePath = absolutePath.getParent();
            }
            if (path2 == null) {
                throw new RuntimeException("Could not find build directory. Please compile first. Searched from: " + String.valueOf(path.toAbsolutePath()));
            }
            Path resolve = path2.resolve("build/classes");
            if (!Files.exists(resolve, new LinkOption[0])) {
                throw new RuntimeException("Build classes directory not found: " + String.valueOf(resolve) + ". Please compile first.");
            }
            System.out.println("Using build directory: " + String.valueOf(resolve));
            return new URLClassLoader(new URL[]{resolve.toUri().toURL()}, getClass().getClassLoader());
        } catch (Exception e) {
            throw new RuntimeException("Failed to create class loader: " + e.getMessage(), e);
        }
    }

    private List<String> extractSkillsFromClass(String str, Path path, ClassLoader classLoader) {
        ArrayList arrayList = new ArrayList();
        try {
            System.out.println("Loading class: " + str);
            for (Method method : classLoader.loadClass(str).getDeclaredMethods()) {
                if (method.isAnnotationPresent(getSkillAnnotationClass(classLoader))) {
                    Annotation annotation = method.getAnnotation(getSkillAnnotationClass(classLoader));
                    String[] strArr = (String[]) annotation.getClass().getMethod("value", new Class[0]).invoke(annotation, new Object[0]);
                    arrayList.addAll(Arrays.asList(strArr));
                    System.out.println("  Found @skill on method " + method.getName() + ": " + Arrays.toString(strArr));
                }
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Warning: Could not find compiled class for " + str + ". Please ensure the class is compiled.");
        } catch (Exception e2) {
            System.err.println("Error extracting skills from class " + str + ": " + e2.getMessage());
            e2.printStackTrace();
        }
        return arrayList;
    }

    private Class<? extends Annotation> getSkillAnnotationClass(ClassLoader classLoader) throws ClassNotFoundException {
        return classLoader.loadClass("skill");
    }

    private String formatSkillsAsJson(List<String> list) {
        if (list.isEmpty()) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append("'").append(list.get(i)).append("'");
        }
        sb.append("]");
        return sb.toString();
    }
}
