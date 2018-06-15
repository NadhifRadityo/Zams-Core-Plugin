package io.github.NadhifRadityo.ZamsNetwork.Core.Helper;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GUIHelper {
	public static ArrayList<Object> unstealable = new ArrayList<Object>();
	
	public static Inventory createGUI(int size, String name) {
		return Bukkit.createInventory(null, size, name);
	}
	
	public static void addItem(Inventory GUI, ItemStack item) {
		GUI.addItem(item);
	}
	
	public static void show(Player player, Inventory inv) {
		player.openInventory(inv);
	}
	
	public static ItemStack createGuiItem(String name, int stack, ArrayList<String> desc, Material mat) {
        ItemStack i = new ItemStack(mat, stack);
        ItemMeta iMeta = i.getItemMeta();
        iMeta.setDisplayName(name);
        iMeta.setLore(desc);
        i.setItemMeta(iMeta);
        return i;
    }

	public static boolean hasClickedTop(InventoryClickEvent event) {
        return event.getRawSlot() == event.getSlot();
    }
}
