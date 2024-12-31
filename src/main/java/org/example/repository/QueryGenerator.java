package org.example.repository;

import java.util.List;

public interface QueryGenerator {
    String createTableQuery(Class<?> clazz);
    String insertQuery(Object entity);
    String insertBulkQuery(List<Object> entities);
    String updateQuery(Object entity);
    String selectQuery(Class<?> clazz, List<String> columns, String whereCondition, List<String> groupByColumns, String havingCondition);
    String deleteQuery(Class<?> clazz, String whereCondition);
    String updateQueryByField(Class<?> clazz, String fieldName, Object value);
    String updateFieldWithValue(Class<?> clazz, String fieldNameToUpdate, Object newValue, String whereFieldName, Object whereValue);
    <T> String selectByIdQuery(Class<T> entityClass, Object id);
}
