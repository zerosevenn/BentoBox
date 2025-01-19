package com.zerosevenn.bentobox.utils;

import com.zerosevenn.bentobox.database.repositories.GridRepository;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Random;

public class LocationUtils {

    private static final Random random = new Random();

    public static Location generateRandomLocation(World world, int minDistance, GridRepository gridRepository) {
        if (world == null) throw new IllegalArgumentException("World cannot be null");

        int maxCoordinate = (int) Math.min(world.getWorldBorder().getSize() / 2, Integer.MAX_VALUE / 2);

        for (int attempts = 0; attempts < 100; attempts++) {
            int x = random.nextInt(maxCoordinate * 2) - maxCoordinate;
            int z = random.nextInt(maxCoordinate * 2) - maxCoordinate;
            int gridX = x / 16;
            int gridZ = z / 16;

            if (gridRepository.isCellAvailable(gridX, gridZ)) {
                int y = world.getHighestBlockYAt(x, z) + 1;
                Location location = new Location(world, x, y, z);

                if (location.getWorld().getNearbyEntities(location, minDistance, minDistance, minDistance).isEmpty()) {
                    return location;
                }
            }
        }
        throw new RuntimeException("Failed to find a suitable random location within 100 attempts");
    }
}
