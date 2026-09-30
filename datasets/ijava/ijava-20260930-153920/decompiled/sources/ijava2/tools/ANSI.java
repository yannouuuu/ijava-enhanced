package ijava2.tools;

/* loaded from: ijava.jar:ijava2/tools/ANSI.class */
public interface ANSI {
    public static final String RESET = "\u001b[0m";
    public static final String BOLD = "\u001b[1m";
    public static final String DIM = "\u001b[2m";
    public static final String ITALIC = "\u001b[3m";
    public static final String UNDERLINE = "\u001b[4m";
    public static final String BLINK = "\u001b[5m";
    public static final String REVERSE = "\u001b[7m";
    public static final String STRIKETHROUGH = "\u001b[9m";
    public static final String BLACK = "\u001b[30m";
    public static final String RED = "\u001b[31m";
    public static final String GREEN = "\u001b[32m";
    public static final String YELLOW = "\u001b[33m";
    public static final String BLUE = "\u001b[34m";
    public static final String MAGENTA = "\u001b[35m";
    public static final String CYAN = "\u001b[36m";
    public static final String WHITE = "\u001b[37m";
    public static final String BRIGHT_BLACK = "\u001b[90m";
    public static final String BRIGHT_RED = "\u001b[91m";
    public static final String BRIGHT_GREEN = "\u001b[92m";
    public static final String BRIGHT_YELLOW = "\u001b[93m";
    public static final String BRIGHT_BLUE = "\u001b[94m";
    public static final String BRIGHT_MAGENTA = "\u001b[95m";
    public static final String BRIGHT_CYAN = "\u001b[96m";
    public static final String BRIGHT_WHITE = "\u001b[97m";
    public static final String BG_BLACK = "\u001b[40m";
    public static final String BG_RED = "\u001b[41m";
    public static final String BG_GREEN = "\u001b[42m";
    public static final String BG_YELLOW = "\u001b[43m";
    public static final String BG_BLUE = "\u001b[44m";
    public static final String BG_MAGENTA = "\u001b[45m";
    public static final String BG_CYAN = "\u001b[46m";
    public static final String BG_WHITE = "\u001b[47m";
    public static final String BG_BRIGHT_BLACK = "\u001b[100m";
    public static final String BG_BRIGHT_RED = "\u001b[101m";
    public static final String BG_BRIGHT_GREEN = "\u001b[102m";
    public static final String BG_BRIGHT_YELLOW = "\u001b[103m";
    public static final String BG_BRIGHT_BLUE = "\u001b[104m";
    public static final String BG_BRIGHT_MAGENTA = "\u001b[105m";
    public static final String BG_BRIGHT_CYAN = "\u001b[106m";
    public static final String BG_BRIGHT_WHITE = "\u001b[107m";
    public static final String ORANGE = "\u001b[38;5;208m";
    public static final String PURPLE = "\u001b[38;5;129m";
    public static final String PINK = "\u001b[38;5;205m";
    public static final String LIME = "\u001b[38;5;154m";
    public static final String TURQUOISE = "\u001b[38;5;80m";
    public static final String GOLD = "\u001b[38;5;220m";
    public static final String SILVER = "\u001b[38;5;250m";
    public static final String NAVY = "\u001b[38;5;17m";
    public static final String MAROON = "\u001b[38;5;88m";
    public static final String OLIVE = "\u001b[38;5;142m";
    public static final String RGB_CORAL = "\u001b[38;2;255;127;80m";
    public static final String RGB_LAVENDER = "\u001b[38;2;230;230;250m";
    public static final String RGB_MINT = "\u001b[38;2;152;255;152m";
    public static final String RGB_PEACH = "\u001b[38;2;255;218;185m";

    static String rgb(int i, int i2, int i3, boolean z) {
        return "\u001b[" + (z ? '&' : '0') + ";2;" + i + ";" + i2 + ";" + i3 + "m";
    }
}
