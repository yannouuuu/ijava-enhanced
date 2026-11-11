package defpackage;

import java.util.HashMap;
import java.util.Map;

/* loaded from: ijava.jar:Request.class */
public class Request {
    public HTTP_VERB verb;
    public String path;
    public Map<String, String> queryParams = new HashMap();
    public Map<String, String> postData = new HashMap();

    public Request(HTTP_VERB http_verb, String str) {
        this.verb = http_verb;
        this.path = str;
    }
}
