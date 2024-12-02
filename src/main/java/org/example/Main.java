package org.example;
import java.sql.Connection;

import org.example.config.DatabaseConfig;
import org.example.connection.PostgresConnectionFactory;
import org.example.repository.DatabaseQueryAbstractFactory;
import org.example.repository.PostgresQueryConcrete;
import org.example.service.DatabaseService;
import org.example.service.DatabaseServiceAbstractFactory;
import org.example.service.PostgresConcrete;
import org.example.entity.User;


public class Main {
    public static void main(String[] args) {
        DatabaseConfig dbConfig = new DatabaseConfig("jdbc:postgresql://localhost:5432",
                "testDB","postgres","123");
        PostgresConnectionFactory pgConnection = new PostgresConnectionFactory();
        Connection con = pgConnection.createConnection(dbConfig);

        // Tạo factory và service cho PostgreSQL
        DatabaseServiceAbstractFactory db = new PostgresConcrete();
        DatabaseQueryAbstractFactory query = new PostgresQueryConcrete();
        DatabaseService dbService = db.createDatabaseService(con, query);

        // Tạo bảng cho entity User
        //dbService.createTable(User.class);

        // add user
        // public User(Long id, String name, int phone) {
        //     this.id = id;
        //     this.name = name;
        //     this.phone = phone;
        // }
        // User user = new User(1L, "John", 123456789);
        // dbService.insert(user);
        // update user
        // User user = new User(1L, "John", 987654321);
        //  dbService.update(user);

        dbService.closeConnection(con);
    }
}