package org.example.service.dbservice;

import java.util.List;
import java.sql.Connection;

import org.example.service.DatabaseService;
import org.example.service.adaptee.MySQLServiceAdapter;
import org.example.repository.DatabaseQueryAbstractFactory;

public class MySQLDatabaseService implements DatabaseService {
    private final DatabaseService databaseAdapter;

    public MySQLDatabaseService(Connection connection, DatabaseQueryAbstractFactory queryFactory) {
        this.databaseAdapter = new MySQLServiceAdapter(connection, queryFactory);
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

    @Override
    public <T> boolean delete(T entity) {
        return databaseAdapter.delete(entity);
    }

    @Override
    public <T> T get(Class<T> entityClass, Object id) {
        return databaseAdapter.get(entityClass, id);
    }

    @Override
    public <T> boolean save(T entity) {
        return databaseAdapter.save(entity);
    }

    @Override
    public <T> void set(Class<T> entityClass, Object id, String field, Object value) {
        databaseAdapter.set(entityClass, id, field, value);
    }

}
