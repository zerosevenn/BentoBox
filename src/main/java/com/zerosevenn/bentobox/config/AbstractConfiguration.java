package com.zerosevenn.bentobox.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

public abstract class AbstractConfiguration {

    private File configFile;
    private FileConfiguration configuration;
    private YamlConfiguration defaultConfig;

    public AbstractConfiguration(JavaPlugin instance, String file) {
        this.configFile = new File(instance.getDataFolder(), file + ".yml");
        File parentDir = configFile.getParentFile();

        if (!parentDir.exists() && !parentDir.mkdirs()) {
            throw new IllegalStateException("Failed to create directory: " + parentDir.getPath());
        }

            if (!configFile.exists()) {
                if (instance.getResource(file + ".yml") == null) {
                    throw new IllegalStateException("Resource file not found in plugin JAR: " + file + ".yml");
                }
                try {
                    instance.saveResource(file + ".yml", false);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    throw new IllegalStateException("Error saving resource: " + file + ".yml");
                }
            }


            try (InputStreamReader reader = new InputStreamReader(instance.getResource(file + ".yml"), StandardCharsets.UTF_8)) {
            this.defaultConfig = YamlConfiguration.loadConfiguration(reader);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load default configuration: " + file + ".yml", e);
        }

        this.configuration = YamlConfiguration.loadConfiguration(configFile);
        configuration.setDefaults(defaultConfig);
        saveDefaultConfig();
    }

    public void saveDefaultConfig() {
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                throw new IllegalStateException("Failed to create configuration file: " + configFile.getPath(), e);
            }
        }
        configuration.options().copyDefaults(true);
        try {
            configuration.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("Failed to save configuration file: " + configFile.getPath(), e);
        }
    }

    public String getMessage(String path, Player player) {
        String message = configuration.getString(path, "");
        return message
                .replace("&", "§")
                .replace("{player}", player.getName())
                .replace("{tag}", configuration.getString("Config.tag", "").replace("&", "§"));
    }

    public FileConfiguration getConfiguration() {
        return configuration;
    }

    public File getConfigFile() {
        return configFile;
    }

    public YamlConfiguration getDefaultConfig() {
        return defaultConfig;
    }
}
