package org.example.service;

import java.sql.Connection;

public abstract class DatabaseServiceConcrete {
    public abstract DatabaseService createDatabaseService(Connection connection);
}
