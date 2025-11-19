package org.h2.bnf;

import java.util.ArrayList;

/* loaded from: ijava.jar:org/h2/bnf/BnfVisitor.class */
public interface BnfVisitor {
    void visitRuleElement(boolean z, String str, Rule rule);

    void visitRuleRepeat(boolean z, Rule rule);

    void visitRuleFixed(int i);

    void visitRuleList(boolean z, ArrayList<Rule> arrayList);

    void visitRuleOptional(Rule rule);

    void visitRuleOptional(ArrayList<Rule> arrayList);

    void visitRuleExtension(Rule rule, boolean z);
}
