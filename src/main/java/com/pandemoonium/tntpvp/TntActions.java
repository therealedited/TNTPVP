package com.pandemoonium.tntpvp;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.RegionContainer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;

public class TntActions {

    public static void explodeTntAtEntityLocation(int tickDelay, Entity e) {
        Bukkit.getScheduler().runTaskLater(TntPvp.getInstance(), () ->
                e.getWorld().createExplosion(e.getLocation(), 5L, false, false), tickDelay);
    }

    public static boolean IsExplodeInstantly(Entity entity) {
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regionManager = container.get(BukkitAdapter.adapt(entity.getWorld()));
        if (regionManager == null) return false;

        ApplicableRegionSet set = regionManager.getApplicableRegions(BlockVector3.at(
                entity.getLocation().x(),
                entity.getLocation().y(),
                entity.getLocation().z()));

        return set.testState(null, TntPvp.INSTANT_EXPLOSION_ZONE);
    }
}
