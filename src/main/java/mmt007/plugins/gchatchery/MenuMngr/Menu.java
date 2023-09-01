package mmt007.plugins.gchatchery.MenuMngr;

import mmt007.plugins.gchatchery.MenuMngr.Models.PlayerMenuUtility;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public abstract class Menu implements InventoryHolder {

    protected Inventory inventory;
    protected PlayerMenuUtility playerMenuUtility;

    public Menu(PlayerMenuUtility playerMenuUtility){
        this.playerMenuUtility = playerMenuUtility;
    }

    @NotNull
    public abstract int getSize();
    public void openInventory(String name){
            inventory = Bukkit.createInventory(this, getSize(), name);
            this.setMenuItems();
            playerMenuUtility.getOwner().openInventory(inventory);
    }
    public abstract void peform(InventoryClickEvent e);
    public abstract void setMenuItems();

    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
