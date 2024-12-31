package org.example.repository;

import java.sql.Connection;

import org.example.service.DatabaseService;

public class MySQLQueryConcrete extends DatabaseQueryAbstractFactory {
    @Override
    public QueryGenerator createQueryGenerator() {
        return new MySQLQueryAdapter();
    }
}
