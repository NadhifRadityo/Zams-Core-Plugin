package io.github.NadhifRadityo.ZamsNetwork.Core.Utilization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.NadhifRadityo.ZamsNetwork.Core.Exceptions.KitsException;
import net.minecraft.server.v1_12_R1.Material;

public class InventoryUtils {
	public static Inventory getPlayerMainInventory(Player player) {
		ItemStack[] storage = player.getInventory().getStorageContents();
		Inventory fakeInv = Bukkit.createInventory(null, storage.length, "canStore");
		fakeInv.setContents(storage);
		
		return fakeInv;
	}
	
	@SuppressWarnings("unlikely-arg-type")
	public static boolean isInventoryEnough(Inventory inv, ItemStack items) {
		List<ItemStack> contents = new ArrayList<ItemStack>(Arrays.asList(inv.getContents()));
		int counter = 0;
		for(ItemStack item : contents) {
			if(item == null || item.getType().equals(Material.AIR)) {
				counter += items.getMaxStackSize();
			}else if(item.equals(items)) {
				counter += item.getMaxStackSize() - item.getAmount();
			}
			
			if(counter >= items.getAmount()) {
				return true;
			}
		}
		return false;
	}
	
	public static void addItemsToPlayer(Player player, ItemStack[] items) {
		for(ItemStack item : items) {
			if(item != null) {
				Inventory fakeInv = getPlayerMainInventory(player);
				if(isInventoryEnough(fakeInv, item)) {
					player.getInventory().addItem(item);
				}else {
					player.getWorld().dropItem(player.getLocation(), item);
				}
			}
		}
	}
	
	public static int getSlots(int val) throws KitsException {
		return val % 9 > 0 ? (val / 9 + 1) * 9 : (val / 9) * 9;
	}
}
