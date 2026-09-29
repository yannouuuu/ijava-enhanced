package ijava2.clitools.commands;

import java.util.ArrayDeque;
import java.util.Deque;

/* loaded from: ijava.jar:ijava2/clitools/commands/StudentIO.class */
public class StudentIO {
    public final Deque<Entry> entries = new ArrayDeque();
    private boolean isPrintMode = false;
    private final StringBuilder printBuffer = new StringBuilder();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: ijava.jar:ijava2/clitools/commands/StudentIO$Type.class */
    public enum Type {
        I,
        O,
        P
    }

    public void addInput(Object obj) {
        if (obj.getClass() != String.class && obj.getClass() != Integer.class && obj.getClass() != Double.class && obj.getClass() != Character.class) {
            throw new IllegalStateException("Bug.");
        }
        this.entries.add(Entry.input(obj));
    }

    public void addOutput(String str) {
        if (!this.entries.isEmpty() && this.entries.peekLast().kind == Type.O) {
            this.entries.peekLast().append(str);
        } else {
            this.entries.add(Entry.output(str));
        }
    }

    public void addPrompt(Object obj) {
        this.entries.add(Entry.prompt());
        addInput(obj);
    }

    public int readInt() {
        return ((Integer) read(Integer.class)).intValue();
    }

    public double readDouble() {
        return ((Double) read(Double.class)).doubleValue();
    }

    public char readChar() {
        return ((Character) read(Character.class)).charValue();
    }

    public String readString() {
        return (String) read(String.class);
    }

    public void print(String str) {
        if (!this.isPrintMode) {
            startPrintMode();
        }
        this.printBuffer.append(str);
    }

    public void checkComplete() {
        if (this.isPrintMode) {
            endPrintMode();
        }
        if (!this.entries.isEmpty()) {
            throw UnexpectedIO.of(this.entries.peek(), null);
        }
    }

    private Object read(Class<?> cls) {
        endPrintMode();
        Entry entry = null;
        if (!this.entries.isEmpty()) {
            Entry poll = this.entries.poll();
            entry = poll;
            if (poll.kind == Type.I && entry.value.getClass() == cls) {
                return entry.value;
            }
        }
        throw UnexpectedIO.of(entry, Entry.input(UnexpectedIO.defaultValueForUnexpectedIn(cls)));
    }

    private void startPrintMode() {
        this.isPrintMode = true;
        this.printBuffer.setLength(0);
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x0046, code lost:
    
        if (r5.value.equals(r0) != false) goto L14;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void endPrintMode() {
        /*
            r3 = this;
            r0 = r3
            boolean r0 = r0.isPrintMode
            if (r0 == 0) goto L52
            r0 = r3
            java.lang.StringBuilder r0 = r0.printBuffer
            java.lang.String r0 = r0.toString()
            r4 = r0
            r0 = 0
            r5 = r0
            r0 = r3
            java.util.Deque<ijava2.clitools.commands.StudentIO$Entry> r0 = r0.entries
            boolean r0 = r0.isEmpty()
            if (r0 != 0) goto L49
            r0 = r3
            java.util.Deque<ijava2.clitools.commands.StudentIO$Entry> r0 = r0.entries
            java.lang.Object r0 = r0.poll()
            ijava2.clitools.commands.StudentIO$Entry r0 = (ijava2.clitools.commands.StudentIO.Entry) r0
            r1 = r0
            r5 = r1
            ijava2.clitools.commands.StudentIO$Type r0 = r0.kind
            ijava2.clitools.commands.StudentIO$Type r1 = ijava2.clitools.commands.StudentIO.Type.I
            if (r0 == r1) goto L49
            r0 = r5
            ijava2.clitools.commands.StudentIO$Type r0 = r0.kind
            ijava2.clitools.commands.StudentIO$Type r1 = ijava2.clitools.commands.StudentIO.Type.O
            if (r0 != r1) goto L52
            r0 = r5
            java.lang.Object r0 = r0.value
            r1 = r4
            boolean r0 = r0.equals(r1)
            if (r0 != 0) goto L52
        L49:
            r0 = r5
            r1 = r4
            ijava2.clitools.commands.StudentIO$Entry r1 = ijava2.clitools.commands.StudentIO.Entry.output(r1)
            ijava2.clitools.commands.StudentIO$UnexpectedIO r0 = ijava2.clitools.commands.StudentIO.UnexpectedIO.of(r0, r1)
            throw r0
        L52:
            r0 = r3
            r1 = 0
            r0.isPrintMode = r1
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: ijava2.clitools.commands.StudentIO.endPrintMode():void");
    }

    /* loaded from: ijava.jar:ijava2/clitools/commands/StudentIO$UnexpectedIO.class */
    public static class UnexpectedIO extends RuntimeException {
        public final String expected;
        public final String found;

        static UnexpectedIO of(Entry entry, Entry entry2) {
            if (entry != null && entry.kind == Type.P && entry2.kind == Type.I) {
                return new UnexpectedIO("a prompt", ef(entry2));
            }
            return new UnexpectedIO(ef(entry), ef(entry2));
        }

        private UnexpectedIO(String str, String str2) {
            this.expected = str;
            this.found = str2;
        }

        private static String ef(Entry entry) {
            if (entry == null) {
                return "no more reads or prints";
            }
            switch (entry.kind) {
                case I:
                    return String.format("read%s()", typeToString(entry.value.getClass()));
                case O:
                    return String.format("print |%s|", replaceNewLines(entry.value));
                case P:
                    return "???";
                default:
                    throw new IllegalStateException("Bug.");
            }
        }

        private static String typeToString(Class<?> cls) {
            if (cls == Integer.class) {
                return "Int";
            }
            if (cls == Double.class) {
                return "Double";
            }
            if (cls == Character.class) {
                return "Char";
            }
            return "String";
        }

        static Object defaultValueForUnexpectedIn(Class<?> cls) {
            if (cls == Integer.class) {
                return 0;
            }
            if (cls == Double.class) {
                return Double.valueOf(0.0d);
            }
            if (cls == Character.class) {
                return 'a';
            }
            if (cls == String.class) {
                return "";
            }
            throw new IllegalStateException("Bug.");
        }

        private static String replaceNewLines(Object obj) {
            return ((String) obj).replace('\n', (char) 9166);
        }
    }

    /* loaded from: ijava.jar:ijava2/clitools/commands/StudentIO$Entry.class */
    public static final class Entry {
        final Type kind;
        private Object value;

        static Entry input(Object obj) {
            return new Entry(Type.I, obj);
        }

        static Entry output(String str) {
            return new Entry(Type.O, str);
        }

        static Entry prompt() {
            return new Entry(Type.P, null);
        }

        private Entry(Type type, Object obj) {
            this.kind = type;
            this.value = obj;
        }

        void append(String str) {
            if (this.kind == Type.O) {
                this.value = String.valueOf(this.value) + str;
            }
        }

        public String toString() {
            String obj;
            if (this.kind == Type.O && (this.value instanceof String)) {
                obj = ((String) this.value).replace('\n', (char) 9166);
            } else {
                obj = this.value.toString();
            }
            return "Entry{kind=" + String.valueOf(this.kind) + ", value=" + obj + "}";
        }
    }
}
