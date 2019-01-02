package net.euphalys.eufac.listener;

import net.euphalys.eufac.EuFac;
import net.euphalys.eufac.listener.player.Exp;
import net.euphalys.eufac.listener.player.Join;
import net.euphalys.eufac.listener.player.Move;
import net.euphalys.eufac.listener.player.Quit;
import org.bukkit.plugin.PluginManager;

/**
 * @author Dinnerwolph
 */
public class ListenerManager {

    private final EuFac instance = EuFac.getInstance();
    private final PluginManager pm = instance.getServer().getPluginManager();

    public ListenerManager() {
        init();
    }

    private void init() {
        pm.registerEvents(new Exp(), instance);
        pm.registerEvents(new Join(), instance);
        pm.registerEvents(new Move(), instance);
        pm.registerEvents(new Quit(), instance);
    }
}
