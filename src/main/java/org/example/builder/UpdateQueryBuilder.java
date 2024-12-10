package org.example.builder;

import java.util.HashMap;
import java.util.Map;

public class UpdateQueryBuilder implements QueryBuilder {
    private String tableName;
    private Map<String, String> columns;
    private String whereCondition;
    private Map<String, String> setValues;
    private String updateByField;
    private String updateByValue;

    public UpdateQueryBuilder() {
        this.columns = new HashMap<>();
        this.setValues = new HashMap<>();
        this.updateByField = null;
        this.updateByValue = null;
    }

    @Override
    public QueryBuilder addPrimaryKeyColumn(String columnName, String columnType) {
        columns.put(columnName, columnType + " PRIMARY KEY");
        return this;
    }

    @Override
    public QueryBuilder addColumn(String columnName, String columnType) {
        columns.put(columnName, columnType);
        return this;
    }

    @Override
    public QueryBuilder setTableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public UpdateQueryBuilder set(String columnName, String value) {
        setValues.put(columnName, value);
        return this;
    }

    public UpdateQueryBuilder where(String condition) {
        this.whereCondition = condition;
        return this;
    }

    public UpdateQueryBuilder updateByField(String fieldName, String value) {
        this.updateByField = fieldName;
        this.updateByValue = value;
        return this;
    }

    @Override
    public String build() {
        StringBuilder query = new StringBuilder("UPDATE " + tableName + " SET ");
        
        boolean first = true;
        for (Map.Entry<String, String> entry : setValues.entrySet()) {
            if (!first) query.append(", ");
            query.append(entry.getKey()).append(" = '").append(entry.getValue()).append("'");
            first = false;
        }

        if (updateByField != null) {
            query.append(" WHERE ").append(updateByField).append(" = '").append(updateByValue).append("'");
        } else if (whereCondition != null) {
            query.append(" WHERE ").append(whereCondition);
        }

        return query.toString();
    }
}
