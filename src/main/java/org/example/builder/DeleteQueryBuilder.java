package org.example.builder;

public class DeleteQueryBuilder implements ConditionQueryBuilder {
    private final StringBuilder query;
    private String tableName;
    private String whereCondition;

    public DeleteQueryBuilder() {
        query = new StringBuilder("DELETE FROM ");
    }

    @Override
    public ConditionQueryBuilder where(String condition) {
        this.whereCondition = condition;
        return this;
    }

    @Override
    public ConditionQueryBuilder from(String tableName) {
        this.tableName = tableName;
        return this;
    }

    @Override
    public String build() {
        query.append(tableName);
        if (whereCondition != null) {
            query.append(" WHERE ").append(whereCondition);
        }
        query.append(";");
        return query.toString();
    }
}
