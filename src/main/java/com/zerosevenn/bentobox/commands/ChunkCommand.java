package com.zerosevenn.bentobox.commands;

import com.zerosevenn.bentobox.BentoBox;
import com.zerosevenn.bentobox.managers.ChunkUnlockManager;
import com.zerosevenn.bentobox.managers.WorldManager;
import com.zerosevenn.bentobox.models.IslandModel;
import com.zerosevenn.bentobox.models.PlayerDataModel;
import com.zerosevenn.bentobox.services.ChunkService;
import com.zerosevenn.bentobox.utils.LocationUtils;
import com.zerosevenn.bentobox.views.ChunkMapView;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ChunkCommand implements CommandExecutor {
    private BentoBox plugin;
    private WorldManager worldManager;
    private final ChunkService chunkService;


    public ChunkCommand(JavaPlugin plugin) {
        this.plugin = (BentoBox) plugin;
        this.chunkService = new ChunkService(plugin);
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
            if (plugin.getPlayerDataRepository().readPlayer(player.getUniqueId().toString()) == null) {
                int distance = plugin.getIsConfig().getConfiguration().getInt("min-distance");

                plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
                    try {

                        Location location = LocationUtils.generateRandomLocation(plugin.getIslandWord(), distance, plugin.getGridRepository());

                        plugin.getServer().getScheduler().runTask(plugin, () -> {
                            try {
                                World world = location.getWorld();
                                if (!world.isChunkLoaded(location.getBlockX() >> 4, location.getBlockZ() >> 4)) {
                                    world.loadChunk(location.getBlockX() >> 4, location.getBlockZ() >> 4);
                                }

                                int y = world.getHighestBlockYAt(location.getBlockX(), location.getBlockZ()) + 1;
                                location.setY(y);

                                UUID uuid = player.getUniqueId();

                                IslandModel island = new IslandModel(uuid, location.getBlockX(), location.getBlockZ(), 1, "empty");
                                plugin.getIslandRepository().updateIsland(island);

                                PlayerDataModel playerDataModel = new PlayerDataModel(uuid, 1, 1);
                                plugin.getPlayerDataRepository().updatePlayer(playerDataModel);

                                chunkService.unlockChunk(player, location, island.getId());

                                worldManager = new WorldManager(plugin, new ChunkUnlockManager(plugin));
                                worldManager.generateChunk(player, location);

                                player.teleport(location);
                                player.sendMessage(ChatColor.GREEN + "Island created successfully!");
                            } catch (Exception e) {
                                plugin.getLogger().severe("Error generating chunk or teleporting player: " + e.getMessage());
                                e.printStackTrace();
                            }
                        });
                    } catch (Exception e) {
                        plugin.getLogger().severe("Error processing island creation: " + e.getMessage());
                        e.printStackTrace();
                    }
                });
                return true;
            }

            ChunkMapView chunkMapView = new ChunkMapView(plugin);
            player.openInventory(chunkMapView.createChunkMap(player));

        }



        return false;
    }
}
