package com.zerosevenn.bentobox.database.repositories;

import com.zerosevenn.bentobox.database.provider.MySQLContainer;
import com.zerosevenn.bentobox.models.IslandMemberModel;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class IslandMemberRepository extends MySQLContainer {

    public IslandMemberRepository(JavaPlugin instance) {
        super(instance);
    }

    public void createMemberTable() {
        String sqlMySQL = "CREATE TABLE IF NOT EXISTS `island_members` ("
                + "`id` INT AUTO_INCREMENT PRIMARY KEY, "
                + "`islandId` VARCHAR(255) NOT NULL, "
                + "`playerUuid` CHAR(36) NOT NULL, "
                + "`invited` TINYINT(1) NOT NULL DEFAULT 0, "
                + "`banned` TINYINT(1) NOT NULL DEFAULT 0, "
                + "`trusted` TINYINT(1) NOT NULL DEFAULT 0, "
                + "FOREIGN KEY (`islandId`) REFERENCES `island_data`(`id`) "
                + "ON DELETE CASCADE ON UPDATE CASCADE"
                + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci";

        String sqlSQLite = "CREATE TABLE IF NOT EXISTS `island_members` ("
                + "`id` INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "`islandId` VARCHAR(255) NOT NULL, "
                + "`playerUuid` TEXT NOT NULL, "
                + "`invited` INTEGER NOT NULL DEFAULT 0, "
                + "`banned` INTEGER NOT NULL DEFAULT 0, "
                + "`trusted` INTEGER NOT NULL DEFAULT 0, "
                + "FOREIGN KEY (`islandId`) REFERENCES `island_data`(`id`) "
                + "ON DELETE CASCADE ON UPDATE CASCADE"
                + ")";

        try (Connection connection = getConnection()) {
            boolean isSQLite = connection.getMetaData().getDatabaseProductName().equalsIgnoreCase("SQLite");

            String createTableSQL = isSQLite ? sqlSQLite : sqlMySQL;

            try (PreparedStatement stmt = connection.prepareStatement(createTableSQL)) {
                stmt.execute();
            }

            if (isSQLite) {
                try (PreparedStatement pragma = connection.prepareStatement("PRAGMA foreign_keys = ON;")) {
                    pragma.execute();
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error creating island_members table", e);
        }
    }

    public IslandMemberModel getMember(UUID playerUuid) {
        String sql = "SELECT * FROM island_members WHERE playerUuid = ?";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, playerUuid.toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new IslandMemberModel(
                            resultSet.getInt("islandId"),
                            UUID.fromString(resultSet.getString("playerUuid")),
                            resultSet.getBoolean("invited"),
                            resultSet.getBoolean("banned"),
                            resultSet.getBoolean("trusted")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void updateMember(IslandMemberModel member) {
        String sqlUpdate = "UPDATE island_members SET invited = ?, banned = ?, trusted = ? WHERE playerUuid = ?";
        String sqlInsert = "INSERT INTO island_members (islandId, playerUuid, invited, banned, trusted) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sqlUpdate)) {
            statement.setBoolean(1, member.isInvited());
            statement.setBoolean(2, member.isBanned());
            statement.setBoolean(3, member.isTrusted());
            statement.setString(4, member.getPlayerUuid().toString());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 0) {
                try (PreparedStatement insertStatement = connection.prepareStatement(sqlInsert)) {
                    insertStatement.setInt(1, member.getIslandId());
                    insertStatement.setString(2, member.getPlayerUuid().toString());
                    insertStatement.setBoolean(3, member.isInvited());
                    insertStatement.setBoolean(4, member.isBanned());
                    insertStatement.setBoolean(5, member.isTrusted());
                    insertStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteMember(UUID playerUuid) {
        String sql = "DELETE FROM island_members WHERE playerUuid = ?";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, playerUuid.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        return getConnection("island_data.sql", "islands");
    }
}
