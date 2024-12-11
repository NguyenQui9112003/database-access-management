package org.example.builder;

public interface ConditionQueryBuilder extends BaseQueryBuilder {
    ConditionQueryBuilder where(String condition); // Shared WHERE clause
    ConditionQueryBuilder from(String tableName);  // Shared FROM clause
}
