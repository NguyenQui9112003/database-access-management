package org.example.builder;

public class CreateTableBuilder implements QueryBuilder {
    private final StringBuilder query;
    private boolean hasIdColumn;
    private String tableName;

    public CreateTableBuilder() {
        query = new StringBuilder();
    }

    @Override
    public QueryBuilder setTableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    @Override
    public QueryBuilder addPrimaryKeyColumn(String columnName, String columnType) {
        if (query.length() == 0) {
            initializeQuery();
        }
        query.append(columnName).append(" ").append(columnType).append(" PRIMARY KEY, ");
        hasIdColumn = true;
        return this;
    }

    @Override
    public QueryBuilder addColumn(String columnName, String columnType) {
        if (query.length() == 0) {
            initializeQuery();
        }
        query.append(columnName).append(" ").append(columnType).append(", ");
        return this;
    }

    private void initializeQuery() {
        query.append("CREATE TABLE ").append(tableName).append(" (");
    }

    @Override
    public String build() {
        if (tableName == null || tableName.trim().isEmpty()) {
            throw new IllegalStateException("Table name must be set before building the query");
        }

        if (query.length() == 0) {
            initializeQuery();
        }

        if (!hasIdColumn) {
            query.insert(query.indexOf("(") + 1, "id SERIAL PRIMARY KEY, ");
        }

        // Remove trailing comma and space
        if (query.substring(query.length() - 2).equals(", ")) {
            query.setLength(query.length() - 2);
        }

        query.append(");");
        return query.toString();
    }
}
