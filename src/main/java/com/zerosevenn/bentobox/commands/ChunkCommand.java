package com.zerosevenn.bentobox.commands;

import com.zerosevenn.bentobox.managers.WorldManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class ChunkCommand implements CommandExecutor {
    private JavaPlugin plugin;
    public WorldManager worldManager;

    public ChunkCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        Player player = (Player) commandSender;

        if(!player.hasPermission("bentobox.chunk")) {
            return false;
        }

        if(!player.isOp()){
            return false;
        }

        if (strings.length == 0) {
            worldManager = new WorldManager(plugin);
            worldManager.generatePlayerChunkWithAnimation(player);

        }

        return false;
    }
}
