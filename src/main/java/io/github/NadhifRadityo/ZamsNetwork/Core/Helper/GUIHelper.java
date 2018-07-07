package io.github.NadhifRadityo.ZamsNetwork.Core.Helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.NadhifRadityo.ZamsNetwork.Core.Object.GUI;
import io.github.NadhifRadityo.ZamsNetwork.Main.Main;

public class GUIHelper implements Listener{
	private Map<Player, Inventory> openedInventory = new HashMap<Player, Inventory>();
	private Map<Player, GUI> openedGui = new HashMap<Player, GUI>();
	private Main Plugin;
	
	public void initMain(Main plugins) {
		this.Plugin = plugins;
		this.Plugin.getServer().getPluginManager().registerEvents(this, this.Plugin);
	}
	
	public static Inventory createGUI(int size, String name) {
		return Bukkit.createInventory(null, size, name);
	}
	
	public static void addItem(Inventory GUI, ItemStack item, int slot) {
		GUI.setItem(slot, item);
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
	
	public void addOpenInventory(Player player, Inventory inv) {
		this.openedInventory.put(player, inv);
	}
	public boolean isInventoryOpen(Player player, Inventory inv) {
		return this.openedInventory.containsKey(player) ? this.openedInventory.get(player).equals(inv) : false;
	}
	public void removeOpenInventory(Player player, Inventory inv) {
		if(this.isInventoryOpen(player, inv)) {
			this.openedInventory.remove(player, inv);
		}
	}
	public Map<Player, Inventory> getInventory(){
		return openedInventory;
	}
	@EventHandler
	public void onInventoryOpen(InventoryOpenEvent event) {
		this.addOpenInventory((Player) event.getPlayer(), event.getInventory());
	}
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		this.removeOpenInventory((Player) event.getPlayer(), event.getInventory());
	}
	
	
	public void addOpenGui(Player player, GUI inv) {
		this.openedGui.put(player, inv);
	}
	public boolean isGuiOpen(Player player, GUI inv) {
		return this.openedGui.containsKey(player) ? this.openedGui.get(player).equals(inv) : false;
	}
	public void removeOpenGui(Player player, GUI inv) {
		if(this.isGuiOpen(player, inv)) {
			this.openedGui.remove(player, inv);
		}
	}
	public Map<Player, GUI> getGui(){
		return openedGui;
	}
	
	public void destroyAllGui() {
		for(Entry<Player, GUI> pair : this.openedGui.entrySet()) {
			GUI gui = pair.getValue();
			Player key = pair.getKey();
			if(gui.isDestroyed() == false) {
				System.out.println("Destroying gui: '" + gui.getName() + "' with player: " + key.getName());
				gui.destroy();
			}
		}
	}
}
