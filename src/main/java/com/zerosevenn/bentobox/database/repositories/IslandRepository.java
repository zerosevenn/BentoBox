package com.zerosevenn.bentobox.database.repositories;

import com.zerosevenn.bentobox.database.provider.MySQLContainer;
import com.zerosevenn.bentobox.models.IslandModel;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

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

    public IslandModel readIsland(String id) {
        String sql = "SELECT * FROM island_data WHERE id = ?";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new IslandModel(
                            UUID.fromString(resultSet.getString("ownerUuid")),
                            resultSet.getInt("centerX"),
                            resultSet.getInt("centerZ"),
                            resultSet.getInt("gridSize"),
                            resultSet.getString("blueprint")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void updateIsland(IslandModel island) {
        String sql = "UPDATE island_data SET ownerUuid = ?, centerX = ?, centerZ = ?, gridSize = ?, blueprint = ? WHERE id = ?";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, island.getOwnerUuid().toString());
            statement.setInt(2, island.getCenterX());
            statement.setInt(3, island.getCenterZ());
            statement.setInt(4, island.getGridSize());
            statement.setString(5, island.getBlueprint());
            statement.setInt(6, island.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteIsland(String id) {
        String sql = "DELETE FROM island_data WHERE id = ?";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        return getConnection("island_data.sql");
    }

}
