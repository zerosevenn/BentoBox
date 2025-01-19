package com.zerosevenn.bentobox.services;

import com.zerosevenn.bentobox.database.repositories.ChunkDataRepository;
import com.zerosevenn.bentobox.database.repositories.GridRepository;
import com.zerosevenn.bentobox.database.repositories.IslandRepository;
import com.zerosevenn.bentobox.database.repositories.PlayerDataRepository;
import com.zerosevenn.bentobox.managers.WorldManager;
import com.zerosevenn.bentobox.models.ChunkDataModel;
import com.zerosevenn.bentobox.models.IslandModel;
import com.zerosevenn.bentobox.models.PlayerDataModel;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

public class ChunkService {
    private final ChunkDataRepository chunkRepository;
    private final IslandRepository islandRepository;
    private final PlayerDataRepository playerDataRepository;
    private final WorldManager worldManager;

    public ChunkService(ChunkDataRepository chunkRepository, IslandRepository islandRepository,
                        PlayerDataRepository playerDataRepository, WorldManager worldManager) {
        this.chunkRepository = chunkRepository;
        this.islandRepository = islandRepository;
        this.playerDataRepository = playerDataRepository;
        this.worldManager = worldManager;
    }

    public boolean unlockChunk(Player player, int chunkX, int chunkZ, String islandId) {
        if (!validateChunkUnlock(player, chunkX, chunkZ, islandId)) {
            return false;
        }

        worldManager.generateChunk(player, player.getLocation());
        saveChunk(chunkX, chunkZ, , islandId);
        updateIsland(islandId);
        updatePlayerProgress(player);
        player.sendMessage("§aChunk desbloqueado com sucesso em [" + chunkX + ", " + chunkZ + "]!");

        return true;
    }

    private boolean validateChunkUnlock(Player player, int chunkX, int chunkZ, String islandId) {
        ChunkDataModel existingChunk = chunkRepository.getChunkByCoordinates(chunkX, chunkZ, islandId);
        if (existingChunk != null && existingChunk.isUnlocked()) {
            player.sendMessage("§cEste chunk já foi desbloqueado!");
            return false;
        }

        return true;
    }

    private void saveChunk(int chunkX, int chunkZ,String world, String islandId) {
        ChunkDataModel chunk = new ChunkDataModel();
        chunk.setIslandId(islandId);
        chunk.setChunkX(chunkX);
        chunk.setChunkZ(chunkZ);
        chunk.setUnlocked(true);
        Chunk minecraftChunk = Bukkit.getWorld(world).getChunkAt(chunkX, chunkZ);
        chunkRepository.insertChunkData(minecraftChunk, islandId, true);
    }

    private void updateIsland(String islandId) {
        IslandModel island = islandRepository.readIsland(islandId);
        island.setChunksUnlocked(island.getChunksUnlocked() + 1);
        islandRepository.updateIsland(island);
    }

    private void updatePlayerProgress(Player player) {
        PlayerDataModel playerData = playerDataRepository.readPlayer(player.getUniqueId().toString());
        playerData.setTotalChunksUnlocked(playerData.getTotalChunksUnlocked() + 1);
        playerDataRepository.updatePlayer(playerData);
    }
}

