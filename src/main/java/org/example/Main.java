package org.example;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

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

        // DatabaseConfig dbConfig = new DatabaseConfig("jdbc:postgresql://localhost:5432",
        //         "DAM-Framework","postgres","123456");
        DatabaseConfig dbConfig = new DatabaseConfig("jdbc:postgresql://localhost:5432",
                "testDB","postgres","123");
        PostgresConnectionFactory pgConnection = new PostgresConnectionFactory();

        // Từ factory này sẽ gọi một connection mới và kết nối vào trong database
        Connection con = connectionManagerSingleton.addConnection(pgConnection.createConnection(dbConfig));
        // Connection con = pgConnection.createConnection(dbConfig);

        // Tạo factory và service cho PostgresSQL
        DatabaseServiceAbstractFactory db = new PostgresConcrete();
        DatabaseQueryAbstractFactory query = new PostgresQueryConcrete();
        DatabaseService dbService = db.createDatabaseService(con, query);

        try {
            // Create table for User entity
            dbService.createTable(User.class);

            // Create users with different IDs - make sure ID and phone are different values
            User user1 = new User(4L, "John Doe", 555111); // ID=1, phone=555111
            //User user2 = new User(5L, "Jane Smith", 555222); // ID=2, phone=555222
            
            // Insert users
            // dbService.insert(user1);
            // dbService.insert(user2);

  
            user1.setPhone(555666);
            dbService.update(user1);
          

            // Update name to "Jane Doe" where phone = 555666
            dbService.updateFieldWithValue(
                User.class,        // entity class
                "name",           // field to update
                "Jane Doe",       // new value
                "phone",          // where field
                555666           // where value
            );
            
            // Update phone to 999999 where id = 4
            dbService.updateFieldWithValue(
                User.class,        // entity class
                "phone",          // field to update
                999999,           // new value
                "id",             // where field
                4L                // where value
            );

        } catch (Exception e) {
            System.out.println("Database operation failed: " + e.getMessage());
            e.printStackTrace();  // Add stack trace for better debugging
        } finally {
            dbService.closeConnection(con);
        }
    }
}