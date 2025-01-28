package com.zerosevenn.bentobox;

import com.zerosevenn.bentobox.commands.ChunkCommand;
import com.zerosevenn.bentobox.config.GeneralConfiguration;
import com.zerosevenn.bentobox.config.IslandConfiguration;
import com.zerosevenn.bentobox.config.MessagesConfiguration;
import com.zerosevenn.bentobox.database.repositories.*;
import com.zerosevenn.bentobox.listeners.InventoryInteractListener;
import com.zerosevenn.bentobox.managers.EconomyManager;
import com.zerosevenn.bentobox.utils.ConfigUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class BentoBox extends JavaPlugin {

    private static BentoBox instance;

    private ChunkDataRepository chunkDataRepository;
    private IslandRepository islandRepository;
    private GridRepository gridRepository;
    private PlayerDataRepository playerDataRepository;
    private IslandMemberRepository islandMemberRepository;
    private GeneralConfiguration gConfig;
    private IslandConfiguration isConfig;
    private MessagesConfiguration messagesConfig;
    private World islandWord;

    @Override
    public void onEnable() {
        logInitializationStart();

        initializeConfigurations();

        this.islandWord = Bukkit.getWorld(getIsConfig().getConfiguration().getString("Islands-world"));

        if (this.islandWord == null) {
            getServer().getConsoleSender().sendMessage("§c[ERROR] The configured world does not exist or could not be loaded.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        initializeRepositories();
        initializeDatabaseTables();
        initializeCommands();
        initializeListeners();


        logInitializationEnd();
    }

    private void initializeConfigurations() {
        logInitializationStep("Loading configurations...");
        this.gConfig = new GeneralConfiguration(this);
        this.isConfig = new IslandConfiguration(this);
        new EconomyManager();
        ConfigUtils.initialize(this);
        logInitializationStep("Configurations loaded successfully.");
    }

    private void initializeRepositories() {
        logInitializationStep("Initializing repositories...");
        this.islandRepository = new IslandRepository(this);
        this.islandMemberRepository = new IslandMemberRepository(this);
        this.chunkDataRepository = new ChunkDataRepository(this);
        this.gridRepository = new GridRepository(this);
        this.playerDataRepository = new PlayerDataRepository(this);
        logInitializationStep("Repositories initialized successfully.");
    }

    private void initializeDatabaseTables() {
        logInitializationStep("Attemping to initialize data repositories...");
        gridRepository.createGridDataTable();
        islandRepository.createIslandTable();
        playerDataRepository.createTable();
        islandMemberRepository.createMemberTable();
    }

    private void initializeCommands() {
        logInitializationStep("Registering commands...");
        getCommand("chunk").setExecutor(new ChunkCommand(this));
        logInitializationStep("Commands registered successfully.");
    }

    private void initializeListeners() {
        logInitializationStep("Registering event listeners...");
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new InventoryInteractListener(this), this);
        logInitializationStep("Event listeners registered successfully.");
    }

    private void logInitializationStart() {
        getServer().getConsoleSender().sendMessage("§a===============================================");
        getServer().getConsoleSender().sendMessage("§a        BentoBox Plugin - Initialization       ");
        getServer().getConsoleSender().sendMessage("§a===============================================");
    }

    private void logInitializationEnd() {
        getServer().getConsoleSender().sendMessage("§a===============================================");
        getServer().getConsoleSender().sendMessage("§a BentoBox has been successfully initialized!   ");
        getServer().getConsoleSender().sendMessage("§a===============================================");
    }

    private void logInitializationStep(String message) {
        getServer().getConsoleSender().sendMessage("§a[INFO] " + message);
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage("§cBentoBox is shutting down...");
    }

    public PlayerDataRepository getPlayerDataRepository() {
        return playerDataRepository;
    }

    public GridRepository getGridRepository() {
        return gridRepository;
    }

    public IslandRepository getIslandRepository() {
        return islandRepository;
    }

    public ChunkDataRepository getChunkDataRepository() {
        return chunkDataRepository;
    }

    public IslandConfiguration getIsConfig() {
        return isConfig;
    }

    public GeneralConfiguration getgConfig() {
        return gConfig;
    }

    public World getIslandWord() {
        return islandWord;
    }

    public MessagesConfiguration getMessagesConfig() {
        return messagesConfig;
    }
}
