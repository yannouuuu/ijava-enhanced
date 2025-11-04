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

/* loaded from: ijava.jar:ijava2/webapp/UnifiedWebApp.class */
public class UnifiedWebApp {
    private static final int PORT = 8080;
    private static final int AUTO_SHUTDOWN_MINUTES = 30;
    private static UnifiedWebApp instance;
    private HttpServer server;
    private ScheduledExecutorService scheduler;
    private QCMDefinition activeQCM;
    private String activeQCMPath;
    private String activeSessionName;
    private String activeQCMName;
    private boolean isRunning = false;
    private long lastActivity = System.currentTimeMillis();
    private Map<String, String> qcmAnswers = new HashMap();
    private boolean qcmCompleted = false;

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

    public void setupQCM(QCMDefinition qCMDefinition, String str, String str2, boolean z) {
        this.activeQCM = qCMDefinition;
        this.activeSessionName = str;
        this.activeQCMName = str2;
        this.activeQCMPath = str2.replace(".qcm", "").replace(" ", "_");
        this.qcmAnswers.clear();
        this.qcmCompleted = false;
        updateActivity();
        if (z) {
            openBrowserToQCM();
        }
    }

    public void openExerciseDescription(String str, String str2) {
        updateActivity();
        openBrowser("/exercise/" + str + "/" + str2);
    }

    private void openBrowserToQCM() {
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

    public boolean isQCMCompleted() {
        return this.qcmCompleted;
    }

    public void waitForQCMCompletion() {
        while (!this.qcmCompleted && this.isRunning) {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
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
        if (this.activeQCMPath != null) {
            return generateRedirectHTML("/qcm/" + this.activeQCMPath, "QCM");
        }
        return generateWelcomeHTML();
    }

    private String handleQCM(String str, String str2, HttpExchange httpExchange) throws IOException {
        if (this.activeQCM == null) {
            return generateErrorHTML("No QCM currently active");
        }
        if ("GET".equals(str)) {
            return generateQCMHTML();
        }
        if ("POST".equals(str)) {
            return processQCMAnswers(httpExchange);
        }
        return generateErrorHTML("Method not allowed");
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
            setupQCM(loadFromResource, str, str2, false);
            return generateFullQCMHTML();
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
        return String.format("<!DOCTYPE html>\n<html>\n<head>\n    <meta charset=\"UTF-8\">\n    <title>QCM Not Found</title>\n    <style>\n        body { font-family: Arial, sans-serif; text-align: center; padding: 50px; }\n        .error { color: #d32f2f; }\n    </style>\n</head>\n<body>\n    <div class=\"error\">\n        <h1>QCM Not Found</h1>\n        <p>Could not find QCM: <strong>%s</strong> in session <strong>%s</strong></p>\n        <p>Expected resource: /syllabus/%s/%s.qcm</p>\n    </div>\n</body>\n</html>\n", str2, str, str, str2);
    }

    private String generateFullQCMHTML() {
        if (this.activeQCM == null) {
            return generateErrorHTML("No QCM currently active");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>\n<html>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-16\"><head>\n");
        sb.append("<title>").append(this.activeQCM.title).append("</title>\n");
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
        sb.append("<h1>").append(this.activeQCM.title).append("</h1>\n");
        sb.append("<form method='POST' action='/qcm/").append(this.activeQCMPath).append("'>\n");
        for (int i = 0; i < this.activeQCM.questions.size(); i++) {
            QCMDefinition.Question question = this.activeQCM.questions.get(i);
            sb.append("<div class='question'>\n");
            sb.append("<h3>Question ").append(i + 1).append("</h3>\n");
            sb.append("<p>").append(escapeHtml(question.question)).append("</p>\n");
            String str = "q" + i;
            if ("multiple_choice".equals(question.type)) {
                sb.append("<div class='options'>\n");
                for (int i2 = 0; i2 < question.options.size(); i2++) {
                    sb.append("<label>");
                    sb.append("<input type='radio' name='").append(str).append("' value='").append(i2).append("'>");
                    sb.append(" ").append(escapeHtml(question.options.get(i2)));
                    sb.append("</label>\n");
                }
                sb.append("</div>\n");
            } else if ("text".equals(question.type)) {
                sb.append("<input type='text' name='").append(str).append("' required>\n");
            }
            sb.append("</div>\n");
        }
        sb.append("<input type='submit' value='Submit Quiz'>\n");
        sb.append("</form>\n");
        sb.append("</body>\n</html>");
        return sb.toString();
    }

    private String generateQCMHTML() {
        return generateFullQCMHTML();
    }

    private String processQCMAnswers(HttpExchange httpExchange) {
        if (this.activeQCM == null) {
            return generateErrorHTML("No QCM currently active");
        }
        try {
            Map<String, String> parsePostData = parsePostData(httpExchange);
            this.qcmAnswers.clear();
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < this.activeQCM.questions.size(); i++) {
                String str = "q" + i;
                String str2 = parsePostData.get(str);
                this.qcmAnswers.put(str, str2 != null ? str2 : "");
                arrayList.add(str2 != null ? str2 : "");
            }
            int calculateScore = this.activeQCM.calculateScore(arrayList);
            int totalQuestions = this.activeQCM.getTotalQuestions();
            double d = totalQuestions > 0 ? (calculateScore / totalQuestions) * 100.0d : 0.0d;
            try {
                if (this.activeSessionName != null && this.activeQCMName != null) {
                    AnalyticsLogger.logQCM(this.activeSessionName, this.activeQCMName, this.qcmAnswers, calculateScore, totalQuestions);
                }
            } catch (Exception e) {
            }
            String generateQCMCompletionHTML = generateQCMCompletionHTML(calculateScore, totalQuestions, d, arrayList);
            this.qcmCompleted = true;
            return generateQCMCompletionHTML;
        } catch (Exception e2) {
            return generateErrorHTML("Error processing QCM answers: " + e2.getMessage());
        }
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

    /* JADX WARN: Removed duplicated region for block: B:65:0x03b8  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x03e9  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x03ef  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x03bd  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private java.lang.String generateQCMCompletionHTML(int r9, int r10, double r11, java.util.List<java.lang.String> r13) {
        /*
            Method dump skipped, instructions count: 1105
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: ijava2.webapp.UnifiedWebApp.generateQCMCompletionHTML(int, int, double, java.util.List):java.lang.String");
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
