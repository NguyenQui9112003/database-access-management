package org.example.connection.dbconnect;

import java.sql.Connection;
import java.sql.DriverManager;
import org.example.config.dbconfig.DatabaseConfig;
import org.example.connection.ConnectionFactory;

public class PostgresConnectionFactory extends ConnectionFactory {
    @Override
    public Connection createConnection(DatabaseConfig config) {
        Connection con = null;
        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(config.getUrl() + "/" + config.getDatabase(), config.getUsername(), config.getPassword());
            if(con != null) {
                System.out.println("Connection success.");
            } else {
                System.out.println("Connection failed.");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return con;
    }
}
