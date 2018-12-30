package net.euphalys.eufac.listener.player;

import net.euphalys.eufac.EuFac;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;

/**
 * @author Dinnerwolph
 */
public class Exp implements Listener {

    @EventHandler
    public void onPlayerExpChange(PlayerExpChangeEvent event) {
        if (EuFac.getInstance().xpBooster != 0)
            event.setAmount(event.getAmount() * EuFac.getInstance().xpBooster);
    }
}
