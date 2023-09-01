package mmt007.plugins.gchatchery.MenuMngr.Utils;

import mmt007.plugins.gchatchery.GCHatchery;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;

public class MenuCreationUtil {
    static Plugin main = GCHatchery.getPlugin();

    //Creates Item
    public static ItemStack createItem(String name, Material material,String[] lore){
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();

        itemMeta.setDisplayName(name);
        itemMeta.setLore(Arrays.asList(lore));
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(main,"MenuItem"), PersistentDataType.BOOLEAN,true);

        item.setItemMeta(itemMeta);

        return item;
    }

    //Creates Background Items
    public static ArrayList<ItemStack> createBackGround(int Size, Material material){
        ArrayList<ItemStack> items = new ArrayList<>(Size);

        ItemStack backGroundItem = createItem(
                " ",
                material,
                new String[]{});

        while (items.size() < Size) {items.add(backGroundItem);}

        return items;
    }

    //Changes ArrayList<ItemsStack> to ItemStack[]
    //(Default One Giving Errors)
    public static ItemStack[] toArray(ArrayList<ItemStack> arrayList){
        ItemStack[] list = new ItemStack[arrayList.size()];
        for(int i = 0; i < arrayList.size(); i ++){
            list[i] = arrayList.get(i);
        }

        return list;
    }
}
