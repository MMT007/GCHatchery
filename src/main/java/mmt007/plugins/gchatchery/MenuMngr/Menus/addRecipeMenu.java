package mmt007.plugins.gchatchery.MenuMngr.Menus;

import mmt007.plugins.gchatchery.GCHatchery;
import mmt007.plugins.gchatchery.MenuMngr.Menu;
import mmt007.plugins.gchatchery.MenuMngr.Models.PlayerMenuUtility;
import mmt007.plugins.gchatchery.MenuMngr.Utils.MenuCreationUtil;
import mmt007.plugins.gchatchery.RecipeMngr.Models.Recipe;
import mmt007.plugins.gchatchery.RecipeMngr.RecipeMngr;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


public class addRecipeMenu extends Menu {
    Plugin main = GCHatchery.getPlugin();
    int[] emptyPlaceAbleSlots = {10,11,12,14,15,16};
    int[] loadingSlots = {20,24,29,33,40};
    int[] outputSlots = {47,48,49,50,51};
    HashMap<Integer,ItemStack> inputs = new HashMap<>();
    HashMap<Integer,ItemStack> outputs = new HashMap<>();
    double makingTime = 5.0d;
    public addRecipeMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public int getSize() {return 54;}

    @Override
    public void peform(InventoryClickEvent e) {

        if(e.getCurrentItem() == null){return;}
        if(e.getCursor() == null){return;}

        ItemStack playerHand = e.getCursor();

        int index = e.getSlot();
        switch (index){
            case 10, 11, 12, 14, 15, 16 -> {
                //Input Case

                //Checks If Player Has Empty Hand And The Clicked Item Is A Menu Item
                if (isMenuItem(e.getCurrentItem()) && playerHand.getType().isAir())
                    {e.setCancelled(true);return;}

                //Checks If Player Got A Regular Item, If True, Fill Empty Gap On GUI
                if (!isMenuItem(e.getCurrentItem())){
                    inventory.setItem(index,MenuCreationUtil.createItem(
                        "§r§7Fish Slot",Material.IRON_BARS, new String[]{}
                    ));

                    inputs.remove(index);

                    return;
                }

                //Removes GUI Item and Adds The Item To Lists
                inputs.put(index,playerHand);
                e.setCurrentItem(playerHand);
            }
            case 47, 48, 49, 50, 51 -> {
                //Output Case

                //Checks If Player Hand is Empty And Clicked A Menu Item
                if (isMenuItem(e.getCurrentItem()) && playerHand.getType().isAir())
                    {e.setCancelled(true);return;}

                //Checks If Player Got A Regular Item, If True, Fills Gap In GUI
                if (!isMenuItem(e.getCurrentItem())){
                    inventory.setItem(index,MenuCreationUtil.createItem(
                        "§rOutput Slot",Material.WHITE_STAINED_GLASS_PANE, new String[]{}
                    ));
                    outputs.remove(index);

                    return;
                }

                //Removes GUI Item And Adds To Lists.
                outputs.put(index,playerHand);
                inventory.setItem(index,playerHand);
            }
            case 30 -> {
                //Time Add Case
                e.setCancelled(true);

                //Determines Time Change Type
                if (e.isShiftClick()) {makingTime += 0.1d;}
                if (e.isRightClick()) {makingTime += 10d;}
                if (e.isLeftClick()) {makingTime += 1d;}

                //Caps Time To 3600s (1H)
                if (makingTime > 3600) {makingTime = 3600;}

                //Reload Time Display
                inventory.setItem(31,MenuCreationUtil.createItem(
                        "§eTime: "+ makingTime + "s",Material.CLOCK,new String[]{}
                ));
            }
            case 32 -> {
                //Time Remove Case
                e.setCancelled(true);

                //Determines Time Change Type
                if (e.isShiftClick()){makingTime -= 0.1d;}
                if (e.isRightClick()){makingTime -= 10d;}
                if (e.isLeftClick()){makingTime -= 1d;}

                //Caps Time to 0.1s
                if (makingTime < 0.1d){makingTime = 0.1d;}

                //Reload Time Display
                inventory.setItem(31,MenuCreationUtil.createItem(
                        "§eTime: "+ makingTime + "s",Material.CLOCK,new String[]{}
                ));
            }
            case 13 -> {
                //Add Recipe Case
                e.setCancelled(true);

                Recipe recipe = Recipe.getEmptyRecipe();

                //Sets Recipe Variables
                recipe.setName(e.getView().getTitle());
                recipe.setInput(inputs);
                recipe.setOutput(outputs);
                recipe.setTime(makingTime);

                Player plr = (Player) e.getWhoClicked();

                //Adds Recipe To Recipe File
                plr.closeInventory();
                if (RecipeMngr.addRecipe(recipe)) {
                    plr.sendMessage("§aRecipe Added!");
                } else {
                    plr.sendMessage("§cCould Not Add Recipe!");
                }
            }
        }
    }

    //Sets Default GUI Item
    @Override
    public void setMenuItems() {
        ArrayList<ItemStack> items = MenuCreationUtil.createBackGround(
                getSize(), Material.BLACK_STAINED_GLASS_PANE);


        for(int i : emptyPlaceAbleSlots){
            items.set(i, MenuCreationUtil.createItem(
                    "§r§7Fish Slot",Material.IRON_BARS, new String[]{}
            ));
        }

        for(int i : loadingSlots){
            items.set(i, MenuCreationUtil.createItem(
                    "",Material.LIGHT_BLUE_STAINED_GLASS_PANE, new String[]{}
            ));
        }

        for(int i : outputSlots){
            items.set(i, MenuCreationUtil.createItem(
                    "§rOutput Slot",Material.WHITE_STAINED_GLASS_PANE, new String[]{}
            ));
        }

        items.set(13, MenuCreationUtil.createItem(
                "§2Add Recipe",Material.GREEN_WOOL,new String[]{}
        ));
        items.set(30, MenuCreationUtil.createItem(
                "§2+1s",Material.GREEN_CONCRETE,new String[]
                        {"§aRightClick : +10s", "§aShift + Click : +0.1s"}
        ));
        items.set(31, MenuCreationUtil.createItem(
                "§eTime: "+ makingTime + "s",Material.CLOCK,new String[]{}
        ));
        items.set(32, MenuCreationUtil.createItem(
                "§4-1s",Material.RED_CONCRETE,new String[]
                        {"§cRightClick : -10s","§cShift + Click : -0.1s"}
        ));

        inventory.setContents(MenuCreationUtil.toArray(items));
    }


    private boolean isMenuItem(ItemStack item){
        if(Objects.equals(item.getItemMeta(),null)){return false;}
        return item.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(main,"MenuItem"),PersistentDataType.BOOLEAN);
    }
}
