package org.example;
import java.sql.Connection;
import java.sql.SQLException;

import org.example.config.DatabaseConfig;
import org.example.connection.PostgresConnectionFactory;
import org.example.repository.DatabaseQueryAbstractFactory;
import org.example.repository.PostgresQueryConcrete;
import org.example.connectionManager.ConnectionManagerSingleton;
import org.example.service.DatabaseService;
import org.example.service.DatabaseServiceAbstractFactory;
import org.example.service.PostgresConcrete;
import org.example.entity.User;

public class Main {
    public static void main(String[] args) throws SQLException {
        // Tạo một cái connection manager
        ConnectionManagerSingleton connectionManagerSingleton = ConnectionManagerSingleton.getInstance(10);

        DatabaseConfig dbConfig = new DatabaseConfig("jdbc:postgresql://localhost:5432",
                "DAM-Framework","postgres","123456");
        PostgresConnectionFactory pgConnection = new PostgresConnectionFactory();

        // Từ factory này sẽ gọi một connection mới và kết nối vào trong database
        Connection con = connectionManagerSingleton.addConnection(pgConnection.createConnection(dbConfig));
        // Connection con = pgConnection.createConnection(dbConfig);

        // Tạo factory và service cho PostgresSQL
        DatabaseServiceAbstractFactory db = new PostgresConcrete();
        DatabaseQueryAbstractFactory query = new PostgresQueryConcrete();
        DatabaseService dbService = db.createDatabaseService(con, query);

        // Tạo bảng cho entity User
        dbService.createTable(User.class);

        // dbService.insert()
        // dbService.update()

        dbService.closeConnection(con);
    }
}