package com.pandemoonium.tntpvp;

import com.pandemoonium.tntpvp.listeners.BlockListener;
import com.pandemoonium.tntpvp.listeners.DamageListener;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;


public final class TntPvp extends JavaPlugin {

    private static TntPvp instance;
    public static StateFlag INSTANT_EXPLOSION_ZONE;

    public static HashMap<UUID, UUID> tntOwnershipList = new HashMap<>();
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
    public void onEnable() {
        instance = this;
        WorldGuardPlugin wgp = registerWorldGuard();
        if (wgp == null) {
            getLogger().warning("This plugin requires WorldGuard, disabling.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        BlockListener blockListener = new BlockListener(this);
        DamageListener damageListener = new DamageListener(this);
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

    public static TntPvp getInstance() {
        return instance;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
