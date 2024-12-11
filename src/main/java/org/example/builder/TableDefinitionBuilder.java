package org.example.builder;

public interface TableDefinitionBuilder extends BaseQueryBuilder{
    TableDefinitionBuilder addPrimaryKeyColumn(String columnName, String columnType);
    TableDefinitionBuilder addColumn(String columnName, String columnType);
}
