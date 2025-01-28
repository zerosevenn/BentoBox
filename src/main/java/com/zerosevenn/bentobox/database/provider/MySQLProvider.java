package com.zerosevenn.bentobox.database.provider;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLProvider {
    private final String host;
    private final int port;
    private final String database;
    private final String username;
    private final String password;
    private Connection connection;
    private final FileConfiguration fileConfiguration;

    public MySQLProvider(FileConfiguration config) {
        this.host = config.getString("Database.host");
        this.port = config.getInt("Database.port");
        this.database = config.getString("Database.database");
        this.username = config.getString("Database.username");
        this.password = config.getString("Database.password");
        this.fileConfiguration = config;
    }

    public void connect(String dataName) throws ClassNotFoundException, SQLException {
        if (connection != null && !connection.isClosed()) {
            logInfo("Database is already connected!");
            return;
        }

        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false&autoReconnect=true";

        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            logInfo("Failed to connect to the database: " + e.getMessage());
        }
    }

    public void disconnect() {
        if (connection == null) {
            logInfo("No active database connection to close.");
            return;
        }

        try {
            if (!connection.isClosed()) {
                connection.close();
                logInfo("Disconnected from the database.");
            }
        } catch (SQLException e) {
            logInfo("Failed to disconnect from the database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    public FileConfiguration getFileConfiguration() {
        return fileConfiguration;
    }

    private void logInfo(String message) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[INFO] " + message);
    }
}
