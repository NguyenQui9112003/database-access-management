package org.example.connection;
import org.example.config.DatabaseConfig;
import java.sql.Connection;

public abstract class ConnectionFactory {
    public abstract Connection createConnection(DatabaseConfig config);
}
