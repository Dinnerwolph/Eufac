package net.euphalys.eufac;

import net.euphalys.eufac.commands.RTpCommand;
import net.euphalys.eufac.commands.ShopCommand;
import net.euphalys.eufac.database.DatabaseManager;
import net.euphalys.eufac.database.PlayerManager;
import net.euphalys.eufac.entity.EuphalysPlayer;
import net.euphalys.eufac.group.Group;
import net.euphalys.eufac.listener.ListenerManager;
import net.euphalys.eufac.tasks.AFKTask;
import net.euphalys.eufac.utils.BossBar;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Dinnerwolph
 */
public class EuFac extends JavaPlugin {

    private static EuFac instance;
    public Map<Player, Long> rtpTimer = new HashMap();
    public int xpBooster = 0;
    public BossBar bar;
    public DatabaseManager databaseManager;
    public PlayerManager playerManager;
    public Map<Integer, Group> groupMap = new HashMap();
    private Map<UUID, EuphalysPlayer> playerMap = new HashMap();

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        databaseManager = new DatabaseManager("jdbc:mysql://" +
                getConfig().getString("bdd.host") + ":" + getConfig().getInt("bdd.port") + "/" +
                getConfig().getString("bdd.database"), getConfig().getString("bdd.username"),
                getConfig().getString("bdd.password"), 0, 200);
        playerManager = new PlayerManager();
        bar = new BossBar(this, "");
        new ListenerManager();
        this.getCommand("rtp").setExecutor(new RTpCommand());
        this.getCommand("shop").setExecutor(new ShopCommand());
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new AFKTask(), 0, 20);
    }

    public void addPlayer(Player player) {
        playerMap.put(player.getUniqueId(), new EuphalysPlayer(player));
    }

    public void removePlayer(Player player) {
        playerMap.remove(player.getUniqueId());
    }

    public static EuFac getInstance() {
        return instance;
    }

    @Deprecated
    public EuphalysPlayer getPlayer(String name) {
        return getPlayer(Bukkit.getPlayer(name));
    }

    public EuphalysPlayer getPlayer(Player player) {
        return getPlayer(player.getUniqueId());
    }

    public EuphalysPlayer getPlayer(UUID uuid) {
        return playerMap.get(uuid);
    }
}
