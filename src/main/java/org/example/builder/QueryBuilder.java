package org.example.builder;

public interface QueryBuilder {
    QueryBuilder addPrimaryKeyColumn(String columnName, String columnType);
    QueryBuilder addColumn(String columnName, String columnType);
    QueryBuilder setTableName(String tableName);
    String build();
}
