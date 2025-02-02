package com.zerosevenn.bentobox.managers;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.block.BlockType;
import com.sk89q.worldedit.world.block.BlockTypes;
import com.zerosevenn.bentobox.BentoBox;
import com.zerosevenn.bentobox.utils.ConfigUtils;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorldManager {
    private final BentoBox plugin;
    private World randomWorld;
    private final ChunkUnlockManager chunkUnlockManager;

    public WorldManager(JavaPlugin plugin, ChunkUnlockManager chunkUnlockManager) {
        this.plugin = (BentoBox) plugin;
        this.chunkUnlockManager = chunkUnlockManager;
        initializeRandomWorld();
    }

    public void generateChunk(Player player, Location location, int delay, int minY, int maxY) {
        try {
            Chunk bukkitChunk = location.getChunk();
            bukkitChunk.load(true);
            if (!bukkitChunk.isLoaded()) {
                plugin.getLogger().severe("Failed to load chunk [" + bukkitChunk.getX() + ", " + bukkitChunk.getZ() + "]");
                return;
            }
            World targetWorld = bukkitChunk.getWorld();
            int cx = bukkitChunk.getX();
            int cz = bukkitChunk.getZ();
            int realMaxY = Integer.MIN_VALUE;
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    int worldX = (cx << 4) + x;
                    int worldZ = (cz << 4) + z;
                    int topY = randomWorld.getHighestBlockYAt(worldX, worldZ);
                    if (topY > realMaxY) {
                        realMaxY = topY;
                    }
                }
            }
            if (realMaxY < minY) {
                realMaxY = minY;
            }
            realMaxY = Math.min(realMaxY, maxY);
            for (int y = minY; y <= realMaxY; y++) {
                final int currentY = y;
                int finalRealMaxY = realMaxY;
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    try (EditSession session = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(targetWorld))) {
                        for (int x = 0; x < 16; x++) {
                            for (int z = 0; z < 16; z++) {
                                int worldX = (cx << 4) + x;
                                int worldZ = (cz << 4) + z;
                                Block randomWorldBlock = randomWorld.getBlockAt(worldX, currentY, worldZ);
                                Material material = randomWorldBlock.getType();
                                if (material == Material.AIR || material == Material.CAVE_AIR || material == Material.VOID_AIR) {
                                    continue;
                                }
                                BlockType blockType = BukkitAdapter.asBlockType(material);
                                if (blockType == null) {
                                    blockType = BlockTypes.AIR;
                                }
                                session.setBlock(BlockVector3.at(worldX, currentY, worldZ), blockType.getDefaultState());
                            }
                        }
                    } catch (Exception e) {
                        plugin.getLogger().severe("Error generating layer " + currentY + ": " + e.getMessage());
                    }
                }, delay * (y - minY));
            }
        } catch (Exception e) {
            plugin.getLogger().severe("Error during animated generation: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public void generateChunk(Player player, Location location) {
        int delay = 1;
        int minY = location.getWorld().getMinHeight();
        int maxY = location.getWorld().getMaxHeight();
        generateChunk(player, location, delay, minY, maxY);
    }

    private void initializeRandomWorld() {
        String worldName = ConfigUtils.getIslandsWorld();
        if (worldName == null || worldName.isEmpty()) {
            throw new IllegalStateException("O nome do mundo na configuração não está definido ou está vazio.");
        }

        randomWorld = Bukkit.getWorld(worldName);
        if (randomWorld == null) {
            plugin.getLogger().info("Criando mundo: " + worldName);
            WorldCreator creator = new WorldCreator(worldName);
            creator.environment(World.Environment.NORMAL);
            creator.type(WorldType.NORMAL);
            creator.generateStructures(false);
            creator.generator(new CustomChunkGenerator(convertStringsToBiomes()));
            randomWorld = creator.createWorld();
            if (randomWorld == null) {
                throw new IllegalStateException("Falha ao criar ou carregar o mundo: " + worldName);
            }
        }
    }

    public List<Biome> convertStringsToBiomes() {
        List<String> biomeNames = ConfigUtils.getIslandGenerationBiomes();
        List<Biome> biomes = new ArrayList<>();
        for (String name : biomeNames) {
            try {
                Biome biome = Biome.valueOf(name.toUpperCase());
                biomes.add(biome);
            } catch (IllegalArgumentException e) {
                System.out.println("Bioma desconhecido: " + name);
            }
        }
        return biomes;
    }

    public World getRandomWorld() {
        return randomWorld;
    }


    private static class CustomChunkGenerator extends ChunkGenerator {
        private final List<Biome> biomes;

        public CustomChunkGenerator(List<Biome> biomes) {
            this.biomes = biomes;
        }

        public void generateSurface(World world, Random random, int x, int z, ChunkData chunkData) {
            Biome biome = biomes.get(Math.abs((x + z) % biomes.size()));
            for (int bx = 0; bx < 16; bx++) {
                for (int bz = 0; bz < 16; bz++) {
                    int worldX = (x << 4) + bx;
                    int worldZ = (z << 4) + bz;
                    world.setBiome(worldX, worldZ, biome);
                }
            }
        }
    }
}

