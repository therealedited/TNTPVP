package com.pandemoonium.tntpvp.listeners;

import com.pandemoonium.tntpvp.TntPvp;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
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
            for (Map.Entry<UUID, UUID> mapEntry : TntPvp.tntOwnershipList.entrySet()) {
                if (mapEntry.getKey().equals(e.getDamager().getUniqueId())) {
                    if (mapEntry.getValue() == e.getEntity().getUniqueId()) {
                        Player player = (Player) e.getEntity();
                        TNTPrimed tnt = (TNTPrimed) e.getDamager();
                        Vector kb = tnt.getLocation().toVector().subtract(player.getLocation().toVector());
                        player.setVelocity(kb.multiply(-2));
                        e.setCancelled(true);
                    }
                }
            }
        }
    }
}
