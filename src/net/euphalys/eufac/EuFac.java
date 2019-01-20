package net.euphalys.eufac;

import net.euphalys.eufac.commands.BoostXPCommand;
import net.euphalys.eufac.commands.RTpCommand;
import net.euphalys.eufac.commands.RankCommand;
import net.euphalys.eufac.database.DatabaseManager;
import net.euphalys.eufac.database.PlayerManager;
import net.euphalys.eufac.entity.EuphalysPlayer;
import net.euphalys.eufac.group.Group;
import net.euphalys.eufac.listener.ListenerManager;
import net.euphalys.eufac.tasks.AFKTask;
import net.euphalys.eufac.utils.BossBar;
import net.euphalys.eufac.utils.BrewingRecipe;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.scoreboard.Team;

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
        this.getCommand("boostxp").setExecutor(new BoostXPCommand());
        this.getCommand("rank").setExecutor(new RankCommand());
        ItemStack apple = new ItemStack(Material.APPLE);
        ItemMeta meta = apple.getItemMeta();
        meta.setDisplayName("§1❄ §bPomme givrée §1❄");
        meta.addEnchant(Enchantment.FROST_WALKER, 0, false);
        apple.setItemMeta(meta);
        this.getServer().addRecipe(new ShapedRecipe(apple).shape("XNX").setIngredient('X', Material.ICE).setIngredient('N', Material.APPLE));
        new BrewingRecipe(apple, (inventory, item, ingridient) -> {//Some lambda magic
            if (!ingridient.isSimilar(apple))
                return;
            for (ItemStack itemStack : inventory.getContents()) {
                if (itemStack == null)
                    return;
                PotionData potionData = ((PotionMeta) item.getItemMeta()).getBasePotionData();
                if (potionData.getType() == PotionType.INSTANT_HEAL && potionData.isUpgraded()) {
                    ItemStack potion = new ItemStack(itemStack.getType());
                    PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.HEAL, 1, 2), true);
                    potionMeta.setDisplayName("Potion of Healing");
                    itemStack.setItemMeta(potionMeta);
                }
            }
        }, true);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new AFKTask(), 0, 20);

    }

    @Override
    public void onDisable() {
        for (Team team : Bukkit.getScoreboardManager().getMainScoreboard().getTeams())
            team.unregister();
    }

    public void addPlayer(Player player) {
        playerMap.put(player.getUniqueId(), new EuphalysPlayer(player));
    }

    public void removePlayer(Player player) {
        playerMap.get(player.getUniqueId()).unload();
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
