package org.example.repository.builder;

public class CreateTableBuilder implements QueryBuilder {
    private final StringBuilder query;
    private boolean hasIdColumn;

    public CreateTableBuilder(String tableName) {
        query = new StringBuilder();
        query.append("CREATE TABLE ").append(tableName).append(" (");
    }

    @Override
    public QueryBuilder addPrimaryKeyColumn(String columnName, String columnType) {
        query.append(columnName).append(" ").append(columnType).append(" PRIMARY KEY, ");
        hasIdColumn = true;
        return this;
    }

    @Override
    public QueryBuilder addColumn(String columnName, String columnType) {
        query.append(columnName).append(" ").append(columnType).append(", ");
        return this;
    }

    @Override
    public String build() {
        if (!hasIdColumn) {
            query.insert(query.indexOf("(") + 1, "id SERIAL PRIMARY KEY, ");
        }
        query.setLength(query.length() - 2);
        query.append(");");
        return query.toString();
    }
}
