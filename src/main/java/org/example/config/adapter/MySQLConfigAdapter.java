package org.example.config.adaptee;

import org.example.config.IDatabaseConfig;
import org.example.config.dbconfig.PostgresConfig;

public class MySQLConfigAdapter implements IDatabaseConfig {
    private final PostgresConfig postgresConfig;
    
    public MySQLConfigAdapter(PostgresConfig postgresConfig) {
        this.postgresConfig = postgresConfig;
    }
    
    @Override
    public String getUrl() {
        // Convert postgres URL to MySQL URL format
        String postgresUrl = postgresConfig.getUrl();
        return postgresUrl.replace("jdbc:postgresql:", "jdbc:mysql:");
    }
    
    @Override
    public String getDatabaseName() {
        return postgresConfig.getDatabaseName();
    }
    
    @Override
    public String getUsername() {
        return postgresConfig.getUsername();
    }
    
    @Override
    public String getPassword() {
        return postgresConfig.getPassword();
    }
    
    @Override
    public String getFullUrl() {
        return getUrl() + "/" + getDatabaseName();
    }
}