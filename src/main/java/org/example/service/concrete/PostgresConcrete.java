package org.example.service;

import java.sql.Connection;
import org.example.repository.DatabaseQueryAbstractFactory;

public class PostgresConcrete extends DatabaseServiceAbstractFactory {
    @Override
    public DatabaseService createDatabaseService(Connection connection, DatabaseQueryAbstractFactory factory) {
        return new PostgresDatabaseService(connection, factory);
    }
}
