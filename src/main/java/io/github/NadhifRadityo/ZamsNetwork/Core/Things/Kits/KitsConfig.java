package io.github.NadhifRadityo.ZamsNetwork.Core.Things.Kits;

import org.bukkit.inventory.ItemStack;

public class KitsConfig {
	private ItemStack display;
	private boolean showContents;
	private boolean showIfDoesntHasPermission = true;
	
	public KitsConfig(ItemStack display, boolean showContents, boolean showIfDoesntHasPermission) {
		this.display = display;
		this.showContents = showContents;
		this.showIfDoesntHasPermission = showIfDoesntHasPermission;
	}
	
	public ItemStack getDisplay() {
		return this.display;
	}
	public boolean getShowContents() {
		return this.showContents;
	}
	public boolean showIfDoesntHasPermission() {
		return this.showIfDoesntHasPermission;
	}
}
