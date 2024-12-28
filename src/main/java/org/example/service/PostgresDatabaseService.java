package org.example.service;
import java.sql.Connection;
import java.sql.Statement;

import org.example.repository.DatabaseQueryAbstractFactory;
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
