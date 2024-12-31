package org.example.connection.dbconnect;

import java.sql.Connection;
import java.sql.DriverManager;
import org.example.config.dbconfig.PostgresConfig;
import org.example.connection.ConnectionFactory;

public class MySQLConnectionFactory extends ConnectionFactory {
    @Override
    public Connection createConnection(PostgresConfig config) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(
                config.getFullUrl(),
                config.getUsername(),
                config.getPassword()
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to create MySQL connection: " + e.getMessage());
        }
    }
}
