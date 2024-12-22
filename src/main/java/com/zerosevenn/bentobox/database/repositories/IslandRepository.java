package com.zerosevenn.bentobox.database.repositories;

import com.zerosevenn.bentobox.database.provider.MySQLContainer;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class IslandRepository extends MySQLContainer {
    public IslandRepository(JavaPlugin instance) {
        super(instance);
    }

    public void createIslandTable() {
        String sql = "CREATE TABLE IF NOT EXISTS `island_data` (\n" +
                "    `id` INT AUTO_INCREMENT PRIMARY KEY,\n" +
                "    `ownerUuid` CHAR(36) NOT NULL,\n" +
                "    `centerX` INT NOT NULL,\n" +
                "    `centerZ` INT NOT NULL,\n" +
                "    `gridSize` INT NOT NULL,\n" +
                "    `blueprint` TEXT\n" +
                ");";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        return getConnection("island_data.sql");
    }

}
