package net.euphalys.eufac.listener.player;

import net.euphalys.eufac.EuFac;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * @author Dinnerwolph
 */
public class Join implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        EuFac.getInstance().bar.addPlayer(event.getPlayer());
    }
}
