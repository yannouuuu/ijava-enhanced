package org.h2.expression;

import org.h2.message.DbException;

/* loaded from: ijava.jar:org/h2/expression/ExpressionWithVariableParameters.class */
public interface ExpressionWithVariableParameters {
    void addParameter(Expression expression);

    void doneWithParameters() throws DbException;
}
