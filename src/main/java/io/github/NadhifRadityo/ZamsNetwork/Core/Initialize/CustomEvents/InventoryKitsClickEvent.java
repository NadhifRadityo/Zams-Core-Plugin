package io.github.NadhifRadityo.ZamsNetwork.Core.Initialize.CustomEvents;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.NadhifRadityo.ZamsNetwork.Core.Things.Kits.InventoryKitsClickTypes;
import io.github.NadhifRadityo.ZamsNetwork.Core.Things.Kits.KitsHelper;

public class InventoryKitsClickEvent extends Event{
	private static final HandlerList handlers = new HandlerList();
	private InventoryClickEvent event;
	private KitsHelper helper;
	private Player player;
	private Inventory inv;
	private Inventory topinv;
	private ItemStack item;
	private int slot;
	private int rawSlot;
	private ClickType click;
	private InventoryKitsClickTypes kitsClickType;
	
	public InventoryKitsClickEvent(InventoryClickEvent event, KitsHelper helper, Player player, Inventory inventory, Inventory topinv, ItemStack item, int slot, int rawslot, ClickType click, InventoryKitsClickTypes kitsClickType) {
		this.event = event;
		this.helper = helper;
		this.player = player;
		this.inv = inventory;
		this.topinv = topinv;
		this.item = item;
		this.slot = slot;
		this.rawSlot = rawslot;
		this.click = click;
		this.kitsClickType = kitsClickType;
	}
	
	public InventoryKitsClickEvent(InventoryClickEvent event, InventoryKitsClickTypes kitsClickType, KitsHelper helper) {
		this(event, helper, (Player) event.getWhoClicked(), event.getInventory(), event.getView().getTopInventory(), event.getCurrentItem(), event.getSlot(), event.getRawSlot(), event.getClick(), kitsClickType);
	}
	
	public InventoryClickEvent getEvent() {
		return event;
	}
	
	public KitsHelper getKitsHelper() {
		return helper;
	}
	
	public HandlerList getHandlers() {
		return handlers;
    }
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

	public Player getPlayer() {
		return player;
	}

	public Inventory getInv() {
		return inv;
	}

	public Inventory getTopinv() {
		return topinv;
	}

	public ItemStack getItem() {
		return item;
	}

	public int getSlot() {
		return slot;
	}

	public int getRawSlot() {
		return rawSlot;
	}

	public ClickType getClick() {
		return click;
	}
	
	public InventoryKitsClickTypes getKitsClickType() {
		return kitsClickType;
	}
}	
