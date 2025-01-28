package com.zerosevenn.bentobox.utils;

import com.zerosevenn.bentobox.BentoBox;
import org.bukkit.configuration.file.FileConfiguration;
import java.util.List;

public class MsgConfigUtils {

    private static FileConfiguration config;

    public static void initialize(BentoBox plugin) {
        config = plugin.getConfig();
    }

    // Generic Messages
    public static String getCriticalServerErrorMessage() {
        return config.getString("Generics.Error.Critical Server Error", "&cAn error occurred.");
    }

    public static String getPlayerNotFoundMessage() {
        return config.getString("Generics.Error.Player Not Found", "&cPlayer not found.");
    }

    public static String getCommandSyntaxErrorMessage() {
        return config.getString("Generics.Error.Command Syntax Error", "&eTry &6/chunk help.");
    }

    public static String getCommandNoPermissionMessage() {
        return config.getString("Generics.Error.Command No Permission", "&cYou do not have permission.");
    }

    public static String getNoIslandErrorMessage() {
        return config.getString("Generics.Error.No Island Error", "&cYou do not have an island yet.");
    }

    public static String getInQueueMessage() {
        return config.getString("Generics.Error.In Queue", "&cYou already are in queue.");
    }

    public static String getTeleportMessage() {
        return config.getString("Generics.Misc.Teleport", "&7Teleporting...");
    }

    public static String getGeneratingIslandMessage() {
        return config.getString("Generics.Misc.Generating Island", "&eGenerating island...");
    }

    public static String getMissingIslandMessage() {
        return config.getString("Generics.Misc.Missing Island", "&cYour island may have been removed.");
    }

    // Help Command
    public static List<String> getHelpCommandDefault() {
        return config.getStringList("Help Command.Default");
    }

    public static List<String> getHelpCommandAdmin() {
        return config.getStringList("Help Command.Admin");
    }

    // Chunk Lock Messages
    public static String getChunkLockMessage() {
        return config.getString("Chunk Lock.Lock", "&cLocking island from visitors.");
    }

    public static String getChunkUnlockMessage() {
        return config.getString("Chunk Lock.Unlock", "&aUnlocking island for visitors.");
    }

    // Chunk Visit Messages
    public static String getChunkVisitLockedMessage() {
        return config.getString("Chunk Visit.Error.Locked", "&cThe island you tried to visit is locked.");
    }

    public static String getChunkVisitBannedMessage() {
        return config.getString("Chunk Visit.Error.Banned", "&cYou cannot visit this island.");
    }

    public static String getChunkVisitOwnIslandMessage() {
        return config.getString("Chunk Visit.Error.Own Island", "&cYou cannot visit your own island.");
    }

    // Chunk Kick Messages
    public static String getChunkKickExecutorMessage() {
        return config.getString("Chunk Kick.Executor", "&aSuccessfully kicked %player_name%.");
    }

    public static String getChunkKickRecipientMessage() {
        return config.getString("Chunk Kick.Recipient", "&eYou have been kicked from the island.");
    }

    public static String getChunkKickNoPermissionMessage() {
        return config.getString("Chunk Kick.Error.No Permission", "&cYou need to be owner of the island to do that!");
    }

    public static String getChunkKickVisitorNotFoundMessage() {
        return config.getString("Chunk Kick.Error.Visitor Not Found", "&cPlayer not found as visitor.");
    }
}
