package ijava2.clitools;

import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/* loaded from: ijava.jar:ijava2/clitools/MarkdownRenderer.class */
public class MarkdownRenderer {
    public static final String RESET = "\u001b[0m";
    public static final String BOLD = "\u001b[1m";
    public static final String BLUE = "\u001b[34m";
    public static final String GREEN = "\u001b[32m";
    public static final String YELLOW = "\u001b[33m";
    public static final String CYAN = "\u001b[36m";
    public static final String MAGENTA = "\u001b[35m";
    private static final Parser parser;
    private static final HtmlRenderer renderer;

    static {
        MutableDataSet mutableDataSet = new MutableDataSet();
        mutableDataSet.set(Parser.EXTENSIONS, Arrays.asList(TablesExtension.create()));
        parser = Parser.builder(mutableDataSet).build();
        renderer = HtmlRenderer.builder(mutableDataSet).build();
    }

    public static String renderFromResource(String str) {
        try {
            InputStream resourceAsStream = MarkdownRenderer.class.getResourceAsStream(str);
            if (resourceAsStream == null) {
                return null;
            }
            return renderToTerminal(new String(resourceAsStream.readAllBytes(), StandardCharsets.UTF_8));
        } catch (Exception e) {
            System.err.println("Error loading markdown from resource: " + str);
            e.printStackTrace();
            return null;
        }
    }

    public static String renderToTerminal(String str) {
        try {
            return htmlToTerminal(renderer.render(parser.parse(str)));
        } catch (Exception e) {
            System.err.println("Error rendering Markdown content");
            e.printStackTrace();
            return str;
        }
    }

    private static String htmlToTerminal(String str) {
        return str.replaceAll("<h1[^>]*>(.*?)</h1>", "\u001b[1m\u001b[34m$1\u001b[0m").replaceAll("<h2[^>]*>(.*?)</h2>", "\u001b[1m\u001b[32m$1\u001b[0m").replaceAll("<h3[^>]*>(.*?)</h3>", "\u001b[1m\u001b[33m$1\u001b[0m").replaceAll("<h[456][^>]*>(.*?)</h[456]>", "\u001b[1m$1\u001b[0m").replaceAll("<(strong|b)[^>]*>(.*?)</(strong|b)>", "\u001b[1m$2\u001b[0m").replaceAll("<(em|i)[^>]*>(.*?)</(em|i)>", "\u001b[36m$2\u001b[0m").replaceAll("<pre><code[^>]*>(.*?)</code></pre>", "\n\u001b[35m```\u001b[0m\n$1\n\u001b[35m```\u001b[0m\n").replaceAll("<code[^>]*>(.*?)</code>", "\u001b[33m`$1`\u001b[0m").replaceAll("<ul[^>]*>", "").replaceAll("</ul>", "").replaceAll("<ol[^>]*>", "").replaceAll("</ol>", "").replaceAll("<li[^>]*>(.*?)</li>", "  • $1").replaceAll("<blockquote[^>]*>(.*?)</blockquote>", "\u001b[32m│ \u001b[0m$1").replaceAll("<p[^>]*>", "").replaceAll("</p>", "\n").replaceAll("<[^>]+>", "").replace("&lt;", "<").replace("&gt;", ">").replace("&amp;", "&").replace("&quot;", "\"").replace("&#39;", "'").replaceAll("\n{3,}", "\n\n").trim();
    }

    public static void displayInTerminal(String str) {
        if (str == null || str.trim().isEmpty()) {
            System.out.println("No description available.");
            return;
        }
        String renderToTerminal = renderToTerminal(str);
        System.out.println();
        System.out.println("\u001b[1m\u001b[34m=" + "=".repeat(60) + "\u001b[0m");
        System.out.println(renderToTerminal);
        System.out.println("\u001b[1m\u001b[34m=" + "=".repeat(60) + "\u001b[0m");
        System.out.println();
    }

    public static boolean displayFromResource(String str) {
        String renderFromResource = renderFromResource(str);
        if (renderFromResource != null) {
            displayInTerminal(renderFromResource);
            return true;
        }
        return false;
    }
}
