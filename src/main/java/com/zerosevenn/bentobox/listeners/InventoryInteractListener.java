package com.zerosevenn.bentobox.listeners;

import com.zerosevenn.bentobox.BentoBox;
import com.zerosevenn.bentobox.managers.ChunkUnlockManager;
import com.zerosevenn.bentobox.managers.WorldManager;
import com.zerosevenn.bentobox.services.ChunkService;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryInteractListener implements Listener {

    private static final int CENTER_COL = 4;
    private static final int CENTER_ROW = 2;

    private final BentoBox plugin;
    private final ChunkService chunkService;
    private final WorldManager worldManager;

    public InventoryInteractListener(BentoBox plugin) {
        this.plugin = plugin;
        this.chunkService = new ChunkService(plugin);
        this.worldManager = new WorldManager(plugin, new ChunkUnlockManager(plugin));
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!e.getView().getTitle().equalsIgnoreCase("Kraftersi - Panel Island")) return;
        e.setCancelled(true);
        if (e.getClickedInventory() == null || !e.getClickedInventory().equals(e.getView().getTopInventory())) return;

        Player player = (Player) e.getWhoClicked();
        int clickedSlot = e.getSlot();

        int centerX = player.getLocation().getChunk().getX();
        int centerZ = player.getLocation().getChunk().getZ();

        int rowOffset = (clickedSlot / 9) - CENTER_ROW;
        int colOffset = (clickedSlot % 9) - CENTER_COL;

        int targetChunkX = centerX + colOffset;
        int targetChunkZ = centerZ + rowOffset;

        if (clickedSlot == 22) {
            if(isChunkUnlocked(centerX, centerZ)) {
                teleportPlayerToChunk(player, centerX, centerZ);
            }
            return;
        }

        if (isChunkUnlocked(targetChunkX, targetChunkZ)) {
            teleportPlayerToChunk(player, targetChunkX, targetChunkZ);
            return;
        }

        boolean canUnlock = false;
        boolean isDiagonalUnlock = false;

        int[][] adjacentOffsets = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1},  // Ortogonais
                {-1, -1}, {-1, 1}, {1, -1}, {1, 1}  // Diagonais
        };

        for (int[] offset : adjacentOffsets) {
            int adjacentChunkX = targetChunkX + offset[0];
            int adjacentChunkZ = targetChunkZ + offset[1];

            if (isChunkUnlocked(adjacentChunkX, adjacentChunkZ)) {
                canUnlock = true;
                if (Math.abs(offset[0]) == 1 && Math.abs(offset[1]) == 1) {
                    isDiagonalUnlock = true;
                } else {
                    isDiagonalUnlock = false;
                }
            }
        }

        if (canUnlock) {
            if (isDiagonalUnlock) {
                unlockDiagonalChunk(player, targetChunkX, targetChunkZ);
            } else {
                unlockChunk(player, targetChunkX, targetChunkZ);
            }
        } else {
            player.sendMessage(ChatColor.RED + "You can only unlock adjacent chunks.");
        }
    }

    private boolean isChunkUnlocked(int chunkX, int chunkZ) {
        return plugin.getChunkDataRepository().isChunkUnlocked(chunkService.getWorld(), chunkX << 4, chunkZ << 4);
    }

    private void unlockChunk(Player player, int chunkX, int chunkZ) {
        Location location = new Location(plugin.getIslandWord(), chunkX << 4, player.getLocation().getY(), chunkZ << 4);
        chunkService.unlockChunk(player, location, plugin.getIslandRepository().readIsland(player.getUniqueId().toString()).getId());
        worldManager.generateChunk(player, location);
    }

    private void unlockDiagonalChunk(Player player, int chunkX, int chunkZ) {
        Location location = new Location(plugin.getIslandWord(), chunkX << 4, player.getLocation().getY(), chunkZ << 4);
        chunkService.unlockChunk(player, location, plugin.getIslandRepository().readIsland(player.getUniqueId().toString()).getId());
        player.sendMessage(ChatColor.YELLOW + "Diagonal chunk unlocked with different visuals.");
        worldManager.generateChunk(player, location);
    }

    private void teleportPlayerToChunk(Player player, int chunkX, int chunkZ) {
        World world = chunkService.getWorld();
        int bx = (chunkX << 4) + 8;
        int bz = (chunkZ << 4) + 8;
        int by = world.getHighestBlockYAt(bx, bz);
        player.teleport(new Location(world, bx + 0.5, by, bz + 0.5));
    }
}
