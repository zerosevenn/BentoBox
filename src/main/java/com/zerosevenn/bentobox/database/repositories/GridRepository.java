package com.zerosevenn.bentobox.database.repositories;

import com.zerosevenn.bentobox.database.provider.MySQLContainer;
import com.zerosevenn.bentobox.models.GridDataModel;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    public void createGrid(GridDataModel grid) {
        String sql = "INSERT INTO grid_data (id, gridX, gridZ, isOccupied) VALUES (?, ?, ?, ?)";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, grid.getId());
            statement.setInt(2, grid.getGridX());
            statement.setInt(3, grid.getGridZ());
            statement.setBoolean(4, grid.isOccupied());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public GridDataModel readGrid(String id) {
        String sql = "SELECT * FROM grid_data WHERE id = ?";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new GridDataModel(
                            resultSet.getInt("gridX"),
                            resultSet.getInt("gridZ"),
                            resultSet.getBoolean("isOccupied")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void updateGrid(GridDataModel grid) {
        String sql = "UPDATE grid_data SET gridX = ?, gridZ = ?, isOccupied = ? WHERE id = ?";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, grid.getGridX());
            statement.setInt(2, grid.getGridZ());
            statement.setBoolean(3, grid.isOccupied());
            statement.setInt(4, grid.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteGrid(String id) {
        String sql = "DELETE FROM grid_data WHERE id = ?";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isCellAvailable(int gridX, int gridZ) {
        String sql = "SELECT isOccupied FROM grid_data WHERE gridX = ? AND gridZ = ?";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, gridX);
            statement.setInt(2, gridZ);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return !resultSet.getBoolean("isOccupied");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }


    public Connection getConnection() {
        return getConnection("grid_data.sql", "grids");
    }
}
