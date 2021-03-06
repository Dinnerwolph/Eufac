package net.euphalys.eufac.commands;

import net.euphalys.eufac.EuFac;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Random;

/**
 * @author Dinnerwolph
 */
public class RTpCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player))
            return false;
        Player player = (Player) commandSender;
        if (EuFac.getInstance().rtpTimer.containsKey(player))
            if (System.currentTimeMillis() - EuFac.getInstance().rtpTimer.get(player) / 1000 > 300 && !player.hasPermission("eufac.rtp.bypass")) {
                player.sendMessage("§cVous ne pouvez executer cette commande toute les 5 minutes");
                return true;
            } else {
                if (rtp(player))
                    EuFac.getInstance().rtpTimer.put(player, System.currentTimeMillis());
                else
                    player.sendMessage("§cLa téléportation à échoué.");
                return true;
            }
        else if (rtp(player))
            EuFac.getInstance().rtpTimer.put(player, System.currentTimeMillis());
        return true;
    }

    private boolean rtp(Player player) {
        int z1 = new Random().nextInt(7500);
        int z2 = new Random().nextInt(7500);
        int x1 = new Random().nextInt(7500);
        int x2 = new Random().nextInt(7500);
        int z = z1 - z2;
        int x = x1 - x2;
        for (int i = 255; i > 0; i--) {
            Block block = Bukkit.getWorlds().get(0).getBlockAt(x, i, z);
            if (block.getType() != Material.AIR)
                if (block.getType().equals(Material.WATER))
                    return rtp(player);
                else {
                    loadChuck(new Location(player.getWorld(), x + 0.5, i + 1.5, z + 0.5), player);
                    return true;
                }
        }
        return false;
    }

    private void loadChuck(Location loc, Player player) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(EuFac.getInstance(), () -> {
            loc.getWorld().loadChunk(loc.getBlockX(), loc.getBlockZ());
            player.teleport(loc);
        }, 1L);
    }

}
