package org.example.config.dbconfig;

import org.example.config.IDatabaseConfig;

public class PostgresConfig implements IDatabaseConfig {
    private String url;
    private String databaseName;
    private String username;
    private String password;

    public PostgresConfig(String url, String databaseName, String username, String password) {
        this.url = url;
        this.databaseName = databaseName;
        this.username = username;
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public String getDatabase() {
        return databaseName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    
    @Override
    public String getFullUrl() {
        return url + "/" + databaseName;
    }

    @Override
    public String getDatabaseName() {
        return databaseName;
    }
}
