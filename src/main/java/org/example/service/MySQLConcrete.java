package org.example.service;

import java.sql.Connection;

import org.example.repository.DatabaseQueryAbstractFactory;

public class MySQLConcrete extends DatabaseServiceAbstractFactory {
    @Override
    public DatabaseService createDatabaseService(Connection connection, DatabaseQueryAbstractFactory queryFactory) {
        return new MySQLDatabaseService(connection, queryFactory);
    }
}
