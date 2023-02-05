package com.pandemoonium.tntpvp.listeners;

import com.pandemoonium.tntpvp.TntActions;
import com.pandemoonium.tntpvp.TntPvp;
import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PlayerListener implements Listener {

    TntPvp tntPvp;

    public PlayerListener(TntPvp tntPvp) {
        this.tntPvp = tntPvp;
    }


    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        //Did the death happen in the playing field?
        if (TntActions.IsExplodeInstantly(event.getPlayer())) {
            String leadingPlayer = "";
            Integer playerScore = -1;
            //Drop nothing
            ArrayList<ItemStack> itemsToDrop = new ArrayList<>();
            List<ItemStack> itemsAboutToGetDropped = event.getDrops();
            itemsAboutToGetDropped.retainAll(itemsToDrop);

            //Remove everything else
            event.getPlayer().getInventory().clear();

            //Get the leading player
            for (Map.Entry<UUID, Integer> mapEntry : TntPvp.scoreList.entrySet()) {
                if (mapEntry.getValue() > playerScore) {
                    leadingPlayer = TntPvp.players.get(mapEntry.getKey());
                }
            }

            //Update every player's scoreboard
            for (Map.Entry<UUID, FastBoard> mapEntry : TntPvp.scoreboards.entrySet()) {
                TntPvp.updateBoard(mapEntry.getValue(), leadingPlayer);
            }
        }
    }
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (TntActions.IsExplodeInstantly(event.getPlayer())) {
            Player player = event.getPlayer();
            ItemStack tntStack = new ItemStack(Material.TNT, 64);
            ItemStack stoneStack = new ItemStack(Material.STONE, 64);
            ItemStack porkStack = new ItemStack(Material.COOKED_PORKCHOP, 64);
            ItemStack stick = new ItemStack(Material.STICK, 1);
            player.getInventory().addItem(tntStack, stoneStack, porkStack, stick);
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (TntActions.IsExplodeInstantly(event.getPlayer())) {
            Player p = event.getPlayer();

            if (event.getItemDrop().getItemStack().getType() == Material.STICK) {
                event.setCancelled(true);
                p.sendMessage(ChatColor.RED + "You can't drop a token.");
            }
        }
    }
}
