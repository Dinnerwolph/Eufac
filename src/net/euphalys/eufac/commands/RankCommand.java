package net.euphalys.eufac.commands;

import net.euphalys.eufac.EuFac;
import net.euphalys.eufac.utils.RankTabList;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

/**
 * @author Dinnerwolph
 */
public class RankCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length == 0) {

        } else {
            if (args[0].equalsIgnoreCase("reload")) {
                for (int id : EuFac.getInstance().groupMap.keySet())
                    EuFac.getInstance().groupMap.put(id, EuFac.getInstance().playerManager.updateGroup(id));
                for (Team team : Bukkit.getScoreboardManager().getMainScoreboard().getTeams())
                    team.unregister();
                for (Player player : Bukkit.getOnlinePlayers())
                    RankTabList.updateRank(player);
            } else if (args[0].equalsIgnoreCase("set")) {
                if (args.length == 3) {
                    EuFac.getInstance().playerManager.setGroup(args[1], Integer.parseInt(args[2]));
                    for (Team team : Bukkit.getScoreboardManager().getMainScoreboard().getTeams())
                        team.unregister();
                    for (Player player : Bukkit.getOnlinePlayers())
                        RankTabList.updateRank(player);
                } else {
                    commandSender.sendMessage("/rank set <player> <rankId>");
                }
            }
        }
        return false;
    }

}
