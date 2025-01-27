package com.zerosevenn.bentobox.models;

import java.util.UUID;

public class IslandModel {
    private String id;
    private UUID ownerUuid;
    private int centerX;
    private int centerZ;
    private int gridSize;
    private String blueprint;

    public IslandModel(UUID ownerUuid, int centerX, int centerZ, int gridSize, String blueprint) {
        this.id = UUID.randomUUID().toString();
        this.ownerUuid = ownerUuid;
        this.centerX = centerX;
        this.centerZ = centerZ;
        this.gridSize = gridSize;
        this.blueprint = blueprint;
    }

    public IslandModel(String id, UUID ownerUuid, int centerX, int centerZ, int gridSize, String blueprint) {
        this.id = id;
        this.ownerUuid = ownerUuid;
        this.centerX = centerX;
        this.centerZ = centerZ;
        this.gridSize = gridSize;
        this.blueprint = blueprint;
    }

    public IslandModel() {
    }

    public String getBlueprint() {
        return blueprint;
    }

    public void setBlueprint(String blueprint) {
        this.blueprint = blueprint;
    }

    public int getGridSize() {
        return gridSize;
    }

    public void setGridSize(int gridSize) {
        this.gridSize = gridSize;
    }

    public int getCenterZ() {
        return centerZ;
    }

    public void setCenterZ(int centerZ) {
        this.centerZ = centerZ;
    }

    public int getCenterX() {
        return centerX;
    }

    public void setCenterX(int centerX) {
        this.centerX = centerX;
    }

    public UUID getOwnerUuid() {
        return ownerUuid;
    }

    public void setOwnerUuid(UUID ownerUuid) {
        this.ownerUuid = ownerUuid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
