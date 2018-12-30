package net.euphalys.eufac;

import net.euphalys.eufac.commands.RTpCommand;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Dinnerwolph
 */
public class EuFac extends JavaPlugin {

    private static EuFac instance;

    @Override
    public void onEnable() {
        instance = this;
        this.getCommand("rtp").setExecutor(new RTpCommand());
    }

    public static EuFac getInstance() {
        return instance;
    }
}
