package mmt007.plugins.gchatchery.MenuMngr;

import mmt007.plugins.gchatchery.MenuMngr.Models.PlayerMenuUtility;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.HashMap;
import java.util.Map;

public class MenuMngr implements Listener {
private static final Map<Player, PlayerMenuUtility> playerMenuUtilityMap = new HashMap<>();
    @EventHandler
    public void onItemClick(InventoryClickEvent e){
        //Triggers Click Event And Performs Menu Action
        if(e.getClickedInventory().getHolder() instanceof Menu holder){
            e.setCancelled(true);

            holder.peform(e);
        }
    }

    //Used To Check What Menu Player Is On
    public static PlayerMenuUtility getPlayerMenuUtility(Player plr){
        PlayerMenuUtility playerMenuUtility;
        if(playerMenuUtilityMap.containsKey(plr)){
            return playerMenuUtilityMap.get(plr);
        }else{
            playerMenuUtility = new PlayerMenuUtility(plr);
            playerMenuUtilityMap.put(plr, playerMenuUtility);

            return playerMenuUtility;
        }
    }
}

