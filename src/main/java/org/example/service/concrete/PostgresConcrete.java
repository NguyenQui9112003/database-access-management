package org.example.service.concrete;

import java.sql.Connection;

import org.example.service.DatabaseService;
import org.example.repository.DatabaseQueryAbstractFactory;
import org.example.service.DatabaseServiceAbstractFactory;
import org.example.service.dbservice.PostgresDatabaseService;

public class PostgresConcrete extends DatabaseServiceAbstractFactory {
    @Override
    public DatabaseService createDatabaseService(Connection connection, DatabaseQueryAbstractFactory factory) {
        return new PostgresDatabaseService(connection, factory);
    }
}
