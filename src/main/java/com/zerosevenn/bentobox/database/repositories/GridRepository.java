package com.zerosevenn.bentobox.database.repositories;

import com.zerosevenn.bentobox.database.provider.MySQLContainer;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GridRepository extends MySQLContainer {
    public GridRepository(JavaPlugin instance) {
        super(instance);
    }

    public void createGridDataTable() {
        String sql = "CREATE TABLE IF NOT EXISTS `grid_data` (\n" +
                "    `id` INT AUTO_INCREMENT PRIMARY KEY,\n" +
                "    `gridX` INT NOT NULL,\n" +
                "    `gridZ` INT NOT NULL,\n" +
                "    `isOccupied` BOOLEAN NOT NULL DEFAULT FALSE\n" +
                ");";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public Connection getConnection() {
        return getConnection("grid_data.sql");
    }
}
