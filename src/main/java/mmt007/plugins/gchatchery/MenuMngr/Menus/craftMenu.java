package mmt007.plugins.gchatchery.MenuMngr.Menus;

import mmt007.plugins.gchatchery.Delay;
import mmt007.plugins.gchatchery.GCHatchery;
import mmt007.plugins.gchatchery.MenuMngr.Menu;
import mmt007.plugins.gchatchery.MenuMngr.Models.PlayerMenuUtility;
import mmt007.plugins.gchatchery.MenuMngr.Utils.MenuCreationUtil;
import mmt007.plugins.gchatchery.RecipeMngr.Models.Recipe;
import mmt007.plugins.gchatchery.RecipeMngr.RecipeMngr;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class craftMenu extends Menu {
    Plugin main = GCHatchery.getPlugin();
    int[] emptyPlaceAbleSlots = {10,11,12,14,15,16};
    int[] loadingSlots = {20,24,29,33,30,32,31,40};
    int[] outputSlots = {47,48,49,50,51};
    HashMap<Integer,ItemStack> inputs = new HashMap<>();
    HashMap<Integer,ItemStack> outputs = new HashMap<>();

    public craftMenu(PlayerMenuUtility playerMenuUtility) {
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

                //Checks If Player Clicked With Empty Hand On A Item Menu
                if(isMenuItem(e.getCurrentItem()) && playerHand.getType().isAir())
                    {e.setCancelled(true);return;}

                //Checks If Player Got A Regular Item, If True, Fill The Gap In GUI
                if (!isMenuItem(e.getCurrentItem())){
                    //Sets Cursor To Picked Item
                    e.getView().setCursor(e.getCurrentItem());

                    inventory.setItem(index,MenuCreationUtil.createItem(
                        "§r§7Fish Slot",Material.IRON_BARS, new String[]{}
                    ));

                    //Removes Item from Input List
                    inputs.remove(index);

                    return;
                }

                //Removes GUI Item and Adds To Lists
                inventory.setItem(index,playerHand);
                inputs.put(index,playerHand);

                //Clears Cursor
                e.getView().setCursor(new ItemStack(Material.AIR));
            }
            case 47, 48, 49, 50, 51 -> {
                //Output Case

                //Checks If Player Didn't Click With An Empty Hand On A Menu Item
                if (isMenuItem(e.getCurrentItem()) && !playerHand.getType().isAir())
                    {e.setCancelled(true);return;}

                //Sets Cursor To Clicked Item
                e.getView().setCursor(e.getCurrentItem());

                //Fills Gap In GUI
                inventory.setItem(index,MenuCreationUtil.createItem(
                        "§rOutput Slot",Material.WHITE_STAINED_GLASS_PANE, new String[]{}
                ));

                outputs.remove(index);
            }

            case 13 -> Craft(e);
        }
    }

    //Sets Default GUI Items
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
                    " ",Material.LIGHT_BLUE_STAINED_GLASS_PANE, new String[]{}
            ));
        }

        for(int i : outputSlots){
            items.set(i, MenuCreationUtil.createItem(
                    "§rOutput Slot",Material.WHITE_STAINED_GLASS_PANE, new String[]{}
            ));
        }

        items.set(13, MenuCreationUtil.createItem(
                "§2Craft",Material.GREEN_WOOL,new String[]{}
        ));

        inventory.setContents(MenuCreationUtil.toArray(items));
    }


    private void Craft(InventoryClickEvent e){
        //Checks If Player Clicked With Empty Hands
        if(e.getCursor() == null){return;}
        if (!e.getCursor().getType().isAir()){e.setCancelled(true);return;}

        HashMap<Integer,ItemStack> input = new HashMap<>();

        //Adds Items Added By Player On The {input} Variable
        for(int is : emptyPlaceAbleSlots){
            ItemStack item = inventory.getItem(is);
            if(item == null){return;}

            if(!isMenuItem(item)){
                input.put(is,item);
            }
        }

        //Gets Recipe And Its Output
        Recipe recipe = RecipeMngr.getRecipe(input);
        Map<Integer,ItemStack> recipeOutputs = recipe.getOutput();

        //Check If Recipe Is Valid
        if(!recipe.equals(Recipe.getEmptyRecipe())){
            //Calculates The Loading Time
            long loadingTime = (long) Math.floor(recipe.getTime() * 20);
            long dividedTime = loadingTime / 4;

            //Clears Items From Inputs
            if(!clearInput(recipe)){return;}

            //Loop For Crafting Delay
            for(int i = 0; i < 4;i++){
                //Gets Loading Slots Indexes
                int index = loadingSlots[i * 2];
                int indexLast = loadingSlots[i * 2 + 1];

                new Delay(() -> {
                    //Changes Color Of Loading Items
                    inventory.setItem(index, MenuCreationUtil.createItem(
                            "",Material.LIME_STAINED_GLASS_PANE,new String[]{}
                    ));
                    inventory.setItem(indexLast, MenuCreationUtil.createItem(
                            "",Material.LIME_STAINED_GLASS_PANE,new String[]{}
                    ));

                },dividedTime * i);
            }

            //Loops Through Each Output Items
            setOutput(recipeOutputs,dividedTime);
        }
    }

    private boolean isMenuItem(ItemStack item){
        if(Objects.equals(item.getItemMeta(),null)){return false;}
        return item.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(main,"MenuItem"), PersistentDataType.BOOLEAN);
    }

    private boolean clearInput(Recipe recipe){
        ArrayList<ItemStack> Items = new ArrayList<>();

        //Loops Through The Items On The Recipe And Adds Up Similar Items
        for(ItemStack item : recipe.getInput().values()){
            item.setAmount(0);

            for(ItemStack item2 : recipe.getInput().values()){
                if(item2.isSimilar(item)) {
                    item.setAmount(item.getAmount() + item.getAmount());
                }
            }

            Items.add(item);
        }

        //Goes Through The Input Slots
        for(int i : emptyPlaceAbleSlots){
            //Gets Item On The Slot And Check If It's A Menu Item Or Null
            ItemStack slotItem = inventory.getItem(i);

            if(slotItem == null){continue;}
            if(isMenuItem(slotItem)){continue;}

            //Gets The First Instance Of The Same Item In The Recipe Inputs
            //If It Couldn't Find Any, Cancel The Operation
            ItemStack recipeItem = null;

            for(ItemStack item : Items){
                if(item.isSimilar(slotItem)){
                    recipeItem = slotItem;
                    break;
                }
            }

            if(recipeItem == null){return false;}

            //Calculates The Item Amount and Sets The Item
            int itemAmount = slotItem.getAmount() - recipeItem.getAmount();

            if(itemAmount == 0){
                inventory.setItem(i, MenuCreationUtil.createItem(
                        "§r§7Fish Slot",Material.IRON_BARS, new String[]{}
                ));}
            else if(itemAmount < 0){return false;}
            else {
                slotItem.setAmount(itemAmount);
                inventory.setItem(i,slotItem);
            }

        }

        return true;
    }

    //Sets The Recipe Output
    private void setOutput(Map<Integer,ItemStack> recipeOutputs,long dividedTime){
        new Delay(() -> recipeOutputs.forEach((index, item) ->{
            //Loops Through Each Output Slot
            outputSlotLoop:
            for(int os : outputSlots){
                //Checks If The Item On The Slot Is The Same As The Output One
                if(item.isSimilar(inventory.getItem(os))){
                    //Gets Item And Adds Its Amount With The Output Item
                    ItemStack invItem = inventory.getItem(os);
                    if(invItem == null){return;}
                    invItem.setAmount(invItem.getAmount() + item.getAmount());

                    inventory.setItem(os,invItem);
                    break;
                }else{
                    //Loops To Check If There Is An Empty Slot Available
                    for(int os2 : outputSlots){
                        if(inventory.getItem(os2) == null){return;}

                        if(isMenuItem(Objects.requireNonNull(inventory.getItem(os2)))){
                            inventory.setItem(os,item);
                            break outputSlotLoop;
                        }
                    }
                }
            }
        }),dividedTime * 4);
    }

}
