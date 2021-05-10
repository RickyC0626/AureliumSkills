package com.archyx.aureliumskills.commands;

import com.archyx.aureliumskills.AureliumSkills;
import com.archyx.aureliumskills.lang.CommandMessage;
import com.archyx.aureliumskills.lang.Lang;
import com.archyx.aureliumskills.rewards.RewardException;
import com.archyx.aureliumskills.stats.Luck;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;

public class ReloadManager {

    private final AureliumSkills plugin;
    private final Lang lang;

    public ReloadManager(AureliumSkills plugin) {
        this.plugin = plugin;
        this.lang = plugin.getLang();
    }

    public void reload(CommandSender sender) {
        Locale locale = plugin.getLang().getLocale(sender);
        // Load config
        plugin.reloadConfig();
        plugin.saveDefaultConfig();
        plugin.getOptionLoader().loadOptions();
        plugin.getRequirementManager().load();
        // Load sources_config
        plugin.getSourceManager().loadSources();
        plugin.getCheckBlockReplace().reloadCustomBlocks();
        // Load rewards
        try {
            plugin.getRewardManager().loadRewards();
        } catch (FileNotFoundException e) {
            plugin.getLogger().warning(" Error while loading rewards: " + e.getMessage());
        } catch (RewardException e) {
            plugin.getLogger().warning("Error while loading rewards file " + e.getFileName() + " at path " + e.getPath() + ": " + e.getMessage());
        }
        // Load language files
        lang.loadLanguageFiles();
        lang.loadEmbeddedMessages(plugin.getCommandManager());
        lang.loadLanguages(plugin.getCommandManager());
        // Load menus
        try {
            plugin.getMenuLoader().load();
        }
        catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
            if (sender instanceof Player) {
                sender.sendMessage(ChatColor.YELLOW + "[AureliumSkills] Error while loading menus, see console for details");
            } else {
                Bukkit.getLogger().warning("[AureliumSkills] Error while loading menus!");
            }
        }
        // Load ability_config
        plugin.getAbilityManager().loadOptions();

        plugin.getLeveler().loadLevelRequirements();
        // Load loot tables
        plugin.getLootTableManager().loadLootTables();
        // Load worlds and regions
        plugin.getWorldManager().loadWorlds();
        if (plugin.isWorldGuardEnabled()) {
            plugin.getWorldGuardSupport().loadRegions();
        }
        // Recalculate health and luck stats
        Luck luck = new Luck(plugin);
        for (Player player : Bukkit.getOnlinePlayers()) {
            plugin.getHealth().reload(player);
            luck.reload(player);
        }
        // Resets all action bars
        plugin.getActionBar().resetActionBars();
        // Load boss bars
        plugin.getBossBar().loadOptions();
        sender.sendMessage(AureliumSkills.getPrefix(locale) + ChatColor.GREEN + Lang.getMessage(CommandMessage.RELOAD, locale));
    }

}