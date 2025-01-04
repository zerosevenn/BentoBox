package com.zerosevenn.bentobox.database.repositories;

import com.zerosevenn.bentobox.database.provider.MySQLContainer;
import com.zerosevenn.bentobox.models.ChunkDataModel;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ChunkDataRepository extends MySQLContainer {
    public ChunkDataRepository(JavaPlugin instance) {
        super(instance);
    }

    public void createChunkDataTable() {
        String sql = "CREATE TABLE IF NOT EXISTS `chunk_data` (\n" +
                "    `id` INT AUTO_INCREMENT PRIMARY KEY,\n" +
                "    `islandId` INT NOT NULL,\n" +
                "    `chunkX` INT NOT NULL,\n" +
                "    `chunkZ` INT NOT NULL,\n" +
                "    `isUnlocked` BOOLEAN NOT NULL DEFAULT FALSE,\n" +
                "    FOREIGN KEY (`islandId`) REFERENCES `island_data`(`id`)\n" +
                ");";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertChunkData(int islandId, int chunkX, int chunkZ, boolean isUnlocked) {
        String sql = "INSERT INTO `chunk_data` (`islandId`, `chunkX`, `chunkZ`, `isUnlocked`) VALUES (?, ?, ?, ?);";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, islandId);
            statement.setInt(2, chunkX);
            statement.setInt(3, chunkZ);
            statement.setBoolean(4, isUnlocked);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ChunkDataModel getChunkDataById(int id) {
        String sql = "SELECT * FROM `chunk_data` WHERE `id` = ?;";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            return new ChunkDataModel(rs.getInt("islandId"), rs.getInt("chunkX"), rs.getInt("chunkZ"), rs.getBoolean("isUnlocked"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateChunkData(int id, boolean isUnlocked) {
        String sql = "UPDATE `chunk_data` SET `isUnlocked` = ? WHERE `id` = ?;";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBoolean(1, isUnlocked);
            statement.setInt(2, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteChunkData(int id) {
        String sql = "DELETE FROM `chunk_data` WHERE `id` = ?;";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        return getConnection("chunk_data.sql");
    }
}
