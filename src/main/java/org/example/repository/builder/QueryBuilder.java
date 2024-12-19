package org.example.repository.builder;

public interface QueryBuilder {
    QueryBuilder select(String... columns);
    QueryBuilder from(String table);
    QueryBuilder where(String condition);
    QueryBuilder set(String... setColumns);
    QueryBuilder insert(String table, String... columns);
    QueryBuilder values(Object... values);
    QueryBuilder update(String table);
    String build();
}
