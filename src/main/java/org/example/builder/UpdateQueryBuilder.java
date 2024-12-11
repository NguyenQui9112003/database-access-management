package org.example.builder;

public interface UpdateQueryBuilder extends BaseQueryBuilder {
    UpdateQueryBuilder set(String columnName, String value);
    UpdateQueryBuilder where(String condition);
    UpdateQueryBuilder from(String tableName);
}
