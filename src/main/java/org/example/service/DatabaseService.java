package org.example.service;

import java.sql.Connection;
import java.util.List;

public interface DatabaseService {
    void createTable(Class<?> entity);
    void insert(Object entity);
    void update(Object entity);
    List<Object[]> select(Class<?> entity, List<String> columns, String whereCondition, List<String> groupByColumns, String havingCondition);
    void delete(Class<?> entity, String whereCondition);
    void updateByField(Class<?> entity, String fieldName, Object value);
    void updateFieldWithValue(Class<?> entity, String fieldNameToUpdate, Object newValue, String whereFieldName, Object whereValue);
    void closeConnection(Connection con);
}
