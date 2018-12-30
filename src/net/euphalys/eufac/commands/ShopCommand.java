package net.euphalys.eufac.commands;

import net.euphalys.eufac.EuFac;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * @author Dinnerwolph
 */
public class ShopCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length == 0)
            return false;
        if (args[0].equals("reset")) {
            EuFac.getInstance().xpBooster = 0;
            return true;
        }
        EuFac.getInstance().xpBooster +=2;
        EuFac.getInstance().bar.addTime(args[0]);
        return true;
    }
}
