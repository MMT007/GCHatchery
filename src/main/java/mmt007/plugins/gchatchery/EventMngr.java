package mmt007.plugins.gchatchery;

import mmt007.plugins.gchatchery.Commands.Subcommands.giveSubCommand;
import mmt007.plugins.gchatchery.MenuMngr.MenuMngr;
import mmt007.plugins.gchatchery.MenuMngr.Menus.craftMenu;
import mmt007.plugins.gchatchery.MenuMngr.Models.PlayerMenuUtility;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

public class EventMngr implements Listener {
    Plugin main = GCHatchery.getPlugin();
    ItemStack hatchery =  new giveSubCommand().getHactchery();
    NamespacedKey namespace = new NamespacedKey(main,"Hatchery");

    //Listens For When The Player Interacts With A TileEntity
    @EventHandler
    public void openHatchery(PlayerInteractEvent e){
        Block block = e.getClickedBlock();

        if(!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){return;}

        if(!(block.getState() instanceof TileState state)){return;}

        //Gets Block NBT
        PersistentDataContainer container = state.getPersistentDataContainer();

        //Checls If Block Has The NBT Tag
        if (block == null){return;}
        if (!container.has(namespace,PersistentDataType.BOOLEAN)){return;}

        //Cancels Normal GUI and Opens Custom Menu
        PlayerMenuUtility pmu = MenuMngr.getPlayerMenuUtility(e.getPlayer());
        e.setCancelled(true);
        new craftMenu(pmu).openInventory(main.getConfig().getString("Craft-GUI-Name"));

    }

    //Listens When Player Places A Hatchery Block
    @EventHandler
    public void playerPlace(BlockPlaceEvent e){
        ItemStack item = e.getItemInHand();
        Block block = e.getBlockPlaced();

        if(!(block.getState() instanceof TileState state)){return;}

        //Gets Block NBT
        PersistentDataContainer container = state.getPersistentDataContainer();

        //Checks If The Item The Player Is Holding is Null
        if(item.getItemMeta() == null){return;}
        if(hatchery.getItemMeta() == null){return;}

        //Checks If The Item The Player Is Holding has The NBT Tag
        //If True, Adds The NBT Tag To The Block
        if(item.getItemMeta().hashCode() == hatchery.getItemMeta().hashCode()){
            container.set(namespace, PersistentDataType.BOOLEAN, true);
            state.update();
        }
    }

    //Listens If A Player Broke A Hatchery Block
    @EventHandler
    public void playerBreak(BlockBreakEvent e){
        Block block = e.getBlock();
        Player plr = e.getPlayer();

        if(!(block.getState() instanceof TileState state)){return;}

        //Gets Block NBT
        PersistentDataContainer container = state.getPersistentDataContainer();

        //Checks If Player Isn't In Creative
        if(plr.getGameMode().equals(GameMode.CREATIVE)){return;}
        if(hatchery.getItemMeta() == null){return;}

        //Checks If the Block Has The NBT
        //If True, Remove Normal Drops And Adds Hatchery Item To The Player's Inventory
        if(container.has(namespace,PersistentDataType.BOOLEAN)){
            e.setDropItems(false);
            PlayerInventory inv = plr.getInventory();
            inv.addItem(hatchery);
        }
    }
}
