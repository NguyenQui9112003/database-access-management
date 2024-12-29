package org.example.service;

import java.sql.Connection;
import java.util.List;
import org.example.repository.DatabaseQueryAbstractFactory;

public class MySQLDatabaseService implements DatabaseService {
    private final DatabaseService databaseAdapter;

    public MySQLDatabaseService(Connection connection, DatabaseQueryAbstractFactory queryFactory) {
        this.databaseAdapter = new PostgresAdapter(connection, queryFactory);
    }

    @Override
    public void createTable(Class<?> entity) {
        databaseAdapter.createTable(entity);
    }

    @Override
    public void insert(Object entity) {
        databaseAdapter.insert(entity);
    }

    @Override
    public void insertBulk(List<Object> entities) {
        databaseAdapter.insertBulk(entities);
        
    }

    @Override
    public void update(Object entity) {
        databaseAdapter.update(entity);
    }

    @Override
    public void updateByField(Class<?> entity, String fieldName, Object value) {
        databaseAdapter.updateByField(entity, fieldName, value);
    }

    @Override
    public void updateFieldWithValue(Class<?> entity, String fieldNameToUpdate, Object newValue, 
                                   String whereFieldName, Object whereValue) {
        databaseAdapter.updateFieldWithValue(entity, fieldNameToUpdate, newValue, whereFieldName, whereValue);
    }

    @Override
    public List<Object[]> select(Class<?> entity, List<String> columns, String whereCondition, 
                                List<String> groupByColumns, String havingCondition) {
        return databaseAdapter.select(entity, columns, whereCondition, groupByColumns, havingCondition);
    }

    @Override
    public void delete(Class<?> entity, String whereCondition) {
        databaseAdapter.delete(entity, whereCondition);
    }

    @Override
    public void createRelationships(Class<?> entity) {
        databaseAdapter.createRelationships(entity);
    }

    @Override
    public void closeConnection(Connection con) {
        databaseAdapter.closeConnection(con);
    }

    
}
