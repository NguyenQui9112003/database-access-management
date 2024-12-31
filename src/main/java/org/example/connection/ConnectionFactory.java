package org.example.connection;
import org.example.config.dbconfig.PostgresConfig;
import java.sql.Connection;

public abstract class ConnectionFactory {
    public abstract Connection createConnection(PostgresConfig config);
}
