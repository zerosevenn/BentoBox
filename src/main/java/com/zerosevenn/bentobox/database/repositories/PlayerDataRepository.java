package com.zerosevenn.bentobox.database.repositories;

import com.zerosevenn.bentobox.database.provider.MySQLContainer;
import com.zerosevenn.bentobox.models.PlayerDataModel;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

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

    public void createPlayer(PlayerDataModel player) {
        String sql = "INSERT INTO player_data (id, playerUuid, islandsOwned, totalChunksUnlocked) VALUES (?, ?, ?, ?)";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, player.getId());
            statement.setString(2, player.getPlayerUuid().toString());
            statement.setInt(3, player.getIslandsOwned());
            statement.setInt(4, player.getTotalChunksUnlocked());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public PlayerDataModel readPlayer(String id) {
        String sql = "SELECT * FROM player_data WHERE id = ?";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new PlayerDataModel(
                            UUID.fromString(resultSet.getString("playerUuid")),
                            resultSet.getInt("islandsOwned"),
                            resultSet.getInt("totalChunksUnlocked")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void updatePlayer(PlayerDataModel player) {
        String sql = "UPDATE player_data SET playerUuid = ?, islandsOwned = ?, totalChunksUnlocked = ? WHERE id = ?";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, player.getPlayerUuid().toString());
            statement.setInt(2, player.getIslandsOwned());
            statement.setInt(3, player.getTotalChunksUnlocked());
            statement.setInt(4, player.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deletePlayer(String id) {
        String sql = "DELETE FROM player_data WHERE id = ?";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        return getConnection("player_data.sql");
    }

}
