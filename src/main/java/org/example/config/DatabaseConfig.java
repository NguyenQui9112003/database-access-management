package org.example.config;

public class DatabaseConfig {
    private String url;
    private String databaseName;
    private String username;
    private String password;

    public DatabaseConfig(String url, String databaseName, String username, String password) {
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
}
