package ijava2.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: ijava.jar:ijava2/tools/SkillsMetadata.class */
public class SkillsMetadata {

    /* loaded from: ijava.jar:ijava2/tools/SkillsMetadata$ExerciseMetadata.class */
    public static class ExerciseMetadata {
        public String exerciseName;
        public List<String> skills = new ArrayList();
        public Integer totalQuestions;
        public Integer totalStudentTests;
        public Integer totalProfTests;

        public ExerciseMetadata(String str) {
            this.exerciseName = str;
        }

        public boolean hasMetadata() {
            return (this.totalQuestions == null && this.totalStudentTests == null && this.totalProfTests == null) ? false : true;
        }

        public String toSkillsFileLine() {
            StringBuilder sb = new StringBuilder();
            sb.append(this.exerciseName).append(" : ");
            if (this.skills.isEmpty()) {
                sb.append("[]");
            } else {
                sb.append("[");
                for (int i = 0; i < this.skills.size(); i++) {
                    if (i > 0) {
                        sb.append(", ");
                    }
                    sb.append("'").append(this.skills.get(i)).append("'");
                }
                sb.append("]");
            }
            if (hasMetadata()) {
                sb.append(" | ");
                ArrayList arrayList = new ArrayList();
                if (this.totalQuestions != null) {
                    arrayList.add("questions:" + this.totalQuestions);
                }
                if (this.totalStudentTests != null) {
                    arrayList.add("student_tests:" + this.totalStudentTests);
                }
                if (this.totalProfTests != null) {
                    arrayList.add("prof_tests:" + this.totalProfTests);
                }
                sb.append(String.join(",", arrayList));
            }
            return sb.toString();
        }
    }

    public static Map<String, ExerciseMetadata> loadFromFile(Path path) throws IOException {
        ExerciseMetadata parseLine;
        HashMap hashMap = new HashMap();
        if (!Files.exists(path, new LinkOption[0])) {
            return hashMap;
        }
        for (String str : Files.readAllLines(path)) {
            if (!str.trim().isEmpty() && (parseLine = parseLine(str)) != null) {
                hashMap.put(parseLine.exerciseName, parseLine);
            }
        }
        return hashMap;
    }

    private static ExerciseMetadata parseLine(String str) {
        try {
            int indexOf = str.indexOf(" : ");
            if (indexOf == -1) {
                return null;
            }
            String trim = str.substring(0, indexOf).trim();
            String trim2 = str.substring(indexOf + 3).trim();
            ExerciseMetadata exerciseMetadata = new ExerciseMetadata(trim);
            int indexOf2 = trim2.indexOf(" | ");
            String str2 = null;
            if (indexOf2 != -1) {
                trim2.substring(0, indexOf2).trim();
                str2 = trim2.substring(indexOf2 + 3).trim();
            }
            if (str2 != null) {
                parseMetadata(exerciseMetadata, str2);
            }
            return exerciseMetadata;
        } catch (Exception e) {
            System.err.println("Warning: Could not parse skills line: " + str);
            return null;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Failed to find 'out' block for switch in B:21:0x00a7. Please report as an issue. */
    /* JADX WARN: Failed to find 'out' block for switch in B:9:0x0055. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:22:0x00c0 A[Catch: NumberFormatException -> 0x00e4, TryCatch #0 {NumberFormatException -> 0x00e4, blocks: (B:8:0x0042, B:9:0x0055, B:10:0x0078, B:14:0x0088, B:17:0x0098, B:21:0x00a7, B:22:0x00c0, B:23:0x00cc, B:24:0x00d8), top: B:7:0x0042 }] */
    /* JADX WARN: Removed duplicated region for block: B:23:0x00cc A[Catch: NumberFormatException -> 0x00e4, TryCatch #0 {NumberFormatException -> 0x00e4, blocks: (B:8:0x0042, B:9:0x0055, B:10:0x0078, B:14:0x0088, B:17:0x0098, B:21:0x00a7, B:22:0x00c0, B:23:0x00cc, B:24:0x00d8), top: B:7:0x0042 }] */
    /* JADX WARN: Removed duplicated region for block: B:24:0x00d8 A[Catch: NumberFormatException -> 0x00e4, TryCatch #0 {NumberFormatException -> 0x00e4, blocks: (B:8:0x0042, B:9:0x0055, B:10:0x0078, B:14:0x0088, B:17:0x0098, B:21:0x00a7, B:22:0x00c0, B:23:0x00cc, B:24:0x00d8), top: B:7:0x0042 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static void parseMetadata(ijava2.tools.SkillsMetadata.ExerciseMetadata r3, java.lang.String r4) {
        /*
            r0 = r4
            java.lang.String r1 = ","
            java.lang.String[] r0 = r0.split(r1)
            r5 = r0
            r0 = r5
            r6 = r0
            r0 = r6
            int r0 = r0.length
            r7 = r0
            r0 = 0
            r8 = r0
        L10:
            r0 = r8
            r1 = r7
            if (r0 >= r1) goto Lec
            r0 = r6
            r1 = r8
            r0 = r0[r1]
            r9 = r0
            r0 = r9
            java.lang.String r0 = r0.trim()
            java.lang.String r1 = ":"
            java.lang.String[] r0 = r0.split(r1)
            r10 = r0
            r0 = r10
            int r0 = r0.length
            r1 = 2
            if (r0 != r1) goto Le6
            r0 = r10
            r1 = 0
            r0 = r0[r1]
            java.lang.String r0 = r0.trim()
            r11 = r0
            r0 = r10
            r1 = 1
            r0 = r0[r1]
            java.lang.String r0 = r0.trim()
            r12 = r0
            r0 = r12
            int r0 = java.lang.Integer.parseInt(r0)     // Catch: java.lang.NumberFormatException -> Le4
            r13 = r0
            r0 = r11
            r14 = r0
            r0 = -1
            r15 = r0
            r0 = r14
            int r0 = r0.hashCode()     // Catch: java.lang.NumberFormatException -> Le4
            switch(r0) {
                case -1782234803: goto L78;
                case -1381519941: goto L98;
                case -581739747: goto L88;
                default: goto La5;
            }     // Catch: java.lang.NumberFormatException -> Le4
        L78:
            r0 = r14
            java.lang.String r1 = "questions"
            boolean r0 = r0.equals(r1)     // Catch: java.lang.NumberFormatException -> Le4
            if (r0 == 0) goto La5
            r0 = 0
            r15 = r0
            goto La5
        L88:
            r0 = r14
            java.lang.String r1 = "student_tests"
            boolean r0 = r0.equals(r1)     // Catch: java.lang.NumberFormatException -> Le4
            if (r0 == 0) goto La5
            r0 = 1
            r15 = r0
            goto La5
        L98:
            r0 = r14
            java.lang.String r1 = "prof_tests"
            boolean r0 = r0.equals(r1)     // Catch: java.lang.NumberFormatException -> Le4
            if (r0 == 0) goto La5
            r0 = 2
            r15 = r0
        La5:
            r0 = r15
            switch(r0) {
                case 0: goto Lc0;
                case 1: goto Lcc;
                case 2: goto Ld8;
                default: goto Le1;
            }     // Catch: java.lang.NumberFormatException -> Le4
        Lc0:
            r0 = r3
            r1 = r13
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch: java.lang.NumberFormatException -> Le4
            r0.totalQuestions = r1     // Catch: java.lang.NumberFormatException -> Le4
            goto Le1
        Lcc:
            r0 = r3
            r1 = r13
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch: java.lang.NumberFormatException -> Le4
            r0.totalStudentTests = r1     // Catch: java.lang.NumberFormatException -> Le4
            goto Le1
        Ld8:
            r0 = r3
            r1 = r13
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch: java.lang.NumberFormatException -> Le4
            r0.totalProfTests = r1     // Catch: java.lang.NumberFormatException -> Le4
        Le1:
            goto Le6
        Le4:
            r13 = move-exception
        Le6:
            int r8 = r8 + 1
            goto L10
        Lec:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: ijava2.tools.SkillsMetadata.parseMetadata(ijava2.tools.SkillsMetadata$ExerciseMetadata, java.lang.String):void");
    }

    public static ExerciseMetadata loadForExercise(String str, String str2) {
        String readLine;
        try {
            Path path = Paths.get("build", str + ".skills");
            if (Files.exists(path, new LinkOption[0])) {
                return loadFromFile(path).get(str2);
            }
            InputStream resourceAsStream = SkillsMetadata.class.getResourceAsStream("/syllabus/" + str + "/" + str + ".skills");
            if (resourceAsStream != null) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resourceAsStream));
                do {
                    readLine = bufferedReader.readLine();
                    if (readLine == null) {
                        bufferedReader.close();
                    }
                } while (!readLine.startsWith(str2 + " :"));
                ExerciseMetadata parseLine = parseLine(readLine);
                bufferedReader.close();
                return parseLine;
            }
            return null;
        } catch (Exception e) {
            System.err.println("Debug: Could not load metadata for " + str + "/" + str2 + ": " + e.getMessage());
            return null;
        }
    }
}
