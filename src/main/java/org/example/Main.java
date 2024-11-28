package org.example;
import java.sql.Connection;

import org.example.config.DatabaseConfig;
import org.example.connection.PostgresConnectionFactory;
import org.example.service.DatabaseService;
import org.example.service.DatabaseServiceConcrete;
import org.example.service.PostgresDatabaseServiceConcrete;
import org.example.entity.User;

public class Main {
    public static void main(String[] args) {
        DatabaseConfig dbConfig = new DatabaseConfig("jdbc:postgresql://localhost:5432",
                "DAM-Framework","postgres","123456");
        PostgresConnectionFactory pgConnection = new PostgresConnectionFactory();
        Connection con = pgConnection.createConnection(dbConfig);

        // Tạo factory và service cho PostgreSQL
        DatabaseServiceConcrete factory = new PostgresDatabaseServiceConcrete();
        DatabaseService dbService = factory.createDatabaseService(con);

        // Tạo bảng cho entity User
        dbService.createTable(User.class);
        dbService.closeConnection(con);
    }
}