package org.example.service.dbservice;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.example.annotations.OneToMany;
import org.example.annotations.OneToOne;
import org.example.repository.DatabaseQueryAbstractFactory;
import org.example.repository.dbquery.PostgresQueryGenerator;
import org.example.repository.QueryGenerator;

import org.example.annotations.Column;  // Update this import
import org.example.service.DatabaseService;

public class PostgresDatabaseService implements DatabaseService {
    private Connection connection;
    private QueryGenerator queryGenerator;

    public PostgresDatabaseService(Connection connection, DatabaseQueryAbstractFactory factory) {
        this.connection = connection;
        this.queryGenerator = factory.createQueryGenerator();
    }

    @Override
    public void createTable(Class<?> entity) {
        try {
            // Add IF NOT EXISTS to prevent errors if table already exists
            String createTableQuery = queryGenerator.createTableQuery(entity).replace(
                "CREATE TABLE", 
                "CREATE TABLE IF NOT EXISTS"
            );
            System.out.println("Create table SQL: " + createTableQuery);
            
            try (Statement stmt = connection.createStatement()) {
                stmt.executeUpdate(createTableQuery);
                System.out.println("Table created/verified successfully");
            }
        } catch (Exception e) {
            System.out.println("Error creating table: " + e.getMessage());
        }
    }

    @Override
    public void insert(Object entity) {
        String insertQuery = queryGenerator.insertQuery(entity);
        System.out.println("Insert SQL: " + insertQuery); // Add logging
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(insertQuery);
            System.out.println("Record inserted successfully");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void insertBulk(List<Object> entities) {
        if (entities == null || entities.isEmpty()) {
            return;
        }
        
        String insertQuery = queryGenerator.insertBulkQuery(entities);
        System.out.println("Bulk Insert SQL: " + insertQuery);
        
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(insertQuery);
            System.out.println(entities.size() + " records inserted successfully");
        } catch (Exception e) {
            System.out.println("Error in bulk insert: " + e.getMessage());
        }
    }

    @Override
    public void update(Object entity) {
        String updateQuery = queryGenerator.updateQuery(entity);
        System.out.println("Update SQL: " + updateQuery);
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(updateQuery);
            System.out.println("Record updated successfully");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void updateByField(Class<?> entity, String fieldName, Object value) {
        String updateQuery = queryGenerator.updateQueryByField(entity, fieldName, value);
        System.out.println("Update by field SQL: " + updateQuery);
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(updateQuery);
            System.out.println("Record updated successfully");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void updateFieldWithValue(Class<?> entity, String fieldNameToUpdate, Object newValue, String whereFieldName, Object whereValue) {
        String updateQuery = queryGenerator.updateFieldWithValue(entity, fieldNameToUpdate, newValue, whereFieldName, whereValue);
        System.out.println("Update field with value SQL: " + updateQuery);
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(updateQuery);
            System.out.println("Field updated successfully");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public List<Object[]> select(Class<?> entity, List<String> columns, String whereCondition, List<String> groupByColumns, String havingCondition) {
        String selectQuery = queryGenerator.selectQuery(entity, columns, whereCondition, groupByColumns, havingCondition);
        System.out.println("Select SQL: " + selectQuery);
        List<Object[]> results = new ArrayList<>();

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(selectQuery)) {
            while (rs.next()) {
                Object[] row = new Object[columns.size()];
                for (int i = 0; i < columns.size(); i++) {
                    row[i] = rs.getObject(columns.get(i));
                }
                results.add(row);
            }
            System.out.println("Select query executed successfully");
        } catch (Exception e) {
            System.out.println(e);
        }
        return results;
    }

    public void delete(Class<?> entity, String whereCondition) {
        String deleteQuery = queryGenerator.deleteQuery(entity, whereCondition);
        System.out.println("Delete SQL: " + deleteQuery);

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(deleteQuery);
            System.out.println("Record(s) deleted successfully");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void createRelationships(Class<?> entity) {
        List<String> relationshipQueries = ((PostgresQueryGenerator) queryGenerator).createRelationshipQueries(entity);

        for (String query : relationshipQueries) {
            System.out.println("Relationship SQL: " + query); // Add logging
            try (Statement stmt = connection.createStatement()) {
                stmt.executeUpdate(query);
                System.out.println("Relationship created successfully");
            } catch (Exception e) {
                System.out.println("Error creating relationship: " + e.getMessage());
            }
        }
    }

    @Override
    public void closeConnection (Connection con) {
        try {
            if(con != null && !con.isClosed()) {
                con.close();
                System.out.println("Close connection success");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public <T> boolean delete(T entity) {
        try {
            String deleteQuery = queryGenerator.deleteQuery(entity.getClass(), null);
            try (Statement stmt = connection.createStatement()) {
                stmt.executeUpdate(deleteQuery);
                System.out.println("Record deleted successfully");
                return true;
            }
        } catch (Exception e) {
            System.out.println("Error deleting record: " + e.getMessage());
            return false;
        }
    }

    @Override
    public <T> T get(Class<T> entityClass, Object id) {
        try {
            String selectQuery = queryGenerator.selectByIdQuery(entityClass, id);
            System.out.println("Executing SQL: " + selectQuery);
            
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(selectQuery)) {
                
                if (rs.next()) {
                    T entity = entityClass.getDeclaredConstructor().newInstance();
                    for (Field field : entityClass.getDeclaredFields()) {
                        field.setAccessible(true);
                        
                        // Skip relationship fields
                        if (field.isAnnotationPresent(OneToOne.class) || 
                            field.isAnnotationPresent(OneToMany.class)) {
                            continue;
                        }
                        
                        String columnName = field.getName();
                        if (field.isAnnotationPresent(Column.class)) {
                            Column column = field.getAnnotation(Column.class);
                            String name = column.name();
                            if (name != null && !name.isEmpty()) {
                                columnName = name;
                            }
                        }
                        
                        Object value = rs.getObject(columnName.toLowerCase());
                        if (value != null) {
                            field.set(entity, value);
                        }
                    }
                    return entity;
                }
            }
        } catch (Exception e) {
            System.out.println("Error in get() method: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <T> boolean save(T entity) {
        try {
            Class<?> entityClass = entity.getClass();
            Field idField = entityClass.getDeclaredField("id");
            idField.setAccessible(true);
            Object id = idField.get(entity);

            String query;
            if (id == null) {
                query = queryGenerator.insertQuery(entity);
            } else {
                query = queryGenerator.updateQuery(entity);
            }
            
            System.out.println("Save SQL: " + query);
            try (Statement stmt = connection.createStatement()) {
                stmt.executeUpdate(query);
                return true;
            }
        } catch (Exception e) {
            System.out.println("Error in save() method: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public <T> void set(Class<T> entityClass, Object id, String field, Object value) {
        try {
            String updateQuery = queryGenerator.updateFieldWithValue(
                entityClass,
                field,
                value,
                "id",
                id
            );
            System.out.println("Set SQL: " + updateQuery);
            
            try (Statement stmt = connection.createStatement()) {
                stmt.executeUpdate(updateQuery);
            }
        } catch (Exception e) {
            System.out.println("Error in set() method: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
