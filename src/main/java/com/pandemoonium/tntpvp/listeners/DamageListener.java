package com.pandemoonium.tntpvp.listeners;

import com.pandemoonium.tntpvp.TntPvp;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Map;
import java.util.UUID;

public class DamageListener implements Listener {

    TntPvp tntPvp;

    public DamageListener(TntPvp tntPvp) {
        this.tntPvp = tntPvp;
    }
    //The player do not take fall damage and explosion damage if the damage was from their own TNT.
    //Damager = tnt
    //Entity = player
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e){
        if(e.getEntity() instanceof Player
                && e.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) {
            //Iterate through the entire tnt ownership map
            for (Map.Entry<UUID, UUID> mapEntry : TntPvp.tntOwnershipList.entrySet()) {
                //The current damage was done by a tnt owned by a player.
                if (mapEntry.getKey().equals(e.getDamager().getUniqueId())) {
                    //The damaged is another player.
                    if (mapEntry.getValue() != e.getEntity().getUniqueId()) {
                        Player otherPlayer = (Player) e.getEntity();
                        //Are they dead?
                        if (otherPlayer.getHealth() == 0D) {
                            String killerName = TntPvp.players.get(mapEntry.getValue());
                            otherPlayer.sendMessage(String.format("You were blown up by %s", killerName));
                            //Increment the killer's score.
                            Integer numberOfSticks = 0;
                            for(ItemStack item : otherPlayer.getInventory()) {
                                if (item.getType() == Material.STICK) {
                                    numberOfSticks += item.getAmount();
                                }
                            }
                            //We get the killer and add the sticks to their inventory.
                            Player killer = Bukkit.getPlayer(mapEntry.getValue());
                            if (killer != null) {
                                killer.getInventory().addItem(new ItemStack(Material.STICK, numberOfSticks));
                                TntPvp.scoreList.put(mapEntry.getValue(), numberOfSticks);
                            }
                        }
                    }
                    //The damaged is the player that placed the tnt.
                    if (mapEntry.getValue() == e.getEntity().getUniqueId()) {
                        Player player = (Player) e.getEntity();
                        TNTPrimed tnt = (TNTPrimed) e.getDamager();
                        Vector kb = tnt.getLocation().toVector().subtract(player.getLocation().toVector());
                        //Push the player that detonated the tnt away from the other player.
                        player.setVelocity(kb.multiply(-2));
                        //Negate self damage.
                        e.setCancelled(true);
                    }
                }
            }
        }
    }
}
