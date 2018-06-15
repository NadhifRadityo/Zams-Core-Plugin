package io.github.NadhifRadityo.ZamsNetwork.Core.Things.Kits;

import java.util.ArrayList;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.NadhifRadityo.ZamsNetwork.Core.GetCommandExecutorClass;
import io.github.NadhifRadityo.ZamsNetwork.Core.Utils;
import io.github.NadhifRadityo.ZamsNetwork.Core.Exceptions.FileException;
import io.github.NadhifRadityo.ZamsNetwork.Core.Exceptions.InventoryException;
import io.github.NadhifRadityo.ZamsNetwork.Core.Exceptions.KitsException;
import io.github.NadhifRadityo.ZamsNetwork.Core.Exceptions.ConfigException;
import io.github.NadhifRadityo.ZamsNetwork.Core.Helper.GUIHelper;
import io.github.NadhifRadityo.ZamsNetwork.Main.Main;

public class Kits implements CommandExecutor, GetCommandExecutorClass, Listener{
	private Main Plugin;
	private boolean isInit = false;
	
	private YamlConfiguration KitsConfig;
	private YamlConfiguration KitsMessages;
	
	private KitsHelper KitsHelper;
	
	@Override
	public boolean initMain(Main plugins) {
		this.Plugin = plugins;
		
		Bukkit.getPluginManager().registerEvents(this, this.Plugin);
		this.KitsHelper = new KitsHelper(this.Plugin);
		this.init();
		
		this.isInit = true;
		return this.isInit;
	}
	
	@Override
	public boolean isInit() {
		return this.isInit;
	}
	
	public void init() {
		try {
			this.KitsConfig = this.KitsHelper.getKitsConfig();
			this.KitsMessages = this.KitsHelper.getKitsMessages();
		} catch (ConfigException e) {
			this.Plugin.Helper.Throw("Can not get kits configs!");
		}
	}
	
	//COMMAND HANDLER
	private boolean setKits(Player player, String args[]) {
		if(args.length > 1) { //Check if the second argument is set
			String nameKits = this.KitsHelper.getKitsName(1, args);
			if(nameKits == null) {
				KitsHelper.sendChat(player, KitsMessages.getString("IllegalKitsName"));
				return true;
			}
			try{
				if(this.KitsHelper.isKitsExist(nameKits)) {
					KitsHelper.sendChat(player, KitsMessages.getString("SuccessChangeKits"));
				} else {
					KitsHelper.sendChat(player, KitsMessages.getString("SuccessCreateKits"));
				}
				this.Plugin.Helper.InventoryHelper.save(player, null, KitsConfig.getString("storeKits"), nameKits);
				this.Plugin.Helper.ConfigHelper.updateYaml(KitsConfig.getString("storeKits"), nameKits, new Object[][] {
					{
						"config.showContents",
						true
					}
//					{
//						"config.display",				// causing kits title not displayed
//						new ItemStack(Material.CHEST, 1)
//					}
				});
			}catch(KitsException | InventoryException | ConfigException e){
				e.printStackTrace();
				KitsHelper.sendChat(player, Utils.replace(KitsMessages.getString("CanNotCreateKits"), "<StackTrace>", ExceptionUtils.getStackTrace(e)));
				return true;
			}
			return true;
		} else {
			KitsHelper.sendChat(player, KitsMessages.getString("KitsNameNotIncluded"));
			return true;
		}
	}
	
	private boolean deleteKits(Player player, String[] args) {
		String nameKits;
		if(args.length > 1) {
			nameKits = this.KitsHelper.getKitsName(1, args);
			if(nameKits == null) {
				KitsHelper.sendChat(player, KitsMessages.getString("IllegalKitsName"));
				return true;
			}
			try {
				if(!this.KitsHelper.isKitsExist(nameKits)) {
					KitsHelper.sendChat(player, KitsMessages.getString("KitsNotFound"));
					return true;
				}
			} catch (KitsException e) {
				e.printStackTrace();
				KitsHelper.sendChat(player, Utils.replace(KitsMessages.getString("CanNotDeleteKits"), "<StackTrace>", ExceptionUtils.getStackTrace(e)));
				return true;
			}
		} else {
			KitsHelper.sendChat(player, KitsMessages.getString("KitsNameNotIncluded"));
			return true;
		}
		
		try {
			if(this.Plugin.Helper.FileHelper.getFile("config/kits/", nameKits + ".yml").delete()) {
				KitsHelper.sendChat(player, KitsMessages.getString("SuccessDeleteKits"));
				return true;
			} else {
				KitsHelper.sendChat(player, KitsMessages.getString("CanNotDeleteKits"));
				return true;
			}
		} catch (FileException e) {
			KitsHelper.sendChat(player, Utils.replace(KitsMessages.getString("CanNotDeleteKits"), "<StackTrace>", ExceptionUtils.getStackTrace(e)));
			return true;
		}
	}
	
	private boolean openKits(Player player, String[] args) {
		KitsObject[] kitsList = null;
		try {
			kitsList = this.KitsHelper.getAllKits();
//			KitsHelper.sendChat(player, "b: '" + kitsList.length / 9 + "a: '" + ((kitsList.length / 9 + 1) * 9));
		} catch (KitsException e) {
			KitsHelper.sendChat(player, Utils.replace(KitsMessages.getString("CanNotOpenKits"), "<StackTrace>", ExceptionUtils.getStackTrace(e)));
		}
		
		Inventory inv = null;
		try {
			inv = GUIHelper.createGUI(KitsHelper.getSlots(kitsList.length), KitsConfig.getString("GuiTitle"));
		} catch (KitsException e) {
			KitsHelper.sendChat(player, Utils.replace(KitsMessages.getString("CanNotOpenKits"), "<StackTrace>", ExceptionUtils.getStackTrace(e)));
		}
		
		for(KitsObject kit : kitsList) {
			ArrayList<String> Lore = new ArrayList<String>();
			if(kit.config.getShowContents() == true) {
				Lore.addAll(kit.getLore());
			}
			
			//add item lore to item list lore
			ItemStack item;
			if(kit.config.getDisplay() != null) {
				item = kit.config.getDisplay();
				ItemMeta newMeta = item.getItemMeta();
				
				if(newMeta.getLore() != null) { //https://bukkit.org/threads/getting-item-lore-error.354780/
					Lore.add(0, ""); //new line
					Lore.addAll(0, newMeta.getLore());
				}
				
				newMeta.setLore(Lore);
				item.setItemMeta(newMeta);
			} else {
				item = GUIHelper.createGuiItem(Utils.Chat(Utils.replace(KitsConfig.getString("defaultItemName"), "<Kit Name>", kit.getName())), 1, Lore, Material.CHEST);
			}
			
			GUIHelper.addItem(inv, item);
		}
		GUIHelper.show(player, inv);
		
//		// Create a new ItemStack (type: diamond)
//		ItemStack diamond = new ItemStack(Material.DIAMOND);
//
//		// Create a new ItemStack (type: brick)
//		ItemStack bricks = new ItemStack(Material.BRICK);
//
//		// Set the amount of the ItemStack
//		bricks.setAmount(20);
//
//		// Give the player our items (comma-seperated list of all ItemStack)
//		player.getInventory().addItem(bricks, diamond);
		
		return true;
	}
	
	private boolean setConfigDisplay(Player player, String[] args) {
		String nameKits;
		if(args.length > 2) { //kits config[0] display[1] [kits][>1] length = >3
			nameKits = this.KitsHelper.getKitsName(2, args);
			if(nameKits == null) {
				KitsHelper.sendChat(player, KitsMessages.getString("IllegalKitsName"));
				return true;
			}
			try {
				if(!this.KitsHelper.isKitsExist(nameKits)) {
					KitsHelper.sendChat(player, KitsMessages.getString("KitsNotFound"));
					return true;
				}
			} catch (KitsException e) {
				e.printStackTrace();
				KitsHelper.sendChat(player, Utils.replace(KitsMessages.getString("CanNotConfigKits"), "<StackTrace>", ExceptionUtils.getStackTrace(e)));
				return true;
			}
		} else {
			KitsHelper.sendChat(player, KitsMessages.getString("KitsNameNotIncluded"));
			return true;
		}
		
		try {
			this.Plugin.Helper.ConfigHelper.updateYaml("config/kits/", nameKits, new Object[][] {
				{
					"config.display",
					player.getInventory().getItemInMainHand()
				}
			});
		} catch (ConfigException e) {
			e.printStackTrace();
			KitsHelper.sendChat(player, Utils.replace(KitsMessages.getString("CanNotConfigKits"), "<StackTrace>", ExceptionUtils.getStackTrace(e)));
			return true;
		}
		KitsHelper.sendChat(player, KitsMessages.getString("SuccessConfigKits"));
		return true;
	}
	
	private boolean setShowContents(Player player, String[] args) {
		String nameKits;
		if(args.length > 3) { //kits config[0] showcontents[1] [boolean][2] [kits][>2] length = >4
//			System.out.println(args[3]);
			nameKits = this.KitsHelper.getKitsName(3, args);
			if(nameKits == null) {
				KitsHelper.sendChat(player, KitsMessages.getString("IllegalKitsName"));
				return true;
			}
			try {
				if(!this.KitsHelper.isKitsExist(nameKits)) {
					KitsHelper.sendChat(player, KitsMessages.getString("KitsNotFound"));
					return true;
				}
			} catch (KitsException e) {
				e.printStackTrace();
				KitsHelper.sendChat(player, Utils.replace(KitsMessages.getString("CanNotConfigKits"), "<StackTrace>", ExceptionUtils.getStackTrace(e)));
				return true;
			}
		} else {
			KitsHelper.sendChat(player, KitsMessages.getString("KitsNameNotIncluded"));
			return true;
		}
		
		try {
			this.Plugin.Helper.ConfigHelper.updateYaml("config/kits/", nameKits, new Object[][] {
				{
					"config.showContents",
					Boolean.parseBoolean(args[2])
				}
			});
		} catch (ConfigException e) {
			e.printStackTrace();
			KitsHelper.sendChat(player, Utils.replace(KitsMessages.getString("CanNotConfigKits"), "<StackTrace>", ExceptionUtils.getStackTrace(e)));
			return true;
		}
		KitsHelper.sendChat(player, KitsMessages.getString("SuccessConfigKits"));
		return true;
	}
	
	private boolean setConfig(Player player, String[] args) {
		if(args.length > 1) {
			
			if(args[1].equals("display")) {
				return this.setConfigDisplay(player, args);
			} else if(args[1].equals("showcontents")){
				return this.setShowContents(player, args);
			}else {
				KitsHelper.sendChat(player, KitsMessages.getString("UnknownCommand"));
			}
		}
		return true;
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
//		try {
//			if(event.getInventory().getTitle() == "Kits" && event.getInventory().getSize() == this.KitsHelper.getSlots()){
//				Player player = (Player) event.getWhoClicked();
//				String clickedItemString = event.getCurrentItem().getItemMeta().getLocalizedName();
//				if(event.isLeftClick()) {
//					player.performCommand("kits info " + clickedItemString);
//				}if (event.isRightClick()) {
//					player.performCommand("kits get " + clickedItemString);
//				}
//			}
//		} catch (KitsException e) {
//			KitsHelper.sendChat((Player) event.getWhoClicked(), "Tidak dapat membuka kits!" + e.getStackTrace());
//		}
//		if(event.getRawSlot() == event.getSlot()) {
//			event.setCancelled(true);
//			event.getWhoClicked().sendMessage("APALO :V");
//		}
    }
	
	private void kitsConsole() {
		System.out.println("Command ini hanya bisa diakses oleh player!");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(this.isInit == false) {
			System.out.println("Kits belum di init!");
		}
		
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if(args.length > 0) { //Check if arguments is set
				if(args[0].equalsIgnoreCase("set")) {
					return this.setKits(player, args);
				}else if(args[0].equalsIgnoreCase("delete")) {
					return this.deleteKits(player, args);
				}else if(args[0].equalsIgnoreCase("config")) {
					return this.setConfig(player, args);
				}
			}
			return this.openKits(player, args);
	    }else {
	    	this.kitsConsole();
	    }
	    // If the player (or console) uses our command correct, we can return true
		return true;
	}
}
