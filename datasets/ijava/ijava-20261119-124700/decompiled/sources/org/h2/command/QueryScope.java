package org.h2.command;

import java.util.LinkedHashMap;
import org.h2.table.Table;

/* loaded from: ijava.jar:org/h2/command/QueryScope.class */
public final class QueryScope {
    public final QueryScope parent;
    public final LinkedHashMap<String, Table> tableSubqueries = new LinkedHashMap<>();

    public QueryScope(QueryScope queryScope) {
        this.parent = queryScope;
    }
}
