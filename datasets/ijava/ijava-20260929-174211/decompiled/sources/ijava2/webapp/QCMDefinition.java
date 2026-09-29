package ijava2.webapp;

import java.util.List;

/* loaded from: ijava.jar:ijava2/webapp/QCMDefinition.class */
public class QCMDefinition {
    public String title;
    public List<Question> questions;
    public String fullHtmlContent;

    /* loaded from: ijava.jar:ijava2/webapp/QCMDefinition$Question.class */
    public static class Question {
        public String type;
        public String question;
        public List<String> options;
        public List<String> explanations;
        public Integer correct;
        public String correctText;
        public String explanation;

        public Question() {
        }

        public Question(String str, String str2) {
            this.type = str;
            this.question = str2;
        }

        public Question(String str, String str2, List<String> list, Integer num) {
            this.type = str;
            this.question = str2;
            this.options = list;
            this.correct = num;
        }
    }

    public QCMDefinition() {
    }

    public QCMDefinition(String str, List<Question> list) {
        this.title = str;
        this.questions = list;
    }

    public int getTotalQuestions() {
        if (this.questions != null) {
            return this.questions.size();
        }
        return 0;
    }

    public int calculateScore(List<String> list) {
        if (list == null || this.questions == null) {
            return 0;
        }
        int i = 0;
        for (int i2 = 0; i2 < Math.min(list.size(), this.questions.size()); i2++) {
            if (isCorrectAnswer(this.questions.get(i2), list.get(i2))) {
                i++;
            }
        }
        return i;
    }

    private boolean isCorrectAnswer(Question question, String str) {
        if (str == null) {
            return false;
        }
        String str2 = question.type;
        boolean z = -1;
        switch (str2.hashCode()) {
            case 3556653:
                if (str2.equals("text")) {
                    z = true;
                    break;
                }
                break;
            case 1669382832:
                if (str2.equals("multiple_choice")) {
                    z = false;
                    break;
                }
                break;
        }
        switch (z) {
            case false:
                try {
                    int parseInt = Integer.parseInt(str);
                    if (question.correct != null) {
                        if (parseInt == question.correct.intValue()) {
                            return true;
                        }
                    }
                    return false;
                } catch (NumberFormatException e) {
                    return false;
                }
            case true:
                return question.correctText != null && question.correctText.trim().equalsIgnoreCase(str.trim());
            default:
                return false;
        }
    }
}
