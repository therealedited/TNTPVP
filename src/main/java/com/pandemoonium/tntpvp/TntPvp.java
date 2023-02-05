package com.pandemoonium.tntpvp;

import com.pandemoonium.tntpvp.listeners.BlockListener;
import com.pandemoonium.tntpvp.listeners.DamageListener;
import com.pandemoonium.tntpvp.listeners.PlayerListener;
import com.pandemoonium.tntpvp.listeners.RegionListener;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;

import de.netzkronehd.wgregionevents.WgRegionEvents;

import fr.mrmicky.fastboard.FastBoard;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public final class TntPvp extends JavaPlugin implements CommandExecutor {

    public static StateFlag INSTANT_EXPLOSION_ZONE;

    public static HashMap<UUID, UUID> tntOwnershipList = new HashMap<>();
    public static Map<UUID, Integer> scoreList = new HashMap<>();
    public static Map<UUID, FastBoard> scoreboards = new HashMap<>();
    public static Map<UUID, String> players = new HashMap<>();

    @Override
    public void onLoad() {

        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {
            StateFlag L_flag = new StateFlag("instant-explosion-zone", false);
            registry.register(L_flag);
            INSTANT_EXPLOSION_ZONE = L_flag;
        } catch (FlagConflictException e) {
            Bukkit.getLogger().warning("Impossible to set instant-explosion-zone, flag already exists");
        }
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, String[] args) {
        String commandName = command.getName().toLowerCase();
        if (commandName.equals("tntpvp")) {
            if(args[0].equals("reset")) {
                scoreList = new HashMap<>();
            }
            if(args[0].equals("listplayers")) {
                Object[] allPlayers = players.values().toArray();
                sender.sendMessage(Arrays.copyOf(allPlayers, allPlayers.length, String[].class));
            }
            if(args[0].equals("listscores")) {
                StringBuilder scores = new StringBuilder();
                for (Map.Entry<UUID, Integer> mapEntry : TntPvp.scoreList.entrySet()) {
                    scores.append(String.format("%s - %s\n", players.get(mapEntry.getKey()), mapEntry.getValue()));
                }
                sender.sendMessage(String.valueOf(scores));
            }
        }
        return true;
    }
    @Override
    public void onEnable() {
        WorldGuardPlugin wgp = registerWorldGuard();
        if (wgp == null) {
            getLogger().warning("This plugin requires WorldGuard, disabling.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        WgRegionEvents wre = registerWorldGuardRegionEvents();
        if (wre == null) {
            getLogger().warning("This plugin requires WGRegionEvents, disabling.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        PluginCommand pluginCommand = this.getCommand("tntpvp");
        if (pluginCommand != null) {
            pluginCommand.setExecutor(this);
        }

        BlockListener blockListener = new BlockListener(this);
        RegionListener regionListener = new RegionListener(this);
        DamageListener damageListener = new DamageListener(this);
        PlayerListener playerListener = new PlayerListener(this);
        getServer().getPluginManager().registerEvents(regionListener, this);
        getServer().getPluginManager().registerEvents(playerListener, this);
        getServer().getPluginManager().registerEvents(blockListener, this);
        getServer().getPluginManager().registerEvents(damageListener, this);
    }

    public WorldGuardPlugin registerWorldGuard() {
        Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");

        if (!(plugin instanceof WorldGuardPlugin)) {
            return null;
        }

        return (WorldGuardPlugin) plugin;
    }

    public WgRegionEvents registerWorldGuardRegionEvents() {
        Plugin plugin = getServer().getPluginManager().getPlugin("WGRegionEvents");

        if (!(plugin instanceof WgRegionEvents)) {
            return null;
        }

        return (WgRegionEvents) plugin;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static void updateBoard(FastBoard board, String leading, Integer score) {
        Bukkit.getLogger().warning(leading);
        board.updateLines(
                "",
                "1st: " + leading + "- " + score);
    }
}
