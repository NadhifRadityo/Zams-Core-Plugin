package io.github.NadhifRadityo.ZamsNetwork.Core.Things.Kits;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.NadhifRadityo.ZamsNetwork.Core.Utils;
import io.github.NadhifRadityo.ZamsNetwork.Core.Exceptions.ConfigException;
import io.github.NadhifRadityo.ZamsNetwork.Core.Exceptions.FileException;
import io.github.NadhifRadityo.ZamsNetwork.Core.Exceptions.InventoryException;
import io.github.NadhifRadityo.ZamsNetwork.Core.Exceptions.KitsException;
import io.github.NadhifRadityo.ZamsNetwork.Core.Helper.GUIHelper;
import io.github.NadhifRadityo.ZamsNetwork.Core.Initialize.Commands.GetCommandExecutorClass;
import io.github.NadhifRadityo.ZamsNetwork.Core.Initialize.CustomEvents.InventoryKitsClickEvent;
import io.github.NadhifRadityo.ZamsNetwork.Core.Object.GUI;
import io.github.NadhifRadityo.ZamsNetwork.Core.Object.GUIDecor;
import io.github.NadhifRadityo.ZamsNetwork.Core.Object.Input;
import io.github.NadhifRadityo.ZamsNetwork.Core.Object.Input.responseEvent;
import io.github.NadhifRadityo.ZamsNetwork.Core.Object.InputMethod;
import io.github.NadhifRadityo.ZamsNetwork.Core.Object.Title;
import io.github.NadhifRadityo.ZamsNetwork.Core.Utilization.InventoryUtils;
import io.github.NadhifRadityo.ZamsNetwork.Core.Utilization.PlayerUtils;
import io.github.NadhifRadityo.ZamsNetwork.Main.Main;

public class Kits implements CommandExecutor, GetCommandExecutorClass, Listener {
	private Main Plugin;
	private boolean isInit = false;
	
	private YamlConfiguration KitsConfig;
	private YamlConfiguration KitsMessages;
	
	private KitsHelper KitsHelper;
	
	@Override
	public boolean initMain(Main plugins) {
		this.Plugin = plugins;
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
					},
					{
						"config.showIfDoesntHasPermission",
						true
					}
//					{
//						"config.display",				// causing kits title not displayed
//						new ItemStack(Material.CHEST, 1)
//					}
				});
			}catch(KitsException | InventoryException | ConfigException e){
				e.printStackTrace();
				KitsHelper.sendChat(player, Utils.replace(KitsMessages.getString("CanNotCreateKits"), "<Error>", ExceptionUtils.getRootCauseMessage(e)));
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
				KitsHelper.sendChat(player, Utils.replace(KitsMessages.getString("CanNotDeleteKits"), "<Error>", ExceptionUtils.getRootCauseMessage(e)));
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
			KitsHelper.sendChat(player, Utils.replace(KitsMessages.getString("CanNotDeleteKits"), "<Error>", ExceptionUtils.getRootCauseMessage(e)));
			return true;
		}
	}
	
	private boolean openKits(Player player, String[] args) {
		KitsObject[] kitsList = null;
		GUI inv = null;
		try {
			kitsList = this.KitsHelper.getAllKits();
			inv = new GUI(KitsConfig.getString("GuiTitle"), KitsHelper.getSlots(kitsList), false, new GUI.OptionClickEventHandler() {
				@Override
				public void onOptionClick(GUI.OptionClickEvent event) {
					InventoryKitsClickEvent callEvent = new InventoryKitsClickEvent(event.getEvent(), InventoryKitsClickTypes.CHOOSE_KITS, KitsHelper);
					Plugin.getServer().getPluginManager().callEvent(callEvent);
					
					String[] kaboom = KitsConfig.getString("loreTag.kitsName").split("<Name>");
					List<String> lores = event.getItem().getItemMeta().getLore();
					String nameLore = null;
					
					for(String lore : lores) {
						if(lore.startsWith(Utils.Chat(kaboom[0]))) {
							nameLore = lore;
						}
					}
					
					String kitsName = nameLore.split(Utils.Chat(kaboom[0]))[1];
					kitsName = kaboom.length > 1 ? kitsName.split(Utils.Chat(kaboom[1]))[0] : kitsName;
					
					if(event.getEvent().isLeftClick()) {
						if(KitsConfig.getString("getKits").equalsIgnoreCase("Left")) {
							event.getPlayer().closeInventory();
							event.getPlayer().chat("/kits get " + kitsName);
						}else if(KitsConfig.getString("seeKitsContents").equalsIgnoreCase("Left")) {
							event.getPlayer().closeInventory();
							event.getPlayer().chat("/kits info " + kitsName);
						}
					}else if(event.getEvent().isRightClick()) {
						if(KitsConfig.getString("getKits").equalsIgnoreCase("Right")) {
							event.getPlayer().closeInventory();
							event.getPlayer().chat("/kits get " + kitsName);
						}else if(KitsConfig.getString("seeKitsContents").equalsIgnoreCase("Right")) {
							event.getPlayer().closeInventory();
							event.getPlayer().chat("/kits info " + kitsName);
						}
					}
				}
			}, this.Plugin);
//			inv = GUIHelper.createGUI(KitsHelper.getSlots(kitsList.length), KitsConfig.getString("GuiTitle"));
//			KitsHelper.sendChat(player, "b: '" + kitsList.length / 9 + "a: '" + ((kitsList.length / 9 + 1) * 9));
		} catch (KitsException e) {
			KitsHelper.sendChat(player, Utils.replace(KitsMessages.getString("CanNotOpenKits"), "<Error>", ExceptionUtils.getRootCauseMessage(e)));
		}
		
		for(KitsObject kit : kitsList) {
			ArrayList<String> Lore = new ArrayList<String>();
			ItemStack item;
			
			if(kit.getPermissions() != null && !PlayerUtils.playerHasPermissions(player, kit.getPermissions()) && !kit.config.showIfDoesntHasPermission()) {
				continue; //skip;
			}
			
			if(kit.config.getDisplay() != null) {
				item = kit.config.getDisplay();
				ItemMeta newMeta = item.getItemMeta();
				
				if(newMeta.getLore() != null) { //https://bukkit.org/threads/getting-item-lore-error.354780/
					Lore.addAll(newMeta.getLore());
				}
			} else {
				item = GUIHelper.createGuiItem(Utils.Chat(Utils.replace(KitsConfig.getString("defaultItemName"), "<Kit Name>", kit.getName())), 1, Lore, Material.CHEST);
			}

			if(kit.config.getShowContents() == true) {
				Lore.add(Utils.Chat(KitsConfig.getString("seeKitsContents").equalsIgnoreCase("Right") ? KitsConfig.getString("loreTag.kitsRightClick") : KitsConfig.getString("loreTag.kitsLeftClick")));
			}
			Lore.add(Utils.Chat(KitsConfig.getString("getKits").equalsIgnoreCase("Left") ? KitsConfig.getString("loreTag.kitsLeftClick") : KitsConfig.getString("loreTag.kitsRightClick")));
			Lore.add("");
			Lore.add(Utils.Chat(Utils.replace(KitsConfig.getString("loreTag.kitsName"), "<Name>", kit.getName())));
			Lore.add(Utils.Chat("&6Cost: " + kit.getCost()));
			
			String[] perms = kit.getPermissions();
			Lore.add(Utils.Chat(Utils.replace(KitsConfig.getString("loreTag.kitsPermission"), "<Permission>", perms == null ? "Everyone" : (perms.length > 1 ? "" : perms[0]))));
			if(perms != null && perms.length > 1) {
//				Lore.add(" &6- " );
				for(String perm : perms) {
					Lore.add(Utils.Chat(" &6- " + perm));
				}
			}
			
			if(kit.config.getShowContents() == true) {
				Lore.addAll(kit.getLore());
			}
			
			ItemMeta meta = item.getItemMeta();
			meta.setLore(Lore);
			item.setItemMeta(meta);

			inv.setOption(inv.getNullIndex(), item, true);
		}
		inv.open(player);
		
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
				KitsHelper.sendChat(player, Utils.replace(KitsMessages.getString("CanNotConfigKits"), "<Error>", ExceptionUtils.getRootCauseMessage(e)));
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
			KitsHelper.sendChat(player, Utils.replace(KitsMessages.getString("CanNotConfigKits"), "<Error>", ExceptionUtils.getRootCauseMessage(e)));
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
				KitsHelper.sendChat(player, Utils.replace(KitsMessages.getString("CanNotConfigKits"), "<Error>", ExceptionUtils.getRootCauseMessage(e)));
				return true;
			}
		} else {
			KitsHelper.sendChat(player, KitsMessages.getString("KitsNameNotIncluded"));
			return true;
		}
		
		try {
			this.Plugin.Helper.ConfigHelper.updateYaml(KitsConfig.getString("storeKits"), nameKits, new Object[][] {
				{
					"config.showContents",
					Boolean.parseBoolean(args[2])
				}
			});
		} catch (ConfigException e) {
			e.printStackTrace();
			KitsHelper.sendChat(player, Utils.replace(KitsMessages.getString("CanNotConfigKits"), "<Error>", ExceptionUtils.getRootCauseMessage(e)));
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
		}else {
			KitsHelper.sendChat(player, KitsMessages.getString("KitsNameNotIncluded"));
			return true;
		}
		return true;
	}
	
	private void kitsConsole() {
		System.out.println("Command ini hanya bisa diakses oleh player!");
	}
	
	private boolean showContents(Player player, String[] args) {
		String nameKits = null;
		if(args.length > 1) {
			nameKits = this.KitsHelper.getKitsName(1, args);
			if(nameKits == null) {
				KitsHelper.sendChat(player, KitsMessages.getString("IllegalKitsName"));
				return true;
			}
		}else {
			KitsHelper.sendChat(player, KitsMessages.getString("KitsNameNotIncluded"));
			return true;
		}
		
		KitsObject kit = null;
		try {
			if(!this.KitsHelper.isKitsExist(nameKits)) {
				KitsHelper.sendChat(player, KitsMessages.getString("KitsNotFound"));
				return true;
			}
			kit = this.KitsHelper.getKits(nameKits);
			
			ItemStack[] contents = kit.getContents()[1];
			GUI inv = new GUI(kit.getName(), 54, false, new GUI.OptionClickEventHandler() {
				@Override
				public void onOptionClick(GUI.OptionClickEvent event) {
					InventoryKitsClickEvent callEvent = new InventoryKitsClickEvent(event.getEvent(), InventoryKitsClickTypes.SHOW_CONTENTS, KitsHelper);
					Plugin.getServer().getPluginManager().callEvent(callEvent);
					
					event.setCancelled(true);
					event.setWillClose(false);
					if(event.getEvent().getRawSlot() == 53) {
						event.getPlayer().closeInventory();
					}else if(event.getEvent().getRawSlot() == 45) {
						event.getPlayer().closeInventory();
						event.getPlayer().chat("/kits");
					}else if(event.getEvent().getRawSlot() == 52) {
						event.getPlayer().closeInventory();
						event.getPlayer().chat("/kits edit " + event.getGUI().getName());
					}
				}
			}, this.Plugin);
			inv.setContents(contents, true, 0);
			inv.setDecoration(GUIDecor.RANDOM_SAME_TYPE, 36, 53);
			
			ItemStack back = new ItemStack(Material.ARROW);
			ItemMeta backMeta = back.getItemMeta();
			backMeta.setDisplayName(Utils.Chat("&aBack"));
			back.setItemMeta(backMeta);
			inv.setOption(45, back, true);
			
			if(player.hasPermission("kit.edit." + kit.getName())) {
				ItemStack edit = new ItemStack(Material.STICK);
				ItemMeta editMeta = edit.getItemMeta();
				editMeta.setDisplayName(Utils.Chat("&6Edit"));
				edit.setItemMeta(editMeta);
				inv.setOption(52, edit, true);
			}
			
			ItemStack close = new ItemStack(Material.BARRIER);
			ItemMeta closeMeta = close.getItemMeta();
			closeMeta.setDisplayName(Utils.Chat("&cClose"));
			close.setItemMeta(closeMeta);
			inv.setOption(53, close, true);
			
			inv.open(player);
			
		} catch (KitsException e) {
			KitsHelper.sendChat(player, Utils.replace(KitsMessages.getString("CanNotGiveKits"), "<Error>", ExceptionUtils.getRootCauseMessage(e)));
		}
		return true;
	}
	
	private boolean getKit(Player player, String[] args) {
		String nameKits = null;
		
		if(args.length > 1) {
			nameKits = this.KitsHelper.getKitsName(1, args);
			if(nameKits == null) {
				KitsHelper.sendChat(player, KitsMessages.getString("IllegalKitsName"));
				return true;
			}
		}else {
			KitsHelper.sendChat(player, KitsMessages.getString("KitsNameNotIncluded"));
			return true;
		}
		
		try {
			if(!this.KitsHelper.isKitsExist(nameKits)) {
				KitsHelper.sendChat(player, KitsMessages.getString("KitsNotFound"));
				return true;
			}
			KitsObject kit = this.KitsHelper.getKits(nameKits);
			
			if(kit.getPermissions() != null) {
				if(kit.getPermissions() == null || PlayerUtils.playerHasPermissions(player, kit.getPermissions())) {
					InventoryUtils.addItemsToPlayer(player, kit.getContents()[1]);
					KitsHelper.sendChat(player, KitsMessages.getString("SuccessGiveKits"));
					return true;
				}else {
					KitsHelper.sendChat(player, KitsMessages.getString("DoNotHavePermission"));
				}
			}else {
				InventoryUtils.addItemsToPlayer(player, kit.getContents()[1]);
				KitsHelper.sendChat(player, KitsMessages.getString("SuccessGiveKits"));
				return true;
			}
		} catch (KitsException e) {
			KitsHelper.sendChat(player, Utils.replace(KitsMessages.getString("CanNotGiveKits"), "<Error>", ExceptionUtils.getRootCauseMessage(e)));
		}
		return true;
	}
	
	private void setKitsContents(ItemStack[] items, String kitsName) {
		ItemStack[][] data = new ItemStack[2][];
		data[0] = new ItemStack[4];
		for(int i = 0; i < data[0].length; i++) {
			data[0][i] = null;
		}
		data[1] = items;
		
		try {
			this.Plugin.Helper.InventoryHelper.updateInventory(data, null, KitsConfig.getString("storeKits"), kitsName);
		} catch (InventoryException e) {
			e.printStackTrace();
		}
	}
	
	private void editPermission(String kitName, Player player) {
		player.sendMessage(kitName);
		Input getInput = new Input(new Title("Masukkan nama permission", "", 10, 15, 10), new Input.responseEventHandler() {
			@Override
			public void onResponse(responseEvent event) {
				event.getPlayer().sendMessage(event.getResponse());
			}
		}, this.Plugin);
		String error = getInput.onlyAccept("^[A-Za-z.]*$"); //all alphabet and dot
		if(error != null) {
			player.sendMessage(error);
		}
		getInput.setInputMethod(InputMethod.SIGN);
		getInput.send(player);
	}
	
	private boolean editKit(Player player, String[] args) {
		String nameKits = null;
		
		if(args.length > 1) {
			nameKits = this.KitsHelper.getKitsName(1, args);
			if(nameKits == null) {
				KitsHelper.sendChat(player, KitsMessages.getString("IllegalKitsName"));
				return true;
			}
		}else {
			KitsHelper.sendChat(player, KitsMessages.getString("KitsNameNotIncluded"));
			return true;
		}
		
		try {
			if(!this.KitsHelper.isKitsExist(nameKits)) {
				KitsHelper.sendChat(player, KitsMessages.getString("KitsNotFound"));
				return true;
			}
			
			KitsObject kit = this.KitsHelper.getKits(nameKits);
			ItemStack[] contents = kit.getContents()[1];
			GUI inv = new GUI(kit.getName(), 54, false, new GUI.OptionClickEventHandler() {
				@Override
				public void onOptionClick(GUI.OptionClickEvent event) {
					InventoryKitsClickEvent callEvent = new InventoryKitsClickEvent(event.getEvent(), InventoryKitsClickTypes.EDIT_KITS, KitsHelper);
					Plugin.getServer().getPluginManager().callEvent(callEvent);
					
					event.setWillClose(false);
					if(event.getEvent().getRawSlot() == 53) {
						event.getPlayer().closeInventory();
					}else if(event.getEvent().getRawSlot() == 52 && event.getItem().getItemMeta().getDisplayName().equals(Utils.Chat("&aBack"))) {
						event.getPlayer().closeInventory();
						event.getPlayer().chat("/kits info " + event.getGUI().getName());
					}else if(event.getEvent().getRawSlot() == 45 && event.getItem().getItemMeta().getDisplayName().equals(Utils.Chat("&6Edit Contents"))) {
						event.getGUI().setUnstealable(0, 35, false);
						
						ItemStack editContents = new ItemStack(Material.BOOK_AND_QUILL);
						ItemMeta editContentsMeta = editContents.getItemMeta();
						editContentsMeta.setDisplayName(Utils.Chat("&aFinish Edit"));
						editContents.setItemMeta(editContentsMeta);

						event.getGUI().setOption(45, editContents, true); //update so event called
						event.getPlayer().getOpenInventory().setItem(45, editContents);
						event.getGUI().setCanPutInGui(true);
						
					}else if(event.getEvent().getRawSlot() == 45 && event.getItem().getItemMeta().getDisplayName().equals(Utils.Chat("&aFinish Edit"))) {
						ItemStack[] items = event.getPlayer().getOpenInventory().getTopInventory().getContents();
						List<ItemStack> kitItems = new ArrayList<ItemStack>();
						for(int i = 0; i < items.length; i++) {
							if(!event.getGUI().getUnstealable().contains(i)) {
								kitItems.add(items[i]);
							}
						}
						setKitsContents(kitItems.toArray(new ItemStack[kitItems.size()]), event.getGUI().getName());
						event.getGUI().setUnstealable(0, 35, true);
						
						ItemStack editContents = new ItemStack(Material.STICK);
						ItemMeta editContentsMeta = editContents.getItemMeta();
						editContentsMeta.setDisplayName(Utils.Chat("&6Edit Contents"));
						editContents.setItemMeta(editContentsMeta);
						
						event.getGUI().setOption(45, editContents, true);
						event.getPlayer().getOpenInventory().setItem(45, editContents);
						event.getGUI().setCanPutInGui(false);
					}else if(event.getEvent().getRawSlot() == 46 && event.getItem().getItemMeta().getDisplayName().equals(Utils.Chat("&6Set Cost"))) {
//						event.getPlayer()
					}else if(event.getEvent().getRawSlot() == 47 && event.getItem().getItemMeta().getDisplayName().equals(Utils.Chat("&6Set Permission"))) {
						event.getPlayer().closeInventory();
						editPermission(event.getGUI().getName(), event.getPlayer());
					}
				}
			}, this.Plugin);
			inv.setContents(contents, true, 0);
//			inv.setContents(contents, false, 0);
			
//			ItemStack decor = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)4);
//			ItemMeta decorMeta = decor.getItemMeta();
//			decorMeta.setDisplayName("");
//			decor.setItemMeta(decorMeta);
//			
//			for(int i = 36; i < 53; i++) {
//				inv.setOption(i, decor, true);
//			}
			inv.setDecoration(GUIDecor.RANDOM_SAME_TYPE, 36, 53);
			
			ItemStack editContents = new ItemStack(Material.STICK);
			ItemMeta editContentsMeta = editContents.getItemMeta();
			editContentsMeta.setDisplayName(Utils.Chat("&6Edit Contents"));
			editContents.setItemMeta(editContentsMeta);
			inv.setOption(45, editContents, true);
			
			ItemStack setCost = new ItemStack(Material.PAPER);
			ItemMeta setCostMeta = setCost.getItemMeta();
			setCostMeta.setDisplayName(Utils.Chat("&6Set Cost"));
			setCost.setItemMeta(setCostMeta);
			inv.setOption(46, setCost, true);
			
			ItemStack setPermission = new ItemStack(Material.SKULL_ITEM);
			ItemMeta setPermissionMeta = setCost.getItemMeta();
			setPermissionMeta.setDisplayName(Utils.Chat("&6Set Permission"));
			setPermission.setItemMeta(setPermissionMeta);
			inv.setOption(47, setPermission, true);
			
			ItemStack back = new ItemStack(Material.ARROW);
			ItemMeta backMeta = back.getItemMeta();
			backMeta.setDisplayName(Utils.Chat("&aBack"));
			back.setItemMeta(backMeta);
			inv.setOption(52, back, true);
			
			ItemStack close = new ItemStack(Material.BARRIER);
			ItemMeta closeMeta = close.getItemMeta();
			closeMeta.setDisplayName(Utils.Chat("&cClose"));
			close.setItemMeta(closeMeta);
			inv.setOption(53, close, true);
			
			inv.open(player);
		}catch(KitsException e) {
			
		}
		return true;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length > 0) {
			if(args[0].equalsIgnoreCase("test")) {
				return true;
			}
		}
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
				}else if(args[0].equalsIgnoreCase("get")) {
					return this.getKit(player, args);
				}else if(args[0].equalsIgnoreCase("info")) {
					return this.showContents(player, args);
				}else if(args[0].equalsIgnoreCase("edit")) {
					return this.editKit(player, args);
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
