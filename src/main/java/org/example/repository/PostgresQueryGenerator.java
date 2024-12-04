package org.example.repository;
import org.example.annotations.*;
import java.lang.reflect.Field;

public class PostgresQueryGenerator implements QueryGenerator {
    @Override
    public String createTableQuery(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(Entity.class)) {
            throw new RuntimeException("Class is not an entity");
        }

        StringBuilder createTableQuery = new StringBuilder();
        String tableName = clazz.getAnnotation(Table.class).name();
        createTableQuery.append("CREATE TABLE ").append(tableName).append(" (");

        boolean hasId = false;

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                hasId = true;
                break;
            }
        }

        if (!hasId) {
            createTableQuery.append("id SERIAL PRIMARY KEY, ");
        }

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class)) {
                Column columnAnnotation = field.getAnnotation(Column.class);
                String columnName = columnAnnotation.name();
                String columnType = mapJavaTypeToPostgresType(field.getType());

                createTableQuery.append(columnName).append(" ").append(columnType);

                if (field.isAnnotationPresent(Id.class)) {
                    createTableQuery.append(" PRIMARY KEY ");
                    hasId = true;
                }

                createTableQuery.append(", ");
            }
        }

        createTableQuery.delete(createTableQuery.length() - 2, createTableQuery.length());
        createTableQuery.append(");");

        return createTableQuery.toString();
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
        } else {
            throw new RuntimeException("Unsupported type: " + javaType.getName());
        }
    }
}
