package org.example.builder;

import java.util.LinkedHashMap;
import java.util.Map;

public class UpdateQueryBuilderImpl implements UpdateQueryBuilder {
    private final StringBuilder query;
    private String tableName;
    private final Map<String, String> setColumns;
    private String whereCondition;

    public UpdateQueryBuilderImpl() {
        query = new StringBuilder("UPDATE ");
        setColumns = new LinkedHashMap<>();
    }

    @Override
    public UpdateQueryBuilder from(String tableName) {
        this.tableName = tableName;
        return this;
    }

    @Override
    public UpdateQueryBuilder set(String columnName, String value) {
        setColumns.put(columnName, value);
        return this;
    }

    @Override
    public UpdateQueryBuilder where(String condition) {
        this.whereCondition = condition;
        return this;
    }

    @Override
    public String build() {
        if (tableName == null || setColumns.isEmpty()) {
            throw new IllegalStateException("Table name and SET columns must be specified.");
        }

        query.append(tableName).append(" SET ");
        setColumns.forEach((column, value) -> {
            query.append(column).append(" = ").append(value).append(", ");
        });
        query.setLength(query.length() - 2); // Remove the trailing comma and space.

        if (whereCondition != null) {
            query.append(" WHERE ").append(whereCondition);
        }
        query.append(";");
        return query.toString();
    }
}
