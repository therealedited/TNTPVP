package com.pandemoonium.tntpvp;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;

public class TntActions {


    public static void explodeTntAtEntityLocation(int tickDelay, Block block) {
        Bukkit.getScheduler().runTaskLater(TntPvp.getInstance(), () ->
                block.getWorld().createExplosion(block.getLocation(), 5L, false, false), tickDelay);
    }
}
