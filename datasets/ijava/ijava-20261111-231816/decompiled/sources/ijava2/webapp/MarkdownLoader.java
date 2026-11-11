package ijava2.webapp;

import ijava2.webapp.QCMDefinition;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: ijava.jar:ijava2/webapp/MarkdownLoader.class */
public class MarkdownLoader {
    private static final Pattern QUESTION_START = Pattern.compile("^:::?\\s*question\\s*$");
    private static final Pattern QUESTION_END = Pattern.compile("^:::?\\s*$");
    private static final Pattern QUESTION_TEXT = Pattern.compile("^\\*\\*(.+?)\\*\\*\\s*$");
    private static final Pattern OPTION_UNCHECKED = Pattern.compile("^-\\s*\\[\\s*\\]\\s*(.+)$");
    private static final Pattern OPTION_CHECKED = Pattern.compile("^-\\s*\\[x\\]\\s*(.+)$", 2);
    private static final Pattern EXPLANATION = Pattern.compile("^>\\s*(.*)$");
    private static final Pattern ANSWER_LINE = Pattern.compile("^Answer:\\s*(.+)$", 2);
    private static final Pattern TITLE_H1 = Pattern.compile("^#\\s+(.+)$");

    public static QCMDefinition loadFromResource(String str, String str2) {
        String str3 = "/syllabus/" + str + "/" + str2 + ".md";
        InputStream resourceAsStream = MarkdownLoader.class.getResourceAsStream(str3);
        if (resourceAsStream == null) {
            System.err.println("Markdown QCM not found: " + str3);
            return null;
        }
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resourceAsStream));
            try {
                ArrayList arrayList = new ArrayList();
                while (true) {
                    String readLine = bufferedReader.readLine();
                    if (readLine == null) {
                        break;
                    }
                    arrayList.add(readLine);
                }
                QCMDefinition parseMarkdown = parseMarkdown(arrayList);
                if (parseMarkdown != null) {
                    try {
                        InputStream resourceAsStream2 = MarkdownLoader.class.getResourceAsStream("/syllabus/" + str + "/" + str2 + ".html");
                        if (resourceAsStream2 != null) {
                            try {
                                parseMarkdown.fullHtmlContent = new String(resourceAsStream2.readAllBytes(), StandardCharsets.UTF_8);
                            } catch (Throwable th) {
                                if (resourceAsStream2 != null) {
                                    try {
                                        resourceAsStream2.close();
                                    } catch (Throwable th2) {
                                        th.addSuppressed(th2);
                                    }
                                }
                                throw th;
                            }
                        }
                        if (resourceAsStream2 != null) {
                            resourceAsStream2.close();
                        }
                    } catch (IOException e) {
                        System.err.println("Could not load HTML content for " + str2 + ": " + e.getMessage());
                    }
                }
                bufferedReader.close();
                return parseMarkdown;
            } finally {
            }
        } catch (IOException e2) {
            System.err.println("Error reading Markdown QCM: " + e2.getMessage());
            return null;
        }
    }

    public static QCMDefinition parseMarkdown(List<String> list) {
        QCMDefinition qCMDefinition = new QCMDefinition();
        qCMDefinition.questions = new ArrayList();
        String str = null;
        int i = 0;
        Iterator<String> it = list.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Matcher matcher = TITLE_H1.matcher(it.next().trim());
            if (matcher.matches()) {
                str = matcher.group(1).trim();
                break;
            }
        }
        qCMDefinition.title = str != null ? str : "Quiz";
        while (i < list.size()) {
            if (QUESTION_START.matcher(list.get(i).trim()).matches()) {
                QCMDefinition.Question parseQuestionBlock = parseQuestionBlock(list, i + 1);
                if (parseQuestionBlock != null) {
                    qCMDefinition.questions.add(parseQuestionBlock);
                }
                i = findQuestionBlockEnd(list, i + 1);
            }
            i++;
        }
        return qCMDefinition;
    }

    private static int findQuestionBlockEnd(List<String> list, int i) {
        for (int i2 = i; i2 < list.size(); i2++) {
            if (QUESTION_END.matcher(list.get(i2).trim()).matches()) {
                return i2;
            }
        }
        return list.size() - 1;
    }

    private static QCMDefinition.Question parseQuestionBlock(List<String> list, int i) {
        QCMDefinition.Question question = new QCMDefinition.Question();
        int i2 = i;
        boolean z = false;
        while (i2 < list.size()) {
            String trim = list.get(i2).trim();
            if (QUESTION_END.matcher(trim).matches()) {
                break;
            }
            if (trim.isEmpty()) {
                i2++;
            } else {
                if (!z) {
                    Matcher matcher = QUESTION_TEXT.matcher(trim);
                    if (matcher.matches()) {
                        question.question = matcher.group(1).trim();
                        z = true;
                        question.options = new ArrayList();
                        question.explanations = new ArrayList();
                        i2++;
                    }
                }
                Matcher matcher2 = ANSWER_LINE.matcher(trim);
                if (matcher2.matches()) {
                    question.type = "text";
                    question.correctText = matcher2.group(1).trim();
                    i2++;
                    if (i2 < list.size()) {
                        Matcher matcher3 = EXPLANATION.matcher(list.get(i2).trim());
                        if (matcher3.matches()) {
                            question.explanation = matcher3.group(1).trim();
                            while (true) {
                                i2++;
                                if (i2 < list.size()) {
                                    String trim2 = list.get(i2).trim();
                                    if (EXPLANATION.matcher(trim2).matches()) {
                                        question.explanation += " " + EXPLANATION.matcher(trim2).group(1).trim();
                                    }
                                }
                            }
                        }
                    }
                } else {
                    Matcher matcher4 = OPTION_UNCHECKED.matcher(trim);
                    if (matcher4.matches()) {
                        question.type = "multiple_choice";
                        question.options.add(matcher4.group(1).trim());
                        i2++;
                        if (i2 < list.size()) {
                            question.explanations.add(readExplanation(list, i2));
                            while (i2 < list.size() && EXPLANATION.matcher(list.get(i2).trim()).matches()) {
                                i2++;
                            }
                        }
                    } else {
                        Matcher matcher5 = OPTION_CHECKED.matcher(trim);
                        if (matcher5.matches()) {
                            question.type = "multiple_choice";
                            question.options.add(matcher5.group(1).trim());
                            question.correct = Integer.valueOf(question.options.size() - 1);
                            i2++;
                            if (i2 < list.size()) {
                                question.explanations.add(readExplanation(list, i2));
                                while (i2 < list.size() && EXPLANATION.matcher(list.get(i2).trim()).matches()) {
                                    i2++;
                                }
                            }
                        } else {
                            i2++;
                        }
                    }
                }
            }
        }
        if (question.question == null || question.question.isEmpty()) {
            return null;
        }
        return question;
    }

    private static String readExplanation(List<String> list, int i) {
        StringBuilder sb = new StringBuilder();
        for (int i2 = i; i2 < list.size(); i2++) {
            Matcher matcher = EXPLANATION.matcher(list.get(i2).trim());
            if (!matcher.matches()) {
                break;
            }
            if (sb.length() > 0) {
                sb.append(" ");
            }
            sb.append(matcher.group(1).trim());
        }
        return sb.toString();
    }
}
