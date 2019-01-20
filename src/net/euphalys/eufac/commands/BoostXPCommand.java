package net.euphalys.eufac.commands;

import net.euphalys.eufac.EuFac;
import net.euphalys.eufac.entity.EuphalysPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Date;

/**
 * @author Dinnerwolph
 */
public class BoostXPCommand implements CommandExecutor {

    private final String prefix = "§6[BoostXP] ";
    private final String prefixa = "§6[BoostXP §4Admin§6] ";

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player))
            return false;
        EuphalysPlayer player = EuFac.getInstance().getPlayer((Player) commandSender);
        if (args.length == 0) {
            player.sendMessage("§6BoostXP, plugin développé par §eDinnerwolph");
            return true;
        }
        if (args[0].equalsIgnoreCase("start"))
            if (player.getBoostXpCount() > 0 || player.hasPermission("eufac.boostxp.start.admin")) {
                player.useXpBoost();
                EuFac.getInstance().xpBooster += 2;
                EuFac.getInstance().bar.addTime(commandSender.getName());
                player.sendMessage(prefix + "§eVous venez d'activer un BoostXP ! §7(" + player.getBoostXpCount() + " restants)");
                return true;
            } else {
                player.sendMessage(prefix + " §c Vous n'avez aucun BoostXP.");
                return true;
            }
        else if (args[0].equalsIgnoreCase("give")) {
            if (!player.hasPermission("eufac.boostxp.give"))
                return noPerms(player);
            else if (args.length < 2) {
                //TODO message
                return true;
            } else {
                EuphalysPlayer target = EuFac.getInstance().getPlayer(Bukkit.getOfflinePlayer(args[1]).getUniqueId());
                if (args.length > 2) {
                    target.setBoostXpCount(target.getBoostXpCount() + Integer.parseInt(args[2]));
                    player.sendMessage(prefixa + "Vous venez d'ajouter §e" + args[2] + " §6BoostXP à §e" + args[1]);
                } else {
                    target.setBoostXpCount(target.getBoostXpCount() + 1);
                    player.sendMessage(prefixa + "Vous venez d'ajouter §e1 §6BoostXP à §e" + args[1]);
                }
                return true;
            }
        } else if (args[0].equalsIgnoreCase("remove")) {
            if (!player.hasPermission("eufac.boostxp.remove"))
                return noPerms(player);
            else if (args.length < 2) {
                //TODO message
                return true;
            } else {
                EuphalysPlayer target = EuFac.getInstance().getPlayer(Bukkit.getOfflinePlayer(args[1]).getUniqueId());
                if (target.getBoostXpCount() <= 0) {
                    player.sendMessage(prefixa + "§c" + args[1] + " n'a aucun BoostXp.");
                }
                if (args.length > 2) {
                    target.setBoostXpCount(target.getBoostXpCount() - Integer.parseInt(args[2]));
                    player.sendMessage(prefixa + "Vous venez de retirer §e" + args[2] + " §6BoostXP à §e" + args[1]);
                } else {
                    target.setBoostXpCount(target.getBoostXpCount() - 1);
                    player.sendMessage(prefixa + "Vous venez de retirer §e1 §6BoostXP à §e" + args[1]);
                }
                return true;
            }
        } else if (args[0].equalsIgnoreCase("info")) {
            if (player.hasPermission("eufac.boostxp.info"))
                if (args.length == 1) {
                    player.sendMessage("§6----- Info §eBoostXP §6-----");
                    if (player.getBoostXpCount() > 0)
                        player.sendMessage("§e" + player.getName() + "§6, il vous reste §e" + player.getBoostXpCount() + " §6BoostXP.");
                    else
                        player.sendMessage("§e" + player.getName() + "§6, vous n'avez §eaucun §6BoostXP.");
                    player.sendMessage("§6------------------------");
                } else {
                    if (args[1].equalsIgnoreCase("-p"))
                        if (player.hasPermission("eufac.boostxp.info.p")) {
                            if (args[2].equalsIgnoreCase("-h") && player.hasPermission("eufac.boostxp.h")) {
                                EuphalysPlayer target = EuFac.getInstance().getPlayer(Bukkit.getOfflinePlayer(args[3]).getName());
                                player.sendMessage("§6----- Historique §eBoostXP " + args[3] + " §6-----");
                                int size = 5;
                                if (target.getDates().size() < 5)
                                    size = target.getDates().size();
                                for (int i = 0; i < size; i++)
                                    player.sendMessage("§e" + target.getDates().get(i).getDayOfYear() + "/" + target.getDates().get(i).getMonthValue() + "/" + target.getDates().get(i).getYear() + "," + target.getDates().get(i).getHour() + "h" + target.getDates().get(i).getMinute() + " §6Utilisation d'§e1 §6BoostXP");

                                String footer = "§6----------------------------";
                                for (int i = 0; i < args[3].length(); i++)
                                    footer += "-";
                                player.sendMessage(footer);
                            } else {
                                player.sendMessage("§6----- Info §eBoostXP " + args[2] + " §6-----");
                                EuphalysPlayer target = EuFac.getInstance().getPlayer(Bukkit.getOfflinePlayer(args[2]).getName());
                                if (target.getBoostXpCount() > 0)
                                    player.sendMessage("§6Il reste §e" + target.getBoostXpCount() + " §6BoostXP à §e" + args[2]);
                                else
                                    player.sendMessage("§e" + args[2] + " §6n'a §eaucun §6BoostXP");
                                String footer = "§6--------------------";
                                for (int i = 0; i < args[2].length(); i++)
                                    footer += "-";
                                player.sendMessage(footer);
                            }


                        } else
                            return noPerms(player);
                    else if (args[1].equalsIgnoreCase("-e"))
                        if (player.hasPermission("eufac.boostxp.info.e")) {
                            player.sendMessage("§6----- §eBoostXP §6en cours -----");
                            if (EuFac.getInstance().bar.hasBoostXp()) {
                                player.sendMessage("§6BoostXP actuel: §ex" + EuFac.getInstance().xpBooster + "§6(§e" + EuFac.getInstance().xpBooster / 2 + " §6Boosts)");
                                for (String name : EuFac.getInstance().bar.getCount().keySet()) {
                                    if (EuFac.getInstance().bar.getCount().get(name) != 0) {
                                        Long timer = 0L;
                                        for (Long time : EuFac.getInstance().bar.getTimer().keySet()) {
                                            if (EuFac.getInstance().bar.getTimer().get(time).getKey().equalsIgnoreCase(name))
                                                if (timer < time)
                                                    timer = time;
                                        }
                                        Date date = new Date(timer);
                                        player.sendMessage("§e" + name + "§6,§e " + EuFac.getInstance().bar.getCount().get(name) + " §6Boosts. Le dernier a été lancé à §e" + date.getHours() + "h" + date.getMinutes());
                                    }
                                }
                            } else
                                player.sendMessage("§eAucun BoostXP n'est en cours d'utilisation");
                            player.sendMessage("§6----------------------------");
                        } else
                            return noPerms(player);
                }
            else
                return noPerms(player);
        }
        if (args[0].equals("reset")) {
            EuFac.getInstance().xpBooster = 0;
            return true;
        }
        return true;
    }

    private boolean noPerms(Player player) {
        player.sendMessage(prefix + "§cErreur: vous n'avez pas la permission.");
        return true;
    }
}
