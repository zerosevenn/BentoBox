package com.zerosevenn.bentobox.utils;

import com.zerosevenn.bentobox.database.repositories.GridRepository;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Random;

public class LocationUtils {

    private static final Random random = new Random();
    private static final int MAX_RANDOM_RANGE = 10000;
    private static final int MIN_DISTANCE = 500;

    public static Location generateRandomLocation(World world, int minDistance, GridRepository gridRepository) {
        if (world == null) throw new IllegalArgumentException("World cannot be null");
        if (world.getWorldBorder() == null || world.getWorldBorder().getSize() <= 0) {
            throw new IllegalStateException("World border is not configured correctly or is zero.");
        }
        int halfBorder = (int) (world.getWorldBorder().getSize() / 2.0);
        int maxCoordinate = Math.min(halfBorder, MAX_RANDOM_RANGE);
        int attempts = 0;

        while (attempts < 100) {
            int x = random.nextInt(maxCoordinate * 2) - maxCoordinate;
            int z = random.nextInt(maxCoordinate * 2) - maxCoordinate;
            int gridX = x / 16;
            int gridZ = z / 16;

            if (gridRepository.isCellAvailable(gridX, gridZ) && isFarEnough(gridX, gridZ, gridRepository)) {
                return new Location(world, x, 0, z);
            }
            attempts++;
        }
        throw new RuntimeException("Failed to find a suitable random location within 100 attempts");
    }

    private static boolean isFarEnough(int gridX, int gridZ, GridRepository gridRepository) {
        for (int dx = -MIN_DISTANCE / 16; dx <= MIN_DISTANCE / 16; dx++) {
            for (int dz = -MIN_DISTANCE / 16; dz <= MIN_DISTANCE / 16; dz++) {
                if (!gridRepository.isCellAvailable(gridX + dx, gridZ + dz)) {
                    return false;
                }
            }
        }
        return true;
    }
}
