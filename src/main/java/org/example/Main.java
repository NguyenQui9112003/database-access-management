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
                "DAM-Framework","postgres","123456");
        PostgresConnectionFactory pgConnection = new PostgresConnectionFactory();
        Connection con = pgConnection.createConnection(dbConfig);

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