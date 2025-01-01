package org.example.config.adapter;

import org.example.config.IDatabaseConfig;
import org.example.config.dbconfig.DatabaseConfig;

public class MySQLConfigAdapter implements IDatabaseConfig {
    private final DatabaseConfig databaseConfig;
    
    public MySQLConfigAdapter(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }
    
    @Override
    public String getUrl() {
        // Convert postgres URL to MySQL URL format
        String postgresUrl = databaseConfig.getUrl();
        return postgresUrl.replace("jdbc:postgresql:", "jdbc:mysql:");
    }
    
    @Override
    public String getDatabaseName() {
        return databaseConfig.getDatabaseName();
    }
    
    @Override
    public String getUsername() {
        return databaseConfig.getUsername();
    }
    
    @Override
    public String getPassword() {
        return databaseConfig.getPassword();
    }
    
    @Override
    public String getFullUrl() {
        return getUrl() + "/" + getDatabaseName();
    }
}