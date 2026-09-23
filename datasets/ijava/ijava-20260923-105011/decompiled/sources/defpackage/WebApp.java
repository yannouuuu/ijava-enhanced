package defpackage;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.concurrent.Executor;

/* loaded from: ijava.jar:WebApp.class */
class WebApp extends Program {
    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // defpackage.Program
    public void algorithm() {
        try {
            HttpServer create = HttpServer.create(new InetSocketAddress(8080), 0);
            create.createContext("/", this::handleRequest);
            create.setExecutor((Executor) null);
            create.start();
            println("Web server started on port 8080 : http://localhost:8080");
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI("http://localhost:8080/home"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public void handleRequest(HttpExchange httpExchange) throws IOException {
        String str;
        String readLine;
        try {
            HTTP_VERB valueOf = HTTP_VERB.valueOf(httpExchange.getRequestMethod());
            String replaceFirst = httpExchange.getRequestURI().getPath().replaceFirst("/", "");
            Request request = new Request(valueOf, replaceFirst);
            println("Received query VERB=[" + String.valueOf(valueOf) + "] and PATH=[" + replaceFirst + "]");
            String query = httpExchange.getRequestURI().getQuery();
            if (query != null) {
                for (String str2 : query.split("&")) {
                    String[] split = str2.split("=");
                    if (split.length == 2) {
                        request.queryParams.put(split[0], split[1]);
                    }
                }
                println("         paramaters : " + String.valueOf(request.queryParams));
            }
            if (valueOf == HTTP_VERB.POST && (readLine = new BufferedReader(new InputStreamReader(httpExchange.getRequestBody(), "UTF-8")).readLine()) != null) {
                for (String str3 : readLine.split("&")) {
                    String[] split2 = str3.split("=");
                    if (split2.length == 2) {
                        request.postData.put(split2[0], split2[1]);
                    }
                }
                println("         POST data : " + String.valueOf(request.postData));
            }
            int i = 200;
            try {
                System.err.println("Looking for method : " + replaceFirst + "(" + Request.class.getName() + ") in " + getClass().getName());
                Method declaredMethod = getClass().getDeclaredMethod(replaceFirst, Request.class);
                declaredMethod.setAccessible(true);
                System.err.println("Found : " + String.valueOf(declaredMethod));
                str = (String) declaredMethod.invoke(this, request);
                System.err.println("Method executed successfully, response length: " + str.length());
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                str = "Not Found";
                i = HTTP_ERROR.NOT_FOUND.getCode();
            } catch (Exception e2) {
                System.err.println("Exception during method invocation: " + e2.getMessage());
                e2.printStackTrace();
                str = "Internal Server Error: " + e2.getMessage();
                i = 500;
            }
            sendResponse(httpExchange, str, i);
        } catch (IllegalArgumentException e3) {
            sendResponse(httpExchange, "Method Not Allowed", HTTP_ERROR.METHOD_NOT_ALLOWED.getCode());
        }
    }

    private void sendResponse(HttpExchange httpExchange, String str, int i) throws IOException {
        httpExchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
        byte[] bytes = str.getBytes("UTF-8");
        httpExchange.sendResponseHeaders(i, bytes.length);
        httpExchange.getResponseBody().write(bytes);
        httpExchange.getResponseBody().close();
    }
}
