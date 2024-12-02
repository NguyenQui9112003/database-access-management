package org.example.repository;

public interface QueryGenerator {
    String createTableQuery(Class<?> clazz);
    // crud
    String insertQuery(Object entity);

    String updateQuery(Object entity);
    
}
