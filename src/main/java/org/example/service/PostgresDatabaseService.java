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
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(updateQuery);
            System.out.println("Record updated successfully");
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
