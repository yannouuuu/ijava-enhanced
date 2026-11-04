package org.h2.command.ddl;

import java.util.ArrayList;
import org.h2.api.ErrorCode;
import org.h2.command.query.Query;
import org.h2.engine.Database;
import org.h2.engine.SessionLocal;
import org.h2.expression.Parameter;
import org.h2.message.DbException;
import org.h2.schema.Schema;
import org.h2.table.Column;
import org.h2.table.Table;
import org.h2.table.TableType;
import org.h2.table.TableView;
import org.h2.value.TypeInfo;

/* loaded from: ijava.jar:org/h2/command/ddl/CreateView.class */
public class CreateView extends SchemaOwnerCommand {
    private Query query;
    private String viewName;
    private boolean ifNotExists;
    private String selectSQL;
    private String[] columnNames;
    private String comment;
    private boolean orReplace;
    private boolean force;

    public CreateView(SessionLocal sessionLocal, Schema schema) {
        super(sessionLocal, schema);
    }

    public void setViewName(String str) {
        this.viewName = str;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    public void setIfNotExists(boolean z) {
        this.ifNotExists = z;
    }

    public void setSelectSQL(String str) {
        this.selectSQL = str;
    }

    public void setColumnNames(String[] strArr) {
        this.columnNames = strArr;
    }

    public void setComment(String str) {
        this.comment = str;
    }

    public void setOrReplace(boolean z) {
        this.orReplace = z;
    }

    public void setForce(boolean z) {
        this.force = z;
    }

    @Override // org.h2.command.ddl.SchemaOwnerCommand
    long update(Schema schema) {
        String planSQL;
        Database database = getDatabase();
        TableView tableView = null;
        Table findTableOrView = schema.findTableOrView(this.session, this.viewName);
        if (findTableOrView != null) {
            if (this.ifNotExists) {
                return 0L;
            }
            if (!this.orReplace || TableType.VIEW != findTableOrView.getTableType()) {
                throw DbException.get(ErrorCode.VIEW_ALREADY_EXISTS_1, this.viewName);
            }
            tableView = (TableView) findTableOrView;
        }
        int objectId = getObjectId();
        if (this.query == null) {
            planSQL = this.selectSQL;
        } else {
            ArrayList<Parameter> parameters = this.query.getParameters();
            if (parameters != null && !parameters.isEmpty()) {
                throw DbException.getUnsupportedException("parameters in views");
            }
            planSQL = this.query.getPlanSQL(0);
        }
        Column[] columnArr = null;
        if (this.columnNames != null) {
            columnArr = new Column[this.columnNames.length];
            Column[] columnArr2 = new Column[this.columnNames.length];
            for (int i = 0; i < this.columnNames.length; i++) {
                columnArr[i] = new Column(this.columnNames[i], TypeInfo.TYPE_UNKNOWN);
                columnArr2[i] = new Column(this.columnNames[i], TypeInfo.TYPE_VARCHAR);
            }
        }
        if (tableView == null) {
            tableView = new TableView(schema, objectId, this.viewName, planSQL, columnArr, this.session);
        } else {
            tableView.replace(planSQL, columnArr, this.session, this.force);
            tableView.setModified();
        }
        if (this.comment != null) {
            tableView.setComment(this.comment);
        }
        if (findTableOrView == null) {
            database.addSchemaObject(this.session, tableView);
            database.unlockMeta(this.session);
            return 0L;
        }
        database.updateMeta(this.session, tableView);
        return 0L;
    }

    @Override // org.h2.command.Prepared
    public int getType() {
        return 34;
    }
}
