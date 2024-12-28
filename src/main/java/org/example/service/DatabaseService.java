package org.example.service;

import java.sql.Connection;

public interface DatabaseService {
    void createTable(Class<?> entity);
    void insert(Object entity);
    void update(Object entity);
    void updateByField(Class<?> entity, String fieldName, Object value);
    void updateFieldWithValue(Class<?> entity, String fieldNameToUpdate, Object newValue, String whereFieldName, Object whereValue);
    void closeConnection(Connection con);
}
