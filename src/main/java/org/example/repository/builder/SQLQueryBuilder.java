package org.example.repository.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SQLQueryBuilder implements QueryBuilder {
    private StringBuilder query = new StringBuilder();
    private List<String> columns = new ArrayList<>();
    private List<Object> values = new ArrayList<>();

    @Override
    public QueryBuilder select(String... columns) {
        query.append("SELECT ");
        query.append(String.join(", ", columns));
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
