package org.example.repository;
import org.example.annotations.*;
import java.lang.reflect.Field;

public class PostgresQueryGeneratorConcrete implements QueryGeneratorFactory {
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
}
