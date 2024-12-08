package org.example.repository;
import org.example.annotations.*;
import org.example.builder.CreateTableBuilder;
import org.example.builder.QueryBuilder;

import java.lang.reflect.Field;

public class PostgresQueryGenerator implements QueryGenerator {
    @Override
    public String createTableQuery(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(Entity.class)) {
            throw new RuntimeException("Class is not an entity");
        }

        String tableName = clazz.getAnnotation(Table.class).name();
        QueryBuilder builder = new CreateTableBuilder(tableName);

        boolean hasId = false;

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                Column columnAnnotation = field.getAnnotation(Column.class);
                String columnName = columnAnnotation.name();
                String columnType = mapJavaTypeToPostgresType(field.getType());
                builder.addPrimaryKeyColumn(columnName, columnType);
                hasId = true;
            }
        }

        if (!hasId) {
            builder.addPrimaryKeyColumn("id", "SERIAL");
        }

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class) && !field.isAnnotationPresent(Id.class)) {
                Column columnAnnotation = field.getAnnotation(Column.class);
                String columnName = columnAnnotation.name();
                String columnType = mapJavaTypeToPostgresType(field.getType());
                builder.addColumn(columnName, columnType);
            }
        }

        return builder.build();
    }

    @Override
    public String insertQuery(Object entity) {
        Class<?> clazz = entity.getClass();
        if (!clazz.isAnnotationPresent(Entity.class)) {
            throw new RuntimeException("Class is not an entity");
        }

        StringBuilder insertQuery = new StringBuilder();
        String tableName = clazz.getAnnotation(Table.class).name();
        insertQuery.append("INSERT INTO ").append(tableName).append(" (");

        StringBuilder valuesPart = new StringBuilder("VALUES (");

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class)) {
                Column columnAnnotation = field.getAnnotation(Column.class);
                insertQuery.append(columnAnnotation.name()).append(", ");
                field.setAccessible(true);
                try {
                    valuesPart.append("'").append(field.get(entity)).append("', ");
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        // Remove the last comma and space
        insertQuery.delete(insertQuery.length() - 2, insertQuery.length());
        valuesPart.delete(valuesPart.length() - 2, valuesPart.length());

        insertQuery.append(") ").append(valuesPart).append(");");

        return insertQuery.toString();
    }

    @Override
    public String updateQuery(Object entity) {
        Class<?> clazz = entity.getClass();
        if (!clazz.isAnnotationPresent(Entity.class)) {
            throw new RuntimeException("Class is not an entity");
        }

        StringBuilder updateQuery = new StringBuilder();
        String tableName = clazz.getAnnotation(Table.class).name();
        updateQuery.append("UPDATE ").append(tableName).append(" SET ");

        String idColumnName = null;
        Object idValue = null;

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class)) {
                Column columnAnnotation = field.getAnnotation(Column.class);
                field.setAccessible(true);
                try {
                    if (field.getName().equals("id")) {
                        idColumnName = columnAnnotation.name();
                        idValue = field.get(entity);
                    } else {
                        updateQuery.append(columnAnnotation.name()).append(" = '").append(field.get(entity)).append("', ");
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        // Remove the last comma and space
        updateQuery.delete(updateQuery.length() - 2, updateQuery.length());

        if (idColumnName == null || idValue == null) {
            throw new RuntimeException("Entity does not have an id field");
        }

        updateQuery.append(" WHERE ").append(idColumnName).append(" = '").append(idValue).append("';");

        return updateQuery.toString();
    }

    private String mapJavaTypeToPostgresType(Class<?> javaType) {
        if (javaType == String.class) {
            return "VARCHAR(255)";
        } else if (javaType == Long.class) {
            return "BIGINT";
        } else if (javaType == Integer.class || javaType == int.class) {
            return "INTEGER";
        } else if (javaType == Short.class || javaType == short.class) {
            return "SMALLINT";
        } else if (javaType == Float.class || javaType == float.class) {
            return "REAL";
        } else if (javaType == Double.class || javaType == double.class) {
            return "DOUBLE PRECISION";
        } else if (javaType == Boolean.class || javaType == boolean.class) {
            return "BOOLEAN";
        } else if (javaType == java.math.BigDecimal.class) {
            return "DECIMAL";
        } else if (javaType == byte[].class) {
            return "BYTEA";
        } else if (javaType == java.sql.Date.class || javaType == java.time.LocalDate.class) {
            return "DATE";
        } else if (javaType == java.sql.Timestamp.class || javaType == java.time.LocalDateTime.class) {
            return "TIMESTAMP";
        } else if (javaType == java.time.LocalTime.class) {
            return "TIME";
        } else {
            throw new RuntimeException("Unsupported type: " + javaType.getName());
        }
    }
}
