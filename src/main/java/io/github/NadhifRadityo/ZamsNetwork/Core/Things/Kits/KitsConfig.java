package io.github.NadhifRadityo.ZamsNetwork.Core.Things.Kits;

import org.bukkit.inventory.ItemStack;

public class KitsConfig {
	private ItemStack display;
	private boolean showContents;
	
	public void setDisplay(ItemStack display) {
		this.display = display;
	}
	public void setShowContents(boolean showContents) {
		this.showContents = showContents;
	}
	
	public ItemStack getDisplay() {
		return this.display;
	}
	public boolean getShowContents() {
		return this.showContents;
	}
}
