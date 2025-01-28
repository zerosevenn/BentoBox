package com.zerosevenn.bentobox.models;

import java.util.UUID;

public class GridDataModel {
    private int id;
    private int gridX;
    private int gridZ;
    private boolean isOccupied;

    public GridDataModel(int gridX, int gridZ, boolean isOccupied) {
        this.id = Integer.parseInt(UUID.randomUUID().toString());
        this.gridX = gridX;
        this.gridZ = gridZ;
        this.isOccupied = isOccupied;
    }

    public GridDataModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGridX() {
        return gridX;
    }

    public void setGridX(int gridX) {
        this.gridX = gridX;
    }

    public int getGridZ() {
        return gridZ;
    }

    public void setGridZ(int gridZ) {
        this.gridZ = gridZ;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }
}
