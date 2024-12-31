package org.example.repository.concrete;

import org.example.repository.DatabaseQueryAbstractFactory;
import org.example.repository.dbquery.PostgresQueryGenerator;
import org.example.repository.QueryGenerator;

public class PostgresQueryConcrete extends DatabaseQueryAbstractFactory {
    @Override
    public QueryGenerator createQueryGenerator() {
        return new PostgresQueryGenerator();
    }
}
