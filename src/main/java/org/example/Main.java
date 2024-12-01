package org.example;
import java.sql.Connection;
import java.sql.SQLException;

import org.example.config.DatabaseConfig;
import org.example.connection.PostgresConnectionFactory;
import org.example.connectionManager.ConnectionManagerSingleton;
import org.example.service.DatabaseService;
import org.example.service.DatabaseServiceConcrete;
import org.example.service.PostgresDatabaseServiceConcrete;
import org.example.entity.User;

public class Main {
    public static void main(String[] args) throws SQLException {
        // Tạo một cái connection manager
        ConnectionManagerSingleton connectionManagerSingleton = ConnectionManagerSingleton.getInstance(10);

        DatabaseConfig dbConfig = new DatabaseConfig("jdbc:postgresql://localhost:5432",
                "DAM-Framework","postgres","123456");
        // Tạo một cái connection factory
        PostgresConnectionFactory pgConnection = new PostgresConnectionFactory();
        // Từ factory này sẽ gọi một connection mới và kết nối vào trong database
        Connection con = connectionManagerSingleton.addConnection(pgConnection.createConnection(dbConfig));
        // Connection con = pgConnection.createConnection(dbConfig);

        // Tạo factory và service cho PostgreSQL
        DatabaseServiceConcrete factory = new PostgresDatabaseServiceConcrete();
        DatabaseService dbService = factory.createDatabaseService(con);

        // Tạo bảng cho entity User (Test)
        dbService.createTable(User.class);
        dbService.closeConnection(con);
    }
}