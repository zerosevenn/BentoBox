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
import java.util.UUID;

public class ChunkDataRepository extends MySQLContainer {
    public ChunkDataRepository(JavaPlugin instance) {
        super(instance);
    }

    public void insertChunkData(Chunk chunk, String islandId, boolean isUnlocked) {
        String sql = "INSERT INTO `chunk_data` (`id`, `islandId`, `chunkX`, `chunkZ`, `isUnlocked`) VALUES (?, ?, ?, ?, ?);";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            String chunkId = UUID.randomUUID().toString();
            statement.setString(1, chunkId);
            statement.setString(2, islandId);
            statement.setInt(3, chunk.getX());
            statement.setInt(4, chunk.getZ());
            statement.setBoolean(5, isUnlocked);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting chunk data", e);
        }
    }

    public ChunkDataModel getChunkDataByCoordinates(World world, int chunkX, int chunkZ) {
        String sql = "SELECT * FROM `chunk_data` WHERE `chunkX` = ? AND `chunkZ` = ?;";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, chunkX);
            statement.setInt(2, chunkZ);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return new ChunkDataModel(
                        rs.getString("id"),
                        rs.getString("islandId"),
                        rs.getInt("chunkX"),
                        rs.getInt("chunkZ"),
                        rs.getBoolean("isUnlocked")
                );
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching chunk data", e);
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
            throw new RuntimeException("Error updating chunk status", e);
        }
    }

    public boolean isChunkUnlocked(World world, int chunkX, int chunkZ) {
        String sql = "SELECT `isUnlocked` FROM `chunk_data` WHERE `chunkX` = ? AND `chunkZ` = ?;";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, chunkX);
            statement.setInt(2, chunkZ);

            String finalQuery = sql.replaceFirst("\\?", String.valueOf(chunkX))
                    .replaceFirst("\\?", String.valueOf(chunkZ));
            System.out.println("Executing SQL: " + finalQuery);

            ResultSet rs = statement.executeQuery();
            return rs.next() && rs.getBoolean("isUnlocked");
        } catch (SQLException e) {
            throw new RuntimeException("Error checking if chunk is unlocked", e);
        }
    }


    public void deleteChunkData(Chunk chunk) {
        String sql = "DELETE FROM `chunk_data` WHERE `chunkX` = ? AND `chunkZ` = ?;";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, chunk.getX());
            statement.setInt(2, chunk.getZ());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting chunk data", e);
        }
    }

    public Connection getConnection() {
        return super.getConnection("chunk_data.sql", "chunks");
    }
}
