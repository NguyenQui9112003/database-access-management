package org.example.service;

import java.sql.Connection;

public class PostgresDatabaseService extends DatabaseServiceConcrete {
    @Override
    public DatabaseService createDatabaseService(Connection connection) {
        return new PostgresDatabaseServiceConcrete(connection);
    }
}
