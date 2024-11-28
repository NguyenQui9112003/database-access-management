package org.example.repository;

public interface QueryGeneratorFactory {
    String createTableQuery(Class<?> clazz);
}
