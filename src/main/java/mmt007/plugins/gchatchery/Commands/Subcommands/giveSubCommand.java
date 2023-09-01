package mmt007.plugins.gchatchery.Commands.Subcommands;

import mmt007.plugins.gchatchery.Commands.Subcommand;
import mmt007.plugins.gchatchery.GCHatchery;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class giveSubCommand extends Subcommand {
    Plugin main = GCHatchery.getPlugin();
    String hatcheryMaterialName = main.getConfig().getString("Hatchery-Block");
    String hatcheryName = main.getConfig().getString("Hatchery-Name");
    List<String> hatcheryLore = main.getConfig().getStringList("Hatchery-Lore");
    @Override
    public @NotNull String getName() {return "give";}

    @Override
    public @NotNull String getDescription() {return "Gives Hatchery To A Player";}

    @Override
    public @NotNull String getSyntax() {
        return "/gch give [Player]";}

    @Override
    public void peform(Player plr, String[] args) {
        //Checks If Player Gave a Name
        if(args.length == 1){
            PlayerInventory inv = plr.getInventory();
            inv.addItem(getHactchery());
            return;
        }

        //Gets The Player To Be Given, And Gives
        String plrName = args[1];
        Player plrToGive = Bukkit.getPlayer(plrName);

        if(plrToGive == null) {plr.sendMessage("§cPlayer Does Not Exist");return;}

        PlayerInventory inv = plrToGive.getInventory();
        inv.addItem(getHactchery());
    }

    //Sets Default Value For Hatchery Item
    public ItemStack getHactchery(){
        Material hatcheryMaterial = Material.getMaterial(
                hatcheryMaterialName.toUpperCase());
        ItemStack hatchery = new ItemStack(hatcheryMaterial);
        ItemMeta hatcheryMeta = hatchery.getItemMeta();

        try {
            hatcheryMeta.addEnchant(Enchantment.PIERCING, 1, false);
            hatcheryMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            hatcheryMeta.setDisplayName(hatcheryName);
            hatcheryMeta.setLore(hatcheryLore);

            hatcheryMeta.getPersistentDataContainer().set(
                    new NamespacedKey(main,"Hatchery"),
                    PersistentDataType.BOOLEAN,
                    true
            );
        }catch (NullPointerException e){
            e.printStackTrace();
            return new ItemStack(Material.AIR);
        }

        hatchery.setItemMeta(hatcheryMeta);
        return hatchery;
    }
}
