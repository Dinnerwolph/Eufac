package net.euphalys.eufac.entity;

import net.euphalys.eufac.EuFac;
import net.euphalys.eufac.database.PlayerManager;
import net.euphalys.eufac.group.Group;
import org.bukkit.craftbukkit.v1_9_R2.CraftServer;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Dinnerwolph
 */
public class EuphalysPlayer extends CraftPlayer {
    private final PlayerManager playerManager;
    private final int euphId;
    private Group group;
    private int boostXpCount;
    private List<LocalDateTime> dates;

    public EuphalysPlayer(Player player) {
        super((CraftServer) player.getServer(), ((CraftPlayer) player).getHandle());
        playerManager = EuFac.getInstance().playerManager;
        load();
        this.euphId = playerManager.getEuphId(getUniqueId());
        this.dates = playerManager.getXPBOOSTUse(euphId);
        this.boostXpCount = playerManager.getXpBoost(euphId);
    }

    private void load() {
        playerManager.createUser(getPlayer());
        this.group = playerManager.getGroup(getUniqueId());
    }

    public void unload() {
        playerManager.unload(boostXpCount, euphId);
    }

    public Group getGroup() {
        return group;
    }

    public int getBoostXpCount() {
        return boostXpCount;
    }

    public void setBoostXpCount(int boostXpCount) {
        this.boostXpCount = boostXpCount;
    }

    public void useXpBoost() {
        setBoostXpCount(boostXpCount -= 1);
        playerManager.useXPBOOST(euphId);
    }

    public void updateGroup() {
        group = EuFac.getInstance().groupMap.get(group.getId());
    }

    public List<LocalDateTime> getDates() {
        return dates;
    }
}
