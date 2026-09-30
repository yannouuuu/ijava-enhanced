package org.h2.bnf.context;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import org.h2.bnf.BnfVisitor;
import org.h2.bnf.Rule;
import org.h2.bnf.RuleElement;
import org.h2.bnf.RuleHead;
import org.h2.bnf.RuleList;
import org.h2.bnf.Sentence;
import org.h2.util.ParserUtil;
import org.h2.util.StringUtils;

/* loaded from: ijava.jar:org/h2/bnf/context/DbContextRule.class */
public class DbContextRule implements Rule {
    public static final int COLUMN = 0;
    public static final int TABLE = 1;
    public static final int TABLE_ALIAS = 2;
    public static final int NEW_TABLE_ALIAS = 3;
    public static final int COLUMN_ALIAS = 4;
    public static final int SCHEMA = 5;
    public static final int PROCEDURE = 6;
    private final DbContents contents;
    private final int type;
    private String columnType;

    public DbContextRule(DbContents dbContents, int i) {
        this.contents = dbContents;
        this.type = i;
    }

    public void setColumnType(String str) {
        this.columnType = str;
    }

    @Override // org.h2.bnf.Rule
    public void setLinks(HashMap<String, RuleHead> hashMap) {
    }

    @Override // org.h2.bnf.Rule
    public void accept(BnfVisitor bnfVisitor) {
    }

    /* JADX WARN: Code restructure failed: missing block: B:100:0x0280, code lost:
    
        r8 = r8.substring(r0.length());
     */
    /* JADX WARN: Code restructure failed: missing block: B:92:0x0243, code lost:
    
        if (r11 != 36) goto L72;
     */
    /* JADX WARN: Code restructure failed: missing block: B:93:0x0249, code lost:
    
        r0 = r9 + java.lang.Character.charCount(r11);
        r9 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:94:0x0256, code lost:
    
        if (r0 >= r0) goto L180;
     */
    /* JADX WARN: Code restructure failed: missing block: B:95:0x0259, code lost:
    
        r0 = r0.codePointAt(r9);
        r11 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:96:0x0265, code lost:
    
        if (java.lang.Character.isJavaIdentifierPart(r0) == false) goto L179;
     */
    /* JADX WARN: Code restructure failed: missing block: B:98:0x026b, code lost:
    
        r0 = r0.substring(0, r9);
     */
    /* JADX WARN: Code restructure failed: missing block: B:99:0x027a, code lost:
    
        if (org.h2.util.ParserUtil.isKeyword(r0, false) == false) goto L80;
     */
    @Override // org.h2.bnf.Rule
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean autoComplete(org.h2.bnf.Sentence r6) {
        /*
            Method dump skipped, instructions count: 1182
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.h2.bnf.context.DbContextRule.autoComplete(org.h2.bnf.Sentence):boolean");
    }

    private boolean testColumnType(DbColumn dbColumn) {
        if (this.columnType == null) {
            return true;
        }
        String dataType = dbColumn.getDataType();
        if (this.columnType.contains("CHAR") || this.columnType.contains("CLOB")) {
            return dataType.contains("CHAR") || dataType.contains("CLOB");
        }
        if (this.columnType.contains("BINARY") || this.columnType.contains("BLOB")) {
            return dataType.contains("BINARY") || dataType.contains("BLOB");
        }
        return dataType.contains(this.columnType);
    }

    private void autoCompleteProcedure(Sentence sentence) {
        DbSchema lastMatchedSchema = sentence.getLastMatchedSchema();
        if (lastMatchedSchema == null) {
            lastMatchedSchema = this.contents.getDefaultSchema();
        }
        String queryUpper = sentence.getQueryUpper();
        String str = queryUpper;
        int indexOf = queryUpper.indexOf(40);
        if (indexOf != -1) {
            str = StringUtils.trimSubstring(queryUpper, 0, indexOf);
        }
        RuleElement ruleElement = new RuleElement("(", "Function");
        RuleElement ruleElement2 = new RuleElement(")", "Function");
        RuleElement ruleElement3 = new RuleElement(",", "Function");
        for (DbProcedure dbProcedure : lastMatchedSchema.getProcedures()) {
            String name = dbProcedure.getName();
            if (name.startsWith(str)) {
                RuleList ruleList = new RuleList(new RuleElement(name, "Function"), ruleElement, false);
                if (queryUpper.contains("(")) {
                    for (DbColumn dbColumn : dbProcedure.getParameters()) {
                        if (dbColumn.getPosition() > 1) {
                            ruleList = new RuleList(ruleList, ruleElement3, false);
                        }
                        DbContextRule dbContextRule = new DbContextRule(this.contents, 0);
                        String dataType = dbColumn.getDataType();
                        if (dataType.contains("(")) {
                            dataType = dataType.substring(0, dataType.indexOf(40));
                        }
                        dbContextRule.setColumnType(dataType);
                        ruleList = new RuleList(ruleList, dbContextRule, false);
                    }
                    ruleList = new RuleList(ruleList, ruleElement2, false);
                }
                ruleList.autoComplete(sentence);
            }
        }
    }

    private static String autoCompleteTableAlias(Sentence sentence, boolean z) {
        char charAt;
        String query = sentence.getQuery();
        String queryUpper = sentence.getQueryUpper();
        int i = 0;
        while (i < queryUpper.length() && ((charAt = queryUpper.charAt(i)) == '_' || Character.isLetterOrDigit(charAt))) {
            i++;
        }
        if (i == 0) {
            return query;
        }
        String substring = queryUpper.substring(0, i);
        if ("SET".equals(substring) || ParserUtil.isKeyword(substring, false)) {
            return query;
        }
        if (z) {
            sentence.addAlias(substring, sentence.getLastTable());
        }
        HashMap<String, DbTableOrView> aliases = sentence.getAliases();
        if ((aliases != null && aliases.containsKey(substring)) || sentence.getLastTable() == null) {
            if (z && query.length() == substring.length()) {
                return query;
            }
            String substring2 = query.substring(substring.length());
            if (substring2.isEmpty()) {
                sentence.add(substring + ".", ".", 0);
            }
            return substring2;
        }
        HashSet<DbTableOrView> tables = sentence.getTables();
        if (tables != null) {
            String str = null;
            Iterator<DbTableOrView> it = tables.iterator();
            while (it.hasNext()) {
                DbTableOrView next = it.next();
                String upperEnglish = StringUtils.toUpperEnglish(next.getName());
                if (substring.startsWith(upperEnglish) && (str == null || upperEnglish.length() > str.length())) {
                    sentence.setLastMatchedTable(next);
                    str = upperEnglish;
                } else if (query.isEmpty() || upperEnglish.startsWith(substring)) {
                    sentence.add(upperEnglish + ".", upperEnglish.substring(query.length()) + ".", 0);
                }
            }
            if (str != null) {
                String substring3 = query.substring(str.length());
                if (substring3.isEmpty()) {
                    sentence.add(substring + ".", ".", 0);
                }
                return substring3;
            }
        }
        return query;
    }
}
