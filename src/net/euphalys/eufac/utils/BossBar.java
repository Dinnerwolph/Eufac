package net.euphalys.eufac.utils;

import net.euphalys.eufac.EuFac;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Dinnerwolph
 */
public class BossBar extends BukkitRunnable {

    private Map<Long, Pair<String, Integer>> timer = new HashMap();
    private Map<String, Integer> count = new HashMap();
    private final org.bukkit.boss.BossBar bar;

    public BossBar(Plugin instance, String title) {
        this.bar = instance.getServer().createBossBar(title, BarColor.BLUE, BarStyle.SOLID);
        runTaskTimer(instance, 0, 20);
    }

    public void addPlayer(Player player) {
        this.bar.addPlayer(player);
    }

    public void removePlayer(Player player) {
        this.bar.removePlayer(player);
    }

    public void setTitle(String title) {
        this.bar.setTitle(title);
    }

    public void setProgress(double progress) {
        this.bar.setProgress(progress);
    }

    public void addTime(String name) {
        try {
            int i = count.get(name);
            count.put(name, i + 1);
        } catch (NullPointerException e) {
            count.put(name, 1);
        }


        timer.put(System.currentTimeMillis(), new Pair(name, 1800));
    }

    public Map<Long, Pair<String, Integer>> getTimer() {
        return timer;
    }

    public Map<String, Integer> getCount() {
        return count;
    }

    public boolean hasBoostXp() {
        if (timer.size() == 0)
            return false;
        else
            return true;
    }

    @Override
    public void run() {
        int maxtimer = 0;
        for (long time : timer.keySet()) {
            int i = timer.get(time).getValue();
            if (i == 0) {
                EuFac.getInstance().xpBooster -= 2;
                if (count.get(timer.get(time).getKey()) == 0)
                    count.remove(timer.get(time).getKey());
                else
                    count.put(timer.get(time).getKey(), count.get(timer.get(time).getKey()) - 1);
                timer.remove(time);

            } else {
                i--;
                if (maxtimer < i)
                    maxtimer = i;
                timer.put(time, new Pair(timer.get(time).getKey(), i));
            }
        }
        if (maxtimer == 0)
            bar.hide();
        else
            bar.show();
        setProgress((double) maxtimer / 1800);
        setTitle("§4XP Doublé (x" + EuFac.getInstance().xpBooster + ")");
    }
}
