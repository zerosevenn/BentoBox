package com.zerosevenn.bentobox.views;

import com.zerosevenn.bentobox.BentoBox;
import com.zerosevenn.bentobox.services.ChunkService;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class ChunkMapView {

    private static final int GUI_SIZE = 9 * 5;
    private static final int CENTER_COL = 4;
    private static final int CENTER_ROW = 2;

    private final BentoBox plugin;
    private final ChunkService chunkService;

    public ChunkMapView(BentoBox plugin) {
        this.plugin = plugin;
        this.chunkService = new ChunkService(plugin);
    }

    public Inventory createChunkMap(Player player) {
        int blockX = player.getLocation().getBlockX();
        int blockZ = player.getLocation().getBlockZ();
        int centerX = blockX >> 4;
        int centerZ = blockZ >> 4;
        Inventory gui = Bukkit.createInventory(null, GUI_SIZE, "Kraftersi - Panel Island");
        for (int row = -CENTER_ROW; row <= CENTER_ROW; row++) {
            for (int col = -CENTER_COL; col <= CENTER_COL; col++) {
                int slot = (row + CENTER_ROW) * 9 + (col + CENTER_COL);
                int chunkX = centerX + col;
                int chunkZ = centerZ + row;
                ItemStack item = getChunkRepresentation(player, chunkX, chunkZ);
                gui.setItem(slot, item);
            }
        }
        setBorderDecoration(gui);
        gui.setItem(10, createItem(Material.GLOWSTONE_DUST, "Chunk Map"));
        gui.setItem(22, createItem(Material.ENDER_PEARL, "Teleport to Current Chunk"));
        gui.setItem(28, createItem(Material.BARRIER, "Close"));
        return gui;
    }

    private ItemStack getChunkRepresentation(Player player, int chunkX, int chunkZ) {
        int blockX = player.getLocation().getBlockX();
        int blockZ = player.getLocation().getBlockZ();
        int playerChunkX = blockX >> 4;
        int playerChunkZ = blockZ >> 4;
        chunkX = chunkX*16;
        chunkZ = chunkZ*16;
        if (chunkX == playerChunkX && chunkZ == playerChunkZ) {
            return createItem(Material.BLUE_STAINED_GLASS_PANE, ChatColor.GREEN + "Você está aqui (" + chunkX + ", " + chunkZ + ")");
        }
        boolean isUnlocked = plugin.getChunkDataRepository().isChunkUnlocked(chunkService.getWorld(), chunkX, chunkZ);
        Material mat = isUnlocked ? Material.PURPLE_STAINED_GLASS_PANE : Material.BLACK_STAINED_GLASS_PANE;
        String displayName = ChatColor.YELLOW + "Chunk (" + chunkX + ", " + chunkZ + ")";
        ItemStack item = createItem(mat, displayName);
        ItemMeta meta = item.getItemMeta();
        meta.setLore(Arrays.asList(ChatColor.GRAY + "$1000", "", ChatColor.WHITE + "Clique para comprar"));
        item.setItemMeta(meta);
        return item;
    }

    private void setBorderDecoration(Inventory gui) {
        ItemStack bar = createItem(Material.IRON_BARS, " ");
        for (int col = 0; col < 9; col++) {
            gui.setItem(col, bar);
            gui.setItem(36 + col, bar);
        }
        for (int row = 0; row < 5; row++) {
            gui.setItem(row * 9, bar);
            gui.setItem(row * 9 + 8, bar);
        }
    }

    private ItemStack createItem(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }
}
