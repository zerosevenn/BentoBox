package com.zerosevenn.bentobox.models;

import java.util.UUID;

public class PlayerDataModel {
    private int id;
    private UUID playerUuid;
    private int islandsOwned;
    private int totalChunksUnlocked;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
