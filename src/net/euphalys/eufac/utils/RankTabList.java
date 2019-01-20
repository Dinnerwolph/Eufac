package net.euphalys.eufac.utils;

import net.euphalys.eufac.EuFac;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

/**
 * @author Dinnerwolph
 */
public class RankTabList {

    public static void updateRank(Player player) {
        EuFac.getInstance().getPlayer(player).updateGroup();
        for (Player p2 : Bukkit.getOnlinePlayers()) {
            update(player, p2);
        }
    }

    public static void update(Player player, Player target) {
        Scoreboard scoreboard = target.getScoreboard();
        Team team = null;
        int scoreboardId = EuFac.getInstance().getPlayer(player.getUniqueId()).getGroup().getLadder();
        if (scoreboard.getTeam(scoreboardId + "") == null) {
            team = scoreboard.registerNewTeam(scoreboardId + "");
            team.setPrefix(EuFac.getInstance().getPlayer(player.getUniqueId()).getGroup().getPrefix());
            team.setSuffix(EuFac.getInstance().getPlayer(player.getUniqueId()).getGroup().getSuffix());
        } else
            team = scoreboard.getTeam(scoreboardId + "");
        team.addPlayer(player);
    }
}
