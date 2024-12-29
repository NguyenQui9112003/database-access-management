package org.example.repository;

import java.lang.reflect.Field;
import java.util.List;

public class MySQLQueryGenerator extends PostgresQueryGenerator {
    @Override
    public String createTableQuery(Class<?> entity) {
        StringBuilder query = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
        query.append(entity.getSimpleName().toLowerCase())
             .append(" (");
        
        Field[] fields = entity.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            query.append(field.getName().toLowerCase())
                 .append(" ")
                 .append(getMySQLDataType(field.getType()));
                 
            if (field.getName().equalsIgnoreCase("id")) {
                query.append(" PRIMARY KEY AUTO_INCREMENT");
            }
            
            if (i < fields.length - 1) {
                query.append(", ");
            }
        }
        
        query.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;");
        return query.toString();
    }

    private String getMySQLDataType(Class<?> type) {
        if (type == Integer.class || type == int.class) {
            return "INT";
        } else if (type == Long.class || type == long.class) {
            return "BIGINT";
        } else if (type == Double.class || type == double.class) {
            return "DOUBLE";
        } else if (type == String.class) {
            return "VARCHAR(255)";
        } else if (type == Boolean.class || type == boolean.class) {
            return "TINYINT(1)";
        }
        return "VARCHAR(255)";
    }
}
