package net.euphalys.eufac;

import net.euphalys.eufac.commands.RTpCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Dinnerwolph
 */
public class EuFac extends JavaPlugin {

    private static EuFac instance;
    public Map<Player, Long> rtpTimer = new HashMap();

    @Override
    public void onEnable() {
        instance = this;
        this.getCommand("rtp").setExecutor(new RTpCommand());
    }

    public static EuFac getInstance() {
        return instance;
    }
}
