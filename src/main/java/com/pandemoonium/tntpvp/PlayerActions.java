package com.pandemoonium.tntpvp;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerActions {

    public static void addItems(Player p) {
        Player player = p.getPlayer();
        if (player != null) {
            ItemStack tntStack = new ItemStack(Material.TNT, 64);
            ItemStack stoneStack = new ItemStack(Material.STONE, 64);
            ItemStack porkStack = new ItemStack(Material.COOKED_PORKCHOP, 64);
            ItemStack stick = new ItemStack(Material.STICK, 1);
            ItemStack pickaxe = new ItemStack(Material.DIAMOND_PICKAXE, 1);
            ItemStack compass = new ItemStack(Material.COMPASS, 1);
            player.getInventory().addItem(tntStack, stoneStack, porkStack, stick, pickaxe, compass);
        }
    }
}
