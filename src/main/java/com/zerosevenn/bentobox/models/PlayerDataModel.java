package com.zerosevenn.bentobox.models;

import java.util.UUID;

public class PlayerDataModel {
    private String id;
    private UUID playerUuid;
    private int islandsOwned;
    private int totalChunksUnlocked;

    public PlayerDataModel(UUID playerUuid, int islandsOwned, int totalChunksUnlocked) {
        this.id = UUID.randomUUID().toString();
        this.playerUuid = playerUuid;
        this.islandsOwned = islandsOwned;
        this.totalChunksUnlocked = totalChunksUnlocked;
    }

    public PlayerDataModel(String id, UUID playerUuid, int islandsOwned, int totalChunksUnlocked) {
        this.id = id;
        this.playerUuid = playerUuid;
        this.islandsOwned = islandsOwned;
        this.totalChunksUnlocked = totalChunksUnlocked;
    }

    public PlayerDataModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public void setPlayerUuid(UUID playerUuid) {
        this.playerUuid = playerUuid;
    }

    public int getIslandsOwned() {
        return islandsOwned;
    }

    public void setIslandsOwned(int islandsOwned) {
        this.islandsOwned = islandsOwned;
    }

    public int getTotalChunksUnlocked() {
        return totalChunksUnlocked;
    }

    public void setTotalChunksUnlocked(int totalChunksUnlocked) {
        this.totalChunksUnlocked = totalChunksUnlocked;
    }
}
