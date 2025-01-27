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
        String sqlMySQL = "CREATE TABLE IF NOT EXISTS `island_data` ("
                + "`id` VARCHAR(255) PRIMARY KEY, "
                + "`ownerUuid` CHAR(36) NOT NULL, "
                + "`centerX` INT NOT NULL, "
                + "`centerZ` INT NOT NULL, "
                + "`gridSize` INT NOT NULL, "
                + "`blueprint` TEXT"
                + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci";

        String sqlSQLite = "CREATE TABLE IF NOT EXISTS `island_data` ("
                + "`id` TEXT PRIMARY KEY, "
                + "`ownerUuid` TEXT NOT NULL, "
                + "`centerX` INTEGER NOT NULL, "
                + "`centerZ` INTEGER NOT NULL, "
                + "`gridSize` INTEGER NOT NULL, "
                + "`blueprint` TEXT"
                + ")";

        String sqlMySQLChunk = "CREATE TABLE IF NOT EXISTS `chunk_data` ("
                + "`id` INT AUTO_INCREMENT PRIMARY KEY, "
                + "`islandId` VARCHAR(255) NOT NULL, "
                + "`chunkX` INT NOT NULL, "
                + "`chunkZ` INT NOT NULL, "
                + "`isUnlocked` TINYINT(1) NOT NULL DEFAULT 0, "
                + "FOREIGN KEY (`islandId`) REFERENCES `island_data`(`id`) "
                + "ON DELETE CASCADE ON UPDATE CASCADE"
                + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci";

        String sqlSQLiteChunk = "CREATE TABLE IF NOT EXISTS `chunk_data` ("
                + "`id` INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "`islandId` TEXT NOT NULL, "
                + "`chunkX` INTEGER NOT NULL, "
                + "`chunkZ` INTEGER NOT NULL, "
                + "`isUnlocked` INTEGER NOT NULL DEFAULT 0, "
                + "FOREIGN KEY (`islandId`) REFERENCES `island_data`(`id`) "
                + "ON DELETE CASCADE ON UPDATE CASCADE"
                + ")";

        try (Connection connection = getConnection()) {
            boolean isSQLite = connection.getMetaData().getDatabaseProductName().equalsIgnoreCase("SQLite");

            String createIslandTable = isSQLite ? sqlSQLite : sqlMySQL;
            String createChunkTable = isSQLite ? sqlSQLiteChunk : sqlMySQLChunk;

            try (PreparedStatement stmt1 = connection.prepareStatement(createIslandTable)) {
                stmt1.execute();
            }

            try (PreparedStatement stmt2 = connection.prepareStatement(createChunkTable)) {
                stmt2.execute();
            }

            if (isSQLite) {
                try (PreparedStatement pragma = connection.prepareStatement("PRAGMA foreign_keys = ON;")) {
                    pragma.execute();
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error creating tables", e);
        }
    }




    public IslandModel readIsland(String id) {
        String sql = "SELECT * FROM island_data WHERE ownerUuid= ?";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new IslandModel(
                            resultSet.getString("id"),
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
        String sqlUpdate = "UPDATE island_data SET ownerUuid = ?, centerX = ?, centerZ = ?, gridSize = ?, blueprint = ? WHERE id = ?";
        String sqlInsert = "INSERT INTO island_data (id, ownerUuid, centerX, centerZ, gridSize, blueprint) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sqlUpdate)) {
            statement.setString(1, island.getOwnerUuid().toString());
            statement.setInt(2, island.getCenterX());
            statement.setInt(3, island.getCenterZ());
            statement.setInt(4, island.getGridSize());
            statement.setString(5, island.getBlueprint());
            statement.setString(6, island.getId());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 0) {
                try (PreparedStatement insertStatement = connection.prepareStatement(sqlInsert)) {
                    insertStatement.setString(1, island.getId());
                    insertStatement.setString(2, island.getOwnerUuid().toString());
                    insertStatement.setInt(3, island.getCenterX());
                    insertStatement.setInt(4, island.getCenterZ());
                    insertStatement.setInt(5, island.getGridSize());
                    insertStatement.setString(6, island.getBlueprint());
                    insertStatement.executeUpdate();
                }
            }
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
        return getConnection("island_data.sql", "islands");
    }
}
