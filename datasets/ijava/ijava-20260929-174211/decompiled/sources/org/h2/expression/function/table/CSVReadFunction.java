package org.h2.expression.function.table;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.h2.api.ErrorCode;
import org.h2.engine.SessionLocal;
import org.h2.expression.Expression;
import org.h2.expression.function.CSVWriteFunction;
import org.h2.index.IndexSort;
import org.h2.message.DbException;
import org.h2.result.ResultInterface;
import org.h2.schema.FunctionAlias;
import org.h2.tools.Csv;
import org.h2.util.StringUtils;

/* loaded from: ijava.jar:org/h2/expression/function/table/CSVReadFunction.class */
public final class CSVReadFunction extends TableFunction {
    public CSVReadFunction() {
        super(new Expression[4]);
    }

    @Override // org.h2.expression.function.table.TableFunction
    public ResultInterface getValue(SessionLocal sessionLocal) {
        String str;
        sessionLocal.getUser().checkAdmin();
        String value = getValue(sessionLocal, 0);
        String value2 = getValue(sessionLocal, 1);
        Csv csv = new Csv();
        String value3 = getValue(sessionLocal, 2);
        if (value3 != null && value3.indexOf(61) >= 0) {
            str = csv.setOptions(value3);
        } else {
            str = value3;
            String value4 = getValue(sessionLocal, 3);
            String value5 = getValue(sessionLocal, 4);
            String value6 = getValue(sessionLocal, 5);
            String value7 = getValue(sessionLocal, 6);
            CSVWriteFunction.setCsvDelimiterEscape(csv, value4, value5, value6);
            csv.setNullString(value7);
        }
        try {
            return FunctionAlias.JavaMethod.resultSetToResult(sessionLocal, csv.read(value, StringUtils.arraySplit(value2, csv.getFieldSeparatorRead(), true), str), IndexSort.FULLY_SORTED);
        } catch (SQLException e) {
            throw DbException.convert(e);
        }
    }

    private String getValue(SessionLocal sessionLocal, int i) {
        return getValue(sessionLocal, this.args, i);
    }

    @Override // org.h2.expression.function.table.TableFunction
    public void optimize(SessionLocal sessionLocal) {
        super.optimize(sessionLocal);
        int length = this.args.length;
        if (length < 1 || length > 7) {
            throw DbException.get(ErrorCode.INVALID_PARAMETER_COUNT_2, getName(), "1..7");
        }
    }

    @Override // org.h2.expression.function.table.TableFunction
    public ResultInterface getValueTemplate(SessionLocal sessionLocal) {
        String str;
        sessionLocal.getUser().checkAdmin();
        String value = getValue(sessionLocal, this.args, 0);
        if (value == null) {
            throw DbException.get(ErrorCode.PARAMETER_NOT_SET_1, "fileName");
        }
        String value2 = getValue(sessionLocal, this.args, 1);
        Csv csv = new Csv();
        String value3 = getValue(sessionLocal, this.args, 2);
        if (value3 != null && value3.indexOf(61) >= 0) {
            str = csv.setOptions(value3);
        } else {
            str = value3;
            CSVWriteFunction.setCsvDelimiterEscape(csv, getValue(sessionLocal, this.args, 3), getValue(sessionLocal, this.args, 4), getValue(sessionLocal, this.args, 5));
        }
        try {
            try {
                ResultSet read = csv.read(value, StringUtils.arraySplit(value2, csv.getFieldSeparatorRead(), true), str);
                try {
                    ResultInterface resultSetToResult = FunctionAlias.JavaMethod.resultSetToResult(sessionLocal, read, 0);
                    if (read != null) {
                        read.close();
                    }
                    return resultSetToResult;
                } catch (Throwable th) {
                    if (read != null) {
                        try {
                            read.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    }
                    throw th;
                }
            } catch (SQLException e) {
                throw DbException.convert(e);
            }
        } finally {
            csv.close();
        }
    }

    private static String getValue(SessionLocal sessionLocal, Expression[] expressionArr, int i) {
        if (i < expressionArr.length) {
            return expressionArr[i].getValue(sessionLocal).getString();
        }
        return null;
    }

    @Override // org.h2.expression.function.NamedExpression
    public String getName() {
        return "CSVREAD";
    }

    @Override // org.h2.expression.function.table.TableFunction
    public boolean isDeterministic() {
        return false;
    }
}
