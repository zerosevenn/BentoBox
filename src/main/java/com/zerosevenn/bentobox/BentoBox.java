package com.zerosevenn.bentobox;

import com.zerosevenn.bentobox.commands.ChunkCommand;
import com.zerosevenn.bentobox.database.provider.MySQLProvider;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class BentoBox extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        up();
        registerCommand();
        registerListeners();
    }

    @Override
    public void onDisable() {

    }

    public void up(){

    }

    public void registerCommand(){
        getCommand("chunk").setExecutor(new ChunkCommand(this));
    }

    public void registerListeners(){

    }
}
