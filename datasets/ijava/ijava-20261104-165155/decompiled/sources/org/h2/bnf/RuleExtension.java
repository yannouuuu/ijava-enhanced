package org.h2.bnf;

import java.util.HashMap;

/* loaded from: ijava.jar:org/h2/bnf/RuleExtension.class */
public class RuleExtension implements Rule {
    private final Rule rule;
    private final boolean compatibility;
    private boolean mapSet;

    public RuleExtension(Rule rule, boolean z) {
        this.rule = rule;
        this.compatibility = z;
    }

    @Override // org.h2.bnf.Rule
    public void accept(BnfVisitor bnfVisitor) {
        bnfVisitor.visitRuleExtension(this.rule, this.compatibility);
    }

    @Override // org.h2.bnf.Rule
    public void setLinks(HashMap<String, RuleHead> hashMap) {
        if (!this.mapSet) {
            this.rule.setLinks(hashMap);
            this.mapSet = true;
        }
    }

    @Override // org.h2.bnf.Rule
    public boolean autoComplete(Sentence sentence) {
        sentence.stopIfRequired();
        this.rule.autoComplete(sentence);
        return true;
    }

    public String toString() {
        return (this.compatibility ? "@c@ " : "@h2@ ") + this.rule.toString();
    }
}
