package com.zerosevenn.bentobox.managers;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.block.BlockType;
import com.sk89q.worldedit.world.block.BlockTypes;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class WorldManager {
    private final JavaPlugin plugin;
    private World randomWorld;
    private final ChunkUnlockManager chunkUnlockManager;

    public WorldManager(JavaPlugin plugin, ChunkUnlockManager chunkUnlockManager) {
        this.plugin = plugin;
        this.chunkUnlockManager = chunkUnlockManager;
        this.randomWorld = Bukkit.getWorld("world");
        if (this.randomWorld == null) {
            throw new IllegalStateException("World 'world' does not exist!");
        }
    }

    public void generateChunk(Player player, Location location, int delay, int minY, int maxY) {
        try {
            Chunk bukkitChunk = location.getChunk();
            bukkitChunk.load(true);

            if (!bukkitChunk.isLoaded()) {
                plugin.getLogger().severe("Failed to load chunk [" + bukkitChunk.getX() + ", " + bukkitChunk.getZ() + "]");
                return;
            }

            Location chunkCenter = bukkitChunk.getBlock(8, 0, 8).getLocation();
            World targetWorld = bukkitChunk.getWorld();
            int cx = bukkitChunk.getX();
            int cz = bukkitChunk.getZ();

            for (int y = minY; y < maxY; y++) {
                final int currentY = y;
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    try (EditSession session = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(targetWorld))) {
                        for (int x = 0; x < 16; x++) {
                            for (int z = 0; z < 16; z++) {
                                int worldX = (cx << 4) + x;
                                int worldZ = (cz << 4) + z;
                                Block randomWorldBlock = randomWorld.getBlockAt(worldX, currentY, worldZ);
                                Material material = randomWorldBlock.getType();
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

                    if (currentY == maxY - 1) {
                        player.sendMessage("Chunk fully generated at [" + cx + ", " + cz + "]!");
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

    public void generateChunk(Player player, Location location, int delay) {
        int minY = location.getWorld().getMinHeight();
        int maxY = location.getWorld().getMaxHeight();
        generateChunk(player, location, delay, minY, maxY);
    }

    public void generateChunk(Player player, Location location, int minY, int maxY) {
        int delay = 1;
        generateChunk(player, location, delay, minY, maxY);
    }

    public void generateChunk(Player player, Location location, World customWorld, int delay, int minY, int maxY) {
        World originalWorld = this.randomWorld;
        try {
            this.randomWorld = customWorld;
            generateChunk(player, location, delay, minY, maxY);
        } finally {
            this.randomWorld = originalWorld;
        }
    }
}
