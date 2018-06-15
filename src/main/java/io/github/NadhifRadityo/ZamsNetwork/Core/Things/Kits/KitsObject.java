package io.github.NadhifRadityo.ZamsNetwork.Core.Things.Kits;

import java.util.ArrayList;

import org.bukkit.inventory.ItemStack;

import io.github.NadhifRadityo.ZamsNetwork.Core.Utils;
import io.github.NadhifRadityo.ZamsNetwork.Core.Helper.InventoryHelper;

public class KitsObject {
	private String name;
	private ItemStack[][] contents;
	private String permission;
	private int cost;
	public KitsConfig config = new KitsConfig();

	public ArrayList<String> getLore(int maxDisplay){
		ArrayList<String> Lore = new ArrayList<String>();
		
		int trueLength = InventoryHelper.countItems(this.contents[1]);
		Lore.add(Utils.Chat("&6List: "));
		int val = 1;
		for(ItemStack item : this.contents[1]) { //not using armor inventory because it already in contents which is list[1]
			if(item != null) {
				if(val <= maxDisplay) {
					Lore.add(Utils.Chat(" &l&8" + val + ".&r &7" + Utils.getName(item) + (item.getAmount() == 1 ? "" : " x" + item.getAmount())));
				}else {
					Lore.add(Utils.Chat(" &l&8-&r &7Tersisa &a" + (trueLength - maxDisplay) + " &7Item Lagi"));
					break;
				}
				val++;
			}
		}
		
		return Lore;
	}
	public ArrayList<String> getLore(){
		return this.getLore(9);
	}
	
	public void setName(String name) {
		if(name == null) {
			System.out.println("name null");
		}
		this.name = name;
	}
	public void setContents(ItemStack[][] contents) {
		if(contents == null) {
			System.out.println("contents null");
		}
		this.contents = contents;
	}
	public void setConfig(KitsConfig config) {
		if(config == null) {
			System.out.println("config null");
		}
		this.config = config;
	}
	public void setPermission(String permission) {
		if(permission == null) {
			System.out.println("permission null");
		}
		this.permission = "a";
	}
	public void setCost(int cost) {
		this.cost = cost;
	}
	
	public String getName() {
		return this.name;
	}
	public ItemStack[][] getContents(){
		return this.contents;
	}
	public KitsConfig getConfig() {
		return this.config;
	}
	public String getPermission() {
		return this.permission;
	}
	public int getCost() {
		return this.cost;
	}
}
