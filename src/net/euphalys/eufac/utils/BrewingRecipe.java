package net.euphalys.eufac.utils;

import net.euphalys.eufac.EuFac;
import org.bukkit.Material;
import org.bukkit.block.BrewingStand;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dinnerwolph
 */
public class BrewingRecipe {
    private static List<BrewingRecipe> recipes = new ArrayList<BrewingRecipe>();
    private ItemStack ingridient;
    private BrewAction action;
    private boolean perfect;

    public BrewingRecipe(ItemStack ingridient, BrewAction action, boolean perfect) {
        this.ingridient = ingridient;
        this.action = action;
        this.perfect = perfect;
        recipes.add(this);
    }

    public BrewingRecipe(Material ingridient, BrewAction action) {
        this(new ItemStack(ingridient), action, false);
    }

    public ItemStack getIngridient() {
        return ingridient;
    }

    public BrewAction getAction() {
        return action;
    }

    public boolean isPerfect() {
        return perfect;
    }

    /**
     * Get the BrewRecipe of the given recipe , will return null if no recipe is found
     *
     * @param inventory The inventory
     * @return The recipe
     */
    @Nullable
    public static BrewingRecipe getRecipe(BrewerInventory inventory) {
        boolean notAllAir = false;
        for (int i = 0; i < 3 && !notAllAir; i++) {
            if (inventory.getItem(i) == null)
                continue;
            if (inventory.getItem(i).getType() == Material.AIR)
                continue;
            notAllAir = true;
        }
        if (!notAllAir)
            return null;
        for (BrewingRecipe recipe : recipes) {
            if (!recipe.isPerfect() && inventory.getIngredient().getType() == recipe.getIngridient().getType()) {
                return recipe;
            }
            if (recipe.isPerfect() && inventory.getIngredient().isSimilar(recipe.getIngridient())) {
                return recipe;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "BrewingRecipe{" +
                "ingridient=" + ingridient +
                ", action=" + action +
                ", perfect=" + perfect +
                '}';
    }

    public void startBrewing(BrewerInventory inventory) {
        new BrewClock(this, inventory);
    }

    private class BrewClock extends BukkitRunnable {
        private BrewerInventory inventory;
        private BrewingRecipe recipe;
        private ItemStack ingridient;
        private BrewingStand stand;
        private int time = 400; //Like I said the starting time is 400

        public BrewClock(BrewingRecipe recipe, BrewerInventory inventory) {
            this.recipe = recipe;
            this.inventory = inventory;
            this.ingridient = inventory.getIngredient();
            this.stand = inventory.getHolder();
            runTaskTimer(EuFac.getInstance(), 0L, 1L);
        }

        @Override
        public void run() {
            if (time == 0) {
                inventory.setIngredient(new ItemStack(Material.AIR));
                for (int i = 0; i < 3; i++) {
                    if (inventory.getItem(i) == null || inventory.getItem(i).getType() == Material.AIR)
                        continue;
                    recipe.getAction().brew(inventory, inventory.getItem(i), ingridient);
                }
                cancel();
                return;
            }
            if (inventory.getIngredient() == null)
                return;
            if (!inventory.getIngredient().isSimilar(ingridient)) {
                stand.setBrewingTime(400); //Reseting everything
                cancel();
                return;
            }
            //You should also add here a check to make sure that there are still items to brew
            time--;
            stand.setBrewingTime(time);
        }
    }
}