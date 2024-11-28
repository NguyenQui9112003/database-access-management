package org.example.repository;

public class PostgresQueryConcrete extends DatabaseQueryFactory {
    @Override
    public QueryGeneratorFactory createQueryGenerator() {
        return new PostgresQueryGeneratorConcrete();
    }
}
