package org.example.service.concrete;

import java.sql.Connection;

import org.example.service.DatabaseService;
import org.example.service.DatabaseServiceAbstractFactory;
import org.example.repository.DatabaseQueryAbstractFactory;
import org.example.service.dbservice.MySQLDatabaseService;

public class MySQLConcrete extends DatabaseServiceAbstractFactory {
    @Override
    public DatabaseService createDatabaseService(Connection connection, DatabaseQueryAbstractFactory queryFactory) {
        return new MySQLDatabaseService(connection, queryFactory);
    }
}
