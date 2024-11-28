package org.example.service;

import java.sql.Connection;

public class PostgresDatabaseServiceConcrete extends DatabaseServiceConcrete {
    @Override
    public DatabaseService createDatabaseService(Connection connection) {
        return new PostgresDatabaseService(connection);
    }
}
