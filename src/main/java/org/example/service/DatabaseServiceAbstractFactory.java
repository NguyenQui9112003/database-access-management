package org.example.service;

import java.sql.Connection;
import org.example.repository.DatabaseQueryAbstractFactory;

public abstract class DatabaseServiceAbstractFactory {
    public abstract DatabaseService createDatabaseService(Connection connection, DatabaseQueryAbstractFactory factory);
}
