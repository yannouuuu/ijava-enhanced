package org.h2.bnf;

import java.util.HashMap;

/* loaded from: ijava.jar:org/h2/bnf/Rule.class */
public interface Rule {
    void setLinks(HashMap<String, RuleHead> hashMap);

    boolean autoComplete(Sentence sentence);

    void accept(BnfVisitor bnfVisitor);
}
