package org.example.repository;

import org.example.annotations.*;
import org.example.builder.CreateTableBuilder;
import org.example.builder.UpdateQueryBuilderImpl;
import org.example.repository.builder.QueryBuilder;
import org.example.repository.builder.SQLQueryBuilder;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class PostgresQueryGenerator implements QueryGenerator {
    private final SQLQueryBuilder queryBuilder = new SQLQueryBuilder();

    @Override
    public QueryBuilder getQueryBuilder() {
        return new SQLQueryBuilder();
    }

        @Override
    public String createTableQuery(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(Entity.class)) {
            throw new RuntimeException("Class is not an entity");
        }

        String tableName = clazz.getAnnotation(Table.class).name();
        CreateTableBuilder builder = new CreateTableBuilder(tableName);

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                Column columnAnnotation = field.getAnnotation(Column.class);
                String columnName = columnAnnotation.name();
                String columnType = mapJavaTypeToPostgresType(field.getType());
                builder.addPrimaryKeyColumn(columnName, columnType);
                // print column name , type and annotation
                System.out.println(columnName + " " + columnType + " " + field.getAnnotation(Id.class));
            } else if (field.isAnnotationPresent(Column.class)) {
                Column columnAnnotation = field.getAnnotation(Column.class);
                String columnName = columnAnnotation.name();
                String columnType = mapJavaTypeToPostgresType(field.getType());
                builder.addColumn(columnName, columnType);
                
               // System.out.println(columnName + " " + columnType + " " + field.getAnnotation(Column.class));
            }
        }

        return builder.build();
    }

    @Override
    public String insertQuery(Object entity) {
        Class<?> clazz = entity.getClass();
        String tableName = clazz.getAnnotation(Table.class).name();
        
        List<String> columns = new ArrayList<>();
        List<Object> values = new ArrayList<>();

        try {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Column.class)) {
                    field.setAccessible(true);
                    String columnName = field.getAnnotation(Column.class).name();
                    Object value = field.get(entity);
                    
                    columns.add(columnName);
                    values.add(value);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error creating insert query", e);
        }

        return queryBuilder
                .insert(tableName, columns.toArray(new String[0]))
                .values(values.toArray())
                .build();
    }

    @Override
    public String updateFieldWithValue(Class<?> clazz, String fieldNameToUpdate, Object newValue, String whereFieldName,
            Object whereValue) {
        String tableName = clazz.getAnnotation(Table.class).name();
        String updateColumn = getColumnName(clazz, fieldNameToUpdate);
        String whereColumn = getColumnName(clazz, whereFieldName);
        
        return queryBuilder
                .update(tableName)
                .set(updateColumn + "=" + (newValue instanceof String ? "'" + newValue + "'" : newValue))
                .where(whereColumn + "=" + (whereValue instanceof String ? "'" + whereValue + "'" : whereValue))
                .build();
    }

    @Override
    public String updateQuery(Object entity) {
        Class<?> clazz = entity.getClass();
        String tableName = clazz.getAnnotation(Table.class).name();
        
        String idColumn = null;
        Object idValue = null;
        List<String> setStatements = new ArrayList<>();

        try {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Column.class)) {
                    field.setAccessible(true);
                    String columnName = field.getAnnotation(Column.class).name();
                    Object value = field.get(entity);
                    
                    if (field.isAnnotationPresent(Id.class)) {
                        idColumn = columnName;
                        idValue = value;
                    } else {
                        setStatements.add(columnName + "=" + 
                            (value instanceof String ? "'" + value + "'" : value));
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error creating update query", e);
        }

        return queryBuilder
                .update(tableName)
                .set(setStatements.toArray(new String[0]))
                .where(idColumn + "=" + (idValue instanceof String ? "'" + idValue + "'" : idValue))
                .build();
    }

    @Override
    public String updateQueryByField(Class<?> clazz, String fieldName, Object value) {
        if (!clazz.isAnnotationPresent(Entity.class)) {
            throw new RuntimeException("Class is not an entity");
        }

        String tableName = clazz.getAnnotation(Table.class).name();
        List<String> setStatements = new ArrayList<>();
        String whereColumn = getColumnName(clazz, fieldName);

        // Collect all non-ID fields to set to NULL
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class) && !field.isAnnotationPresent(Id.class)) {
                String columnName = field.getAnnotation(Column.class).name();
                setStatements.add(columnName + " = NULL");
            }
        }

        return queryBuilder
                .update(tableName)
                .set(setStatements.toArray(new String[0]))
                .where(whereColumn + "=" + (value instanceof String ? "'" + value + "'" : value))
                .build();
    }

    // @Override
    // public String createTableQuery(Class<?> clazz) {
    //     if (!clazz.isAnnotationPresent(Entity.class)) {
    //         throw new RuntimeException("Class is not an entity");
    //     }

    //     String tableName = clazz.getAnnotation(Table.class).name();
    //     CreateTableBuilder builder = new CreateTableBuilder(tableName);

    //     for (Field field : clazz.getDeclaredFields()) {
    //         if (field.isAnnotationPresent(Id.class)) {
    //             Column columnAnnotation = field.getAnnotation(Column.class);
    //             String columnName = columnAnnotation.name();
    //             String columnType = mapJavaTypeToPostgresType(field.getType());
    //             builder.addPrimaryKeyColumn(columnName, columnType);
    //             // print column name , type and annotation
    //             System.out.println(columnName + " " + columnType + " " + field.getAnnotation(Id.class));
    //         } else if (field.isAnnotationPresent(Column.class)) {
    //             Column columnAnnotation = field.getAnnotation(Column.class);
    //             String columnName = columnAnnotation.name();
    //             String columnType = mapJavaTypeToPostgresType(field.getType());
    //             builder.addColumn(columnName, columnType);
                
    //            // System.out.println(columnName + " " + columnType + " " + field.getAnnotation(Column.class));
    //         }
    //     }

    //     return builder.build();
    // }

    // @Override
    // public String insertQuery(Object entity) {
    //     Class<?> clazz = entity.getClass();
    //     String tableName = clazz.getAnnotation(Table.class).name();
    //     StringBuilder columns = new StringBuilder();
    //     StringBuilder values = new StringBuilder();

    //     Field[] fields = clazz.getDeclaredFields();
    //     boolean first = true;

    //     try {
    //         for (Field field : fields) {
    //             if (field.isAnnotationPresent(Column.class)) {
    //                 field.setAccessible(true);
    //                 if (!first) {
    //                     columns.append(", ");
    //                     values.append(", ");
    //                 }
    //                 String columnName = field.getAnnotation(Column.class).name();
    //                 Object value = field.get(entity);
                    
    //                 columns.append(columnName);
    //                 // Handle string values with quotes
    //                 if (value instanceof String) {
    //                     values.append("'").append(value).append("'");
    //                 } else {
    //                     values.append(value);
    //                 }
                    
    //                 first = false;
    //             }
    //         }
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }

    //     return String.format("INSERT INTO %s (%s) VALUES (%s)", 
    //                        tableName, columns, values);
    // }

    // @Override
    // public String updateQueryByField(Class<?> clazz, String fieldName, Object value) {
    //     if (!clazz.isAnnotationPresent(Entity.class)) {
    //         throw new RuntimeException("Class is not an entity");
    //     }

    //     String tableName = clazz.getAnnotation(Table.class).name();
    //     StringBuilder query = new StringBuilder();
    //     query.append("UPDATE ").append(tableName).append(" SET ");

    //     List<String> setColumns = new ArrayList<>();
    //     String whereColumn = null;

    //     // Find the target field and its column name
    //     for (Field field : clazz.getDeclaredFields()) {
    //         if (field.isAnnotationPresent(Column.class)) {
    //             Column columnAnnotation = field.getAnnotation(Column.class);
    //             String columnName = columnAnnotation.name();
                
    //             if (field.getName().equals(fieldName)) {
    //                 whereColumn = columnName;
    //             } else if (!field.isAnnotationPresent(Id.class)) {
    //                 // Only add non-ID fields to SET clause
    //                 setColumns.add(columnName + " = NULL");
    //             }
    //         }
    //     }

    //     if (whereColumn == null) {
    //         throw new RuntimeException("Field " + fieldName + " not found or not marked with @Column");
    //     }

    //     // Build the SET clause
    //     query.append(String.join(", ", setColumns));
        
    //     // Add WHERE clause
    //     query.append(" WHERE ").append(whereColumn);
    //     if (value instanceof String) {
    //         query.append(" = '").append(value).append("'");
    //     } else {
    //         query.append(" = ").append(value);
    //     }

    //     return query.toString();
    // }

    // @Override
    // public String updateQuery(Object entity) {
    //     Class<?> clazz = entity.getClass();
    //     if (!clazz.isAnnotationPresent(Entity.class)) {
    //         throw new RuntimeException("Class is not an entity");
    //     }

    //     String tableName = clazz.getAnnotation(Table.class).name();
    //     UpdateQueryBuilderImpl builder = new UpdateQueryBuilderImpl();
    //     builder.from(tableName);

    //     String idFieldName = null;
    //     String idValue = null;

    //     for (Field field : clazz.getDeclaredFields()) {
    //         if (field.isAnnotationPresent(Column.class)) {
    //             field.setAccessible(true);
    //             Column columnAnnotation = field.getAnnotation(Column.class);
    //             String columnName = columnAnnotation.name();
                
    //             try {
    //                 String value = String.valueOf(field.get(entity));
    //                 if (field.isAnnotationPresent(Id.class)) {
    //                     idFieldName = columnName;
    //                     idValue = value;
    //                 } else {
    //                     builder.set(columnName, value);
    //                 }
    //             } catch (IllegalAccessException e) {
    //                 throw new RuntimeException(e);
    //             }
    //         }
    //     }

    //     if (idFieldName != null && idValue != null) {
    //         builder.set(idFieldName, idValue);
    //     } else {
    //         throw new RuntimeException("Entity must have an ID field");
    //     }

    //     return builder.build();
    // }

    // @Override
    // public String updateFieldWithValue(Class<?> clazz, String fieldNameToUpdate, Object newValue, String whereFieldName, Object whereValue) {
    //     if (!clazz.isAnnotationPresent(Entity.class)) {
    //         throw new RuntimeException("Class is not an entity");
    //     }

    //     String tableName = clazz.getAnnotation(Table.class).name();
    //     StringBuilder query = new StringBuilder();
    //     query.append("UPDATE ").append(tableName).append(" SET ");

    //     String updateColumnName = null;
    //     String whereColumnName = null;

    //     // Find the column names
    //     for (Field field : clazz.getDeclaredFields()) {
    //         if (field.isAnnotationPresent(Column.class)) {
    //             Column columnAnnotation = field.getAnnotation(Column.class);
    //             if (field.getName().equals(fieldNameToUpdate)) {
    //                 updateColumnName = columnAnnotation.name();
    //             }
    //             if (field.getName().equals(whereFieldName)) {
    //                 whereColumnName = columnAnnotation.name();
    //             }
    //         }
    //     }

    //     if (updateColumnName == null || whereColumnName == null) {
    //         throw new RuntimeException("Field not found or not marked with @Column");
    //     }

    //     // Build SET clause
    //     query.append(updateColumnName);
    //     if (newValue instanceof String) {
    //         query.append(" = '").append(newValue).append("'");
    //     } else {
    //         query.append(" = ").append(newValue);
    //     }

    //     // Add WHERE clause
    //     query.append(" WHERE ").append(whereColumnName);
    //     if (whereValue instanceof String) {
    //         query.append(" = '").append(whereValue).append("'");
    //     } else {
    //         query.append(" = ").append(whereValue);
    //     }

    //     return query.toString();
    // }

    

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

    private String getColumnName(Class<?> clazz, String fieldName) {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getName().equals(fieldName) && field.isAnnotationPresent(Column.class)) {
                return field.getAnnotation(Column.class).name();
            }
        }
        throw new RuntimeException("Field " + fieldName + " not found or not marked with @Column in class " + clazz.getName());
    }
    
}
