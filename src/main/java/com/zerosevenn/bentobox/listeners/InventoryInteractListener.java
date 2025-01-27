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

        if (clickedSlot == 22) {
            teleportPlayerToChunk(player, centerX, centerZ);
            return;
        }

        int row = (clickedSlot / 9) - CENTER_ROW;
        int col = (clickedSlot % 9) - CENTER_COL;
        int chunkX = centerX + col;
        int chunkZ = centerZ + row;

        if (plugin.getChunkDataRepository().isChunkUnlocked(chunkService.getWorld(), chunkX, chunkZ)) {
            teleportPlayerToChunk(player, chunkX, chunkZ);
        } else {
            Location location = new Location(plugin.getIslandWord(), chunkX << 4, player.getLocation().getY(), chunkZ << 4);
            chunkService.unlockChunk(player, location, plugin.getIslandRepository().readIsland(player.getUniqueId().toString()).getId());
            ItemStack is = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(ChatColor.YELLOW + "Generating...");
            is.setItemMeta(im);
            e.getClickedInventory().setItem(e.getRawSlot(), is);
            player.updateInventory();
            worldManager.generateChunk(player, location);
        }
    }

    private void teleportPlayerToChunk(Player player, int chunkX, int chunkZ) {
        World world = chunkService.getWorld();
        int bx = (chunkX << 4) + 8;
        int bz = (chunkZ << 4) + 8;
        int by = world.getHighestBlockYAt(bx, bz);
        player.teleport(new Location(world, bx + 0.5, by, bz + 0.5));
        player.sendMessage(ChatColor.GREEN + "Teleported to chunk (" + chunkX + ", " + chunkZ + ").");
    }
}
