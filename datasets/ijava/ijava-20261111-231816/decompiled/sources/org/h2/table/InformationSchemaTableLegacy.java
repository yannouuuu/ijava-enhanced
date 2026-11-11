package org.h2.table;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import org.h2.command.Command;
import org.h2.command.ParserBase;
import org.h2.command.dml.Help;
import org.h2.constraint.Constraint;
import org.h2.constraint.ConstraintActionType;
import org.h2.constraint.ConstraintDomain;
import org.h2.constraint.ConstraintReferential;
import org.h2.constraint.ConstraintUnique;
import org.h2.engine.Constants;
import org.h2.engine.DbObject;
import org.h2.engine.QueryStatisticsData;
import org.h2.engine.Right;
import org.h2.engine.RightOwner;
import org.h2.engine.Role;
import org.h2.engine.SessionLocal;
import org.h2.engine.Setting;
import org.h2.engine.User;
import org.h2.expression.ExpressionVisitor;
import org.h2.expression.ValueExpression;
import org.h2.index.Index;
import org.h2.index.MetaIndex;
import org.h2.message.DbException;
import org.h2.result.Row;
import org.h2.result.SearchRow;
import org.h2.schema.Constant;
import org.h2.schema.Domain;
import org.h2.schema.FunctionAlias;
import org.h2.schema.Schema;
import org.h2.schema.SchemaObject;
import org.h2.schema.Sequence;
import org.h2.schema.TriggerObject;
import org.h2.schema.UserDefinedFunction;
import org.h2.store.InDoubtTransaction;
import org.h2.tools.Csv;
import org.h2.util.DateTimeUtils;
import org.h2.util.MathUtils;
import org.h2.util.NetworkConnectionInfo;
import org.h2.util.StringUtils;
import org.h2.util.TimeZoneProvider;
import org.h2.util.Utils;
import org.h2.value.CompareMode;
import org.h2.value.DataType;
import org.h2.value.TypeInfo;
import org.h2.value.Value;
import org.h2.value.ValueBigint;
import org.h2.value.ValueBoolean;
import org.h2.value.ValueDouble;
import org.h2.value.ValueInteger;
import org.h2.value.ValueSmallint;
import org.h2.value.ValueToObjectConverter2;

/* loaded from: ijava.jar:org/h2/table/InformationSchemaTableLegacy.class */
public final class InformationSchemaTableLegacy extends MetaTable {
    private static final String CHARACTER_SET_NAME = "Unicode";
    private static final int TABLES = 0;
    private static final int COLUMNS = 1;
    private static final int INDEXES = 2;
    private static final int TABLE_TYPES = 3;
    private static final int TYPE_INFO = 4;
    private static final int CATALOGS = 5;
    private static final int SETTINGS = 6;
    private static final int HELP = 7;
    private static final int SEQUENCES = 8;
    private static final int USERS = 9;
    private static final int ROLES = 10;
    private static final int RIGHTS = 11;
    private static final int FUNCTION_ALIASES = 12;
    private static final int SCHEMATA = 13;
    private static final int TABLE_PRIVILEGES = 14;
    private static final int COLUMN_PRIVILEGES = 15;
    private static final int COLLATIONS = 16;
    private static final int VIEWS = 17;
    private static final int IN_DOUBT = 18;
    private static final int CROSS_REFERENCES = 19;
    private static final int FUNCTION_COLUMNS = 20;
    private static final int CONSTRAINTS = 21;
    private static final int CONSTANTS = 22;
    private static final int DOMAINS = 23;
    private static final int TRIGGERS = 24;
    private static final int SESSIONS = 25;
    private static final int LOCKS = 26;
    private static final int SESSION_STATE = 27;
    private static final int QUERY_STATISTICS = 28;
    private static final int SYNONYMS = 29;
    private static final int TABLE_CONSTRAINTS = 30;
    private static final int DOMAIN_CONSTRAINTS = 31;
    private static final int KEY_COLUMN_USAGE = 32;
    private static final int REFERENTIAL_CONSTRAINTS = 33;
    private static final int CHECK_CONSTRAINTS = 34;
    private static final int CONSTRAINT_COLUMN_USAGE = 35;
    public static final int META_TABLE_TYPE_COUNT = 36;

    public InformationSchemaTableLegacy(Schema schema, int i, int i2) {
        super(schema, i, i2);
        Column[] columnArr;
        String str = null;
        switch (i2) {
            case 0:
                setMetaTableName("TABLES");
                columnArr = new Column[]{column("TABLE_CATALOG"), column("TABLE_SCHEMA"), column("TABLE_NAME"), column("TABLE_TYPE"), column("STORAGE_TYPE"), column("SQL"), column("REMARKS"), column("LAST_MODIFICATION", TypeInfo.TYPE_BIGINT), column("ID", TypeInfo.TYPE_INTEGER), column("TYPE_NAME"), column("TABLE_CLASS"), column("ROW_COUNT_ESTIMATE", TypeInfo.TYPE_BIGINT)};
                str = "TABLE_NAME";
                break;
            case 1:
                setMetaTableName("COLUMNS");
                columnArr = new Column[]{column("TABLE_CATALOG"), column("TABLE_SCHEMA"), column("TABLE_NAME"), column("COLUMN_NAME"), column("ORDINAL_POSITION", TypeInfo.TYPE_INTEGER), column("COLUMN_DEFAULT"), column("IS_NULLABLE"), column("DATA_TYPE", TypeInfo.TYPE_INTEGER), column("CHARACTER_MAXIMUM_LENGTH", TypeInfo.TYPE_INTEGER), column("CHARACTER_OCTET_LENGTH", TypeInfo.TYPE_INTEGER), column("NUMERIC_PRECISION", TypeInfo.TYPE_INTEGER), column("NUMERIC_PRECISION_RADIX", TypeInfo.TYPE_INTEGER), column("NUMERIC_SCALE", TypeInfo.TYPE_INTEGER), column("DATETIME_PRECISION", TypeInfo.TYPE_INTEGER), column("INTERVAL_TYPE"), column("INTERVAL_PRECISION", TypeInfo.TYPE_INTEGER), column("CHARACTER_SET_NAME"), column("COLLATION_NAME"), column("DOMAIN_CATALOG"), column("DOMAIN_SCHEMA"), column("DOMAIN_NAME"), column("IS_GENERATED"), column("GENERATION_EXPRESSION"), column("TYPE_NAME"), column("NULLABLE", TypeInfo.TYPE_INTEGER), column("IS_COMPUTED", TypeInfo.TYPE_BOOLEAN), column("SELECTIVITY", TypeInfo.TYPE_INTEGER), column("SEQUENCE_NAME"), column("REMARKS"), column("SOURCE_DATA_TYPE", TypeInfo.TYPE_SMALLINT), column("COLUMN_TYPE"), column("COLUMN_ON_UPDATE"), column("IS_VISIBLE"), column("CHECK_CONSTRAINT")};
                str = "TABLE_NAME";
                break;
            case 2:
                setMetaTableName("INDEXES");
                columnArr = new Column[]{column("TABLE_CATALOG"), column("TABLE_SCHEMA"), column("TABLE_NAME"), column("NON_UNIQUE", TypeInfo.TYPE_BOOLEAN), column("INDEX_NAME"), column("ORDINAL_POSITION", TypeInfo.TYPE_SMALLINT), column("COLUMN_NAME"), column("CARDINALITY", TypeInfo.TYPE_INTEGER), column("PRIMARY_KEY", TypeInfo.TYPE_BOOLEAN), column("INDEX_TYPE_NAME"), column("IS_GENERATED", TypeInfo.TYPE_BOOLEAN), column("INDEX_TYPE", TypeInfo.TYPE_SMALLINT), column("ASC_OR_DESC"), column("PAGES", TypeInfo.TYPE_INTEGER), column("FILTER_CONDITION"), column("REMARKS"), column("SQL"), column("ID", TypeInfo.TYPE_INTEGER), column("SORT_TYPE", TypeInfo.TYPE_INTEGER), column("CONSTRAINT_NAME"), column("INDEX_CLASS")};
                str = "TABLE_NAME";
                break;
            case 3:
                setMetaTableName("TABLE_TYPES");
                columnArr = new Column[]{column("TYPE")};
                break;
            case 4:
                setMetaTableName("TYPE_INFO");
                columnArr = new Column[]{column("TYPE_NAME"), column("DATA_TYPE", TypeInfo.TYPE_INTEGER), column("PRECISION", TypeInfo.TYPE_INTEGER), column("PREFIX"), column("SUFFIX"), column("PARAMS"), column("AUTO_INCREMENT", TypeInfo.TYPE_BOOLEAN), column("MINIMUM_SCALE", TypeInfo.TYPE_SMALLINT), column("MAXIMUM_SCALE", TypeInfo.TYPE_SMALLINT), column("RADIX", TypeInfo.TYPE_INTEGER), column("POS", TypeInfo.TYPE_INTEGER), column("CASE_SENSITIVE", TypeInfo.TYPE_BOOLEAN), column("NULLABLE", TypeInfo.TYPE_SMALLINT), column("SEARCHABLE", TypeInfo.TYPE_SMALLINT)};
                break;
            case 5:
                setMetaTableName("CATALOGS");
                columnArr = new Column[]{column("CATALOG_NAME")};
                break;
            case 6:
                setMetaTableName("SETTINGS");
                columnArr = new Column[]{column("NAME"), column("VALUE")};
                break;
            case 7:
                setMetaTableName("HELP");
                columnArr = new Column[]{column("ID", TypeInfo.TYPE_INTEGER), column("SECTION"), column("TOPIC"), column("SYNTAX"), column("TEXT")};
                break;
            case 8:
                setMetaTableName("SEQUENCES");
                columnArr = new Column[]{column("SEQUENCE_CATALOG"), column("SEQUENCE_SCHEMA"), column("SEQUENCE_NAME"), column("DATA_TYPE"), column("NUMERIC_PRECISION", TypeInfo.TYPE_INTEGER), column("NUMERIC_PRECISION_RADIX", TypeInfo.TYPE_INTEGER), column("NUMERIC_SCALE", TypeInfo.TYPE_INTEGER), column("START_VALUE", TypeInfo.TYPE_BIGINT), column("MINIMUM_VALUE", TypeInfo.TYPE_BIGINT), column("MAXIMUM_VALUE", TypeInfo.TYPE_BIGINT), column("INCREMENT", TypeInfo.TYPE_BIGINT), column("CYCLE_OPTION"), column("DECLARED_DATA_TYPE"), column("DECLARED_NUMERIC_PRECISION", TypeInfo.TYPE_INTEGER), column("DECLARED_NUMERIC_SCALE", TypeInfo.TYPE_INTEGER), column("CURRENT_VALUE", TypeInfo.TYPE_BIGINT), column("IS_GENERATED", TypeInfo.TYPE_BOOLEAN), column("REMARKS"), column("CACHE", TypeInfo.TYPE_BIGINT), column("ID", TypeInfo.TYPE_INTEGER), column("MIN_VALUE", TypeInfo.TYPE_BIGINT), column("MAX_VALUE", TypeInfo.TYPE_BIGINT), column("IS_CYCLE", TypeInfo.TYPE_BOOLEAN)};
                break;
            case 9:
                setMetaTableName("USERS");
                columnArr = new Column[]{column("NAME"), column("ADMIN"), column("REMARKS"), column("ID", TypeInfo.TYPE_INTEGER)};
                break;
            case 10:
                setMetaTableName("ROLES");
                columnArr = new Column[]{column("NAME"), column("REMARKS"), column("ID", TypeInfo.TYPE_INTEGER)};
                break;
            case 11:
                setMetaTableName("RIGHTS");
                columnArr = new Column[]{column("GRANTEE"), column("GRANTEETYPE"), column("GRANTEDROLE"), column("RIGHTS"), column("TABLE_SCHEMA"), column("TABLE_NAME"), column("ID", TypeInfo.TYPE_INTEGER)};
                str = "TABLE_NAME";
                break;
            case 12:
                setMetaTableName("FUNCTION_ALIASES");
                columnArr = new Column[]{column("ALIAS_CATALOG"), column("ALIAS_SCHEMA"), column("ALIAS_NAME"), column("JAVA_CLASS"), column("JAVA_METHOD"), column("DATA_TYPE", TypeInfo.TYPE_INTEGER), column("TYPE_NAME"), column("COLUMN_COUNT", TypeInfo.TYPE_INTEGER), column("RETURNS_RESULT", TypeInfo.TYPE_SMALLINT), column("REMARKS"), column("ID", TypeInfo.TYPE_INTEGER), column("SOURCE")};
                break;
            case 13:
                setMetaTableName("SCHEMATA");
                columnArr = new Column[]{column("CATALOG_NAME"), column("SCHEMA_NAME"), column("SCHEMA_OWNER"), column("DEFAULT_CHARACTER_SET_NAME"), column("DEFAULT_COLLATION_NAME"), column("IS_DEFAULT", TypeInfo.TYPE_BOOLEAN), column("REMARKS"), column("ID", TypeInfo.TYPE_INTEGER)};
                break;
            case 14:
                setMetaTableName("TABLE_PRIVILEGES");
                columnArr = new Column[]{column("GRANTOR"), column("GRANTEE"), column("TABLE_CATALOG"), column("TABLE_SCHEMA"), column("TABLE_NAME"), column("PRIVILEGE_TYPE"), column("IS_GRANTABLE")};
                str = "TABLE_NAME";
                break;
            case 15:
                setMetaTableName("COLUMN_PRIVILEGES");
                columnArr = new Column[]{column("GRANTOR"), column("GRANTEE"), column("TABLE_CATALOG"), column("TABLE_SCHEMA"), column("TABLE_NAME"), column("COLUMN_NAME"), column("PRIVILEGE_TYPE"), column("IS_GRANTABLE")};
                str = "TABLE_NAME";
                break;
            case 16:
                setMetaTableName("COLLATIONS");
                columnArr = new Column[]{column("NAME"), column("KEY")};
                break;
            case 17:
                setMetaTableName("VIEWS");
                columnArr = new Column[]{column("TABLE_CATALOG"), column("TABLE_SCHEMA"), column("TABLE_NAME"), column("VIEW_DEFINITION"), column("CHECK_OPTION"), column("IS_UPDATABLE"), column("STATUS"), column("REMARKS"), column("ID", TypeInfo.TYPE_INTEGER)};
                str = "TABLE_NAME";
                break;
            case 18:
                setMetaTableName("IN_DOUBT");
                columnArr = new Column[]{column("TRANSACTION"), column("STATE")};
                break;
            case 19:
                setMetaTableName("CROSS_REFERENCES");
                columnArr = new Column[]{column("PKTABLE_CATALOG"), column("PKTABLE_SCHEMA"), column("PKTABLE_NAME"), column("PKCOLUMN_NAME"), column("FKTABLE_CATALOG"), column("FKTABLE_SCHEMA"), column("FKTABLE_NAME"), column("FKCOLUMN_NAME"), column("ORDINAL_POSITION", TypeInfo.TYPE_SMALLINT), column("UPDATE_RULE", TypeInfo.TYPE_SMALLINT), column("DELETE_RULE", TypeInfo.TYPE_SMALLINT), column("FK_NAME"), column("PK_NAME"), column("DEFERRABILITY", TypeInfo.TYPE_SMALLINT)};
                str = "PKTABLE_NAME";
                break;
            case 20:
                setMetaTableName("FUNCTION_COLUMNS");
                columnArr = new Column[]{column("ALIAS_CATALOG"), column("ALIAS_SCHEMA"), column("ALIAS_NAME"), column("JAVA_CLASS"), column("JAVA_METHOD"), column("COLUMN_COUNT", TypeInfo.TYPE_INTEGER), column("POS", TypeInfo.TYPE_INTEGER), column("COLUMN_NAME"), column("DATA_TYPE", TypeInfo.TYPE_INTEGER), column("TYPE_NAME"), column("PRECISION", TypeInfo.TYPE_INTEGER), column("SCALE", TypeInfo.TYPE_SMALLINT), column("RADIX", TypeInfo.TYPE_SMALLINT), column("NULLABLE", TypeInfo.TYPE_SMALLINT), column("COLUMN_TYPE", TypeInfo.TYPE_SMALLINT), column("REMARKS"), column("COLUMN_DEFAULT")};
                break;
            case 21:
                setMetaTableName("CONSTRAINTS");
                columnArr = new Column[]{column("CONSTRAINT_CATALOG"), column("CONSTRAINT_SCHEMA"), column("CONSTRAINT_NAME"), column("CONSTRAINT_TYPE"), column("TABLE_CATALOG"), column("TABLE_SCHEMA"), column("TABLE_NAME"), column("UNIQUE_INDEX_NAME"), column("CHECK_EXPRESSION"), column("COLUMN_LIST"), column("REMARKS"), column("SQL"), column("ID", TypeInfo.TYPE_INTEGER)};
                str = "TABLE_NAME";
                break;
            case 22:
                setMetaTableName("CONSTANTS");
                columnArr = new Column[]{column("CONSTANT_CATALOG"), column("CONSTANT_SCHEMA"), column("CONSTANT_NAME"), column("DATA_TYPE", TypeInfo.TYPE_INTEGER), column("REMARKS"), column("SQL"), column("ID", TypeInfo.TYPE_INTEGER)};
                break;
            case 23:
                setMetaTableName("DOMAINS");
                columnArr = new Column[]{column("DOMAIN_CATALOG"), column("DOMAIN_SCHEMA"), column("DOMAIN_NAME"), column("DOMAIN_DEFAULT"), column("DOMAIN_ON_UPDATE"), column("DATA_TYPE", TypeInfo.TYPE_INTEGER), column("PRECISION", TypeInfo.TYPE_INTEGER), column("SCALE", TypeInfo.TYPE_INTEGER), column("TYPE_NAME"), column("PARENT_DOMAIN_CATALOG"), column("PARENT_DOMAIN_SCHEMA"), column("PARENT_DOMAIN_NAME"), column("SELECTIVITY", TypeInfo.TYPE_INTEGER), column("REMARKS"), column("SQL"), column("ID", TypeInfo.TYPE_INTEGER), column("COLUMN_DEFAULT"), column("IS_NULLABLE"), column("CHECK_CONSTRAINT")};
                break;
            case 24:
                setMetaTableName("TRIGGERS");
                columnArr = new Column[]{column("TRIGGER_CATALOG"), column("TRIGGER_SCHEMA"), column("TRIGGER_NAME"), column("TRIGGER_TYPE"), column("TABLE_CATALOG"), column("TABLE_SCHEMA"), column("TABLE_NAME"), column("BEFORE", TypeInfo.TYPE_BOOLEAN), column("JAVA_CLASS"), column("QUEUE_SIZE", TypeInfo.TYPE_INTEGER), column("NO_WAIT", TypeInfo.TYPE_BOOLEAN), column("REMARKS"), column("SQL"), column("ID", TypeInfo.TYPE_INTEGER)};
                break;
            case 25:
                setMetaTableName("SESSIONS");
                columnArr = new Column[]{column("ID", TypeInfo.TYPE_INTEGER), column("USER_NAME"), column("SERVER"), column("CLIENT_ADDR"), column("CLIENT_INFO"), column("SESSION_START", TypeInfo.TYPE_TIMESTAMP_TZ), column("ISOLATION_LEVEL"), column("STATEMENT"), column("STATEMENT_START", TypeInfo.TYPE_TIMESTAMP_TZ), column("CONTAINS_UNCOMMITTED", TypeInfo.TYPE_BOOLEAN), column("STATE"), column("BLOCKER_ID", TypeInfo.TYPE_INTEGER), column("SLEEP_SINCE", TypeInfo.TYPE_TIMESTAMP_TZ)};
                break;
            case 26:
                setMetaTableName("LOCKS");
                columnArr = new Column[]{column("TABLE_SCHEMA"), column("TABLE_NAME"), column("SESSION_ID", TypeInfo.TYPE_INTEGER), column("LOCK_TYPE")};
                break;
            case 27:
                setMetaTableName("SESSION_STATE");
                columnArr = new Column[]{column("KEY"), column("SQL")};
                break;
            case 28:
                setMetaTableName("QUERY_STATISTICS");
                columnArr = new Column[]{column("SQL_STATEMENT"), column("EXECUTION_COUNT", TypeInfo.TYPE_INTEGER), column("MIN_EXECUTION_TIME", TypeInfo.TYPE_DOUBLE), column("MAX_EXECUTION_TIME", TypeInfo.TYPE_DOUBLE), column("CUMULATIVE_EXECUTION_TIME", TypeInfo.TYPE_DOUBLE), column("AVERAGE_EXECUTION_TIME", TypeInfo.TYPE_DOUBLE), column("STD_DEV_EXECUTION_TIME", TypeInfo.TYPE_DOUBLE), column("MIN_ROW_COUNT", TypeInfo.TYPE_BIGINT), column("MAX_ROW_COUNT", TypeInfo.TYPE_BIGINT), column("CUMULATIVE_ROW_COUNT", TypeInfo.TYPE_BIGINT), column("AVERAGE_ROW_COUNT", TypeInfo.TYPE_DOUBLE), column("STD_DEV_ROW_COUNT", TypeInfo.TYPE_DOUBLE)};
                break;
            case 29:
                setMetaTableName("SYNONYMS");
                columnArr = new Column[]{column("SYNONYM_CATALOG"), column("SYNONYM_SCHEMA"), column("SYNONYM_NAME"), column("SYNONYM_FOR"), column("SYNONYM_FOR_SCHEMA"), column("TYPE_NAME"), column("STATUS"), column("REMARKS"), column("ID", TypeInfo.TYPE_INTEGER)};
                str = "SYNONYM_NAME";
                break;
            case 30:
                setMetaTableName("TABLE_CONSTRAINTS");
                columnArr = new Column[]{column("CONSTRAINT_CATALOG"), column("CONSTRAINT_SCHEMA"), column("CONSTRAINT_NAME"), column("CONSTRAINT_TYPE"), column("TABLE_CATALOG"), column("TABLE_SCHEMA"), column("TABLE_NAME"), column("IS_DEFERRABLE"), column("INITIALLY_DEFERRED"), column("REMARKS"), column("SQL"), column("ID", TypeInfo.TYPE_INTEGER)};
                str = "TABLE_NAME";
                break;
            case 31:
                setMetaTableName("DOMAIN_CONSTRAINTS");
                columnArr = new Column[]{column("CONSTRAINT_CATALOG"), column("CONSTRAINT_SCHEMA"), column("CONSTRAINT_NAME"), column("DOMAIN_CATALOG"), column("DOMAIN_SCHEMA"), column("DOMAIN_NAME"), column("IS_DEFERRABLE"), column("INITIALLY_DEFERRED"), column("REMARKS"), column("SQL"), column("ID", TypeInfo.TYPE_INTEGER)};
                break;
            case 32:
                setMetaTableName("KEY_COLUMN_USAGE");
                columnArr = new Column[]{column("CONSTRAINT_CATALOG"), column("CONSTRAINT_SCHEMA"), column("CONSTRAINT_NAME"), column("TABLE_CATALOG"), column("TABLE_SCHEMA"), column("TABLE_NAME"), column("COLUMN_NAME"), column("ORDINAL_POSITION", TypeInfo.TYPE_INTEGER), column("POSITION_IN_UNIQUE_CONSTRAINT", TypeInfo.TYPE_INTEGER), column("INDEX_CATALOG"), column("INDEX_SCHEMA"), column("INDEX_NAME")};
                str = "TABLE_NAME";
                break;
            case 33:
                setMetaTableName("REFERENTIAL_CONSTRAINTS");
                columnArr = new Column[]{column("CONSTRAINT_CATALOG"), column("CONSTRAINT_SCHEMA"), column("CONSTRAINT_NAME"), column("UNIQUE_CONSTRAINT_CATALOG"), column("UNIQUE_CONSTRAINT_SCHEMA"), column("UNIQUE_CONSTRAINT_NAME"), column("MATCH_OPTION"), column("UPDATE_RULE"), column("DELETE_RULE")};
                break;
            case 34:
                setMetaTableName("CHECK_CONSTRAINTS");
                columnArr = new Column[]{column("CONSTRAINT_CATALOG"), column("CONSTRAINT_SCHEMA"), column("CONSTRAINT_NAME"), column("CHECK_CLAUSE")};
                break;
            case 35:
                setMetaTableName("CONSTRAINT_COLUMN_USAGE");
                columnArr = new Column[]{column("TABLE_CATALOG"), column("TABLE_SCHEMA"), column("TABLE_NAME"), column("COLUMN_NAME"), column("CONSTRAINT_CATALOG"), column("CONSTRAINT_SCHEMA"), column("CONSTRAINT_NAME")};
                str = "TABLE_NAME";
                break;
            default:
                throw DbException.getInternalError("type=" + i2);
        }
        setColumns(columnArr);
        if (str == null) {
            this.indexColumn = -1;
            this.metaIndex = null;
        } else {
            this.indexColumn = getColumn(this.database.sysIdentifier(str)).getColumnId();
            this.metaIndex = new MetaIndex(this, IndexColumn.wrap(new Column[]{columnArr[this.indexColumn]}), false);
        }
    }

    private static String replaceNullWithEmpty(String str) {
        return str == null ? "" : str;
    }

    @Override // org.h2.table.MetaTable
    public ArrayList<Row> generateRows(SessionLocal sessionLocal, SearchRow searchRow, SearchRow searchRow2) {
        short s;
        short s2;
        Value value = (this.indexColumn < 0 || searchRow == null) ? null : searchRow.getValue(this.indexColumn);
        Value value2 = (this.indexColumn < 0 || searchRow2 == null) ? null : searchRow2.getValue(this.indexColumn);
        ArrayList<Row> newSmallArrayList = Utils.newSmallArrayList();
        String shortName = this.database.getShortName();
        boolean isAdmin = sessionLocal.getUser().isAdmin();
        switch (this.type) {
            case 0:
                getAllTables(sessionLocal, value, value2).forEach(table -> {
                    String str;
                    if (table.isTemporary()) {
                        if (table.isGlobalTemporary()) {
                            str = "GLOBAL TEMPORARY";
                        } else {
                            str = "LOCAL TEMPORARY";
                        }
                    } else {
                        str = table.isPersistIndexes() ? "CACHED" : "MEMORY";
                    }
                    String createSQL = table.getCreateSQL();
                    if (!isAdmin && createSQL != null && createSQL.contains(DbException.HIDE_SQL)) {
                        createSQL = "-";
                    }
                    add(sessionLocal, newSmallArrayList, shortName, table.getSchema().getName(), table.getName(), table.getTableType().toString(), str, createSQL, replaceNullWithEmpty(table.getComment()), ValueBigint.get(table.getMaxDataModificationId()), ValueInteger.get(table.getId()), null, table.getClass().getName(), ValueBigint.get(table.getRowCountApproximation(sessionLocal)));
                });
                break;
            case 1:
                getAllTables(sessionLocal, value, value2).forEach(table2 -> {
                    boolean z;
                    Column[] columns = table2.getColumns();
                    String name = this.database.getCompareMode().getName();
                    for (int i = 0; i < columns.length; i++) {
                        Column column = columns[i];
                        Domain domain = column.getDomain();
                        TypeInfo type = column.getType();
                        ValueInteger valueInteger = ValueInteger.get(MathUtils.convertLongToInt(type.getPrecision()));
                        ValueInteger valueInteger2 = ValueInteger.get(type.getScale());
                        Sequence sequence = column.getSequence();
                        int valueType = type.getValueType();
                        switch (valueType) {
                            case 17:
                            case 18:
                            case 19:
                            case 20:
                            case 21:
                            case 27:
                            case 31:
                            case 33:
                            case 34:
                                z = true;
                                break;
                            case 22:
                            case 23:
                            case 24:
                            case 25:
                            case 26:
                            case 28:
                            case 29:
                            case 30:
                            case 32:
                            default:
                                z = false;
                                break;
                        }
                        boolean isGenerated = column.isGenerated();
                        boolean isIntervalType = DataType.isIntervalType(valueType);
                        String createSQLWithoutName = column.getCreateSQLWithoutName();
                        Object[] objArr = new Object[34];
                        objArr[0] = shortName;
                        objArr[1] = table2.getSchema().getName();
                        objArr[2] = table2.getName();
                        objArr[3] = column.getName();
                        objArr[4] = ValueInteger.get(i + 1);
                        objArr[5] = isGenerated ? null : column.getDefaultSQL();
                        objArr[6] = column.isNullable() ? "YES" : "NO";
                        objArr[7] = ValueInteger.get(DataType.convertTypeToSQLType(type));
                        objArr[8] = valueInteger;
                        objArr[9] = valueInteger;
                        objArr[10] = valueInteger;
                        objArr[11] = ValueInteger.get(10);
                        objArr[12] = valueInteger2;
                        objArr[13] = z ? valueInteger2 : null;
                        objArr[14] = isIntervalType ? createSQLWithoutName.substring(9) : null;
                        objArr[15] = isIntervalType ? valueInteger : null;
                        objArr[16] = CHARACTER_SET_NAME;
                        objArr[17] = name;
                        objArr[18] = domain != null ? shortName : null;
                        objArr[19] = domain != null ? domain.getSchema().getName() : null;
                        objArr[20] = domain != null ? domain.getName() : null;
                        objArr[21] = isGenerated ? "ALWAYS" : "NEVER";
                        objArr[22] = isGenerated ? column.getDefaultSQL() : null;
                        objArr[23] = identifier(isIntervalType ? "INTERVAL" : type.getDeclaredTypeName());
                        objArr[24] = ValueInteger.get(column.isNullable() ? 1 : 0);
                        objArr[25] = ValueBoolean.get(isGenerated);
                        objArr[26] = ValueInteger.get(column.getSelectivity());
                        objArr[27] = sequence == null ? null : sequence.getName();
                        objArr[28] = replaceNullWithEmpty(column.getComment());
                        objArr[29] = null;
                        objArr[30] = createSQLWithoutName;
                        objArr[31] = column.getOnUpdateSQL();
                        objArr[32] = ValueBoolean.get(column.getVisible());
                        objArr[33] = null;
                        add(sessionLocal, newSmallArrayList, objArr);
                    }
                });
                break;
            case 2:
                getAllTables(sessionLocal, value, value2).forEach(table3 -> {
                    ArrayList<Index> indexes = table3.getIndexes();
                    ArrayList<Constraint> constraints = table3.getConstraints();
                    for (int i = 0; indexes != null && i < indexes.size(); i++) {
                        Index index = indexes.get(i);
                        if (index.getCreateSQL() != null) {
                            String str = null;
                            for (int i2 = 0; constraints != null && i2 < constraints.size(); i2++) {
                                Constraint constraint = constraints.get(i2);
                                if (constraint.usesIndex(index)) {
                                    if (index.getIndexType().isPrimaryKey()) {
                                        if (constraint.getConstraintType() == Constraint.Type.PRIMARY_KEY) {
                                            str = constraint.getName();
                                        }
                                    } else {
                                        str = constraint.getName();
                                    }
                                }
                            }
                            IndexColumn[] indexColumns = index.getIndexColumns();
                            int uniqueColumnCount = index.getUniqueColumnCount();
                            String name = index.getClass().getName();
                            int i3 = 0;
                            while (i3 < indexColumns.length) {
                                IndexColumn indexColumn = indexColumns[i3];
                                Column column = indexColumn.column;
                                Object[] objArr = new Object[21];
                                objArr[0] = shortName;
                                objArr[1] = table3.getSchema().getName();
                                objArr[2] = table3.getName();
                                objArr[3] = ValueBoolean.get(i3 >= uniqueColumnCount);
                                objArr[4] = index.getName();
                                objArr[5] = ValueSmallint.get((short) (i3 + 1));
                                objArr[6] = column.getName();
                                objArr[7] = ValueInteger.get(0);
                                objArr[8] = ValueBoolean.get(index.getIndexType().isPrimaryKey());
                                objArr[9] = index.getIndexType().getSQL(false);
                                objArr[10] = ValueBoolean.get(index.getIndexType().getBelongsToConstraint());
                                objArr[11] = ValueSmallint.get((short) 3);
                                objArr[12] = (indexColumn.sortType & 1) != 0 ? "D" : "A";
                                objArr[13] = ValueInteger.get(0);
                                objArr[14] = "";
                                objArr[15] = replaceNullWithEmpty(index.getComment());
                                objArr[16] = index.getCreateSQL();
                                objArr[17] = ValueInteger.get(index.getId());
                                objArr[18] = ValueInteger.get(indexColumn.sortType);
                                objArr[19] = str;
                                objArr[20] = name;
                                add(sessionLocal, newSmallArrayList, objArr);
                                i3++;
                            }
                        }
                    }
                });
                break;
            case 3:
                add(sessionLocal, newSmallArrayList, TableType.TABLE.toString());
                add(sessionLocal, newSmallArrayList, TableType.TABLE_LINK.toString());
                add(sessionLocal, newSmallArrayList, TableType.SYSTEM_TABLE.toString());
                add(sessionLocal, newSmallArrayList, TableType.VIEW.toString());
                add(sessionLocal, newSmallArrayList, TableType.EXTERNAL_TABLE_ENGINE.toString());
                break;
            case 4:
                for (int i = 1; i < 42; i++) {
                    DataType dataType = DataType.getDataType(i);
                    Object[] objArr = new Object[14];
                    objArr[0] = Value.getTypeName(dataType.type);
                    objArr[1] = ValueInteger.get(dataType.sqlType);
                    objArr[2] = ValueInteger.get(MathUtils.convertLongToInt(dataType.maxPrecision));
                    objArr[3] = dataType.prefix;
                    objArr[4] = dataType.suffix;
                    objArr[5] = dataType.params;
                    objArr[6] = ValueBoolean.FALSE;
                    objArr[7] = ValueSmallint.get(MathUtils.convertIntToShort(dataType.minScale));
                    objArr[8] = ValueSmallint.get(MathUtils.convertIntToShort(dataType.maxScale));
                    objArr[9] = DataType.isNumericType(i) ? ValueInteger.get(10) : null;
                    objArr[10] = ValueInteger.get(dataType.type);
                    objArr[11] = ValueBoolean.get(dataType.caseSensitive);
                    objArr[12] = ValueSmallint.get((short) 1);
                    objArr[13] = ValueSmallint.get((short) 3);
                    add(sessionLocal, newSmallArrayList, objArr);
                }
                break;
            case 5:
                add(sessionLocal, newSmallArrayList, shortName);
                break;
            case 6:
                for (Setting setting : this.database.getAllSettings()) {
                    Object stringValue = setting.getStringValue();
                    if (stringValue == null) {
                        stringValue = Integer.toString(setting.getIntValue());
                    }
                    add(sessionLocal, newSmallArrayList, identifier(setting.getName()), stringValue);
                }
                add(sessionLocal, newSmallArrayList, "info.BUILD_ID", "232");
                add(sessionLocal, newSmallArrayList, "info.VERSION_MAJOR", "2");
                add(sessionLocal, newSmallArrayList, "info.VERSION_MINOR", "3");
                add(sessionLocal, newSmallArrayList, "info.VERSION", Constants.FULL_VERSION);
                if (isAdmin) {
                    for (String str : new String[]{"java.runtime.version", "java.vm.name", "java.vendor", "os.name", "os.arch", "os.version", "sun.os.patch.level", "file.separator", "path.separator", "line.separator", "user.country", "user.language", "user.variant", "file.encoding"}) {
                        add(sessionLocal, newSmallArrayList, "property." + str, Utils.getProperty(str, ""));
                    }
                }
                add(sessionLocal, newSmallArrayList, "DEFAULT_NULL_ORDERING", this.database.getDefaultNullOrdering().name());
                Object[] objArr2 = new Object[2];
                objArr2[0] = "EXCLUSIVE";
                objArr2[1] = this.database.getExclusiveSession() == null ? "FALSE" : Constants.CLUSTERING_ENABLED;
                add(sessionLocal, newSmallArrayList, objArr2);
                add(sessionLocal, newSmallArrayList, "MODE", this.database.getMode().getName());
                add(sessionLocal, newSmallArrayList, "QUERY_TIMEOUT", Integer.toString(sessionLocal.getQueryTimeout()));
                add(sessionLocal, newSmallArrayList, "TIME ZONE", sessionLocal.currentTimeZone().getId());
                Object[] objArr3 = new Object[2];
                objArr3[0] = "TRUNCATE_LARGE_LENGTH";
                objArr3[1] = sessionLocal.isTruncateLargeLength() ? Constants.CLUSTERING_ENABLED : "FALSE";
                add(sessionLocal, newSmallArrayList, objArr3);
                Object[] objArr4 = new Object[2];
                objArr4[0] = "VARIABLE_BINARY";
                objArr4[1] = sessionLocal.isVariableBinary() ? Constants.CLUSTERING_ENABLED : "FALSE";
                add(sessionLocal, newSmallArrayList, objArr4);
                Object[] objArr5 = new Object[2];
                objArr5[0] = "OLD_INFORMATION_SCHEMA";
                objArr5[1] = sessionLocal.isOldInformationSchema() ? Constants.CLUSTERING_ENABLED : "FALSE";
                add(sessionLocal, newSmallArrayList, objArr5);
                BitSet nonKeywords = sessionLocal.getNonKeywords();
                if (nonKeywords != null) {
                    add(sessionLocal, newSmallArrayList, "NON_KEYWORDS", ParserBase.formatNonKeywords(nonKeywords));
                }
                add(sessionLocal, newSmallArrayList, "RETENTION_TIME", Integer.toString(this.database.getRetentionTime()));
                for (Map.Entry<String, String> entry : this.database.getSettings().getSortedSettings()) {
                    add(sessionLocal, newSmallArrayList, entry.getKey(), entry.getValue());
                }
                this.database.getStore().getMvStore().populateInfo((str2, str3) -> {
                    add(sessionLocal, newSmallArrayList, str2, str3);
                });
                break;
            case 7:
                try {
                    InputStreamReader inputStreamReader = new InputStreamReader(new ByteArrayInputStream(Utils.getResource("/org/h2/res/help.csv")), StandardCharsets.UTF_8);
                    Csv csv = new Csv();
                    csv.setLineCommentCharacter('#');
                    ResultSet read = csv.read(inputStreamReader, null);
                    int columnCount = read.getMetaData().getColumnCount() - 1;
                    Object[] objArr6 = new String[5];
                    int i2 = 0;
                    while (read.next()) {
                        for (int i3 = 0; i3 < columnCount; i3++) {
                            String string = read.getString(1 + i3);
                            switch (i3) {
                                case 2:
                                    string = Help.stripAnnotationsFromSyntax(string);
                                    break;
                                case 3:
                                    string = Help.processHelpText(string);
                                    break;
                            }
                            objArr6[i3] = string.trim();
                        }
                        add(sessionLocal, newSmallArrayList, ValueInteger.get(i2), objArr6[0], objArr6[1], objArr6[2], objArr6[3]);
                        i2++;
                    }
                    break;
                } catch (Exception e) {
                    throw DbException.convert(e);
                }
            case 8:
                Iterator<SchemaObject> it = getAllSchemaObjects(3).iterator();
                while (it.hasNext()) {
                    Sequence sequence = (Sequence) it.next();
                    TypeInfo dataType2 = sequence.getDataType();
                    Object typeName = Value.getTypeName(dataType2.getValueType());
                    Object obj = ValueInteger.get(dataType2.getScale());
                    Object[] objArr7 = new Object[23];
                    objArr7[0] = shortName;
                    objArr7[1] = sequence.getSchema().getName();
                    objArr7[2] = sequence.getName();
                    objArr7[3] = typeName;
                    objArr7[4] = ValueInteger.get(sequence.getEffectivePrecision());
                    objArr7[5] = ValueInteger.get(10);
                    objArr7[6] = obj;
                    objArr7[7] = ValueBigint.get(sequence.getStartValue());
                    objArr7[8] = ValueBigint.get(sequence.getMinValue());
                    objArr7[9] = ValueBigint.get(sequence.getMaxValue());
                    objArr7[10] = ValueBigint.get(sequence.getIncrement());
                    objArr7[11] = sequence.getCycle().isCycle() ? "YES" : "NO";
                    objArr7[12] = typeName;
                    objArr7[13] = ValueInteger.get((int) dataType2.getPrecision());
                    objArr7[14] = obj;
                    objArr7[15] = ValueBigint.get(sequence.getCurrentValue());
                    objArr7[16] = ValueBoolean.get(sequence.getBelongsToTable());
                    objArr7[17] = replaceNullWithEmpty(sequence.getComment());
                    objArr7[18] = ValueBigint.get(sequence.getCacheSize());
                    objArr7[19] = ValueInteger.get(sequence.getId());
                    objArr7[20] = ValueBigint.get(sequence.getMinValue());
                    objArr7[21] = ValueBigint.get(sequence.getMaxValue());
                    objArr7[22] = ValueBoolean.get(sequence.getCycle().isCycle());
                    add(sessionLocal, newSmallArrayList, objArr7);
                }
                break;
            case 9:
                for (RightOwner rightOwner : this.database.getAllUsersAndRoles()) {
                    if (rightOwner instanceof User) {
                        User user = (User) rightOwner;
                        if (isAdmin || sessionLocal.getUser() == user) {
                            add(sessionLocal, newSmallArrayList, identifier(user.getName()), String.valueOf(user.isAdmin()), replaceNullWithEmpty(user.getComment()), ValueInteger.get(user.getId()));
                        }
                    }
                }
                break;
            case 10:
                for (RightOwner rightOwner2 : this.database.getAllUsersAndRoles()) {
                    if (rightOwner2 instanceof Role) {
                        Role role = (Role) rightOwner2;
                        if (isAdmin || sessionLocal.getUser().isRoleGranted(role)) {
                            add(sessionLocal, newSmallArrayList, identifier(role.getName()), replaceNullWithEmpty(role.getComment()), ValueInteger.get(role.getId()));
                        }
                    }
                }
                break;
            case 11:
                if (isAdmin) {
                    Iterator<Right> it2 = this.database.getAllRights().iterator();
                    while (it2.hasNext()) {
                        Right next = it2.next();
                        Role grantedRole = next.getGrantedRole();
                        DbObject grantee = next.getGrantee();
                        Object obj2 = grantee.getType() == 2 ? "USER" : "ROLE";
                        if (grantedRole == null) {
                            DbObject grantedObject = next.getGrantedObject();
                            Schema schema = null;
                            Table table4 = null;
                            if (grantedObject != null) {
                                if (grantedObject instanceof Schema) {
                                    schema = (Schema) grantedObject;
                                } else if (grantedObject instanceof Table) {
                                    table4 = (Table) grantedObject;
                                    schema = table4.getSchema();
                                }
                            }
                            String name = table4 != null ? table4.getName() : "";
                            Object name2 = schema != null ? schema.getName() : "";
                            if (checkIndex(sessionLocal, name, value, value2)) {
                                add(sessionLocal, newSmallArrayList, identifier(grantee.getName()), obj2, "", next.getRights(), name2, name, ValueInteger.get(next.getId()));
                            }
                        } else {
                            add(sessionLocal, newSmallArrayList, identifier(grantee.getName()), obj2, identifier(grantedRole.getName()), "", "", "", ValueInteger.get(next.getId()));
                        }
                    }
                    break;
                }
                break;
            case 12:
                Iterator<Schema> it3 = this.database.getAllSchemas().iterator();
                while (it3.hasNext()) {
                    for (UserDefinedFunction userDefinedFunction : it3.next().getAllFunctionsAndAggregates()) {
                        if (userDefinedFunction instanceof FunctionAlias) {
                            FunctionAlias functionAlias = (FunctionAlias) userDefinedFunction;
                            try {
                                for (FunctionAlias.JavaMethod javaMethod : functionAlias.getJavaMethods()) {
                                    TypeInfo dataType3 = javaMethod.getDataType();
                                    if (dataType3 == null) {
                                        dataType3 = TypeInfo.TYPE_NULL;
                                    }
                                    Object[] objArr8 = new Object[12];
                                    objArr8[0] = shortName;
                                    objArr8[1] = functionAlias.getSchema().getName();
                                    objArr8[2] = functionAlias.getName();
                                    objArr8[3] = functionAlias.getJavaClassName();
                                    objArr8[4] = functionAlias.getJavaMethodName();
                                    objArr8[5] = ValueInteger.get(DataType.convertTypeToSQLType(dataType3));
                                    objArr8[6] = dataType3.getDeclaredTypeName();
                                    objArr8[7] = ValueInteger.get(javaMethod.getParameterCount());
                                    if (dataType3.getValueType() == 0) {
                                        s2 = 1;
                                    } else {
                                        s2 = 2;
                                    }
                                    objArr8[8] = ValueSmallint.get(s2);
                                    objArr8[9] = replaceNullWithEmpty(functionAlias.getComment());
                                    objArr8[10] = ValueInteger.get(functionAlias.getId());
                                    objArr8[11] = functionAlias.getSource();
                                    add(sessionLocal, newSmallArrayList, objArr8);
                                }
                            } catch (DbException e2) {
                            }
                        } else {
                            add(sessionLocal, newSmallArrayList, shortName, this.database.getMainSchema().getName(), userDefinedFunction.getName(), userDefinedFunction.getJavaClassName(), "", ValueInteger.get(0), "NULL", ValueInteger.get(1), ValueSmallint.get((short) 2), replaceNullWithEmpty(userDefinedFunction.getComment()), ValueInteger.get(userDefinedFunction.getId()), "");
                        }
                    }
                }
                break;
            case 13:
                Object name3 = this.database.getCompareMode().getName();
                for (Schema schema2 : this.database.getAllSchemas()) {
                    Object[] objArr9 = new Object[8];
                    objArr9[0] = shortName;
                    objArr9[1] = schema2.getName();
                    objArr9[2] = identifier(schema2.getOwner().getName());
                    objArr9[3] = CHARACTER_SET_NAME;
                    objArr9[4] = name3;
                    objArr9[5] = ValueBoolean.get(schema2.getId() == 0);
                    objArr9[6] = replaceNullWithEmpty(schema2.getComment());
                    objArr9[7] = ValueInteger.get(schema2.getId());
                    add(sessionLocal, newSmallArrayList, objArr9);
                }
                break;
            case 14:
                Iterator<Right> it4 = this.database.getAllRights().iterator();
                while (it4.hasNext()) {
                    Right next2 = it4.next();
                    DbObject grantedObject2 = next2.getGrantedObject();
                    if (grantedObject2 instanceof Table) {
                        Table table5 = (Table) grantedObject2;
                        if (checkIndex(sessionLocal, table5.getName(), value, value2)) {
                            addPrivileges(sessionLocal, newSmallArrayList, next2.getGrantee(), shortName, table5, null, next2.getRightMask());
                        }
                    }
                }
                break;
            case 15:
                Iterator<Right> it5 = this.database.getAllRights().iterator();
                while (it5.hasNext()) {
                    Right next3 = it5.next();
                    DbObject grantedObject3 = next3.getGrantedObject();
                    if (grantedObject3 instanceof Table) {
                        Table table6 = (Table) grantedObject3;
                        if (checkIndex(sessionLocal, table6.getName(), value, value2)) {
                            DbObject grantee2 = next3.getGrantee();
                            int rightMask = next3.getRightMask();
                            for (Column column : table6.getColumns()) {
                                addPrivileges(sessionLocal, newSmallArrayList, grantee2, shortName, table6, column.getName(), rightMask);
                            }
                        }
                    }
                }
                break;
            case 16:
                for (Locale locale : CompareMode.getCollationLocales(false)) {
                    add(sessionLocal, newSmallArrayList, CompareMode.getName(locale), locale.toString());
                }
                break;
            case 17:
                getAllTables(sessionLocal, value, value2).filter((v0) -> {
                    return v0.isView();
                }).forEach(table7 -> {
                    Object[] objArr10 = new Object[9];
                    objArr10[0] = shortName;
                    objArr10[1] = table7.getSchema().getName();
                    objArr10[2] = table7.getName();
                    objArr10[3] = table7.getCreateSQL();
                    objArr10[4] = "NONE";
                    objArr10[5] = "NO";
                    objArr10[6] = ((table7 instanceof TableView) && ((TableView) table7).isInvalid()) ? "INVALID" : "VALID";
                    objArr10[7] = replaceNullWithEmpty(table7.getComment());
                    objArr10[8] = ValueInteger.get(table7.getId());
                    add(sessionLocal, newSmallArrayList, objArr10);
                });
                break;
            case 18:
                ArrayList<InDoubtTransaction> inDoubtTransactions = this.database.getInDoubtTransactions();
                if (inDoubtTransactions != null && isAdmin) {
                    Iterator<InDoubtTransaction> it6 = inDoubtTransactions.iterator();
                    while (it6.hasNext()) {
                        InDoubtTransaction next4 = it6.next();
                        add(sessionLocal, newSmallArrayList, next4.getTransactionName(), next4.getStateDescription());
                    }
                    break;
                }
                break;
            case 19:
                getAllConstraints(sessionLocal).filter(constraint -> {
                    return constraint.getConstraintType() == Constraint.Type.REFERENTIAL && checkIndex(sessionLocal, constraint.getName(), value, value2);
                }).forEach(constraint2 -> {
                    ConstraintReferential constraintReferential = (ConstraintReferential) constraint2;
                    IndexColumn[] columns = constraintReferential.getColumns();
                    IndexColumn[] refColumns = constraintReferential.getRefColumns();
                    Table table8 = constraintReferential.getTable();
                    Table refTable = constraintReferential.getRefTable();
                    ValueSmallint valueSmallint = ValueSmallint.get(getRefAction(constraintReferential.getUpdateAction()));
                    ValueSmallint valueSmallint2 = ValueSmallint.get(getRefAction(constraintReferential.getDeleteAction()));
                    for (int i4 = 0; i4 < columns.length; i4++) {
                        add(sessionLocal, newSmallArrayList, shortName, refTable.getSchema().getName(), refTable.getName(), refColumns[i4].column.getName(), shortName, table8.getSchema().getName(), table8.getName(), columns[i4].column.getName(), ValueSmallint.get((short) (i4 + 1)), valueSmallint, valueSmallint2, constraintReferential.getName(), constraintReferential.getReferencedConstraint().getName(), ValueSmallint.get((short) 7));
                    }
                });
                break;
            case 20:
                Iterator<Schema> it7 = this.database.getAllSchemas().iterator();
                while (it7.hasNext()) {
                    for (UserDefinedFunction userDefinedFunction2 : it7.next().getAllFunctionsAndAggregates()) {
                        if (userDefinedFunction2 instanceof FunctionAlias) {
                            FunctionAlias functionAlias2 = (FunctionAlias) userDefinedFunction2;
                            try {
                                for (FunctionAlias.JavaMethod javaMethod2 : functionAlias2.getJavaMethods()) {
                                    TypeInfo dataType4 = javaMethod2.getDataType();
                                    if (dataType4 != null && dataType4.getValueType() != 0) {
                                        DataType dataType5 = DataType.getDataType(dataType4.getValueType());
                                        add(sessionLocal, newSmallArrayList, shortName, functionAlias2.getSchema().getName(), functionAlias2.getName(), functionAlias2.getJavaClassName(), functionAlias2.getJavaMethodName(), ValueInteger.get(javaMethod2.getParameterCount()), ValueInteger.get(0), "P0", ValueInteger.get(DataType.convertTypeToSQLType(dataType4)), dataType4.getDeclaredTypeName(), ValueInteger.get(MathUtils.convertLongToInt(dataType5.defaultPrecision)), ValueSmallint.get(MathUtils.convertIntToShort(dataType5.defaultScale)), ValueSmallint.get((short) 10), ValueSmallint.get((short) 2), ValueSmallint.get((short) 5), "", null);
                                    }
                                    Class<?>[] columnClasses = javaMethod2.getColumnClasses();
                                    for (int i4 = 0; i4 < columnClasses.length; i4++) {
                                        if (!javaMethod2.hasConnectionParam() || i4 != 0) {
                                            Class<?> cls = columnClasses[i4];
                                            TypeInfo classToType = ValueToObjectConverter2.classToType(cls);
                                            DataType dataType6 = DataType.getDataType(classToType.getValueType());
                                            Object[] objArr10 = new Object[17];
                                            objArr10[0] = shortName;
                                            objArr10[1] = functionAlias2.getSchema().getName();
                                            objArr10[2] = functionAlias2.getName();
                                            objArr10[3] = functionAlias2.getJavaClassName();
                                            objArr10[4] = functionAlias2.getJavaMethodName();
                                            objArr10[5] = ValueInteger.get(javaMethod2.getParameterCount());
                                            objArr10[6] = ValueInteger.get(i4 + (javaMethod2.hasConnectionParam() ? 0 : 1));
                                            objArr10[7] = "P" + (i4 + 1);
                                            objArr10[8] = ValueInteger.get(DataType.convertTypeToSQLType(classToType));
                                            objArr10[9] = classToType.getDeclaredTypeName();
                                            objArr10[10] = ValueInteger.get(MathUtils.convertLongToInt(dataType6.defaultPrecision));
                                            objArr10[11] = ValueSmallint.get(MathUtils.convertIntToShort(dataType6.defaultScale));
                                            objArr10[12] = ValueSmallint.get((short) 10);
                                            if (cls.isPrimitive()) {
                                                s = 0;
                                            } else {
                                                s = 1;
                                            }
                                            objArr10[13] = ValueSmallint.get(s);
                                            objArr10[14] = ValueSmallint.get((short) 1);
                                            objArr10[15] = "";
                                            objArr10[16] = null;
                                            add(sessionLocal, newSmallArrayList, objArr10);
                                        }
                                    }
                                }
                            } catch (DbException e3) {
                            }
                        }
                    }
                }
                break;
            case 21:
                getAllConstraints(sessionLocal).filter(constraint3 -> {
                    return constraint3.getConstraintType() != Constraint.Type.DOMAIN && checkIndex(sessionLocal, constraint3.getTable().getName(), value, value2);
                }).forEach(constraint4 -> {
                    Constraint.Type constraintType = constraint4.getConstraintType();
                    String str4 = null;
                    IndexColumn[] indexColumnArr = null;
                    Table table8 = constraint4.getTable();
                    Index index = constraint4.getIndex();
                    String str5 = null;
                    if (index != null) {
                        str5 = index.getName();
                    }
                    if (constraintType == Constraint.Type.CHECK) {
                        str4 = constraint4.getExpression().getSQL(0);
                    } else if (constraintType.isUnique()) {
                        indexColumnArr = ((ConstraintUnique) constraint4).getColumns();
                    } else if (constraintType == Constraint.Type.REFERENTIAL) {
                        indexColumnArr = ((ConstraintReferential) constraint4).getColumns();
                    }
                    String str6 = null;
                    if (indexColumnArr != null) {
                        StringBuilder sb = new StringBuilder();
                        int length = indexColumnArr.length;
                        for (int i5 = 0; i5 < length; i5++) {
                            if (i5 > 0) {
                                sb.append(',');
                            }
                            sb.append(indexColumnArr[i5].column.getName());
                        }
                        str6 = sb.toString();
                    }
                    Object[] objArr11 = new Object[13];
                    objArr11[0] = shortName;
                    objArr11[1] = constraint4.getSchema().getName();
                    objArr11[2] = constraint4.getName();
                    objArr11[3] = constraintType == Constraint.Type.PRIMARY_KEY ? constraintType.getSqlName() : constraintType.name();
                    objArr11[4] = shortName;
                    objArr11[5] = table8.getSchema().getName();
                    objArr11[6] = table8.getName();
                    objArr11[7] = str5;
                    objArr11[8] = str4;
                    objArr11[9] = str6;
                    objArr11[10] = replaceNullWithEmpty(constraint4.getComment());
                    objArr11[11] = constraint4.getCreateSQL();
                    objArr11[12] = ValueInteger.get(constraint4.getId());
                    add(sessionLocal, newSmallArrayList, objArr11);
                });
                break;
            case 22:
                Iterator<SchemaObject> it8 = getAllSchemaObjects(11).iterator();
                while (it8.hasNext()) {
                    Constant constant = (Constant) it8.next();
                    ValueExpression value3 = constant.getValue();
                    add(sessionLocal, newSmallArrayList, shortName, constant.getSchema().getName(), constant.getName(), ValueInteger.get(DataType.convertTypeToSQLType(value3.getType())), replaceNullWithEmpty(constant.getComment()), value3.getSQL(0), ValueInteger.get(constant.getId()));
                }
                break;
            case 23:
                Iterator<SchemaObject> it9 = getAllSchemaObjects(12).iterator();
                while (it9.hasNext()) {
                    Domain domain = (Domain) it9.next();
                    Domain domain2 = domain.getDomain();
                    TypeInfo dataType7 = domain.getDataType();
                    Object[] objArr11 = new Object[19];
                    objArr11[0] = shortName;
                    objArr11[1] = domain.getSchema().getName();
                    objArr11[2] = domain.getName();
                    objArr11[3] = domain.getDefaultSQL();
                    objArr11[4] = domain.getOnUpdateSQL();
                    objArr11[5] = ValueInteger.get(DataType.convertTypeToSQLType(dataType7));
                    objArr11[6] = ValueInteger.get(MathUtils.convertLongToInt(dataType7.getPrecision()));
                    objArr11[7] = ValueInteger.get(dataType7.getScale());
                    objArr11[8] = dataType7.getDeclaredTypeName();
                    objArr11[9] = domain2 != null ? shortName : null;
                    objArr11[10] = domain2 != null ? domain2.getSchema().getName() : null;
                    objArr11[11] = domain2 != null ? domain2.getName() : null;
                    objArr11[12] = ValueInteger.get(50);
                    objArr11[13] = replaceNullWithEmpty(domain.getComment());
                    objArr11[14] = domain.getCreateSQL();
                    objArr11[15] = ValueInteger.get(domain.getId());
                    objArr11[16] = domain.getDefaultSQL();
                    objArr11[17] = "YES";
                    objArr11[18] = null;
                    add(sessionLocal, newSmallArrayList, objArr11);
                }
                break;
            case 24:
                Iterator<SchemaObject> it10 = getAllSchemaObjects(4).iterator();
                while (it10.hasNext()) {
                    TriggerObject triggerObject = (TriggerObject) it10.next();
                    Table table8 = triggerObject.getTable();
                    add(sessionLocal, newSmallArrayList, shortName, triggerObject.getSchema().getName(), triggerObject.getName(), triggerObject.getTypeNameList(new StringBuilder()).toString(), shortName, table8.getSchema().getName(), table8.getName(), ValueBoolean.get(triggerObject.isBefore()), triggerObject.getTriggerClassName(), ValueInteger.get(triggerObject.getQueueSize()), ValueBoolean.get(triggerObject.isNoWait()), replaceNullWithEmpty(triggerObject.getComment()), triggerObject.getCreateSQL(), ValueInteger.get(triggerObject.getId()));
                }
                break;
            case 25:
                for (SessionLocal sessionLocal2 : this.database.getSessions(false)) {
                    if (isAdmin || sessionLocal2 == sessionLocal) {
                        NetworkConnectionInfo networkConnectionInfo = sessionLocal2.getNetworkConnectionInfo();
                        Command currentCommand = sessionLocal2.getCurrentCommand();
                        int blockingSessionId = sessionLocal2.getBlockingSessionId();
                        Object[] objArr12 = new Object[13];
                        objArr12[0] = ValueInteger.get(sessionLocal2.getId());
                        objArr12[1] = sessionLocal2.getUser().getName();
                        objArr12[2] = networkConnectionInfo == null ? null : networkConnectionInfo.getServer();
                        objArr12[3] = networkConnectionInfo == null ? null : networkConnectionInfo.getClient();
                        objArr12[4] = networkConnectionInfo == null ? null : networkConnectionInfo.getClientInfo();
                        objArr12[5] = sessionLocal2.getSessionStart();
                        objArr12[6] = sessionLocal2.getIsolationLevel().getSQL();
                        objArr12[7] = currentCommand == null ? null : currentCommand.toString();
                        objArr12[8] = currentCommand == null ? null : sessionLocal2.getCommandStartOrEnd();
                        objArr12[9] = ValueBoolean.get(sessionLocal2.hasPendingTransaction());
                        objArr12[10] = String.valueOf(sessionLocal2.getState());
                        objArr12[11] = blockingSessionId == 0 ? null : ValueInteger.get(blockingSessionId);
                        objArr12[12] = sessionLocal2.getState() == SessionLocal.State.SLEEP ? sessionLocal2.getCommandStartOrEnd() : null;
                        add(sessionLocal, newSmallArrayList, objArr12);
                    }
                }
                break;
            case 26:
                for (SessionLocal sessionLocal3 : this.database.getSessions(false)) {
                    if (isAdmin || sessionLocal3 == sessionLocal) {
                        for (Table table9 : sessionLocal3.getLocks()) {
                            Object[] objArr13 = new Object[4];
                            objArr13[0] = table9.getSchema().getName();
                            objArr13[1] = table9.getName();
                            objArr13[2] = ValueInteger.get(sessionLocal3.getId());
                            objArr13[3] = table9.isLockedExclusivelyBy(sessionLocal3) ? "WRITE" : "READ";
                            add(sessionLocal, newSmallArrayList, objArr13);
                        }
                    }
                }
                break;
            case 27:
                for (String str4 : sessionLocal.getVariableNames()) {
                    Value variable = sessionLocal.getVariable(str4);
                    StringBuilder append = new StringBuilder().append("SET @").append(str4).append(' ');
                    variable.getSQL(append, 0);
                    add(sessionLocal, newSmallArrayList, "@" + str4, append.toString());
                }
                for (Table table10 : sessionLocal.getLocalTempTables()) {
                    add(sessionLocal, newSmallArrayList, "TABLE " + table10.getName(), table10.getCreateSQL());
                }
                String[] schemaSearchPath = sessionLocal.getSchemaSearchPath();
                if (schemaSearchPath != null && schemaSearchPath.length > 0) {
                    StringBuilder sb = new StringBuilder("SET SCHEMA_SEARCH_PATH ");
                    int length = schemaSearchPath.length;
                    for (int i5 = 0; i5 < length; i5++) {
                        if (i5 > 0) {
                            sb.append(", ");
                        }
                        StringUtils.quoteIdentifier(sb, schemaSearchPath[i5]);
                    }
                    add(sessionLocal, newSmallArrayList, "SCHEMA_SEARCH_PATH", sb.toString());
                }
                String currentSchemaName = sessionLocal.getCurrentSchemaName();
                if (currentSchemaName != null) {
                    add(sessionLocal, newSmallArrayList, "SCHEMA", StringUtils.quoteIdentifier(new StringBuilder("SET SCHEMA "), currentSchemaName).toString());
                }
                TimeZoneProvider currentTimeZone = sessionLocal.currentTimeZone();
                if (!currentTimeZone.equals(DateTimeUtils.getTimeZone())) {
                    add(sessionLocal, newSmallArrayList, "TIME ZONE", StringUtils.quoteStringSQL(new StringBuilder("SET TIME ZONE "), currentTimeZone.getId()).toString());
                    break;
                }
                break;
            case 28:
                QueryStatisticsData queryStatisticsData = this.database.getQueryStatisticsData();
                if (queryStatisticsData != null) {
                    for (QueryStatisticsData.QueryEntry queryEntry : queryStatisticsData.getQueries()) {
                        add(sessionLocal, newSmallArrayList, queryEntry.sqlStatement, ValueInteger.get(queryEntry.count), ValueDouble.get(queryEntry.executionTimeMinNanos / 1000000.0d), ValueDouble.get(queryEntry.executionTimeMaxNanos / 1000000.0d), ValueDouble.get(queryEntry.executionTimeCumulativeNanos / 1000000.0d), ValueDouble.get(queryEntry.executionTimeMeanNanos / 1000000.0d), ValueDouble.get(queryEntry.getExecutionTimeStandardDeviation() / 1000000.0d), ValueBigint.get(queryEntry.rowCountMin), ValueBigint.get(queryEntry.rowCountMax), ValueBigint.get(queryEntry.rowCountCumulative), ValueDouble.get(queryEntry.rowCountMean), ValueDouble.get(queryEntry.getRowCountStandardDeviation()));
                    }
                    break;
                }
                break;
            case 29:
                Iterator<TableSynonym> it11 = this.database.getAllSynonyms().iterator();
                while (it11.hasNext()) {
                    TableSynonym next5 = it11.next();
                    add(sessionLocal, newSmallArrayList, shortName, next5.getSchema().getName(), next5.getName(), next5.getSynonymForName(), next5.getSynonymForSchema().getName(), "SYNONYM", "VALID", replaceNullWithEmpty(next5.getComment()), ValueInteger.get(next5.getId()));
                }
                break;
            case 30:
                getAllConstraints(sessionLocal).filter(constraint32 -> {
                    return constraint32.getConstraintType() != Constraint.Type.DOMAIN && checkIndex(sessionLocal, constraint32.getTable().getName(), value, value2);
                }).forEach(constraint5 -> {
                    Constraint.Type constraintType = constraint5.getConstraintType();
                    Table table11 = constraint5.getTable();
                    add(sessionLocal, newSmallArrayList, shortName, constraint5.getSchema().getName(), constraint5.getName(), constraintType.getSqlName(), shortName, table11.getSchema().getName(), table11.getName(), "NO", "NO", replaceNullWithEmpty(constraint5.getComment()), constraint5.getCreateSQL(), ValueInteger.get(constraint5.getId()));
                });
                break;
            case 31:
                Iterator<SchemaObject> it12 = getAllSchemaObjects(5).iterator();
                while (it12.hasNext()) {
                    SchemaObject next6 = it12.next();
                    if (((Constraint) next6).getConstraintType() == Constraint.Type.DOMAIN) {
                        ConstraintDomain constraintDomain = (ConstraintDomain) next6;
                        Domain domain3 = constraintDomain.getDomain();
                        add(sessionLocal, newSmallArrayList, shortName, constraintDomain.getSchema().getName(), constraintDomain.getName(), shortName, domain3.getSchema().getName(), domain3.getName(), "NO", "NO", replaceNullWithEmpty(constraintDomain.getComment()), constraintDomain.getCreateSQL(), ValueInteger.get(constraintDomain.getId()));
                    }
                }
                break;
            case 32:
                getAllConstraints(sessionLocal).forEach(constraint6 -> {
                    IndexColumn[] columns;
                    ConstraintUnique constraintUnique;
                    Constraint.Type constraintType = constraint6.getConstraintType();
                    if (constraintType.isUnique()) {
                        columns = ((ConstraintUnique) constraint6).getColumns();
                    } else if (constraintType == Constraint.Type.REFERENTIAL) {
                        columns = ((ConstraintReferential) constraint6).getColumns();
                    } else {
                        return;
                    }
                    Table table11 = constraint6.getTable();
                    String name4 = table11.getName();
                    if (!checkIndex(sessionLocal, name4, value, value2)) {
                        return;
                    }
                    if (constraintType == Constraint.Type.REFERENTIAL) {
                        constraintUnique = constraint6.getReferencedConstraint();
                    } else {
                        constraintUnique = null;
                    }
                    Index index = constraint6.getIndex();
                    for (int i6 = 0; i6 < columns.length; i6++) {
                        IndexColumn indexColumn = columns[i6];
                        ValueInteger valueInteger = ValueInteger.get(i6 + 1);
                        ValueInteger valueInteger2 = null;
                        if (constraintUnique != null) {
                            Column column2 = ((ConstraintReferential) constraint6).getRefColumns()[i6].column;
                            IndexColumn[] columns2 = constraintUnique.getColumns();
                            int i7 = 0;
                            while (true) {
                                if (i7 >= columns2.length) {
                                    break;
                                }
                                if (!columns2[i7].column.equals(column2)) {
                                    i7++;
                                } else {
                                    valueInteger2 = ValueInteger.get(i7 + 1);
                                    break;
                                }
                            }
                        }
                        Object[] objArr14 = new Object[12];
                        objArr14[0] = shortName;
                        objArr14[1] = constraint6.getSchema().getName();
                        objArr14[2] = constraint6.getName();
                        objArr14[3] = shortName;
                        objArr14[4] = table11.getSchema().getName();
                        objArr14[5] = name4;
                        objArr14[6] = indexColumn.columnName;
                        objArr14[7] = valueInteger;
                        objArr14[8] = valueInteger2;
                        objArr14[9] = index != null ? shortName : null;
                        objArr14[10] = index != null ? index.getSchema().getName() : null;
                        objArr14[11] = index != null ? index.getName() : null;
                        add(sessionLocal, newSmallArrayList, objArr14);
                    }
                });
                break;
            case 33:
                getAllConstraints(sessionLocal).filter(constraint7 -> {
                    return constraint7.getConstraintType() == Constraint.Type.REFERENTIAL && checkIndex(sessionLocal, constraint7.getName(), value, value2);
                }).forEach(constraint8 -> {
                    ConstraintReferential constraintReferential = (ConstraintReferential) constraint8;
                    ConstraintUnique referencedConstraint = constraintReferential.getReferencedConstraint();
                    add(sessionLocal, newSmallArrayList, shortName, constraintReferential.getSchema().getName(), constraintReferential.getName(), shortName, referencedConstraint.getSchema().getName(), referencedConstraint.getName(), "NONE", constraintReferential.getUpdateAction().getSqlName(), constraintReferential.getDeleteAction().getSqlName());
                });
                break;
            case 34:
                getAllConstraints(sessionLocal).filter(constraint9 -> {
                    return constraint9.getConstraintType().isCheck() && checkIndex(sessionLocal, constraint9.getName(), value, value2);
                }).forEach(constraint10 -> {
                    add(sessionLocal, newSmallArrayList, shortName, constraint10.getSchema().getName(), constraint10.getName(), constraint10.getExpression().getSQL(0, 2));
                });
                break;
            case 35:
                getAllConstraints(sessionLocal).forEach(constraint11 -> {
                    switch (constraint11.getConstraintType()) {
                        case CHECK:
                        case DOMAIN:
                            HashSet hashSet = new HashSet();
                            constraint11.getExpression().isEverything(ExpressionVisitor.getColumnsVisitor(hashSet, null));
                            Iterator it13 = hashSet.iterator();
                            while (it13.hasNext()) {
                                Column column2 = (Column) it13.next();
                                if (checkIndex(sessionLocal, column2.getTable().getName(), value, value2)) {
                                    addConstraintColumnUsage(sessionLocal, newSmallArrayList, shortName, constraint11, column2);
                                }
                            }
                            return;
                        case REFERENTIAL:
                            Table refTable = constraint11.getRefTable();
                            if (checkIndex(sessionLocal, refTable.getName(), value, value2)) {
                                Iterator<Column> it14 = constraint11.getReferencedColumns(refTable).iterator();
                                while (it14.hasNext()) {
                                    addConstraintColumnUsage(sessionLocal, newSmallArrayList, shortName, constraint11, it14.next());
                                }
                                break;
                            }
                            break;
                        case PRIMARY_KEY:
                        case UNIQUE:
                            break;
                        default:
                            return;
                    }
                    Table table11 = constraint11.getTable();
                    if (checkIndex(sessionLocal, table11.getName(), value, value2)) {
                        Iterator<Column> it15 = constraint11.getReferencedColumns(table11).iterator();
                        while (it15.hasNext()) {
                            addConstraintColumnUsage(sessionLocal, newSmallArrayList, shortName, constraint11, it15.next());
                        }
                    }
                });
                break;
            default:
                throw DbException.getInternalError("type=" + this.type);
        }
        return newSmallArrayList;
    }

    private static short getRefAction(ConstraintActionType constraintActionType) {
        switch (constraintActionType) {
            case CASCADE:
                return (short) 0;
            case RESTRICT:
                return (short) 1;
            case SET_DEFAULT:
                return (short) 4;
            case SET_NULL:
                return (short) 2;
            default:
                throw DbException.getInternalError("action=" + constraintActionType);
        }
    }

    private void addConstraintColumnUsage(SessionLocal sessionLocal, ArrayList<Row> arrayList, String str, Constraint constraint, Column column) {
        Table table = column.getTable();
        add(sessionLocal, arrayList, str, table.getSchema().getName(), table.getName(), column.getName(), str, constraint.getSchema().getName(), constraint.getName());
    }

    private void addPrivileges(SessionLocal sessionLocal, ArrayList<Row> arrayList, DbObject dbObject, String str, Table table, String str2, int i) {
        if ((i & 1) != 0) {
            addPrivilege(sessionLocal, arrayList, dbObject, str, table, str2, "SELECT");
        }
        if ((i & 4) != 0) {
            addPrivilege(sessionLocal, arrayList, dbObject, str, table, str2, "INSERT");
        }
        if ((i & 8) != 0) {
            addPrivilege(sessionLocal, arrayList, dbObject, str, table, str2, "UPDATE");
        }
        if ((i & 2) != 0) {
            addPrivilege(sessionLocal, arrayList, dbObject, str, table, str2, "DELETE");
        }
    }

    private void addPrivilege(SessionLocal sessionLocal, ArrayList<Row> arrayList, DbObject dbObject, String str, Table table, String str2, String str3) {
        Object obj = "NO";
        if (dbObject.getType() == 2 && ((User) dbObject).isAdmin()) {
            obj = "YES";
        }
        if (str2 == null) {
            add(sessionLocal, arrayList, null, identifier(dbObject.getName()), str, table.getSchema().getName(), table.getName(), str3, obj);
        } else {
            add(sessionLocal, arrayList, null, identifier(dbObject.getName()), str, table.getSchema().getName(), table.getName(), str2, str3, obj);
        }
    }

    private ArrayList<SchemaObject> getAllSchemaObjects(int i) {
        ArrayList<SchemaObject> arrayList = new ArrayList<>();
        Iterator<Schema> it = this.database.getAllSchemas().iterator();
        while (it.hasNext()) {
            it.next().getAll(i, arrayList);
        }
        return arrayList;
    }

    @Override // org.h2.table.Table
    public long getMaxDataModificationId() {
        switch (this.type) {
            case 6:
            case 8:
            case 18:
            case 25:
            case 26:
            case 27:
                return Long.MAX_VALUE;
            default:
                return this.database.getModificationDataId();
        }
    }
}
