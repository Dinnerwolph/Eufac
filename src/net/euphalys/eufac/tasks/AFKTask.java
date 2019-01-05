package net.euphalys.eufac.tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Dinnerwolph
 */
public class AFKTask extends BukkitRunnable {

    private static final Map<Player, Integer> afkList = new HashMap();

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (afkList.containsKey(player))
                if (afkList.get(player) >= 300) {
                    player.kickPlayer("AFK non autorisé");
                } else
                    afkList.put(player, afkList.get(player) + 1);
            else if(!player.hasPermission("afk.bypass"))
                afkList.put(player, 0);
        }
    }

    public static void removePlayer(Player player) {
        afkList.remove(player);
    }
}
