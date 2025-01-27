package com.zerosevenn.bentobox.database.repositories;

import com.zerosevenn.bentobox.database.provider.MySQLContainer;
import com.zerosevenn.bentobox.models.ChunkDataModel;
import org.bukkit.Chunk;
import org.bukkit.World;
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
//        String sql = "CREATE TABLE IF NOT EXISTS `chunk_data` (" +
//                "`id` INT AUTO_INCREMENT PRIMARY KEY," +
//                "`islandId` VARCHAR(255) NOT NULL," +
//                "`chunkX` INT NOT NULL," +
//                "`chunkZ` INT NOT NULL," +
//                "`isUnlocked` BOOLEAN NOT NULL DEFAULT FALSE," +
//                "FOREIGN KEY (`islandId`) REFERENCES `island_data`(`id`) " +
//                "ON DELETE CASCADE ON UPDATE CASCADE" +
//                ") ENGINE=InnoDB;";
//        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
//            statement.execute();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
    }


    public void insertChunkData(Chunk chunk, String islandId, boolean isUnlocked) {
        System.out.println("Tentando insert chunk data");
        String sql = "INSERT INTO `chunk_data` (`islandId`, `chunkX`, `chunkZ`, `isUnlocked`) VALUES (?, ?, ?, ?);";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, islandId);
            statement.setInt(2, chunk.getX());
            statement.setInt(3, chunk.getZ());
            statement.setBoolean(4, isUnlocked);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ChunkDataModel getChunkDataByCoordinates(World world, int chunkX, int chunkZ) {
        String sql = "SELECT * FROM `chunk_data` WHERE `chunkX` = ? AND `chunkZ` = ?;";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, chunkX);
            statement.setInt(2, chunkZ);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                ChunkDataModel chunkData = new ChunkDataModel(
                        rs.getString("islandId"),
                        rs.getInt("chunkX"),
                        rs.getInt("chunkZ"),
                        rs.getBoolean("isUnlocked")
                );
                return chunkData;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateChunkUnlockedStatus(Chunk chunk, boolean isUnlocked) {
        String sql = "UPDATE `chunk_data` SET `isUnlocked` = ? WHERE `chunkX` = ? AND `chunkZ` = ?;";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBoolean(1, isUnlocked);
            statement.setInt(2, chunk.getX());
            statement.setInt(3, chunk.getZ());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isChunkUnlocked(World world, int chunkX, int chunkZ) {
        String sql = "SELECT * FROM `chunk_data` WHERE `chunkX` = ? AND `chunkZ` = ?;";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, chunkX);
            statement.setInt(2, chunkZ);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getBoolean("isUnlocked");
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteChunkData(Chunk chunk) {
        String sql = "DELETE FROM `chunk_data` WHERE `chunkX` = ? AND `chunkZ` = ?;";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, chunk.getX());
            statement.setInt(2, chunk.getZ());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        return super.getConnection("chunk_data.sql", "chunks");
    }
}
