package com.pandemoonium.tntpvp.listeners;

import com.pandemoonium.tntpvp.TntActions;
import com.pandemoonium.tntpvp.TntPvp;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.TNT;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class BlockListener implements Listener {
    TntPvp tntPvp;

    public BlockListener(TntPvp tntPvp) {
        this.tntPvp = tntPvp;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if (e.getBlock().getBlockData() instanceof TNT) {
            if (TntActions.IsExplodeInstantly(e.getPlayer())) {
                if (e.getPlayer().getCooldown(Material.TNT) == 0) {
                    Block tntBlock = e.getBlock();
                    tntBlock.setType(Material.AIR);
                    TNTPrimed tnt = tntBlock.getWorld().spawn(tntBlock.getLocation(), TNTPrimed.class);
                    TntPvp.tntOwnershipList.put(tnt.getUniqueId(), e.getPlayer().getUniqueId());
                    tnt.setFuseTicks(5);
                    e.getPlayer().setCooldown(Material.TNT, 40);
                }
            }
        }
    }

    @EventHandler
    public final void entityDamage(EntityDamageEvent e) {
        if (TntActions.IsExplodeInstantly(e.getEntity())) {
            if (e.getEntity().getType() == EntityType.DROPPED_ITEM) {
                e.setCancelled(true);
            }
        }
    }
    @EventHandler
    public final void explodeEvent(EntityExplodeEvent e) {
        if (TntActions.IsExplodeInstantly(e.getEntity())) {
            Entity entity = e.getEntity();
            if (entity instanceof TNTPrimed) {
                e.blockList().clear();
            }
        }
    }
}
