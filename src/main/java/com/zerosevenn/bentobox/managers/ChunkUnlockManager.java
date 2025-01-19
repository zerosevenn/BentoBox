package com.zerosevenn.bentobox.managers;

import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class ChunkUnlockManager {

    private final JavaPlugin plugin;

    public ChunkUnlockManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void startChunkObservation(Player player, Location chunkCenter, Runnable onComplete) {
    }

    public void unlockChunk(Player player, Chunk chunk) {

    }




}
