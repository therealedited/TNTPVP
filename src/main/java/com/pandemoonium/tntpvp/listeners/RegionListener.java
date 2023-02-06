package com.pandemoonium.tntpvp.listeners;

import com.pandemoonium.tntpvp.PlayerActions;
import com.pandemoonium.tntpvp.TntActions;
import com.pandemoonium.tntpvp.TntPvp;

import de.netzkronehd.wgregionevents.events.RegionEnteredEvent;

import de.netzkronehd.wgregionevents.events.RegionLeaveEvent;
import fr.mrmicky.fastboard.FastBoard;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class RegionListener implements Listener {
    TntPvp tntPvp;

    public RegionListener(TntPvp tntPvp) {
        this.tntPvp = tntPvp;
    }

    //TODO: Don't hardcode the region name.
    @EventHandler
    public void onRegionEntered(RegionEnteredEvent e) {
        //Are we in a playing field?
        if (e.getRegion().getId().equals("battlefield")) {
            e.getPlayer().sendMessage("You've joined the TNTPVP zone");
            FastBoard board = new FastBoard(e.getPlayer());
            board.updateTitle("Score");
            board.updateLines("No players for now :(");
            //Associate uuid to playername.
            TntPvp.players.put(e.getPlayer().getUniqueId(), e.getPlayer().getName());
            //Add the player's board to the global scoreboard list.
            TntPvp.scoreboards.put(e.getPlayer().getUniqueId(), board);
            if (TntActions.IsExplodeInstantly(e.getPlayer())) {
                PlayerActions.addItems(e.getPlayer());
            }
        }
    }

    //TODO: Don't hardcode the region name.
    @EventHandler
    public void onRegionLeave(RegionLeaveEvent e)
    {
        if (e.getRegion().getId().equals("battlefield")) {
            FastBoard board = TntPvp.scoreboards.remove(e.getPlayer().getUniqueId());
            if (board != null) {
                board.delete();
            }
            e.getPlayer().getInventory().clear();
            TntPvp.players.remove(e.getPlayer().getUniqueId());
            TntPvp.scoreList.remove(e.getPlayer().getUniqueId());
        }
    }
}
