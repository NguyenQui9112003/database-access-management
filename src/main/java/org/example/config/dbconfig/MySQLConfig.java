package org.example.config;

public class MySQLConfig implements IDatabaseConfig {
    private final String url;
    private final String databaseName;
    private final String username;
    private final String password;

    public MySQLConfig(String host, int port, String databaseName, String username, String password) {
        this.url = "jdbc:mysql://" + host + ":" + port;
        this.databaseName = databaseName;
        this.username = username;
        this.password = password;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getDatabaseName() {
        return databaseName;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getFullUrl() {
        return url + "/" + databaseName;
    }
}

