package com.zerosevenn.bentobox.managers;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.block.BlockType;
import com.sk89q.worldedit.world.block.BlockTypes;
import com.zerosevenn.bentobox.BentoBox;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class WorldManager {
    private final BentoBox plugin;
    private World randomWorld;
    private final ChunkUnlockManager chunkUnlockManager;

    public WorldManager(JavaPlugin plugin, ChunkUnlockManager chunkUnlockManager) {
        this.plugin = (BentoBox) plugin;
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
                    if (currentY == finalRealMaxY) {
                        teleportPlayerToSurface(player, cx, cz);
                    }
                }, delay * (y - minY));
            }
        } catch (Exception e) {
            plugin.getLogger().severe("Error during animated generation: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void teleportPlayerToSurface(Player player, int chunkX, int chunkZ) {
        World world = plugin.getIslandWord();
        int bx = (chunkX << 4) + 8;
        int bz = (chunkZ << 4) + 8;
        int by = world.getHighestBlockYAt(bx, bz);
        player.teleport(new Location(world, bx + 0.5, by + 1, bz + 0.5));
    }

    public void generateChunk(Player player, Location location) {
        int delay = 1;
        int minY = location.getWorld().getMinHeight();
        int maxY = location.getWorld().getMaxHeight();
        generateChunk(player, location, delay, minY, maxY);
    }
}
