package ijava2.webapp;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import ijava2.clitools.AnalyticsLogger;
import ijava2.webapp.QCMDefinition;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.h2.engine.Constants;

/* loaded from: ijava.jar:ijava2/webapp/UnifiedWebApp.class */
public class UnifiedWebApp {
    private static final int PORT = 8080;
    private static final int AUTO_SHUTDOWN_MINUTES = 30;
    private static UnifiedWebApp instance;
    private HttpServer server;
    private ScheduledExecutorService scheduler;
    private boolean isRunning = false;
    private long lastActivity = System.currentTimeMillis();

    private UnifiedWebApp() {
    }

    public static synchronized UnifiedWebApp getInstance() {
        if (instance == null) {
            instance = new UnifiedWebApp();
        }
        return instance;
    }

    public synchronized void start() {
        if (this.isRunning) {
            System.out.println("Web server already running on http://localhost:8080");
            return;
        }
        try {
            this.server = HttpServer.create(new InetSocketAddress(PORT), 0);
            this.server.createContext("/", this::handleRequest);
            this.server.setExecutor(Executors.newCachedThreadPool(runnable -> {
                Thread thread = new Thread(runnable, "ijava-web-server");
                thread.setDaemon(true);
                return thread;
            }));
            this.server.start();
            this.isRunning = true;
            startAutoShutdownScheduler();
            System.out.println("Web server started on http://localhost:8080 (running as daemon)");
        } catch (IOException e) {
            System.err.println("Failed to start web server: " + e.getMessage());
        }
    }

    public synchronized void stop() {
        if (this.isRunning) {
            if (this.server != null) {
                this.server.stop(0);
                this.isRunning = false;
            }
            if (this.scheduler != null) {
                this.scheduler.shutdown();
            }
            System.out.println("Web server stopped");
        }
    }

    private void startAutoShutdownScheduler() {
        this.scheduler = Executors.newSingleThreadScheduledExecutor(runnable -> {
            Thread thread = new Thread(runnable, "ijava-web-scheduler");
            thread.setDaemon(true);
            return thread;
        });
        this.scheduler.scheduleWithFixedDelay(() -> {
            if (System.currentTimeMillis() - this.lastActivity > 1800000) {
                System.out.println("Auto-stopping web server due to inactivity");
                stop();
            }
        }, 1L, 1L, TimeUnit.MINUTES);
    }

    public void openBrowserToQCM(String str, String str2) {
        updateActivity();
        openBrowser("/qcm/" + str + "/" + str2.replace(".qcm", "").replace(" ", "_"));
    }

    public void openExerciseDescription(String str, String str2) {
        updateActivity();
        openBrowser("/exercise/" + str + "/" + str2);
    }

    private void openBrowser(String str) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI("http://localhost:8080" + str));
                return;
            } catch (Exception e) {
                System.out.println("Please visit: http://localhost:8080" + str);
                return;
            }
        }
        System.out.println("Please visit: http://localhost:8080" + str);
    }

    private void updateActivity() {
        this.lastActivity = System.currentTimeMillis();
    }

    private void handleRequest(HttpExchange httpExchange) throws IOException {
        byte[] bytes;
        updateActivity();
        String requestMethod = httpExchange.getRequestMethod();
        String path = httpExchange.getRequestURI().getPath();
        System.out.println("Request: " + requestMethod + " " + path);
        String str = null;
        byte[] bArr = null;
        int i = 200;
        String str2 = "text/html; charset=UTF-8";
        try {
            if (path.equals("/") || path.equals("")) {
                str = handleHome();
            } else if (path.startsWith("/qcm/")) {
                str = handleQCM(requestMethod, path, httpExchange);
            } else if (path.startsWith("/tp") && isSessionResource(path)) {
                bArr = handleSessionResourceBinary(path);
                if (bArr == null) {
                    str = generateNotFoundHTML();
                    i = 404;
                } else {
                    str2 = getContentTypeForFile(path);
                }
            } else if (path.startsWith("/tp")) {
                str = handleExerciseDescription(path);
            } else if (path.startsWith("/static/")) {
                str = handleStaticFile(path);
                if (str == null) {
                    str = generateNotFoundHTML();
                    i = 404;
                } else if (path.endsWith(".css")) {
                    str2 = "text/css; charset=UTF-8";
                }
            } else {
                str = generateNotFoundHTML();
                i = 404;
            }
        } catch (Exception e) {
            e.printStackTrace();
            str = generateErrorHTML(e.getMessage());
            i = 500;
        }
        if (bArr != null) {
            bytes = bArr;
        } else {
            bytes = str.getBytes(StandardCharsets.UTF_8);
        }
        httpExchange.getResponseHeaders().set("Content-Type", str2);
        httpExchange.sendResponseHeaders(i, bytes.length);
        httpExchange.getResponseBody().write(bytes);
        httpExchange.getResponseBody().close();
    }

    private String handleHome() {
        return generateWelcomeHTML();
    }

    private String handleQCM(String str, String str2, HttpExchange httpExchange) throws IOException {
        String[] split = str2.split("/");
        if (split.length < 3) {
            return generateErrorHTML("Invalid QCM path format. Expected: /qcm/sessionName/qcmName");
        }
        String str3 = split[2];
        String str4 = split.length > 3 ? split[3] : split[2];
        if (split.length == 3) {
            str4 = str3;
            str3 = inferSessionFromContext();
        }
        QCMDefinition loadFromResource = QCMLoader.loadFromResource(str3, str4);
        if (loadFromResource == null) {
            return generateQCMNotFoundHTML(str3, str4);
        }
        if ("GET".equals(str)) {
            return generateQCMHTML(loadFromResource, str3, str4);
        }
        if ("POST".equals(str)) {
            return processQCMAnswers(loadFromResource, str3, str4, httpExchange);
        }
        return generateErrorHTML("Method not allowed");
    }

    private String inferSessionFromContext() {
        String property = System.getProperty("user.dir");
        if (property != null) {
            String[] split = property.split("/");
            String str = split[split.length - 1];
            if (str.startsWith("tp")) {
                return str;
            }
            return "tp10";
        }
        return "tp10";
    }

    private String handleStaticFile(String str) {
        try {
            InputStream resourceAsStream = getClass().getResourceAsStream("/syllabus" + str);
            if (resourceAsStream != null) {
                try {
                    String str2 = new String(resourceAsStream.readAllBytes(), StandardCharsets.UTF_8);
                    if (resourceAsStream != null) {
                        resourceAsStream.close();
                    }
                    return str2;
                } catch (Throwable th) {
                    if (resourceAsStream != null) {
                        try {
                            resourceAsStream.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    }
                    throw th;
                }
            }
            if (resourceAsStream != null) {
                resourceAsStream.close();
            }
            return null;
        } catch (IOException e) {
            System.err.println("Error loading static file: " + str + ": " + e.getMessage());
            return null;
        }
    }

    private boolean isSessionResource(String str) {
        int lastIndexOf = str.lastIndexOf(46);
        return lastIndexOf > str.lastIndexOf(47) && lastIndexOf > 0;
    }

    private String handleQCMViaExercisePath(String str, String str2) {
        try {
            QCMDefinition loadFromResource = QCMLoader.loadFromResource(str, str2);
            if (loadFromResource == null) {
                return generateQCMNotFoundHTML(str, str2);
            }
            return generateFullQCMHTML(loadFromResource, str, str2);
        } catch (Exception e) {
            return generateErrorHTML("Failed to load QCM: " + e.getMessage());
        }
    }

    private String handleExerciseDescription(String str) {
        String[] split = str.split("/");
        if (split.length != 3) {
            return generateNotFoundHTML();
        }
        String str2 = split[1];
        String str3 = split[2];
        if (str3.startsWith("QCM")) {
            return handleQCMViaExercisePath(str2, str3);
        }
        QCMDefinition loadFromResource = MarkdownLoader.loadFromResource(str2, str3);
        if (loadFromResource != null && loadFromResource.questions != null && !loadFromResource.questions.isEmpty()) {
            return generateEnhancedQCMHTML(loadFromResource, str2, str3);
        }
        try {
            InputStream resourceAsStream = getClass().getResourceAsStream("/syllabus/" + str2 + "/" + str3 + ".html");
            try {
                if (resourceAsStream == null) {
                    String generateExerciseNotFoundHTML = generateExerciseNotFoundHTML(str2, str3);
                    if (resourceAsStream != null) {
                        resourceAsStream.close();
                    }
                    return generateExerciseNotFoundHTML;
                }
                String str4 = new String(resourceAsStream.readAllBytes(), StandardCharsets.UTF_8);
                if (resourceAsStream != null) {
                    resourceAsStream.close();
                }
                return str4;
            } finally {
            }
        } catch (IOException e) {
            return generateErrorHTML("Failed to load exercise description: " + e.getMessage());
        }
    }

    private byte[] handleSessionResourceBinary(String str) {
        try {
            InputStream resourceAsStream = getClass().getResourceAsStream("/syllabus" + str);
            if (resourceAsStream != null) {
                try {
                    byte[] readAllBytes = resourceAsStream.readAllBytes();
                    if (resourceAsStream != null) {
                        resourceAsStream.close();
                    }
                    return readAllBytes;
                } catch (Throwable th) {
                    if (resourceAsStream != null) {
                        try {
                            resourceAsStream.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    }
                    throw th;
                }
            }
            if (resourceAsStream != null) {
                resourceAsStream.close();
            }
            return null;
        } catch (IOException e) {
            System.err.println("Error loading session resource: " + str + ": " + e.getMessage());
            return null;
        }
    }

    private String getContentTypeForFile(String str) {
        String lowerCase = str.toLowerCase();
        if (lowerCase.endsWith(".png")) {
            return "image/png";
        }
        if (lowerCase.endsWith(".jpg") || lowerCase.endsWith(".jpeg")) {
            return "image/jpeg";
        }
        if (lowerCase.endsWith(".gif")) {
            return "image/gif";
        }
        if (lowerCase.endsWith(".svg")) {
            return "image/svg+xml";
        }
        if (lowerCase.endsWith(".css")) {
            return "text/css; charset=UTF-8";
        }
        if (lowerCase.endsWith(".js")) {
            return "application/javascript; charset=UTF-8";
        }
        if (lowerCase.endsWith(".html") || lowerCase.endsWith(".htm")) {
            return "text/html; charset=UTF-8";
        }
        return "application/octet-stream";
    }

    private String generateQCMNotFoundHTML(String str, String str2) {
        return String.format("<!DOCTYPE html>\n<html>\n<head>\n    <meta charset=\"UTF-8\">\n    <title>QCM Not Found</title>\n    <style>\n        body { font-family: Arial, sans-serif; text-align: center; padding: 50px; }\n        .error { color: #d32f2f; }\n    </style>\n</head>\n<body>\n    <div class=\"error\">\n        <h1>QCM Not Found</h1>\n        <p>Could not find QCM: <strong>%s</strong> in session <strong>%s</strong></p>\n        <p>Expected resource: /syllabus/%s/%s.md (or .qcm)</p>\n    </div>\n</body>\n</html>\n", str2, str, str, str2);
    }

    private String generateFullQCMHTML(QCMDefinition qCMDefinition, String str, String str2) {
        if (qCMDefinition.fullHtmlContent != null && !qCMDefinition.fullHtmlContent.isEmpty()) {
            return generateEnhancedQCMHTML(qCMDefinition, str, str2);
        }
        StringBuilder sb = new StringBuilder();
        String replace = str2.replace(".qcm", "").replace(" ", "_");
        sb.append("<!DOCTYPE html>\n<html>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-16\"><head>\n");
        sb.append("<title>").append(qCMDefinition.title).append("</title>\n");
        sb.append("<style>\n");
        sb.append("body { font-family: Arial, sans-serif; max-width: 800px; margin: 0 auto; padding: 20px; }\n");
        sb.append(".question { margin: 20px 0; padding: 15px; border: 1px solid #ddd; border-radius: 5px; }\n");
        sb.append(".options { margin: 10px 0; }\n");
        sb.append(".options label { display: block; margin: 5px 0; }\n");
        sb.append("input[type='text'] { width: 300px; padding: 5px; }\n");
        sb.append("input[type='submit'] { background: #007cba; color: white; padding: 10px 20px; border: none; border-radius: 3px; cursor: pointer; }\n");
        sb.append("input[type='submit']:hover { background: #005a87; }\n");
        sb.append("</style>\n");
        sb.append("</head>\n<body>\n");
        sb.append("<h1>").append(qCMDefinition.title).append("</h1>\n");
        sb.append("<form method='POST' action='/qcm/").append(str).append("/").append(replace).append("'>\n");
        for (int i = 0; i < qCMDefinition.questions.size(); i++) {
            QCMDefinition.Question question = qCMDefinition.questions.get(i);
            sb.append("<div class='question'>\n");
            sb.append("<h3>Question ").append(i + 1).append("</h3>\n");
            sb.append("<p>").append(escapeHtml(question.question)).append("</p>\n");
            String str3 = "q" + i;
            if ("multiple_choice".equals(question.type)) {
                sb.append("<div class='options'>\n");
                for (int i2 = 0; i2 < question.options.size(); i2++) {
                    sb.append("<label>");
                    sb.append("<input type='radio' name='").append(str3).append("' value='").append(i2).append("'>");
                    sb.append(" ").append(escapeHtml(question.options.get(i2)));
                    sb.append("</label>\n");
                }
                sb.append("</div>\n");
            } else if ("text".equals(question.type)) {
                sb.append("<input type='text' name='").append(str3).append("' required>\n");
            }
            sb.append("</div>\n");
        }
        sb.append("<input type='submit' value='Submit Quiz'>\n");
        sb.append("</form>\n");
        sb.append("</body>\n</html>");
        return sb.toString();
    }

    private String generateEnhancedQCMHTML(QCMDefinition qCMDefinition, String str, String str2) {
        String replaceQuestionBlocksWithForms = replaceQuestionBlocksWithForms(qCMDefinition.fullHtmlContent, qCMDefinition, str, str2.replace(".qcm", "").replace(" ", "_"));
        if (replaceQuestionBlocksWithForms.contains("</head>")) {
            replaceQuestionBlocksWithForms = replaceQuestionBlocksWithForms.replace("</head>", ("<style>\n.qcm-question { margin: 20px 0; padding: 15px; background: #f5f5f5; border-radius: 5px; }\n.qcm-options { margin: 10px 0; }\n.qcm-options label { display: block; margin: 5px 0; padding: 5px; cursor: pointer; }\n.qcm-options label:hover { background: #e0e0e0; }\ninput[type='text'] { width: 300px; padding: 5px; }\ninput[type='submit'] { background: #007cba; color: white; padding: 10px 20px; border: none; border-radius: 3px; cursor: pointer; font-size: 16px; margin-top: 20px; }\ninput[type='submit']:hover { background: #005a87; }\ninput[type='submit']:disabled { background: #ccc; cursor: not-allowed; }\n.qcm-result { margin: 20px 0; padding: 20px; border-radius: 5px; }\n.qcm-result.success { background: #d4edda; border: 1px solid #c3e6cb; color: #155724; }\n.qcm-result.partial { background: #fff3cd; border: 1px solid #ffeaa7; color: #856404; }\n.qcm-result.failure { background: #f8d7da; border: 1px solid #f5c6cb; color: #721c24; }\n.qcm-feedback { margin: 10px 0; padding: 10px; background: white; border-left: 4px solid #007cba; }\n.question-review { margin: 20px 0; padding: 15px; border: 1px solid #ddd; border-radius: 5px; text-align: left; }\n.question-title { font-size: 16px; font-weight: bold; margin-bottom: 10px; }\n.option { margin: 8px 0; padding: 8px; border-radius: 3px; }\n.option.correct { background: #d4edda; border-left: 4px solid #28a745; }\n.option.wrong-selected { background: #f8d7da; border-left: 4px solid #dc3545; }\n.option.wrong-not-selected { background: #ffe6e6; border-left: 4px solid #ffb3b3; }\n.option.selected { font-weight: bold; }\n.explanation { margin-top: 5px; font-style: italic; color: #666; }\n.text-answer { margin: 10px 0; padding: 10px; background: #f8f9fa; border-radius: 3px; }\n</style>\n") + "</head>");
        }
        return replaceQuestionBlocksWithForms;
    }

    private String replaceQuestionBlocksWithForms(String str, QCMDefinition qCMDefinition, String str2, String str3) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        int i2 = 0;
        while (true) {
            if (i >= str.length() || i2 >= qCMDefinition.questions.size()) {
                break;
            }
            int indexOf = str.indexOf("<p>:::", i);
            if (indexOf == -1) {
                sb.append(str.substring(i));
                break;
            }
            int indexOf2 = str.indexOf("</p>", indexOf);
            if (indexOf2 == -1 || !str.substring(indexOf, Math.min(indexOf2, indexOf + 100)).contains("question")) {
                sb.append(str.substring(i, indexOf + 6));
                i = indexOf + 6;
            } else {
                sb.append(str.substring(i, indexOf));
                int indexOf3 = str.indexOf(":::", indexOf + 10);
                if (indexOf3 == -1) {
                    i = indexOf2 + 4;
                } else {
                    int indexOf4 = str.indexOf("</ul>", indexOf3);
                    if (indexOf4 == -1 || indexOf4 > indexOf3 + Constants.DEFAULT_WRITE_DELAY) {
                        i = indexOf2 + 4;
                    } else {
                        sb.append(generateSingleQuestionForm(qCMDefinition.questions.get(i2), i2, str2, str3));
                        i = indexOf4 + 5;
                        i2++;
                    }
                }
            }
        }
        if (i < str.length()) {
            sb.append(str.substring(i));
        }
        return sb.toString();
    }

    private String generateSingleQuestionForm(QCMDefinition.Question question, int i, String str, String str2) {
        StringBuilder sb = new StringBuilder();
        String str3 = "qcm-container-" + i;
        String str4 = "qcm-form-" + i;
        sb.append("<div id='").append(str3).append("' class='qcm-container'>\n");
        sb.append("<form id='").append(str4).append("' method='POST' action='/qcm/").append(str).append("/").append(str2).append("' class='qcm-single-form'>\n");
        sb.append("<input type='hidden' name='questionIndex' value='").append(i).append("'>\n");
        sb.append("<div class='qcm-question'>\n");
        sb.append("<p><strong>").append(escapeHtml(question.question)).append("</strong></p>\n");
        String str5 = "q" + i;
        if ("multiple_choice".equals(question.type)) {
            sb.append("<div class='qcm-options'>\n");
            for (int i2 = 0; i2 < question.options.size(); i2++) {
                sb.append("<label>");
                sb.append("<input type='radio' name='").append(str5).append("' value='").append(i2).append("' required>");
                sb.append(" ").append(escapeHtml(question.options.get(i2)));
                sb.append("</label>\n");
            }
            sb.append("</div>\n");
        } else if ("text".equals(question.type)) {
            sb.append("<input type='text' name='").append(str5).append("' required>\n");
        }
        sb.append("</div>\n");
        sb.append("<input type='submit' value='Soumettre la réponse'>\n");
        sb.append("</form>\n");
        sb.append("<script>\n");
        sb.append("(function() {\n");
        sb.append("  const form = document.getElementById('").append(str4).append("');\n");
        sb.append("  form.addEventListener('submit', function(e) {\n");
        sb.append("    e.preventDefault();\n");
        sb.append("    const formData = new FormData(form);\n");
        sb.append("    const submitBtn = form.querySelector('input[type=\"submit\"]');\n");
        sb.append("    submitBtn.disabled = true;\n");
        sb.append("    submitBtn.value = 'Envoi en cours...';\n");
        sb.append("    \n");
        sb.append("    fetch(form.action, {\n");
        sb.append("      method: 'POST',\n");
        sb.append("      body: new URLSearchParams(formData)\n");
        sb.append("    })\n");
        sb.append("    .then(response => response.text())\n");
        sb.append("    .then(html => {\n");
        sb.append("      const parser = new DOMParser();\n");
        sb.append("      const doc = parser.parseFromString(html, 'text/html');\n");
        sb.append("      const results = doc.querySelector('.qcm-results-content');\n");
        sb.append("      if (results) {\n");
        sb.append("        document.getElementById('").append(str3).append("').innerHTML = results.innerHTML;\n");
        sb.append("      } else {\n");
        sb.append("        document.getElementById('").append(str3).append("').innerHTML = html;\n");
        sb.append("      }\n");
        sb.append("    })\n");
        sb.append("    .catch(error => {\n");
        sb.append("      alert('Erreur: ' + error);\n");
        sb.append("      submitBtn.disabled = false;\n");
        sb.append("      submitBtn.value = 'Soumettre la réponse';\n");
        sb.append("    });\n");
        sb.append("  });\n");
        sb.append("})();\n");
        sb.append("</script>\n");
        sb.append("</div>\n");
        return sb.toString();
    }

    private String removeMarkdownQuestionBlocks(String str) {
        return str.replaceAll("(?s)<p>:::?\\s*question.*?</ul>\\s*", "");
    }

    private String generateQCMHTML(QCMDefinition qCMDefinition, String str, String str2) {
        return generateFullQCMHTML(qCMDefinition, str, str2);
    }

    private String processQCMAnswers(QCMDefinition qCMDefinition, String str, String str2, HttpExchange httpExchange) {
        try {
            Map<String, String> parsePostData = parsePostData(httpExchange);
            String str3 = parsePostData.get("questionIndex");
            if (str3 != null) {
                return processSingleQuestionAnswer(qCMDefinition, str, str2, Integer.parseInt(str3), parsePostData);
            }
            HashMap hashMap = new HashMap();
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < qCMDefinition.questions.size(); i++) {
                String str4 = "q" + i;
                String str5 = parsePostData.get(str4);
                hashMap.put(str4, str5 != null ? str5 : "");
                arrayList.add(str5 != null ? str5 : "");
            }
            int calculateScore = qCMDefinition.calculateScore(arrayList);
            int totalQuestions = qCMDefinition.getTotalQuestions();
            double d = totalQuestions > 0 ? (calculateScore / totalQuestions) * 100.0d : 0.0d;
            try {
                AnalyticsLogger.logQCM(str, str2, hashMap, calculateScore, totalQuestions);
            } catch (Exception e) {
            }
            return generateQCMCompletionHTML(qCMDefinition, calculateScore, totalQuestions, d, arrayList);
        } catch (Exception e2) {
            return generateErrorHTML("Error processing QCM answers: " + e2.getMessage());
        }
    }

    private String processSingleQuestionAnswer(QCMDefinition qCMDefinition, String str, String str2, int i, Map<String, String> map) {
        boolean z;
        boolean z2;
        if (i < 0 || i >= qCMDefinition.questions.size()) {
            return generateErrorHTML("Invalid question index");
        }
        QCMDefinition.Question question = qCMDefinition.questions.get(i);
        String str3 = "q" + i;
        String str4 = map.get(str3);
        new ArrayList().add(str4 != null ? str4 : "");
        boolean z3 = false;
        if ("multiple_choice".equals(question.type)) {
            try {
                int parseInt = Integer.parseInt(str4);
                if (question.correct != null) {
                    if (parseInt == question.correct.intValue()) {
                        z = true;
                        z3 = z;
                    }
                }
                z = false;
                z3 = z;
            } catch (Exception e) {
            }
        } else if ("text".equals(question.type)) {
            if (question.correctText != null) {
                if (question.correctText.trim().equalsIgnoreCase(str4 != null ? str4.trim() : "")) {
                    z2 = true;
                    z3 = z2;
                }
            }
            z2 = false;
            z3 = z2;
        }
        int i2 = z3 ? 1 : 0;
        if (str != null && str2 != null) {
            HashMap hashMap = new HashMap();
            hashMap.put(str3, str4 != null ? str4 : "");
            try {
                AnalyticsLogger.logQCM(str, str2, hashMap, i2, 1);
            } catch (Exception e2) {
                System.err.println("Warning: Could not log single question answer: " + e2.getMessage());
            }
        }
        return generateSingleQuestionResultHTML(question, i, str4, z3);
    }

    private String generateSingleQuestionResultHTML(QCMDefinition.Question question, int i, String str, boolean z) {
        String str2;
        String str3;
        StringBuilder sb = new StringBuilder();
        sb.append("<div class='qcm-results-content'>\n");
        sb.append("<div class='question-review'>\n");
        sb.append("<div class='question-title'>").append(escapeHtml(question.question)).append("</div>\n");
        if ("multiple_choice".equals(question.type)) {
            int i2 = 0;
            while (i2 < question.options.size()) {
                String str4 = "";
                boolean z2 = question.correct != null && i2 == question.correct.intValue();
                boolean z3 = str != null && str.equals(String.valueOf(i2));
                if (z2) {
                    str2 = "correct";
                    str4 = "✓ ";
                } else if (z3) {
                    str2 = "wrong-selected";
                    str4 = "✗ ";
                } else {
                    str2 = "wrong-not-selected";
                }
                if (z3) {
                    str2 = str2 + " selected";
                }
                sb.append("<div class='option ").append(str2).append("'>\n");
                sb.append("<strong>").append(str4).append((char) (65 + i2)).append(") ");
                sb.append(escapeHtml(question.options.get(i2))).append("</strong>\n");
                if (question.explanations != null && i2 < question.explanations.size() && (str3 = question.explanations.get(i2)) != null && !str3.trim().isEmpty()) {
                    sb.append("<div class='explanation'>\n");
                    sb.append(escapeHtml(str3));
                    sb.append("</div>\n");
                }
                sb.append("</div>\n");
                i2++;
            }
        } else if ("text".equals(question.type)) {
            sb.append("<div class='text-answer'>\n");
            sb.append("<strong>Your answer:</strong> ").append(escapeHtml(str != null ? str : "(no answer)"));
            sb.append("<br><strong>Correct answer:</strong> ").append(escapeHtml(question.correctText));
            sb.append("<br><strong>Result:</strong> ").append(z ? "✓ Correct" : "✗ Incorrect");
            if (question.explanation != null && !question.explanation.isEmpty()) {
                sb.append("<div class='explanation'>\n");
                sb.append(escapeHtml(question.explanation));
                sb.append("</div>\n");
            }
            sb.append("</div>\n");
        }
        sb.append("</div>\n");
        sb.append("</div>\n");
        return sb.toString();
    }

    private String generateWelcomeHTML() {
        return "<!DOCTYPE html>\n<html>\n<head>\n    <meta charset=\"UTF-8\">\n    <title>ijava2 Web Server</title>\n    <style>\n        body { font-family: Arial, sans-serif; text-align: center; padding: 50px; }\n        .welcome { color: #007cba; }\n    </style>\n</head>\n<body>\n    <div class=\"welcome\">\n        <h1>ijava2 Web Server</h1>\n        <p>The web server is running and ready to serve QCM quizzes and exercise descriptions.</p>\n        <p>Use the ijava2 CLI commands to interact with exercises.</p>\n    </div>\n</body>\n</html>\n";
    }

    private String generateRedirectHTML(String str, String str2) {
        return String.format("<!DOCTYPE html>\n<html>\n<head>\n    <meta http-equiv='refresh' content='0; url=%s'>\n    <title>%s</title>\n</head>\n<body>\n    <p>Redirecting to %s...</p>\n</body>\n</html>\n", str, str2, str2);
    }

    private String generateExerciseNotFoundHTML(String str, String str2) {
        return String.format("<!DOCTYPE html>\n<html>\n<head>\n    <meta charset=\"UTF-8\">\n    <title>Exercise Not Found</title>\n    <style>\n        body { font-family: Arial, sans-serif; text-align: center; padding: 50px; }\n        .error { color: #d32f2f; }\n    </style>\n</head>\n<body>\n    <div class=\"error\">\n        <h1>Exercise Description Not Found</h1>\n        <p>Could not find description for: <strong>%s</strong> in session <strong>%s</strong></p>\n        <p>Expected resource: /syllabus/%s/%s.html</p>\n    </div>\n</body>\n</html>\n", str2, str, str, str2);
    }

    private String generateNotFoundHTML() {
        return "<!DOCTYPE html>\n<html>\n<head>\n    <title>404 - Not Found</title>\n    <style>\n        body { font-family: Arial, sans-serif; text-align: center; padding: 50px; }\n        .error { color: #d32f2f; }\n    </style>\n</head>\n<body>\n    <div class=\"error\">\n        <h1>404 - Page Not Found</h1>\n        <p>The requested page could not be found.</p>\n    </div>\n</body>\n</html>\n";
    }

    private String generateErrorHTML(String str) {
        return String.format("<!DOCTYPE html>\n<html>\n<head>\n    <title>Error</title>\n    <style>\n        body { font-family: Arial, sans-serif; text-align: center; padding: 50px; }\n        .error { color: #d32f2f; }\n    </style>\n</head>\n<body>\n    <div class=\"error\">\n        <h1>Server Error</h1>\n        <p>%s</p>\n    </div>\n</body>\n</html>\n", escapeHtml(str));
    }

    private Map<String, String> parsePostData(HttpExchange httpExchange) throws IOException {
        HashMap hashMap = new HashMap();
        InputStreamReader inputStreamReader = new InputStreamReader(httpExchange.getRequestBody(), StandardCharsets.UTF_8);
        try {
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            try {
                String readLine = bufferedReader.readLine();
                if (readLine != null) {
                    for (String str : readLine.split("&")) {
                        String[] split = str.split("=", 2);
                        if (split.length == 2) {
                            hashMap.put(URLDecoder.decode(split[0], StandardCharsets.UTF_8), URLDecoder.decode(split[1], StandardCharsets.UTF_8));
                        }
                    }
                }
                bufferedReader.close();
                inputStreamReader.close();
                return hashMap;
            } finally {
            }
        } catch (Throwable th) {
            try {
                inputStreamReader.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:66:0x0357  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x0388  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x038e  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x035c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private java.lang.String generateQCMCompletionHTML(ijava2.webapp.QCMDefinition r9, int r10, int r11, double r12, java.util.List<java.lang.String> r14) {
        /*
            Method dump skipped, instructions count: 1017
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: ijava2.webapp.UnifiedWebApp.generateQCMCompletionHTML(ijava2.webapp.QCMDefinition, int, int, double, java.util.List):java.lang.String");
    }

    private String escapeHtml(String str) {
        return str == null ? "" : str.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&#x27;");
    }

    public static void main(String[] strArr) {
        System.out.println("Starting ijava2 web server daemon...");
        UnifiedWebApp unifiedWebApp = getInstance();
        unifiedWebApp.start();
        if (unifiedWebApp.isRunning) {
            System.out.println("Web server daemon started successfully on http://localhost:8080");
            while (unifiedWebApp.isRunning) {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    System.out.println("Web server daemon interrupted, shutting down...");
                    unifiedWebApp.stop();
                    return;
                }
            }
            return;
        }
        System.err.println("Failed to start web server daemon");
        System.exit(1);
    }
}
