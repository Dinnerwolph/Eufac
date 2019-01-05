package net.euphalys.eufac.entity;

import net.euphalys.eufac.EuFac;
import net.euphalys.eufac.database.PlayerManager;
import net.euphalys.eufac.group.Group;
import org.bukkit.craftbukkit.v1_9_R1.CraftServer;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

/**
 * @author Dinnerwolph
 */
public class EuphalysPlayer extends CraftPlayer {
    private final PlayerManager playerManager;
    private Group group;

    public EuphalysPlayer(Player player) {
        super((CraftServer) player.getServer(), ((CraftPlayer) player).getHandle());
        playerManager = EuFac.getInstance().playerManager;
        load();
    }

    private void load() {
        playerManager.createUser(getPlayer());
        this.group = playerManager.getGroup(getUniqueId());
    }

    public Group getGroup() {
        return group;
    }
}
