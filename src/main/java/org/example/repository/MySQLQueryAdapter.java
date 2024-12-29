package org.example.repository;

import java.lang.reflect.Field;
import java.util.List;

public class MySQLQueryAdapter extends PostgresQueryGenerator {
    private final PostgresQueryGenerator postgresGenerator;

    public MySQLQueryAdapter() {
        this.postgresGenerator = new PostgresQueryGenerator();
    }

    @Override
    public String createTableQuery(Class<?> entity) {
        String postgresQuery = postgresGenerator.createTableQuery(entity);
        return postgresQuery
            .replace("SERIAL PRIMARY KEY", "INT PRIMARY KEY AUTO_INCREMENT")
            .replace(");", ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;");
    }

    // Reuse other PostgreSQL query methods through delegation
    @Override
    public String insertQuery(Object entity) {
        return postgresGenerator.insertQuery(entity);
    }

    @Override
    public String updateQuery(Object entity) {
        return postgresGenerator.updateQuery(entity);
    }

    @Override
    public String selectQuery(Class<?> entity, java.util.List<String> columns, 
                            String whereCondition, java.util.List<String> groupByColumns, 
                            String havingCondition) {
        return postgresGenerator.selectQuery(entity, columns, whereCondition, 
                                           groupByColumns, havingCondition);
    }

    @Override
    public String insertBulkQuery(List<Object> entities) {
        return postgresGenerator.insertBulkQuery(entities);
    }
}
