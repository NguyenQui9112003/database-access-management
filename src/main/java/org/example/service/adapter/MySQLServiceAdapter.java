package org.example.service.adaptee;

import java.sql.Connection;
import java.util.List;

import org.example.service.DatabaseService;
import org.example.service.dbservice.PostgresDatabaseService;
import org.example.repository.DatabaseQueryAbstractFactory;

public class MySQLServiceAdapter implements DatabaseService {
    private final PostgresDatabaseService postgresService;

    public MySQLServiceAdapter(Connection connection, DatabaseQueryAbstractFactory factory) {
        this.postgresService = new PostgresDatabaseService(connection, factory);
    }

    @Override
    public void createTable(Class<?> entity) {
        postgresService.createTable(entity);
    }

    @Override
    public void insertBulk(List<Object> entities) {
        postgresService.insertBulk(entities);
    }

    @Override
    public void insert(Object entity) {
        postgresService.insert(entity);
    }

    @Override
    public void update(Object entity) {
        postgresService.update(entity);
    }

    @Override
    public void updateByField(Class<?> entity, String fieldName, Object value) {
        postgresService.updateByField(entity, fieldName, value);
    }

    @Override
    public void updateFieldWithValue(Class<?> entity, String fieldNameToUpdate, Object newValue, 
                                   String whereFieldName, Object whereValue) {
        postgresService.updateFieldWithValue(entity, fieldNameToUpdate, newValue, whereFieldName, whereValue);
    }

    @Override
    public List<Object[]> select(Class<?> entity, List<String> columns, String whereCondition, 
                                List<String> groupByColumns, String havingCondition) {
        return postgresService.select(entity, columns, whereCondition, groupByColumns, havingCondition);
    }

    @Override
    public void delete(Class<?> entity, String whereCondition) {
        postgresService.delete(entity, whereCondition);
    }

    @Override
    public void createRelationships(Class<?> entity) {
        postgresService.createRelationships(entity);
    }

    @Override
    public void closeConnection(Connection con) {
        postgresService.closeConnection(con);
    }

    @Override
    public <T> boolean delete(T entity) {
        try {
            return postgresService.delete(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public <T> T get(Class<T> entityClass, Object id) {
        try {
            return postgresService.get(entityClass, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public <T> boolean save(T entity) {
        try {
            return postgresService.save(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public <T> void set(Class<T> entityClass, Object id, String field, Object value) {
        try {
            postgresService.set(entityClass, id, field, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
