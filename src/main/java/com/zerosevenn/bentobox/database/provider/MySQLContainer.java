package com.zerosevenn.bentobox.database.provider;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class MySQLContainer {

    private Connection connection;
    private final JavaPlugin instance;
    private final MySQLProvider mySQL;

    public MySQLContainer(JavaPlugin instance) {
        this.instance = instance;
        this.mySQL = new MySQLProvider(instance.getConfig());
    }

    public Connection getConnection(String file, String dataName) {
        try {
            File dataFolder = new File(instance.getDataFolder(), "/database");
            if (!dataFolder.exists() && !dataFolder.mkdir()) {
                return null;
            }

            File dbFile = new File(dataFolder, file);
            if (!dbFile.exists() && !dbFile.createNewFile()) {
                return null;
            }

            if (mySQL.getFileConfiguration().getBoolean("Database.use-mysql")) {
                try {
                    mySQL.connect(dataName);
                    Connection mysqlConnection = mySQL.getConnection();
                    if (mysqlConnection != null) {
                        return mysqlConnection;
                    }
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

            try {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile);
                return connection;
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
