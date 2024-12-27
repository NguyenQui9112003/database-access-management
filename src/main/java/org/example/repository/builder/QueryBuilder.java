package org.example.repository.builder;

public interface QueryBuilder {
    QueryBuilder createTable(String tableName);
    QueryBuilder addPrimaryKeyColumn(String columnName, String columnType);
    QueryBuilder addColumn(String columnName, String columnType);
    QueryBuilder appendComma(String comma);
    QueryBuilder select(String... columns);
    QueryBuilder from(String table);
    QueryBuilder groupBy(String... columns);
    QueryBuilder having(String condition);
    QueryBuilder delete();
    QueryBuilder where(String condition);
    QueryBuilder set(String... setColumns);
    QueryBuilder insert(String table, String... columns);
    QueryBuilder values(Object... values);
    QueryBuilder update(String table);
    String build();
}
