package net.euphalys.eufac;

import net.euphalys.eufac.commands.RTpCommand;
import net.euphalys.eufac.commands.ShopCommand;
import net.euphalys.eufac.listener.ListenerManager;
import net.euphalys.eufac.tasks.AFKTask;
import net.euphalys.eufac.utils.BossBar;
import org.bukkit.Bukkit;
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
    public int xpBooster = 0;
    public BossBar bar;

    @Override
    public void onEnable() {
        instance = this;
        bar = new BossBar(this, "");
        new ListenerManager();
        this.getCommand("rtp").setExecutor(new RTpCommand());
        this.getCommand("shop").setExecutor(new ShopCommand());
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new AFKTask(), 0, 20);
    }

    public static EuFac getInstance() {
        return instance;
    }
}
