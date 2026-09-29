package org.h2.command.ddl;

import java.util.Iterator;
import org.h2.api.ErrorCode;
import org.h2.constraint.ConstraintActionType;
import org.h2.engine.Database;
import org.h2.engine.DbObject;
import org.h2.engine.SessionLocal;
import org.h2.message.DbException;
import org.h2.schema.Schema;
import org.h2.table.Table;
import org.h2.table.TableType;
import org.h2.table.TableView;

/* loaded from: ijava.jar:org/h2/command/ddl/DropView.class */
public class DropView extends SchemaCommand {
    private String viewName;
    private boolean ifExists;
    private ConstraintActionType dropAction;

    public DropView(SessionLocal sessionLocal, Schema schema) {
        super(sessionLocal, schema);
        ConstraintActionType constraintActionType;
        if (getDatabase().getSettings().dropRestrict) {
            constraintActionType = ConstraintActionType.RESTRICT;
        } else {
            constraintActionType = ConstraintActionType.CASCADE;
        }
        this.dropAction = constraintActionType;
    }

    public void setIfExists(boolean z) {
        this.ifExists = z;
    }

    public void setDropAction(ConstraintActionType constraintActionType) {
        this.dropAction = constraintActionType;
    }

    public void setViewName(String str) {
        this.viewName = str;
    }

    @Override // org.h2.command.Prepared
    public long update() {
        Table findTableOrView = getSchema().findTableOrView(this.session, this.viewName);
        if (findTableOrView == null) {
            if (!this.ifExists) {
                throw DbException.get(ErrorCode.VIEW_NOT_FOUND_1, this.viewName);
            }
            return 0L;
        }
        if (TableType.VIEW != findTableOrView.getTableType()) {
            throw DbException.get(ErrorCode.VIEW_NOT_FOUND_1, this.viewName);
        }
        this.session.getUser().checkSchemaOwner(findTableOrView.getSchema());
        if (this.dropAction == ConstraintActionType.RESTRICT) {
            Iterator<DbObject> it = findTableOrView.getChildren().iterator();
            while (it.hasNext()) {
                DbObject next = it.next();
                if (next instanceof TableView) {
                    throw DbException.get(ErrorCode.CANNOT_DROP_2, this.viewName, next.getName());
                }
            }
        }
        findTableOrView.lock(this.session, 2);
        Database database = getDatabase();
        database.removeSchemaObject(this.session, findTableOrView);
        database.unlockMeta(this.session);
        return 0L;
    }

    @Override // org.h2.command.Prepared
    public int getType() {
        return 48;
    }
}
