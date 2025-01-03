package org.example.repository.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SQLQueryBuilder implements QueryBuilder {
    private StringBuilder query = new StringBuilder();
    private List<String> columns = new ArrayList<>();
    private List<Object> values = new ArrayList<>();

    @Override
    public QueryBuilder createTable(String tableName) {
        query.append("CREATE TABLE ").append(tableName).append(" (");
        return this;
    }

    @Override
    public QueryBuilder addPrimaryKeyColumn(String columnName, String columnType) {
        if (query.toString().contains("CREATE TABLE")) {
            query.append(columnName).append(" ").append(columnType).append(" PRIMARY KEY");
        }
        return this;
    }

    @Override
    public QueryBuilder addColumn(String columnName, String columnType) {
        if (query.toString().contains("CREATE TABLE")) {
            if (query.charAt(query.length() - 1) != '(') {
                query.append(", ");
            }
            query.append(columnName).append(" ").append(columnType);
        }
        return this;
    }

    @Override
    public QueryBuilder appendComma(String comma) {
        query.append(comma);
        return this;
    }

    @Override
    public QueryBuilder select(String... columns) {
        query.append("SELECT ");
        query.append(String.join(", ", columns));
        return this;
    }

    @Override
    public QueryBuilder groupBy(String... columns) {
        query.append(" GROUP BY ");
        query.append(String.join(", ", columns));
        return this;
    }

    @Override
    public QueryBuilder having(String condition) {
        query.append(" HAVING ").append(condition);
        return this;
    }

    @Override
    public QueryBuilder delete() {
        query.append("DELETE ");
        return this;
    }


    @Override
    public QueryBuilder from(String table) {
        query.append(" FROM ").append(table);
        return this;
    }

    @Override
    public QueryBuilder where(String condition) {
        query.append(" WHERE ").append(condition);
        return this;
    }

    @Override
    public QueryBuilder set(String... setColumns) {
        query.append(" SET ");
        query.append(String.join(", ", setColumns));
        return this;
    }

    @Override
    public QueryBuilder insert(String table, String... columns) {
        query.append("INSERT INTO ").append(table);
        if (columns.length > 0) {
            query.append(" (").append(String.join(", ", columns)).append(")");
        }
        return this;
    }

    @Override
    public QueryBuilder values(Object... values) {
        query.append(" VALUES (");
        String valueStr = java.util.Arrays.stream(values)
                .map(val -> val instanceof String ? "'" + val + "'" : String.valueOf(val))
                .collect(Collectors.joining(", "));
        query.append(valueStr).append(")");
        return this;
    }

    @Override
    public QueryBuilder update(String table) {
        query.append("UPDATE ").append(table);
        return this;
    }

    @Override
    public String build() {
        String result = query.append(";").toString();
        // Clear the builder state after building
        query = new StringBuilder();
        columns.clear();
        values.clear();
        return result;
    }
}
