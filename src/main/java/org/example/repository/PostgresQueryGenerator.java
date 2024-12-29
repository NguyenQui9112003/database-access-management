package org.example.repository;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.example.annotations.*;
import org.example.repository.builder.QueryBuilder;
import org.example.repository.builder.PostgresQueryBuilder;

public class PostgresQueryGenerator implements QueryGenerator {
    private final QueryBuilder queryBuilder = new PostgresQueryBuilder();

    @Override
    public QueryBuilder getQueryBuilder() {
        return new PostgresQueryBuilder();
    }

    @Override
    public String createTableQuery(Class<?> clazz) {
        String tableName = clazz.getAnnotation(Table.class).name();
        queryBuilder.createTable(tableName);

        boolean firstColumn = true;
        try {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class)) {
                    if (!firstColumn) {
                        queryBuilder.appendComma("");
                    }
                    String columnName = field.getAnnotation(Column.class).name();
                    String columnType = mapJavaTypeToPostgresType(field.getType());
                    queryBuilder.addPrimaryKeyColumn(columnName, columnType);
                    firstColumn = false;
                } else if (field.isAnnotationPresent(Column.class)) {
                    if (!firstColumn) {
                        queryBuilder.appendComma("");
                    }
                    String columnName = field.getAnnotation(Column.class).name();
                    String columnType = mapJavaTypeToPostgresType(field.getType());
                    queryBuilder.addColumn(columnName, columnType);
                    firstColumn = false;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error creating create table query", e);
        }
        queryBuilder.appendComma(");");
        return queryBuilder.build();
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
    public String updateFieldWithValue(Class<?> clazz, String fieldNameToUpdate,
                                       Object newValue, String whereFieldName,
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

    @Override
    public String selectQuery(Class<?> clazz, List<String> columns, String whereCondition, List<String> groupByColumns, String havingCondition) {
        if (!clazz.isAnnotationPresent(Entity.class)) {
            throw new RuntimeException("Class is not an entity");
        }

        String tableName = clazz.getAnnotation(Table.class).name();

        queryBuilder.select(columns.toArray(new String[0])).from(tableName);

        if (whereCondition != null && !whereCondition.isEmpty()) {
            queryBuilder.where(whereCondition);
        }

        if (groupByColumns != null && !groupByColumns.isEmpty()) {
            queryBuilder.groupBy(groupByColumns.toArray(new String[0]));
        }

        if (havingCondition != null && !havingCondition.isEmpty()) {
            queryBuilder.having(havingCondition);
        }

        return queryBuilder.build();
    }

    @Override
    public String deleteQuery(Class<?> clazz, String whereCondition) {
        if (!clazz.isAnnotationPresent(Entity.class)) {
            throw new RuntimeException("Class is not an entity");
        }

        String tableName = clazz.getAnnotation(Table.class).name();

        queryBuilder.delete().from(tableName);

        if (whereCondition != null && !whereCondition.isEmpty()) {
            queryBuilder.where(whereCondition);
        }

        return queryBuilder.build();
    }

    public List<String> createRelationshipQueries(Class<?> clazz) {
        List<String> relationshipQueries = new ArrayList<>();

        try {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(OneToOne.class)) {
                    OneToOne oneToOne = field.getAnnotation(OneToOne.class);
                    String foreignKeySQL = String.format(
                            "ALTER TABLE %s ADD CONSTRAINT fk_%s FOREIGN KEY (%s) REFERENCES %s (%s);",
                            clazz.getAnnotation(Table.class).name(),
                            field.getName(),
                            oneToOne.foreignKey(),
                            oneToOne.referencedTable(),
                            "id" // Assuming the referenced table uses "id" as the primary key
                    );
                    relationshipQueries.add(foreignKeySQL);
                }

                if (field.isAnnotationPresent(ManyToOne.class)) {
                    ManyToOne manyToOne = field.getAnnotation(ManyToOne.class);
                    String foreignKeySQL = String.format(
                            "ALTER TABLE %s ADD CONSTRAINT fk_%s FOREIGN KEY (%s) REFERENCES %s (%s);",
                            clazz.getAnnotation(Table.class).name(),
                            field.getName(),
                            manyToOne.foreignKey(),
                            manyToOne.referencedTable(),
                            "id" // Assuming the referenced table uses "id" as the primary key
                    );
                    relationshipQueries.add(foreignKeySQL);
                }

                if (field.isAnnotationPresent(OneToMany.class)) {
                    // OneToMany relationships are typically handled on the ManyToOne side.
                    System.out.println("One-to-Many relationships are derived from Many-to-One mappings.");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error creating relationship queries", e);
        }

        return relationshipQueries;
    }

    @Override
    public String insertBulkQuery(List<Object> entities) {
        if (entities == null || entities.isEmpty()) {
            throw new IllegalArgumentException("Entities list cannot be empty");
        }

        Object firstEntity = entities.get(0);
        Class<?> entityClass = firstEntity.getClass();
        String tableName = entityClass.getSimpleName().toLowerCase();
        Field[] fields = entityClass.getDeclaredFields();

        StringBuilder query = new StringBuilder("INSERT INTO ")
            .append(tableName)
            .append(" (");

        // Append column names
        for (int i = 0; i < fields.length; i++) {
            if (i > 0) query.append(", ");
            query.append(fields[i].getName().toLowerCase());
        }

        query.append(") VALUES ");

        // Append values for each entity
        for (int i = 0; i < entities.size(); i++) {
            if (i > 0) query.append(", ");
            query.append("(");
            
            Object entity = entities.get(i);
            for (int j = 0; j < fields.length; j++) {
                if (j > 0) query.append(", ");
                try {
                    fields[j].setAccessible(true);
                    Object value = fields[j].get(entity);
                    if (value instanceof String) {
                        query.append("'").append(value).append("'");
                    } else {
                        query.append(value);
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Error accessing field: " + fields[j].getName(), e);
                }
            }
            query.append(")");
        }

        return query.toString();
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

    private String getColumnName(Class<?> clazz, String fieldName) {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getName().equals(fieldName) && field.isAnnotationPresent(Column.class)) {
                return field.getAnnotation(Column.class).name();
            }
        }
        throw new RuntimeException("Field " + fieldName + " not found or not marked with @Column in class " + clazz.getName());
    }

}
