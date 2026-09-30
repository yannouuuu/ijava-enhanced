package org.h2.schema;

import org.h2.message.DbException;

/* loaded from: ijava.jar:org/h2/schema/UserDefinedFunction.class */
public abstract class UserDefinedFunction extends SchemaObject {
    String className;

    /* JADX INFO: Access modifiers changed from: package-private */
    public UserDefinedFunction(Schema schema, int i, String str, int i2) {
        super(schema, i, str, i2);
    }

    @Override // org.h2.engine.DbObject
    public final void checkRename() {
        throw DbException.getUnsupportedException("RENAME");
    }

    public final String getJavaClassName() {
        return this.className;
    }
}
