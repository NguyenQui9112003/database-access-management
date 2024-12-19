package org.example.repository;

import org.example.repository.builder.QueryBuilder;

public interface QueryGenerator {
    String createTableQuery(Class<?> clazz);
    // crud
    String insertQuery(Object entity);
    String updateQuery(Object entity);
    String updateQueryByField(Class<?> clazz, String fieldName, Object value);
    String updateFieldWithValue(Class<?> clazz, String fieldNameToUpdate, Object newValue, String whereFieldName, Object whereValue);
    
    // Get query builder instance
    QueryBuilder getQueryBuilder();
}
