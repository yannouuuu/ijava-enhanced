package org.h2.command.dml;

import java.util.ArrayList;
import org.h2.engine.SessionLocal;
import org.h2.expression.Expression;
import org.h2.util.Utils;

/* loaded from: ijava.jar:org/h2/command/dml/CommandWithValues.class */
public abstract class CommandWithValues extends DataChangeStatement {
    protected final ArrayList<Expression[]> valuesExpressionList;

    /* JADX INFO: Access modifiers changed from: protected */
    public CommandWithValues(SessionLocal sessionLocal) {
        super(sessionLocal);
        this.valuesExpressionList = Utils.newSmallArrayList();
    }

    public void addRow(Expression[] expressionArr) {
        this.valuesExpressionList.add(expressionArr);
    }
}
