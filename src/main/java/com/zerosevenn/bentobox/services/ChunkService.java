package com.zerosevenn.bentobox.services;

import com.zerosevenn.bentobox.BentoBox;
import com.zerosevenn.bentobox.database.repositories.ChunkDataRepository;
import com.zerosevenn.bentobox.database.repositories.GridRepository;
import com.zerosevenn.bentobox.database.repositories.IslandRepository;
import com.zerosevenn.bentobox.database.repositories.PlayerDataRepository;
import com.zerosevenn.bentobox.managers.ChunkUnlockManager;
import com.zerosevenn.bentobox.managers.WorldManager;
import com.zerosevenn.bentobox.models.ChunkDataModel;
import com.zerosevenn.bentobox.models.IslandModel;
import com.zerosevenn.bentobox.models.PlayerDataModel;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class ChunkService {

    private final BentoBox plugin;
    private final ChunkDataRepository chunkRepository;
    private final IslandRepository islandRepository;
    private final PlayerDataRepository playerDataRepository;
    private final WorldManager worldManager;
    private final World world;

    public ChunkService(JavaPlugin plugin) {
        this.plugin = (BentoBox) plugin;
        this.chunkRepository = this.plugin.getChunkDataRepository();
        this.islandRepository = this.plugin.getIslandRepository();
        this.playerDataRepository = this.plugin.getPlayerDataRepository();
        this.worldManager = new WorldManager(plugin, new ChunkUnlockManager(plugin));
        this.world = this.plugin.getIslandWord();
    }

    public boolean unlockChunk(Player player, Location location, String islandId) {
        int chunkX = location.getBlockX();
        int chunkZ = location.getBlockZ();

        if (!validateChunkUnlock(player, chunkX, chunkZ)) {
            return false;
        }

        worldManager.generateChunk(player, player.getLocation());
        saveChunk(chunkX, chunkZ, world.getName(), islandId);
        updateIsland(player.getUniqueId().toString());
        updatePlayerProgress(player);
        player.sendMessage("§aChunk successfully unlocked at [" + chunkX + ", " + chunkZ + "]!");

        return true;
    }

    private boolean validateChunkUnlock(Player player, int chunkX, int chunkZ) {
        ChunkDataModel existingChunk = chunkRepository.getChunkDataByCoordinates(world,chunkX, chunkZ);
        if (existingChunk != null && existingChunk.isUnlocked()) {
            player.sendMessage("§cThis chunk has already been unlocked!");
            return false;
        }

        return true;
    }

    private void saveChunk(int chunkX, int chunkZ, String world, String islandId) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            ChunkDataModel chunk = new ChunkDataModel();
            chunk.setIslandId(islandId);
            chunk.setChunkX(chunkX);
            chunk.setChunkZ(chunkZ);
            chunk.setUnlocked(true);

            Bukkit.getScheduler().runTask(plugin, () -> {
                World bukkitWorld = Bukkit.getWorld(world);
                if (bukkitWorld == null) {
                    plugin.getLogger().warning("World not found: " + world);
                    return;
                }
                Chunk minecraftChunk = bukkitWorld.getChunkAt(chunkX, chunkZ);
                Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                    chunkRepository.insertChunkData(minecraftChunk, islandId, true);
                });
            });
        });
    }

    private void updateIsland(String islandId) {
        IslandModel island = islandRepository.readIsland(islandId);
        island.setGridSize(island.getGridSize() + 1);
        islandRepository.updateIsland(island);
    }

    private void updatePlayerProgress(Player player) {
        PlayerDataModel playerData = playerDataRepository.readPlayer(player.getUniqueId().toString());
        System.out.println(playerData.toString());
        playerData.setTotalChunksUnlocked(playerData.getTotalChunksUnlocked() + 1);
        playerDataRepository.updatePlayer(playerData);
    }

    public World getWorld() {
        return world;
    }
}
