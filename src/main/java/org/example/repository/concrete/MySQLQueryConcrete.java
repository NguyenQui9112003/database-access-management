package org.example.repository.concrete;

import org.example.repository.QueryGenerator;
import org.example.repository.adaptee.MySQLQueryAdapter;
import org.example.repository.DatabaseQueryAbstractFactory;

public class MySQLQueryConcrete extends DatabaseQueryAbstractFactory {
    @Override
    public QueryGenerator createQueryGenerator() {
        return new MySQLQueryAdapter();
    }
}
