package com.zerosevenn.bentobox.database.repositories;

import com.zerosevenn.bentobox.database.provider.MySQLContainer;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PlayerDataRepository extends MySQLContainer {
    public PlayerDataRepository(JavaPlugin instance) {
        super(instance);
    }


    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS `player_data` (\n" +
                "    `id` INT AUTO_INCREMENT PRIMARY KEY,\n" +
                "    `playerUuid` CHAR(36) NOT NULL,\n" +
                "    `islandsOwned` INT DEFAULT 0,\n" +
                "    `totalChunksUnlocked` INT DEFAULT 0\n" +
                ");";
        try(Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)){
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        return getConnection("player_data.sql");
    }

}
