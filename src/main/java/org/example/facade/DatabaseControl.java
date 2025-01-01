package org.example.facade;

import java.util.List;
import java.sql.Connection;
import java.sql.SQLException;

import org.example.repository.concrete.MySQLQueryConcrete;
import org.example.repository.concrete.PostgresQueryConcrete;
import org.example.service.*;
import org.example.repository.*;

import org.example.connection.ConnectionFactory;
import org.example.config.dbconfig.DatabaseConfig;
import org.example.connection.dbconnect.MySQLConnectionFactory;
import org.example.connection.dbconnect.PostgresConnectionFactory;
import org.example.connectionManager.ConnectionManagerSingleton;
import org.example.service.concrete.MySQLConcrete;
import org.example.service.concrete.PostgresConcrete;

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
        } 
        else if ("mysql".equalsIgnoreCase(dbType)) {
            // create Manager connection
            ConnectionManagerSingleton connectionManagerSingleton = ConnectionManagerSingleton.getInstance(10);
            // create MySQL connection
            ConnectionFactory mysqlConnection = new MySQLConnectionFactory();
            // Từ factory này sẽ gọi một connection mới và kết nối vào trong database
            con = connectionManagerSingleton.addConnection(mysqlConnection.createConnection(dbConfig));
            // create MySQL service
            DatabaseServiceAbstractFactory dbFactory = new MySQLConcrete();
            // create MySQL query
            DatabaseQueryAbstractFactory queryFactory = new MySQLQueryConcrete();
            // create MySQL Facade
            this.dbService = dbFactory.createDatabaseService(con, queryFactory);
        }
        else {
            throw new IllegalArgumentException("System does not support database: " + dbType);
        }
    }

    public void createTable(Class<?> entity) {
        dbService.createTable(entity);
    }

    public void insert(Object entity) {
        dbService.insert(entity);
    }

    public void insertBulk(List<?> entities) {
        for (Object entity : entities) {
            dbService.insert(entity);
        }
    }

    public void update(Object entity) {
        dbService.update(entity);
    }

    public void createRelationships(Class<?> entity) {dbService.createRelationships(entity);}

    public void updateByField(Class<?> entity, String fieldName, Object value) {
        dbService.updateByField(entity, fieldName, value);
    }

    public void updateFieldWithValue(Class<?> entity, String fieldNameToUpdate, Object newValue, String whereFieldName, Object whereValue) {
        dbService.updateFieldWithValue(entity, fieldNameToUpdate, newValue, whereFieldName, whereValue);
    }

    public void closeConnect() {
        dbService.closeConnection(con);
    }

    public List<Object[]> select(Class<?> entity, List<String> columns, String whereCondition, List<String> groupByColumns, String havingCondition) { return dbService.select(entity, columns, whereCondition, groupByColumns, havingCondition);}

    public void delete(Class<?> entity, String whereCondition) { dbService.delete(entity, whereCondition);}

    public <T> T get(Class<T> entityClass, Object id) {
        return dbService.get(entityClass, id);
    }

    public <T> void set(Class<T> entityClass, Object id, String field, Object value) {
        dbService.set(entityClass, id, field, value);
    }

    public <T> boolean save(T entity) {
        return dbService.save(entity);
    }
    
    public <T> boolean delete(T entity) {
        return dbService.delete(entity);
    }
}
