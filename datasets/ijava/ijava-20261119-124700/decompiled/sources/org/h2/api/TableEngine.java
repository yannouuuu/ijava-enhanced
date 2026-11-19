package org.h2.api;

import org.h2.command.ddl.CreateTableData;
import org.h2.table.Table;

/* loaded from: ijava.jar:org/h2/api/TableEngine.class */
public interface TableEngine {
    Table createTable(CreateTableData createTableData);
}
