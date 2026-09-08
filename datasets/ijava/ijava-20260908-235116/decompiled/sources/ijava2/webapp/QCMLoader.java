package ijava2.webapp;

import ijava2.webapp.QCMDefinition;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: ijava.jar:ijava2/webapp/QCMLoader.class */
public class QCMLoader {
    public static QCMDefinition loadFromResource(String str, String str2) {
        QCMDefinition loadFromResource = MarkdownLoader.loadFromResource(str, str2);
        if (loadFromResource != null) {
            return loadFromResource;
        }
        return loadFromResourceLegacy(str, str2);
    }

    private static QCMDefinition loadFromResourceLegacy(String str, String str2) {
        InputStream resourceAsStream = QCMLoader.class.getResourceAsStream("/syllabus/" + str + "/" + str2 + ".qcm");
        if (resourceAsStream == null) {
            System.err.println("QCM definition not found (tried both .md and .qcm): " + str2);
            return null;
        }
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resourceAsStream));
            try {
                ArrayList arrayList = new ArrayList();
                while (true) {
                    String readLine = bufferedReader.readLine();
                    if (readLine != null) {
                        arrayList.add(readLine);
                    } else {
                        QCMDefinition parseQCMText = parseQCMText(arrayList);
                        bufferedReader.close();
                        return parseQCMText;
                    }
                }
            } finally {
            }
        } catch (IOException e) {
            System.err.println("Error reading QCM definition: " + e.getMessage());
            return null;
        }
    }

    private static QCMDefinition parseQCMText(List<String> list) {
        char charAt;
        QCMDefinition qCMDefinition = new QCMDefinition();
        qCMDefinition.questions = new ArrayList();
        QCMDefinition.Question question = null;
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            String trim = it.next().trim();
            if (!trim.isEmpty()) {
                if (trim.startsWith("TITLE:")) {
                    qCMDefinition.title = trim.substring(6).trim();
                } else if (trim.startsWith("Q:")) {
                    if (question != null) {
                        qCMDefinition.questions.add(question);
                    }
                    question = new QCMDefinition.Question();
                    question.question = trim.substring(2).trim();
                    question.options = new ArrayList();
                } else if (question != null && trim.matches("^[A-Z]\\).*")) {
                    question.type = "multiple_choice";
                    question.options.add(trim.substring(2).trim());
                    if (question.explanations == null) {
                        question.explanations = new ArrayList();
                    }
                } else if (question != null && trim.startsWith("EXPLAIN:") && "multiple_choice".equals(question.type) && question.explanations != null) {
                    question.explanations.add(trim.substring(8).trim());
                } else if (question != null && trim.startsWith("ANSWER:") && "multiple_choice".equals(question.type)) {
                    String trim2 = trim.substring(7).trim();
                    if (trim2.length() == 1 && (charAt = trim2.charAt(0)) >= 'A' && charAt <= 'Z') {
                        question.correct = Integer.valueOf(charAt - 'A');
                    }
                } else if (question != null && trim.startsWith("ANSWER:")) {
                    question.type = "text";
                    question.correctText = trim.substring(7).trim();
                } else if (question != null && trim.startsWith("EXPLAIN:") && "text".equals(question.type)) {
                    question.explanation = trim.substring(8).trim();
                }
            }
        }
        if (question != null) {
            qCMDefinition.questions.add(question);
        }
        return qCMDefinition;
    }
}
