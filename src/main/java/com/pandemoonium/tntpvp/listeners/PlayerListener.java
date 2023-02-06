package com.pandemoonium.tntpvp.listeners;

import com.pandemoonium.tntpvp.PlayerActions;
import com.pandemoonium.tntpvp.TntActions;
import com.pandemoonium.tntpvp.TntPvp;

import fr.mrmicky.fastboard.FastBoard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
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


    //TODO: Recalculate score here too.
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        //Did the death happen in the playing field?
        if (TntActions.IsExplodeInstantly(event.getPlayer())) {
            //Only drop sticks
            ArrayList<ItemStack> itemsToDrop = new ArrayList<>();

            for (ItemStack item: event.getPlayer().getInventory()) {
                if (item == null) continue;
                if (item.getType() == Material.STICK) {
                    itemsToDrop.add(item);
                }
            }
            List<ItemStack> itemsAboutToGetDropped = event.getDrops();
            itemsAboutToGetDropped.retainAll(itemsToDrop);

            //Remove everything else
            event.getPlayer().getInventory().clear();
        }
    }
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (TntActions.IsExplodeInstantly(player)) {
            PlayerActions.addItems(player);
        }
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerAttemptPickupItemEvent event) {
        if (TntActions.IsExplodeInstantly(event.getPlayer())) {
            String leadingPlayerName = "";
            Integer playerScore = -1;
            UUID leadingPlayerUUID = null;
            //The person who picked up the sticks gets points.
            Player pickupPlayer = event.getPlayer();
            Integer numberOfSticks = 0;
            //Count all of their sticks stored in their inventory.
            for (ItemStack item : pickupPlayer.getInventory()) {
                if (item == null) continue;
                if (item.getType() == Material.STICK) {
                    numberOfSticks += item.getAmount();
                }
            }
            //Count how many sticks they gathered.
            if (event.getItem().getItemStack().getType() == Material.STICK) {
                numberOfSticks += event.getItem().getItemStack().getAmount();
                TntPvp.scoreList.put(pickupPlayer.getUniqueId(), numberOfSticks);

                //Get the leading player
                for (Map.Entry<UUID, Integer> mapEntry : TntPvp.scoreList.entrySet()) {
                    if (mapEntry.getValue() > playerScore) {
                        leadingPlayerUUID = mapEntry.getKey();
                        leadingPlayerName = TntPvp.players.get(mapEntry.getKey());
                        playerScore = mapEntry.getValue();
                    }
                }

                //We set in leading player in the game manager.
                if (leadingPlayerUUID != null) {
                    TntPvp.leadingPlayer = Bukkit.getPlayer(leadingPlayerUUID);
                }



                //Update every player's scoreboard
                for (Map.Entry<UUID, FastBoard> mapEntry : TntPvp.scoreboards.entrySet()) {
                    TntPvp.updateBoard(mapEntry.getValue(), leadingPlayerName, playerScore);
                }
            }
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

    @EventHandler
    public void onRightClick(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if (TntActions.IsExplodeInstantly(player)) {
            if (TntPvp.leadingPlayer == null) {
                player.sendMessage("There is no currently winning player.");
                return;
            }
            if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
                    && player.getInventory().getItemInMainHand().getType() == Material.COMPASS) {
                player.setCompassTarget(TntPvp.leadingPlayer.getLocation());
                player.sendMessage(String.format("Targeted %s", TntPvp.leadingPlayer.getName()));
            }
        }

    }
}
