package com.zerosevenn.bentobox.utils;

import com.zerosevenn.bentobox.BentoBox;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.List;

public class ConfigUtils {

    private static FileConfiguration config;

    public static void initialize(BentoBox plugin) {
        config = plugin.getIsConfig().getConfiguration();
    }

    // General settings
    public static String getIslandsWorld() {
        return config.getString("Generation-world", "gen-world");
    }

    public static int getMinDistance() {
        return config.getInt("min-distance", 100);
    }

    public static int getLandCost() {
        return config.getInt("land-cost", 1000);
    }

    public static int getIslandInviteExpiration() {
        return config.getInt("Island Invite Expiration", 120);
    }

    public static int getDefaultIslandBorderRadius() {
        return config.getInt("Default Island Border Radius", 3);
    }

    public static String getIslandExpansionCostFormula() {
        return config.getString("Island Expansion Cost Formula", "%chunkblock.chunks.owned%^1.4");
    }

    public static int getMaximumIslandNameLength() {
        return config.getInt("Maximum Island Name Length", 18);
    }

    // Island Removal Cooldown
    public static int getIslandRemovalCost() {
        return config.getInt("Island Removal Cooldown.Cost", 5);
    }

    public static int getIslandRemovalDays() {
        return config.getInt("Island Removal Cooldown.Time.Days", 1);
    }

    public static int getIslandRemovalHours() {
        return config.getInt("Island Removal Cooldown.Time.Hours", 2);
    }

    public static int getIslandRemovalMinutes() {
        return config.getInt("Island Removal Cooldown.Time.Minutes", 30);
    }

    // Island Panel Items
    public static ItemStack getCenterItem() {
        return getIslandPanelItem("Center Item");
    }

    public static ItemStack getBorderItem() {
        return getIslandPanelItem("Border Item");
    }

    public static ItemStack getCachingItem() {
        return getIslandPanelItem("Caching Item");
    }

    public static ItemStack getTooExpensiveItem() {
        return getIslandPanelItem("Too Expensive Item");
    }

    public static ItemStack getPlaceholderItem() {
        return getIslandPanelItem("Placeholder Item");
    }

    public static ItemStack getChunkStatusLoadingItem() {
        return getIslandPanelItem("Chunk Status Item.Loading");
    }

    public static ItemStack getChunkStatusLoadedItem() {
        return getIslandPanelItem("Chunk Status Item.Loaded");
    }

    public static ItemStack getChunkStatusUnlockedItem() {
        return getIslandPanelItem("Chunk Status Item.Unlocked");
    }

    public static ItemStack getChunkStatusLockedItem() {
        return getIslandPanelItem("Chunk Status Item.Locked");
    }

    private static ItemStack getIslandPanelItem(String path) {
        String title = config.getString("Island Panel." + path + ".Title", "");
        Material material = Material.getMaterial(config.getString("Island Panel." + path + ".Material", "STONE"));
        boolean enchanted = config.getBoolean("Island Panel." + path + ".Enchanted", false);
        List<String> lore = config.getStringList("Island Panel." + path + ".Lore");

        ItemStack item = new ItemStack(material != null ? material : Material.STONE);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(title);
            meta.setLore(lore);
            if (enchanted) {
                meta.addEnchant(Enchantment.UNBREAKING, 1, true);
            }
            item.setItemMeta(meta);
        }
        return item;
    }

    public static List<String> getIslandGenerationBiomes() {
        List<String> biomes = config.getStringList("Island Generation.Biomes");
        if (biomes == null || biomes.isEmpty()) {
            System.out.println("Biomes list is empty or invalid!");
        } else {
            System.out.println("Loaded biomes: " + biomes);
        }
        return biomes;
    }


    public static int getIslandGenerationDisplacement() {
        return config.getInt("Island Generation.Displacement", 15);
    }

    public static boolean isIslandGenerationFlipped() {
        return config.getBoolean("Island Generation.Animation.Flipped", false);
    }

    public static int getIslandGenerationDelay() {
        return config.getInt("Island Generation.Animation.Delay", 100);
    }

    public static int getIslandGenerationBlocksPerIteration() {
        return config.getInt("Island Generation.Animation.Blocks Per Iteration", 256);
    }
}
