package org.example.repository;

public class PostgresQueryConcrete extends DatabaseQueryAbstractFactory {
    @Override
    public QueryGenerator createQueryGenerator() {
        return new PostgresQueryGenerator();
    }
}
