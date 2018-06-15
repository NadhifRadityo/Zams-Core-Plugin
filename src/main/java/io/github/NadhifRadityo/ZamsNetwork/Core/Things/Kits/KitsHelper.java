package io.github.NadhifRadityo.ZamsNetwork.Core.Things.Kits;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.NadhifRadityo.ZamsNetwork.Core.Exceptions.FileException;
import io.github.NadhifRadityo.ZamsNetwork.Core.Exceptions.InventoryException;
import io.github.NadhifRadityo.ZamsNetwork.Core.Exceptions.KitsException;
import io.github.NadhifRadityo.ZamsNetwork.Core.Exceptions.ConfigException;
import io.github.NadhifRadityo.ZamsNetwork.Core.Helper.Helper;
import io.github.NadhifRadityo.ZamsNetwork.Main.Main;

public class KitsHelper{
	private Main Plugin;
	
	private YamlConfiguration KitsConfig;
	@SuppressWarnings("unused")
	private YamlConfiguration KitsMessages;
	
	public KitsHelper(Main plugins) {
		this.Plugin = plugins;
		try {
			this.KitsConfig = this.getKitsConfig();
			this.KitsMessages = this.getKitsMessages();
		} catch (ConfigException e) {
			this.Plugin.Helper.Throw("Can not get kits configs!");
		}
	}
	
	public void sendChat(Player player, String msg) {
		Helper.sendChat(player, "&8[&7Kits&8]&r &f" + msg);
	}
	
	public YamlConfiguration getKitsConfig() throws ConfigException {
		return this.Plugin.Helper.ConfigHelper.getYaml(this.Plugin.Config.getString("Config.Kits.Config.path"));
	}
	
	public YamlConfiguration getKitsMessages() throws ConfigException {
		return this.Plugin.Helper.ConfigHelper.getYaml(this.Plugin.Config.getString("Config.Kits.Messages.path"));
	}
	
	
	
	public String getKitsName(int start, String[] args) {
		String nameKits = "";
		for(int index = start; index < args.length; index++) {
			nameKits = nameKits + args[index] + " ";
		}
		StringBuilder fixedName = new StringBuilder(nameKits);
		fixedName.deleteCharAt(nameKits.length() - 1);
		nameKits = fixedName.toString();
		nameKits = this.Plugin.Helper.FileHelper.fixedName(nameKits);
		
		return nameKits;
	}
	
	public boolean isKitsExist(String kitsName) throws KitsException {
		try {
			return this.Plugin.Helper.FileHelper.getFile(KitsConfig.getString("storeKits"), kitsName + ".yml").exists();
		} catch (FileException e) {
			throw new KitsException("Can not open kits file '" + kitsName + "'!", e);
		}
	}
	
	
	
	public KitsObject[] getAllKits() throws KitsException {
		File[] files;
		try {
			files = this.Plugin.Helper.FileHelper.getAllFiles(KitsConfig.getString("storeKits"));
		} catch (FileException e) {
			throw new KitsException("Can not get all kits!", e);
		}
		KitsObject[] results = new KitsObject[this.Plugin.Helper.FileHelper.countFile(files)];
		
		int i = 0;
		for(File file : files) {
			if(file.isFile()) {
				String fileName = file.getName();
				if(fileName.substring(fileName.length() - 4).equals(".yml")) {
					StringBuilder fixedName = new StringBuilder(fileName);
					fixedName.delete(fileName.length() - 4, fileName.length()); // remove ".yml", e.g.: "test.yml" = "test"
					fileName = fixedName.toString();
					
					try {
						results[i] = this.getKits(fileName);
					} catch (KitsException e) {
						throw new KitsException("Can not get all kits!", e);
					}
					i++;
				}
			}
		}
		return results;
	}
	
	public KitsObject getKits(String kitsName) throws KitsException {
		try {
			if(this.isKitsExist(kitsName)) {
				return this.constructKitsObject(kitsName, this.getKitsValue(kitsName), this.getKitsConfig(kitsName));
			}else {
				throw new KitsException("Kits '" + kitsName + "' does not exist!");
			}
		} catch (KitsException e) {
			throw new KitsException("Can not get kits '" + kitsName + "' !", e);
		}
	}
	
	public KitsObject constructKitsObject(String name, ItemStack[][] contents, KitsConfig config) {
		KitsObject results = new KitsObject();
		results.setName(name);
		results.setContents(contents);
		results.setConfig(config);
		return results;
	}
	
	public ItemStack[][] getKitsValue(String kitsName) throws KitsException {
		if(this.isKitsExist(kitsName)) {
			try {
				return this.Plugin.Helper.InventoryHelper.restore(KitsConfig.getString("storeKits"), kitsName);
			} catch (InventoryException e) {
				throw new KitsException("Can not get kits '" + kitsName + "' value!", e);
			}
		}
		return new ItemStack[0][];
	}
	
	public KitsConfig getKitsConfig(String kitsName) throws KitsException {
		ArrayList<Object> contents;
		try {
			contents = this.Plugin.Helper.ConfigHelper.readYaml(KitsConfig.getString("storeKits"), kitsName, new String[] {
											"config.display", //contents.get(0) ItemStack[][]
											"config.showContents" //contents.get(1) boolean
										  });
		} catch (ConfigException e) {
			throw new KitsException("Can not read kits '" + kitsName + "' config!", e);
		}
		
		if(contents.get(1) == null) {
			contents.set(1, true);
		}
		
		KitsConfig result = new KitsConfig();
		result.setDisplay((ItemStack) contents.get(0));
		result.setShowContents((boolean) contents.get(1));
		
		return result;
	}
	
	
	
	
	public int getSlots(int val) throws KitsException {
		return val % 9 > 0 ? (val / 9 + 1) * 9 : (val / 9) * 9;
	}
	
//	public ArrayList<Object> KitsName(int start, String[] args, Player player) {
//		String kitsName = null;
//		ArrayList<Object> result = new ArrayList<Object>();
//		if(args.length >= start) {
//			kitsName = this.getKitsName(start, args);
//			if(!this.isKitsExist(kitsName)) {
//				sendChat(player, "Kits tidak ada!");
//				result.add(0, false);
//			}
//		} else {
//			sendChat(player, "Masukkan nama kits!");
//			result.add(0, false);
//		}
//		result.add(0, true);
//		result.add(1, kitsName);
//		return result;
//	}
}
