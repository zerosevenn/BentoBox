package com.zerosevenn.bentobox.models;
import java.util.UUID;

public class ChunkDataModel {
    private int id;
    private String islandId;
    private int chunkX;
    private int chunkZ;
    private boolean isUnlocked;

    public ChunkDataModel(String islandId, int chunkX, int chunkZ, boolean isUnlocked) {
        this.id = Integer.parseInt(UUID.randomUUID().toString());
        this.islandId = islandId;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.isUnlocked = isUnlocked;
    }

    public ChunkDataModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIslandId() {
        return islandId;
    }

    public void setIslandId(String islandId) {
        this.islandId = islandId;
    }

    public int getChunkX() {
        return chunkX;
    }

    public void setChunkX(int chunkX) {
        this.chunkX = chunkX;
    }

    public int getChunkZ() {
        return chunkZ;
    }

    public void setChunkZ(int chunkZ) {
        this.chunkZ = chunkZ;
    }

    public boolean isUnlocked() {
        return isUnlocked;
    }

    public void setUnlocked(boolean unlocked) {
        isUnlocked = unlocked;
    }
}
