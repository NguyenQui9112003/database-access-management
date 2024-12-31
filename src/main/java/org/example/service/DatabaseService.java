package org.example.service;

import java.sql.Connection;
import java.util.List;

public interface DatabaseService {
    void createTable(Class<?> entity);
    void insert(Object entity);
    void update(Object entity);
    void createRelationships(Class<?> entity);
    List<Object[]> select(Class<?> entity, List<String> columns, String whereCondition, List<String> groupByColumns, String havingCondition);
    void delete(Class<?> entity, String whereCondition);
    void updateByField(Class<?> entity, String fieldName, Object value);
    void updateFieldWithValue(Class<?> entity, String fieldNameToUpdate, Object newValue, String whereFieldName, Object whereValue);
    void closeConnection(Connection con);
    void insertBulk(List<Object> entities);
    <T> T get(Class<T> entityClass, Object id);
    <T> void set(Class<T> entityClass, Object id, String field, Object value);
    <T> boolean save(T entity);
    <T> boolean delete(T entity);
}
