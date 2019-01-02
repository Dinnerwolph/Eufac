package net.euphalys.eufac.listener.player;

import net.euphalys.eufac.tasks.AFKTask;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * @author Dinnerwolph
 */
public class Move implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        AFKTask.removePlayer(event.getPlayer());
    }
}
