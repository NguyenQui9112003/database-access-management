package org.example.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InsertQueryBuilder implements QueryBuilder {
    private String tableName;
    private Map<String, String> columns;
    private List<String> values;

    public InsertQueryBuilder() {
        this.columns = new HashMap<>();
        this.values = new ArrayList<>();
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

    public InsertQueryBuilder values(String... values) {
        this.values.clear();
        for (String value : values) {
            this.values.add(value);
        }
        return this;
    }

    @Override
    public String build() {
        StringBuilder query = new StringBuilder("INSERT INTO " + tableName + " (");
        
        // Add column names
        boolean first = true;
        for (String columnName : columns.keySet()) {
            if (!first) query.append(", ");
            query.append(columnName);
            first = false;
        }
        
        query.append(") VALUES (");
        
        // Add values
        first = true;
        for (String value : values) {
            if (!first) query.append(", ");
            query.append("'").append(value).append("'");
            first = false;
        }
        query.append(")");
        
        return query.toString();
    }
}
