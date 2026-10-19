package ijava2.clitools;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.stream.Collectors;

/* loaded from: ijava.jar:ijava2/clitools/StudentInteractionSequence.class */
public class StudentInteractionSequence {
    private final Deque<IO> elements = new ArrayDeque();
    private final StringBuilder printBuffer = new StringBuilder();
    private boolean printHappened = false;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: ijava.jar:ijava2/clitools/StudentInteractionSequence$OCKind.class */
    public enum OCKind {
        IS,
        CONTAINS,
        STARTS_WITH,
        ENDS_WITH,
        MATCHES,
        ANY
    }

    public void addInput(Object obj) {
        this.elements.add(IO.input(obj));
    }

    public void addOutput(OutputCondition[] outputConditionArr) {
        this.elements.add(IO.output(outputConditionArr));
    }

    public Object read(Class<?> cls) {
        if (this.printHappened) {
            checkPrint();
        }
        if (this.elements.isEmpty()) {
            throw UnexpectedIO.unexpectedRead(cls);
        }
        IO poll = this.elements.poll();
        if (!poll.isInput()) {
            throw UnexpectedIO.readInsteadOfPrint(cls, poll);
        }
        if (poll.inputValue.getClass() != cls) {
            throw UnexpectedIO.wrongReadType(cls, poll);
        }
        return poll.inputValue;
    }

    public void print(String str) {
        this.printBuffer.append(str);
        this.printHappened = true;
    }

    public void studentAlgorithmComplete() {
        if (this.printHappened) {
            checkPrint();
        }
        if (!this.elements.isEmpty()) {
            IO peek = this.elements.peek();
            this.elements.clear();
            if (peek.isOutput()) {
                throw UnexpectedIO.missingPrint(peek);
            }
            throw UnexpectedIO.missingRead(peek.inputValue.getClass());
        }
    }

    private void checkPrint() {
        String sb = this.printBuffer.toString();
        if (this.elements.isEmpty()) {
            throw UnexpectedIO.unexpectedPrint(sb);
        }
        IO poll = this.elements.poll();
        if (!poll.isOutput()) {
            throw UnexpectedIO.printInsteadOfRead(sb, poll);
        }
        if (!Arrays.stream(poll.outputConditions).allMatch(outputCondition -> {
            return outputCondition.accepts(sb);
        })) {
            throw UnexpectedIO.wrongOutput(sb, poll);
        }
        this.printBuffer.setLength(0);
        this.printHappened = false;
    }

    /* loaded from: ijava.jar:ijava2/clitools/StudentInteractionSequence$UnexpectedIO.class */
    public static class UnexpectedIO extends RuntimeException {
        public final String found;
        public final String expected;

        private UnexpectedIO(String str, String str2) {
            this.found = str;
            this.expected = str2;
        }

        static UnexpectedIO missingRead(Class<?> cls) {
            return new UnexpectedIO("no more reads or prints", readFunctionName(cls));
        }

        static UnexpectedIO unexpectedRead(Class<?> cls) {
            return new UnexpectedIO(readFunctionName(cls), "no more reads or prints");
        }

        static UnexpectedIO readInsteadOfPrint(Class<?> cls, IO io) {
            return new UnexpectedIO(readFunctionName(cls), errorMessage(null, io));
        }

        static UnexpectedIO wrongReadType(Class<?> cls, IO io) {
            return new UnexpectedIO(readFunctionName(cls), readFunctionName(io.inputValue.getClass()));
        }

        static UnexpectedIO unexpectedPrint(String str) {
            return new UnexpectedIO("|" + StudentInteractionSequence.replaceNewLines(str) + "|", "no more reads or prints");
        }

        static UnexpectedIO missingPrint(IO io) {
            return new UnexpectedIO("no more reads or prints", errorMessage(null, io));
        }

        static UnexpectedIO printInsteadOfRead(String str, IO io) {
            return new UnexpectedIO("|" + StudentInteractionSequence.replaceNewLines(str) + "|", readFunctionName(io.inputValue.getClass()));
        }

        static UnexpectedIO wrongOutput(String str, IO io) {
            return new UnexpectedIO("|" + StudentInteractionSequence.replaceNewLines(str) + "|", errorMessage(str, io));
        }

        private static String readFunctionName(Class<?> cls) {
            String simpleName;
            if (cls == Integer.class) {
                simpleName = "Int";
            } else if (cls == Character.class) {
                simpleName = "Char";
            } else {
                simpleName = cls.getSimpleName();
            }
            return String.format("read%s()", simpleName);
        }

        private static String errorMessage(String str, IO io) {
            if (str == null) {
                return io.outputConditions[0].errorMessage();
            }
            return ((OutputCondition) Arrays.stream(io.outputConditions).filter(outputCondition -> {
                return !outputCondition.accepts(str);
            }).findFirst().get()).errorMessage();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: ijava.jar:ijava2/clitools/StudentInteractionSequence$IO.class */
    public static class IO {
        private final Object inputValue;
        private final OutputCondition[] outputConditions;

        private IO(Object obj, OutputCondition[] outputConditionArr) {
            this.inputValue = obj;
            this.outputConditions = outputConditionArr;
        }

        static IO input(Object obj) {
            return new IO(obj, null);
        }

        static IO output(OutputCondition[] outputConditionArr) {
            return new IO(null, outputConditionArr);
        }

        boolean isInput() {
            return this.inputValue != null;
        }

        boolean isOutput() {
            return this.outputConditions != null;
        }

        public String toString() {
            return "IO{" + (isInput() ? "input:" + String.valueOf(this.inputValue) : "") + (isOutput() ? "conds:" + Arrays.toString(this.outputConditions) : "") + "}";
        }
    }

    /* loaded from: ijava.jar:ijava2/clitools/StudentInteractionSequence$OutputCondition.class */
    public static class OutputCondition {
        public final OCKind kind;
        public final String constraint;
        private final String customErrorMessage;

        private OutputCondition(OCKind oCKind, String str, String str2) {
            this.kind = oCKind;
            this.constraint = str;
            this.customErrorMessage = str2;
        }

        public static OutputCondition is(String str) {
            return new OutputCondition(OCKind.IS, str, null);
        }

        public static OutputCondition hasSubstring(String str) {
            return new OutputCondition(OCKind.CONTAINS, str, null);
        }

        public static OutputCondition beginsWith(String str) {
            return new OutputCondition(OCKind.STARTS_WITH, str, null);
        }

        public static OutputCondition terminatesWith(String str) {
            return new OutputCondition(OCKind.ENDS_WITH, str, null);
        }

        public static OutputCondition matchesRE(String str, String str2) {
            return new OutputCondition(OCKind.MATCHES, str, str2);
        }

        public static OutputCondition any() {
            return new OutputCondition(OCKind.ANY, null, null);
        }

        public static OutputCondition containsSequence(String... strArr) {
            return new OutputCondition(OCKind.MATCHES, (String) Arrays.stream(strArr).map(str -> {
                return str.replaceAll("\\.", "\\\\.");
            }).collect(Collectors.joining(".*")), null);
        }

        String errorMessage() {
            if (this.customErrorMessage != null) {
                return this.customErrorMessage;
            }
            switch (this.kind) {
                case IS:
                    return "|" + StudentInteractionSequence.replaceNewLines(this.constraint) + "|";
                case CONTAINS:
                    return "result containing |" + StudentInteractionSequence.replaceNewLines(this.constraint) + "| as substring";
                case STARTS_WITH:
                    return "result starting with |" + StudentInteractionSequence.replaceNewLines(this.constraint) + "|";
                case ENDS_WITH:
                    return "result ending with |" + StudentInteractionSequence.replaceNewLines(this.constraint) + "|";
                case MATCHES:
                    return "result that satisfies the requirements";
                case ANY:
                    return "a prompt";
                default:
                    throw new IncompatibleClassChangeError();
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public boolean accepts(String str) {
            switch (this.kind) {
                case IS:
                    return this.constraint.equals(str);
                case CONTAINS:
                    return str.contains(this.constraint);
                case STARTS_WITH:
                    return str.startsWith(this.constraint);
                case ENDS_WITH:
                    return str.endsWith(this.constraint);
                case MATCHES:
                    return str.matches("(?s)" + this.constraint);
                case ANY:
                    return true;
                default:
                    throw new IncompatibleClassChangeError();
            }
        }

        public String toString() {
            return String.valueOf(this.kind) + "(" + this.constraint + ")";
        }
    }

    private static String replaceNewLines(Object obj) {
        return ((String) obj).replace('\n', (char) 9166);
    }
}
