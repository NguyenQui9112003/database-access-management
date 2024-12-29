package org.example.service;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.example.repository.DatabaseQueryAbstractFactory;
import org.example.repository.PostgresQueryGenerator;
import org.example.repository.QueryGenerator;

public class PostgresDatabaseService implements DatabaseService {
    private Connection connection;
    private QueryGenerator queryGenerator;

    public PostgresDatabaseService(Connection connection, DatabaseQueryAbstractFactory factory) {
        this.connection = connection;
        this.queryGenerator = factory.createQueryGenerator();
    }

    @Override
    public void createTable(Class<?> entity) {
        String createTableQuery = queryGenerator.createTableQuery(entity);
        System.out.println("Create table SQL: " + createTableQuery); // Add logging
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(createTableQuery);
            System.out.println("Table created successfully");
        } catch (Exception e) {
            System.out.println(e);
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
}
