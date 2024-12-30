package org.example.repository;

import org.example.repository.builder.QueryBuilder;
import java.util.List;

public interface QueryGenerator {
    String createTableQuery(Class<?> clazz);
    // CRUD
    String insertQuery(Object entity);
    String updateQuery(Object entity);
    String selectQuery(Class<?> clazz, List<String> columns, String whereCondition, List<String> groupByColumns, String havingCondition);
    String deleteQuery(Class<?> clazz, String whereCondition);
    String updateQueryByField(Class<?> clazz, String fieldName, Object value);
    String updateFieldWithValue(Class<?> clazz, String fieldNameToUpdate, Object newValue, String whereFieldName, Object whereValue);
    // Get query builder instance
    String insertBulkQuery(List<Object> entities);
    
    <T> String selectByIdQuery(Class<T> entityClass, Object id);
}
