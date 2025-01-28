package com.zerosevenn.bentobox.models;

import java.util.UUID;

public class IslandMemberModel {
    private int islandId;
    private UUID playerUuid;
    private boolean invited;
    private boolean banned;
    private boolean trusted;

    public IslandMemberModel(int islandId, UUID playerUuid, boolean invited, boolean banned, boolean trusted) {
        this.islandId = islandId;
        this.playerUuid = playerUuid;
        this.invited = invited;
        this.banned = banned;
        this.trusted = trusted;
    }

    public int getIslandId() {
        return islandId;
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public boolean isInvited() {
        return invited;
    }

    public boolean isBanned() {
        return banned;
    }

    public boolean isTrusted() {
        return trusted;
    }

    public void setInvited(boolean invited) {
        this.invited = invited;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    public void setTrusted(boolean trusted) {
        this.trusted = trusted;
    }
}
