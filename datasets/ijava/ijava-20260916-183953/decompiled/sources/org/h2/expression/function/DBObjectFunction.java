package org.h2.expression.function;

import org.h2.engine.Database;
import org.h2.engine.DbObject;
import org.h2.engine.SessionLocal;
import org.h2.expression.Expression;
import org.h2.expression.ExpressionVisitor;
import org.h2.index.Index;
import org.h2.message.DbException;
import org.h2.schema.Constant;
import org.h2.schema.Schema;
import org.h2.table.Table;
import org.h2.value.TypeInfo;
import org.h2.value.Value;
import org.h2.value.ValueBigint;
import org.h2.value.ValueInteger;
import org.h2.value.ValueNull;
import org.h2.value.ValueVarchar;

/* loaded from: ijava.jar:org/h2/expression/function/DBObjectFunction.class */
public final class DBObjectFunction extends FunctionN {
    public static final int DB_OBJECT_ID = 0;
    public static final int DB_OBJECT_SQL = 1;
    public static final int DB_OBJECT_SIZE = 2;
    public static final int DB_OBJECT_TOTAL_SIZE = 3;
    public static final int DB_OBJECT_APPROXIMATE_SIZE = 4;
    public static final int DB_OBJECT_APPROXIMATE_TOTAL_SIZE = 5;
    private static final String[] NAMES = {"DB_OBJECT_ID", "DB_OBJECT_SQL", "DB_OBJECT_SIZE", "DB_OBJECT_TOTAL_SIZE", "DB_OBJECT_APPROXIMATE_SIZE", "DB_OBJECT_APPROXIMATE_TOTAL_SIZE"};
    private final int function;

    public DBObjectFunction(Expression expression, Expression expression2, Expression expression3, int i) {
        super(expression3 == null ? new Expression[]{expression, expression2} : new Expression[]{expression, expression2, expression3});
        this.function = i;
    }

    @Override // org.h2.expression.function.FunctionN
    public Value getValue(SessionLocal sessionLocal, Value value, Value value2, Value value3) {
        Constant findUser;
        sessionLocal.getUser().checkAdmin();
        String string = value.getString();
        if (value3 != null) {
            Schema findSchema = sessionLocal.getDatabase().findSchema(value2.getString());
            if (findSchema == null) {
                return ValueNull.INSTANCE;
            }
            String string2 = value3.getString();
            boolean z = -1;
            switch (string.hashCode()) {
                case -1024145445:
                    if (string.equals("SYNONYM")) {
                        z = 6;
                        break;
                    }
                    break;
                case -341909096:
                    if (string.equals("TRIGGER")) {
                        z = 8;
                        break;
                    }
                    break;
                case 69808306:
                    if (string.equals("INDEX")) {
                        z = 3;
                        break;
                    }
                    break;
                case 79578030:
                    if (string.equals("TABLE")) {
                        z = 7;
                        break;
                    }
                    break;
                case 214815652:
                    if (string.equals("CONSTANT")) {
                        z = false;
                        break;
                    }
                    break;
                case 294715869:
                    if (string.equals("CONSTRAINT")) {
                        z = true;
                        break;
                    }
                    break;
                case 2022099140:
                    if (string.equals("DOMAIN")) {
                        z = 2;
                        break;
                    }
                    break;
                case 2103635108:
                    if (string.equals("ROUTINE")) {
                        z = 4;
                        break;
                    }
                    break;
                case 2132174785:
                    if (string.equals("SEQUENCE")) {
                        z = 5;
                        break;
                    }
                    break;
            }
            switch (z) {
                case false:
                    findUser = findSchema.findConstant(string2);
                    break;
                case true:
                    findUser = findSchema.findConstraint(sessionLocal, string2);
                    break;
                case true:
                    findUser = findSchema.findDomain(string2);
                    break;
                case true:
                    findUser = findSchema.findIndex(sessionLocal, string2);
                    break;
                case true:
                    findUser = findSchema.findFunctionOrAggregate(string2);
                    break;
                case true:
                    findUser = findSchema.findSequence(string2);
                    break;
                case true:
                    findUser = findSchema.getSynonym(string2);
                    break;
                case true:
                    findUser = findSchema.findTableOrView(sessionLocal, string2);
                    break;
                case true:
                    findUser = findSchema.findTrigger(string2);
                    break;
                default:
                    return ValueNull.INSTANCE;
            }
        } else {
            String string3 = value2.getString();
            Database database = sessionLocal.getDatabase();
            boolean z2 = -1;
            switch (string.hashCode()) {
                case -1854658143:
                    if (string.equals("SCHEMA")) {
                        z2 = 2;
                        break;
                    }
                    break;
                case -1591043536:
                    if (string.equals("SETTING")) {
                        z2 = true;
                        break;
                    }
                    break;
                case 2521206:
                    if (string.equals("ROLE")) {
                        z2 = false;
                        break;
                    }
                    break;
                case 2614219:
                    if (string.equals("USER")) {
                        z2 = 3;
                        break;
                    }
                    break;
            }
            switch (z2) {
                case false:
                    findUser = database.findRole(string3);
                    break;
                case true:
                    findUser = database.findSetting(string3);
                    break;
                case true:
                    findUser = database.findSchema(string3);
                    break;
                case true:
                    findUser = database.findUser(string3);
                    break;
                default:
                    return ValueNull.INSTANCE;
            }
        }
        if (findUser == null) {
            return ValueNull.INSTANCE;
        }
        switch (this.function) {
            case 0:
                return ValueInteger.get(findUser.getId());
            case 1:
                String createSQLForMeta = findUser.getCreateSQLForMeta();
                return createSQLForMeta != null ? ValueVarchar.get(createSQLForMeta, sessionLocal) : ValueNull.INSTANCE;
            case 2:
                return getDbObjectSize(findUser, false, false);
            case 3:
                return getDbObjectSize(findUser, true, false);
            case 4:
                return getDbObjectSize(findUser, false, true);
            case 5:
                return getDbObjectSize(findUser, true, true);
            default:
                throw DbException.getInternalError("function=" + this.function);
        }
    }

    private static Value getDbObjectSize(DbObject dbObject, boolean z, boolean z2) {
        long j = 0;
        if (dbObject instanceof Table) {
            j = ((Table) dbObject).getDiskSpaceUsed(z, z2);
        } else if (dbObject instanceof Index) {
            j = ((Index) dbObject).getDiskSpaceUsed(z2);
        }
        return ValueBigint.get(j);
    }

    @Override // org.h2.expression.Expression
    public Expression optimize(SessionLocal sessionLocal) {
        optimizeArguments(sessionLocal, false);
        switch (this.function) {
            case 0:
                this.type = TypeInfo.TYPE_INTEGER;
                break;
            case 1:
                this.type = TypeInfo.TYPE_VARCHAR;
                break;
            case 2:
            case 3:
            case 4:
            case 5:
                this.type = TypeInfo.TYPE_BIGINT;
                break;
            default:
                throw DbException.getInternalError("function=" + this.function);
        }
        return this;
    }

    @Override // org.h2.expression.OperationN, org.h2.expression.Expression
    public boolean isEverything(ExpressionVisitor expressionVisitor) {
        switch (expressionVisitor.getType()) {
            case 2:
                return false;
            default:
                return super.isEverything(expressionVisitor);
        }
    }

    @Override // org.h2.expression.function.NamedExpression
    public String getName() {
        return NAMES[this.function];
    }
}
