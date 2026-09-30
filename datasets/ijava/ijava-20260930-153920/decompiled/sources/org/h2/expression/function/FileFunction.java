package org.h2.expression.function;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import org.h2.engine.Database;
import org.h2.engine.SessionLocal;
import org.h2.expression.Expression;
import org.h2.expression.ExpressionVisitor;
import org.h2.message.DbException;
import org.h2.store.fs.FileUtils;
import org.h2.util.IOUtils;
import org.h2.value.TypeInfo;
import org.h2.value.Value;
import org.h2.value.ValueBigint;
import org.h2.value.ValueBlob;
import org.h2.value.ValueNull;

/* loaded from: ijava.jar:org/h2/expression/function/FileFunction.class */
public final class FileFunction extends Function1_2 {
    public static final int FILE_READ = 0;
    public static final int FILE_WRITE = 1;
    private static final String[] NAMES = {"FILE_READ", "FILE_WRITE"};
    private final int function;

    public FileFunction(Expression expression, Expression expression2, int i) {
        super(expression, expression2);
        this.function = i;
    }

    @Override // org.h2.expression.function.Function1_2, org.h2.expression.Expression
    public Value getValue(SessionLocal sessionLocal) {
        Value value;
        ValueBlob createClob;
        sessionLocal.getUser().checkAdmin();
        Value value2 = this.left.getValue(sessionLocal);
        if (value2 == ValueNull.INSTANCE) {
            return ValueNull.INSTANCE;
        }
        switch (this.function) {
            case 0:
                String string = value2.getString();
                Database database = sessionLocal.getDatabase();
                try {
                    long size = FileUtils.size(string);
                    InputStream newInputStream = FileUtils.newInputStream(string);
                    try {
                        if (this.right == null) {
                            createClob = database.getLobStorage().createBlob(newInputStream, size);
                        } else {
                            Value value3 = this.right.getValue(sessionLocal);
                            createClob = database.getLobStorage().createClob(value3 == ValueNull.INSTANCE ? new InputStreamReader(newInputStream) : new InputStreamReader(newInputStream, value3.getString()), size);
                        }
                        if (newInputStream != null) {
                            newInputStream.close();
                        }
                        value = sessionLocal.addTemporaryLob(createClob);
                        break;
                    } finally {
                    }
                } catch (IOException e) {
                    throw DbException.convertIOException(e, string);
                }
            case 1:
                Value value4 = this.right.getValue(sessionLocal);
                if (value4 == ValueNull.INSTANCE) {
                    value = ValueNull.INSTANCE;
                    break;
                } else {
                    String string2 = value4.getString();
                    try {
                        OutputStream newOutputStream = Files.newOutputStream(Paths.get(string2, new String[0]), new OpenOption[0]);
                        try {
                            InputStream inputStream = value2.getInputStream();
                            try {
                                value = ValueBigint.get(IOUtils.copy(inputStream, newOutputStream));
                                if (inputStream != null) {
                                    inputStream.close();
                                }
                                if (newOutputStream != null) {
                                    newOutputStream.close();
                                }
                                break;
                            } catch (Throwable th) {
                                if (inputStream != null) {
                                    try {
                                        inputStream.close();
                                    } catch (Throwable th2) {
                                        th.addSuppressed(th2);
                                    }
                                }
                                throw th;
                            }
                        } finally {
                        }
                    } catch (IOException e2) {
                        throw DbException.convertIOException(e2, string2);
                    }
                }
            default:
                throw DbException.getInternalError("function=" + this.function);
        }
        return value;
    }

    @Override // org.h2.expression.Expression
    public Expression optimize(SessionLocal sessionLocal) {
        this.left = this.left.optimize(sessionLocal);
        if (this.right != null) {
            this.right = this.right.optimize(sessionLocal);
        }
        switch (this.function) {
            case 0:
                this.type = this.right == null ? TypeInfo.getTypeInfo(7, 2147483647L, 0, null) : TypeInfo.getTypeInfo(3, 2147483647L, 0, null);
                break;
            case 1:
                this.type = TypeInfo.TYPE_BIGINT;
                break;
            default:
                throw DbException.getInternalError("function=" + this.function);
        }
        return this;
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:2:0x0004. Please report as an issue. */
    @Override // org.h2.expression.Operation1_2, org.h2.expression.Expression
    public boolean isEverything(ExpressionVisitor expressionVisitor) {
        switch (expressionVisitor.getType()) {
            case 2:
            case 8:
                return false;
            case 5:
                if (this.function == 1) {
                    return false;
                }
            default:
                return super.isEverything(expressionVisitor);
        }
    }

    @Override // org.h2.expression.function.NamedExpression
    public String getName() {
        return NAMES[this.function];
    }
}
