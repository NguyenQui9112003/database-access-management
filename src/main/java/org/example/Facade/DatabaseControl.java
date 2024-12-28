package org.example.Facade;

import java.sql.Connection;
import java.sql.SQLException;

import org.example.config.DatabaseConfig;
import org.example.connection.ConnectionFactory;
import org.example.connection.PostgresConnectionFactory;
import org.example.connectionManager.ConnectionManagerSingleton;
import org.example.service.*;
import org.example.repository.*;

public class DatabaseControl {
    private Connection con;
    private DatabaseService dbService;

    public DatabaseControl(DatabaseConfig dbConfig, String dbType) throws SQLException {
        if ("postgres".equalsIgnoreCase(dbType)) {
            // create Manager connection
            ConnectionManagerSingleton connectionManagerSingleton = ConnectionManagerSingleton.getInstance(10);
            // create Postgres connection
            ConnectionFactory pgConnection = new PostgresConnectionFactory();
            // Từ factory này sẽ gọi một connection mới và kết nối vào trong database
            con = connectionManagerSingleton.addConnection(pgConnection.createConnection(dbConfig));
            // create Postgres service
            DatabaseServiceAbstractFactory dbFactory = new PostgresConcrete();
            // create Postgres query
            DatabaseQueryAbstractFactory queryFactory = new PostgresQueryConcrete();
            // create Postgres Facade
            this.dbService = dbFactory.createDatabaseService(con, queryFactory);
        } else {
            throw new IllegalArgumentException("System does not support database: " + dbType);
        }
    }

    public void createTable(Class<?> entity) {
        dbService.createTable(entity);
    }

    public void insert(Object entity) {
        dbService.insert(entity);
    }

    public void update(Object entity) {
        dbService.update(entity);
    }

    public void updateByField(Class<?> entity, String fieldName, Object value) {
        dbService.updateByField(entity, fieldName, value);
    }

    public void updateFieldWithValue(Class<?> entity, String fieldNameToUpdate, Object newValue, String whereFieldName, Object whereValue) {
        dbService.updateFieldWithValue(entity, fieldNameToUpdate, newValue, whereFieldName, whereValue);
    }

    public void closeConnect() {
        dbService.closeConnection(con);
    }
}
