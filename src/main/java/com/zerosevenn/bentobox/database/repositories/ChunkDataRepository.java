package com.zerosevenn.bentobox.database.repositories;

import com.zerosevenn.bentobox.database.provider.MySQLContainer;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
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


    public Connection getConnection() {
        return getConnection("chunk_data.sql");
    }
}
