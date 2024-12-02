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
            if (field.isAnnotationPresent(Column.class)) {
                Column columnAnnotation = field.getAnnotation(Column.class);
                createTableQuery.append(columnAnnotation.name()).append(" ");
                // log field type and annotation
                System.out.println("Field: " + field.getName() + ", Type: " + field.getType() + ", Annotation: " + columnAnnotation.name());
                if (field.getName().equals("id")) {
                    // Giả sử trường 'id' là khóa chính và tự tăng trong PostgreSQL
                    createTableQuery.append("SERIAL PRIMARY KEY, ");
                    hasId = true;
                } else {
                    // Nếu không phải id, chỉ cần thêm kiểu dữ liệu là VARCHAR (hoặc kiểu khác tùy theo field)
                    createTableQuery.append("VARCHAR(255), ");
                }
            }
        }

        // Nếu không có 'id', thêm trường id mặc định
        if (!hasId) {
            createTableQuery.append("id SERIAL PRIMARY KEY, ");
        }

        // Loại bỏ dấu phẩy cuối cùng và đóng dấu ngoặc
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

}
