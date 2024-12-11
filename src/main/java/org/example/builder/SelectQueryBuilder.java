package org.example.builder;

import java.util.ArrayList;
import java.util.List;

public class SelectQueryBuilder implements ConditionQueryBuilder {
    private final StringBuilder query;
    private final List<String> columns;
    private String tableName;
    private String whereCondition;
    private List<String> groupByColumns;
    private String havingCondition;

    public SelectQueryBuilder() {
        query = new StringBuilder("SELECT ");
        columns = new ArrayList<>();
        groupByColumns = new ArrayList<>();
    }

    public SelectQueryBuilder addColumn(String columnName) {
        columns.add(columnName);
        return this;
    }

    @Override
    public ConditionQueryBuilder where(String condition) {
        this.whereCondition = condition;
        return this;
    }

    @Override
    public ConditionQueryBuilder from(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public SelectQueryBuilder groupBy(String... columns) {
        for (String column : columns) {
            groupByColumns.add(column);
        }
        return this;
    }

    public SelectQueryBuilder having(String condition) {
        this.havingCondition = condition;
        return this;
    }

    @Override
    public String build() {
        if (columns.isEmpty()) {
            query.append("*");
        } else {
            query.append(String.join(", ", columns));
        }
        query.append(" FROM ").append(tableName);

        if (whereCondition != null) {
            query.append(" WHERE ").append(whereCondition);
        }
        if (!groupByColumns.isEmpty()) {
            query.append(" GROUP BY ").append(String.join(", ", groupByColumns));
        }
        if (havingCondition != null) {
            query.append(" HAVING ").append(havingCondition);
        }
        query.append(";");
        return query.toString();
    }
}
