package com.zerosevenn.bentobox;
import com.zerosevenn.bentobox.commands.ChunkCommand;
import com.zerosevenn.bentobox.database.provider.MySQLContainer;
import com.zerosevenn.bentobox.database.repositories.ChunkDataRepository;
import com.zerosevenn.bentobox.database.repositories.GridRepository;
import com.zerosevenn.bentobox.database.repositories.IslandRepository;
import com.zerosevenn.bentobox.database.repositories.PlayerDataRepository;
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
        new ChunkDataRepository(this).createChunkDataTable();
        new GridRepository(this).createGridDataTable();
        new IslandRepository(this).createIslandTable();
        new PlayerDataRepository(this).createTable();
    }

    public void registerCommand(){
        getCommand("chunk").setExecutor(new ChunkCommand(this));
    }

    public void registerListeners(){

    }
}
