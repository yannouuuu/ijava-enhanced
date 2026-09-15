package org.h2.command.dml;

import java.util.List;

/* loaded from: ijava.jar:org/h2/command/dml/SetTypes.class */
public class SetTypes {
    public static final int IGNORECASE = 0;
    public static final int MAX_LOG_SIZE = 1;
    public static final int MODE = 2;
    public static final int READONLY = 3;
    public static final int LOCK_TIMEOUT = 4;
    public static final int DEFAULT_LOCK_TIMEOUT = 5;
    public static final int DEFAULT_TABLE_TYPE = 6;
    public static final int CACHE_SIZE = 7;
    public static final int TRACE_LEVEL_SYSTEM_OUT = 8;
    public static final int TRACE_LEVEL_FILE = 9;
    public static final int TRACE_MAX_FILE_SIZE = 10;
    public static final int COLLATION = 11;
    public static final int CLUSTER = 12;
    public static final int WRITE_DELAY = 13;
    public static final int DATABASE_EVENT_LISTENER = 14;
    public static final int MAX_MEMORY_ROWS = 15;
    public static final int LOCK_MODE = 16;
    public static final int DB_CLOSE_DELAY = 17;
    public static final int THROTTLE = 18;
    public static final int MAX_MEMORY_UNDO = 19;
    public static final int MAX_LENGTH_INPLACE_LOB = 20;
    public static final int ALLOW_LITERALS = 21;
    public static final int SCHEMA = 22;
    public static final int OPTIMIZE_REUSE_RESULTS = 23;
    public static final int SCHEMA_SEARCH_PATH = 24;
    public static final int REFERENTIAL_INTEGRITY = 25;
    public static final int MAX_OPERATION_MEMORY = 26;
    public static final int EXCLUSIVE = 27;
    public static final int CREATE_BUILD = 28;
    public static final int VARIABLE = 29;
    public static final int QUERY_TIMEOUT = 30;
    public static final int REDO_LOG_BINARY = 31;
    public static final int JAVA_OBJECT_SERIALIZER = 32;
    public static final int RETENTION_TIME = 33;
    public static final int QUERY_STATISTICS = 34;
    public static final int QUERY_STATISTICS_MAX_ENTRIES = 35;
    public static final int LAZY_QUERY_EXECUTION = 36;
    public static final int BUILTIN_ALIAS_OVERRIDE = 37;
    public static final int AUTHENTICATOR = 38;
    public static final int IGNORE_CATALOGS = 39;
    public static final int CATALOG = 40;
    public static final int NON_KEYWORDS = 41;
    public static final int TIME_ZONE = 42;
    public static final int VARIABLE_BINARY = 43;
    public static final int DEFAULT_NULL_ORDERING = 44;
    public static final int TRUNCATE_LARGE_LENGTH = 45;
    private static final int COUNT = 46;
    private static final List<String> TYPES;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !SetTypes.class.desiredAssertionStatus();
        TYPES = List.of((Object[]) new String[]{"IGNORECASE", "MAX_LOG_SIZE", "MODE", "READONLY", "LOCK_TIMEOUT", "DEFAULT_LOCK_TIMEOUT", "DEFAULT_TABLE_TYPE", "CACHE_SIZE", "TRACE_LEVEL_SYSTEM_OUT", "TRACE_LEVEL_FILE", "TRACE_MAX_FILE_SIZE", "COLLATION", "CLUSTER", "WRITE_DELAY", "DATABASE_EVENT_LISTENER", "MAX_MEMORY_ROWS", "LOCK_MODE", "DB_CLOSE_DELAY", "THROTTLE", "MAX_MEMORY_UNDO", "MAX_LENGTH_INPLACE_LOB", "ALLOW_LITERALS", "SCHEMA", "OPTIMIZE_REUSE_RESULTS", "SCHEMA_SEARCH_PATH", "REFERENTIAL_INTEGRITY", "MAX_OPERATION_MEMORY", "EXCLUSIVE", "CREATE_BUILD", "@", "QUERY_TIMEOUT", "REDO_LOG_BINARY", "JAVA_OBJECT_SERIALIZER", "RETENTION_TIME", "QUERY_STATISTICS", "QUERY_STATISTICS_MAX_ENTRIES", "LAZY_QUERY_EXECUTION", "BUILTIN_ALIAS_OVERRIDE", "AUTHENTICATOR", "IGNORE_CATALOGS", "CATALOG", "NON_KEYWORDS", "TIME ZONE", "VARIABLE_BINARY", "DEFAULT_NULL_ORDERING", "TRUNCATE_LARGE_LENGTH"});
        if (!$assertionsDisabled && TYPES.size() != 46) {
            throw new AssertionError();
        }
    }

    private SetTypes() {
    }

    public static int getType(String str) {
        return TYPES.indexOf(str);
    }

    public static List<String> getTypes() {
        return TYPES;
    }

    public static String getTypeName(int i) {
        return TYPES.get(i);
    }
}
