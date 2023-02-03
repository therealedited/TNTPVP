package com.pandemoonium.tntpvp.listeners;

import com.pandemoonium.tntpvp.TntPvp;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

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
            Bukkit.getLogger().warning(TntPvp.tntOwnershipList.entrySet().toString());
            for (Map.Entry<UUID, UUID> mapEntry : TntPvp.tntOwnershipList.entrySet()) {
                if (mapEntry.getKey().equals(e.getDamager().getUniqueId())) {
                    if (mapEntry.getValue() == e.getEntity().getUniqueId()) {
                        Bukkit.getLogger().warning("Negated player self damage");
                        if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
                            TntPvp.tntOwnershipList.remove(e.getDamager().getUniqueId());
                        }
                        e.setCancelled(true);
                    }
                }
            }
        }
    }
}
