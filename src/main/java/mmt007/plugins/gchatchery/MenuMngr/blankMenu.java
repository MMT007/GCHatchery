package mmt007.plugins.gchatchery.MenuMngr;

import mmt007.plugins.gchatchery.MenuMngr.Utils.MenuCreationUtil;
import mmt007.plugins.gchatchery.MenuMngr.Models.PlayerMenuUtility;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class blankMenu extends Menu {
    public blankMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public int getSize() {return 0;}

    @Override
    public void peform(InventoryClickEvent e) {
        if(e.getCurrentItem() == null){return;}
        Player plr = (Player) e.getWhoClicked();
        switch (e.getCurrentItem().getType()) {

        }
    }

    @Override
    public void setMenuItems() {
        ArrayList<ItemStack> items = MenuCreationUtil.createBackGround(
                getSize(), Material.LIGHT_BLUE_STAINED_GLASS_PANE);

        inventory.setContents(MenuCreationUtil.toArray(items));
    }
}
