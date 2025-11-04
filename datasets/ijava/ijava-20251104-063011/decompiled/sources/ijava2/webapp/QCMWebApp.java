package ijava2.webapp;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import ijava2.clitools.AnalyticsLogger;
import ijava2.webapp.QCMDefinition;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

/* loaded from: ijava.jar:ijava2/webapp/QCMWebApp.class */
public class QCMWebApp {
    private QCMDefinition qcm;
    private String sessionName;
    private String qcmName;
    private String qcmPath;
    private Map<String, String> studentAnswers = new HashMap();
    private boolean completed = false;

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: ijava.jar:ijava2/webapp/QCMWebApp$HTTP_VERB.class */
    public enum HTTP_VERB {
        GET,
        POST,
        PUT,
        DELETE
    }

    /* loaded from: ijava.jar:ijava2/webapp/QCMWebApp$HTTP_ERROR.class */
    protected enum HTTP_ERROR {
        NOT_FOUND(404),
        METHOD_NOT_ALLOWED(405);

        private int code;

        HTTP_ERROR(int i) {
            this.code = i;
        }

        public int getCode() {
            return this.code;
        }
    }

    /* loaded from: ijava.jar:ijava2/webapp/QCMWebApp$Request.class */
    public static class Request {
        public HTTP_VERB verb;
        public String path;
        public Map<String, String> queryParams = new HashMap();
        public Map<String, String> postData = new HashMap();

        public Request(HTTP_VERB http_verb, String str) {
            this.verb = http_verb;
            this.path = str;
        }
    }

    public QCMWebApp(QCMDefinition qCMDefinition, String str, String str2) {
        this.qcm = qCMDefinition;
        this.sessionName = str;
        this.qcmName = str2;
        this.qcmPath = str2.replace(".qcm", "").replace(" ", "_");
    }

    public void launch() {
        algorithm();
    }

    private void algorithm() {
        try {
            HttpServer create = HttpServer.create(new InetSocketAddress(8080), 0);
            create.createContext("/", this::handleRequest);
            create.setExecutor((Executor) null);
            create.start();
            println("QCM Web server started on port 8080 : http://localhost:8080");
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI("http://localhost:8080/" + this.qcmPath));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            while (!this.completed) {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e2) {
                    Thread.currentThread().interrupt();
                }
            }
            println("QCM completed. Stopping server...");
            create.stop(0);
        } catch (IOException e3) {
            e3.printStackTrace();
        }
    }

    public String quiz(Request request) {
        if (request.verb == HTTP_VERB.GET) {
            return generateQCMHTML();
        }
        if (request.verb == HTTP_VERB.POST) {
            return processAnswers(request);
        }
        return "Method not allowed";
    }

    private String generateQCMHTML() {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>\n<html>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-16\"><head>\n");
        sb.append("<title>").append(this.qcm.title).append("</title>\n");
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
        sb.append("<h1>").append(this.qcm.title).append("</h1>\n");
        sb.append("<form method='POST' action='/").append(this.qcmPath).append("'>\n");
        for (int i = 0; i < this.qcm.questions.size(); i++) {
            QCMDefinition.Question question = this.qcm.questions.get(i);
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

    private String processAnswers(Request request) {
        this.studentAnswers.clear();
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < this.qcm.questions.size(); i++) {
            String str = "q" + i;
            String str2 = request.postData.get(str);
            this.studentAnswers.put(str, str2 != null ? str2 : "");
            arrayList.add(str2 != null ? str2 : "");
        }
        int calculateScore = this.qcm.calculateScore(arrayList);
        int totalQuestions = this.qcm.getTotalQuestions();
        double d = totalQuestions > 0 ? (calculateScore / totalQuestions) * 100.0d : 0.0d;
        AnalyticsLogger.logQCM(this.sessionName, this.qcmName, this.studentAnswers, calculateScore, totalQuestions);
        String generateCompletionHTML = generateCompletionHTML(calculateScore, totalQuestions, d);
        this.completed = true;
        return generateCompletionHTML;
    }

    /* JADX WARN: Removed duplicated region for block: B:56:0x0391  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x03c2  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x03c8  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x0396  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private java.lang.String generateCompletionHTML(int r9, int r10, double r11) {
        /*
            Method dump skipped, instructions count: 1066
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: ijava2.webapp.QCMWebApp.generateCompletionHTML(int, int, double):java.lang.String");
    }

    private String escapeHtml(String str) {
        return str == null ? "" : str.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&#x27;");
    }

    public String home(Request request) {
        return "<html><head><meta http-equiv='refresh' content='0; url=/" + this.qcmPath + "'></head><body>Redirecting to quiz...</body></html>";
    }

    public void handleRequest(HttpExchange httpExchange) throws IOException {
        String home;
        String readLine;
        try {
            HTTP_VERB valueOf = HTTP_VERB.valueOf(httpExchange.getRequestMethod());
            String replaceFirst = httpExchange.getRequestURI().getPath().replaceFirst("/", "");
            Request request = new Request(valueOf, replaceFirst);
            println("Received QCM request VERB=[" + String.valueOf(valueOf) + "] and PATH=[" + replaceFirst + "]");
            String query = httpExchange.getRequestURI().getQuery();
            if (query != null) {
                for (String str : query.split("&")) {
                    String[] split = str.split("=");
                    if (split.length == 2) {
                        request.queryParams.put(split[0], split[1]);
                    }
                }
            }
            if (valueOf == HTTP_VERB.POST && (readLine = new BufferedReader(new InputStreamReader(httpExchange.getRequestBody(), "UTF-8")).readLine()) != null) {
                for (String str2 : readLine.split("&")) {
                    String[] split2 = str2.split("=");
                    if (split2.length == 2) {
                        request.postData.put(URLDecoder.decode(split2[0], "UTF-8"), URLDecoder.decode(split2[1], "UTF-8"));
                    }
                }
            }
            int i = 200;
            if (replaceFirst.isEmpty() || replaceFirst.equals("home")) {
                home = home(request);
            } else if (replaceFirst.equals("quiz") || replaceFirst.equals(this.qcmPath)) {
                home = quiz(request);
            } else {
                try {
                    home = (String) getClass().getMethod(replaceFirst, Request.class).invoke(this, request);
                } catch (NoSuchMethodException e) {
                    home = "Not Found: No method '" + replaceFirst + "' found";
                    i = HTTP_ERROR.NOT_FOUND.getCode();
                } catch (Exception e2) {
                    home = "Method Not Allowed: " + e2.getMessage();
                    i = HTTP_ERROR.METHOD_NOT_ALLOWED.getCode();
                }
            }
            sendResponse(httpExchange, home, i);
        } catch (IllegalArgumentException e3) {
            sendResponse(httpExchange, "Method Not Allowed", HTTP_ERROR.METHOD_NOT_ALLOWED.getCode());
        }
    }

    private void sendResponse(HttpExchange httpExchange, String str, int i) throws IOException {
        httpExchange.sendResponseHeaders(i, str.getBytes("UTF-8").length);
        httpExchange.getResponseBody().write(str.getBytes("UTF-8"));
        httpExchange.close();
    }

    private void println(String str) {
        System.out.println(str);
    }
}
