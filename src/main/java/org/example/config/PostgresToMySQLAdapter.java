
package org.example.config;

public class PostgresToMySQLAdapter implements IDatabaseConfig {
    private final DatabaseConfig postgresConfig;
    
    public PostgresToMySQLAdapter(DatabaseConfig postgresConfig) {
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