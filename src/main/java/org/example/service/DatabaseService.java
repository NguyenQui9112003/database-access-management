package org.example.service;

import java.sql.Connection;

public interface DatabaseService {
    void createTable(Class<?> entity);
    void insert(Object entity);
    void update(Object entity);
   
    void closeConnection(Connection con);
}
