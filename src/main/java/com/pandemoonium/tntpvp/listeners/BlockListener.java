package com.pandemoonium.tntpvp.listeners;

import com.pandemoonium.tntpvp.TntActions;
import com.pandemoonium.tntpvp.TntPvp;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.TNT;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockListener implements Listener {
    TntPvp tntPvp;

    public BlockListener(TntPvp tntPvp) {
        this.tntPvp = tntPvp;
    }
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e){
        if (e.getBlock().getBlockData() instanceof TNT) {
            if(TntActions.IsExplodeInstantly(e.getPlayer())) {
                if (e.getPlayer().getCooldown(Material.TNT) == 0) {
                    Block tntBlock = e.getBlock();
                    tntBlock.setType(Material.AIR);
                    Entity tnt = tntBlock.getWorld().spawn(tntBlock.getLocation(), TNTPrimed.class);
                    Bukkit.getLogger().warning(String.format("Adding {tntID=%s; playerID=%s", tnt.getUniqueId(), e.getPlayer().getUniqueId()));
                    TntPvp.tntOwnershipList.put(tnt.getUniqueId(), e.getPlayer().getUniqueId());
                    TntActions.explodeTntAtEntityLocation(5, tnt);
                    tnt.remove();
                    e.getPlayer().setCooldown(Material.TNT, 40);

                }
            }
        }
    }
}
