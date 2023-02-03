package com.pandemoonium.tntpvp.listeners;

import com.pandemoonium.tntpvp.TntActions;
import com.pandemoonium.tntpvp.TntPvp;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.RegionContainer;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.TNT;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Map;
import java.util.UUID;

public class BlockListener implements Listener {
    TntPvp tntPvp;

    public BlockListener(TntPvp tntPvp) {
        this.tntPvp = tntPvp;
    }
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e){
        if (e.getBlock().getBlockData() instanceof TNT) {
            if(IsExplodeInstantly(e.getPlayer())) {
                if (e.getPlayer().getCooldown(Material.TNT) == 0) {
                    Block tntBlock = e.getBlock();
                    tntBlock.setType(Material.AIR);
                    Entity tnt = tntBlock.getWorld().spawn(tntBlock.getLocation(), TNTPrimed.class);
                    TntActions.explodeTntAtEntityLocation(5, (Block)tnt);
                    TntPvp.tntOwnershipList.put(tnt.getUniqueId(), e.getPlayer().getUniqueId());
                    e.getPlayer().setCooldown(Material.TNT, 40);
                }
            }
        }
    }

    //The player do not take fall damage and explosion damage if the damage was from their own TNT.
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e){
        if (e.getDamager() instanceof Player) {
            if (IsExplodeInstantly((Player)e.getDamager())) {
                for (Map.Entry<UUID, UUID> mapEntry : TntPvp.tntOwnershipList.entrySet()) {
                    if (mapEntry.getValue().equals(e.getDamager().getUniqueId())) {
                        if (mapEntry.getKey() == e.getEntity().getUniqueId()) {
                            if (e.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION
                            || (e.getCause() == EntityDamageEvent.DamageCause.FALL)) {
                                e.setCancelled(true);
                                TntPvp.tntOwnershipList.remove(e.getDamager().getUniqueId());
                            }
                        }
                    }
                }
            }
        }
    }



    public boolean IsExplodeInstantly(Player player) {
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regionManager = container.get(BukkitAdapter.adapt(player.getWorld()));
        if (regionManager == null) return false;

        ApplicableRegionSet set = regionManager.getApplicableRegions(BlockVector3.at(
                player.getLocation().x(),
                player.getLocation().y(),
                player.getLocation().z()));

        return set.testState(null, TntPvp.INSTANT_EXPLOSION_ZONE);
    }


}
