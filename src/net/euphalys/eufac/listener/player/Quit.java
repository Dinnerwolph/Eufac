package net.euphalys.eufac.listener.player;

import net.euphalys.eufac.EuFac;
import net.euphalys.eufac.tasks.AFKTask;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author Dinnerwolph
 */
public class Quit implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        EuFac.getInstance().removePlayer(event.getPlayer());
        EuFac.getInstance().bar.removePlayer(event.getPlayer());
        AFKTask.removePlayer(event.getPlayer());
    }
}
