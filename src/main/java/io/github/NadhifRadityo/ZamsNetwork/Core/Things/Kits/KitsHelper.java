package io.github.NadhifRadityo.ZamsNetwork.Core.Things.Kits;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.NadhifRadityo.ZamsNetwork.Core.Exceptions.ConfigException;
import io.github.NadhifRadityo.ZamsNetwork.Core.Exceptions.FileException;
import io.github.NadhifRadityo.ZamsNetwork.Core.Exceptions.InventoryException;
import io.github.NadhifRadityo.ZamsNetwork.Core.Exceptions.KitsException;
import io.github.NadhifRadityo.ZamsNetwork.Core.Helper.Helper;
import io.github.NadhifRadityo.ZamsNetwork.Core.Utilization.InventoryUtils;
import io.github.NadhifRadityo.ZamsNetwork.Main.Main;

public class KitsHelper{
	public Main Plugin;
	
	public YamlConfiguration KitsConfig;
	public YamlConfiguration KitsMessages;
	
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
		Helper.sendChat(player, KitsMessages.getString("prefix") + msg);
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
		
		if(this.Plugin.Helper.FileHelper.checkName(nameKits)) {
			return nameKits;
		}else {
			return null;
		}
	}
	
	public boolean isKitsExist(File kitsFile) throws KitsException {
		if(kitsFile == null) {
			throw new KitsException("File is null!");
		}
		return kitsFile.exists();
	}
	
	public boolean isKitsExist(String kitsName) throws KitsException {
		try {
			return this.isKitsExist(this.getKitsFile(kitsName));
		} catch (KitsException e) {
			throw new KitsException("Can not open kits file '" + kitsName + "'!", e);
		}
	}
	
	public boolean isKitsExist(YamlConfiguration config) throws KitsException {
		if(config == null) {
			return false;
		}
		return this.isKitsExist(config.getName());
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
			File kitsFile = this.getKitsFile(kitsName);
			if(this.isKitsExist(kitsFile)) {
				YamlConfiguration kitsYaml = this.getKitsYaml(kitsFile);
				KitsConfig config = this.getKitsConfig(kitsName);
				return new KitsObject(kitsName, this.getKitsContents(kitsYaml), this.getKitsPermissions(kitsYaml), this.getKitsCost(kitsYaml), config);
			}else {
				throw new KitsException("Kits '" + kitsName + "' does not exist!");
			}
		} catch (KitsException e) {
			throw new KitsException("Can not get kits '" + kitsName + "' !", e);
		}
	}
	
	public File getKitsFile(String kitsName) throws KitsException {
		try {
			return this.Plugin.Helper.FileHelper.getFile(KitsConfig.getString("storeKits"), kitsName + ".yml");
		} catch (FileException e) {
			throw new KitsException("Can not get kits '"+ kitsName +"' file!", e);
		}
	}
	
	public YamlConfiguration getKitsYaml(File kitsFile) throws KitsException {
		if(kitsFile != null) {
			return this.Plugin.Helper.ConfigHelper.getYaml(kitsFile);
		}else {
			throw new KitsException("Kits file is null!");
		}
	}
	
	public YamlConfiguration getKitsYaml(String kitsName) throws KitsException {
		try {
			return this.getKitsYaml(this.getKitsFile(kitsName));
		} catch (KitsException e) {
			throw new KitsException("Can not get kits '" + kitsName + "' yaml!", e);
		}
	}
	
	public ItemStack[][] getKitsContents(String kitsName) throws KitsException {
		return this.getKitsContents(this.getKitsFile(kitsName));
	}
	
	public ItemStack[][] getKitsContents(File kitsFile) throws KitsException {
		return this.getKitsContents(this.getKitsYaml(kitsFile));
	}
	
	public ItemStack[][] getKitsContents(YamlConfiguration kitsYaml) throws KitsException {
		try {
			return this.Plugin.Helper.InventoryHelper.restore(kitsYaml);
		} catch (InventoryException e) {
			throw new KitsException("Can not get kits '" + kitsYaml.getName() + "' value!", e);
		}
	}
	
	public int getKitsCost(String kitsName) throws KitsException {
		return this.getKitsCost(this.getKitsFile(kitsName));
	}
	
	public int getKitsCost(File kitsFile) throws KitsException {
		return this.getKitsCost(this.getKitsYaml(kitsFile));
	}
	
	public int getKitsCost(YamlConfiguration kitsYaml) throws KitsException {
		if(kitsYaml == null) {
			throw new KitsException("Kits Yaml is null!");
		}
		
		return kitsYaml.getInt("cost");
	}
	
	public String[] getKitsPermissions(String kitsName) throws KitsException {
		return this.getKitsPermissions(this.getKitsFile(kitsName));
	}
	
	public String[] getKitsPermissions(File kitsFile) throws KitsException {
		return this.getKitsPermissions(this.getKitsYaml(kitsFile));
	}
	
	public String[] getKitsPermissions(YamlConfiguration kitsYaml) throws KitsException {
		if(kitsYaml == null) {
			throw new KitsException("Kits Yaml is null!");
		}
		
		List<String> permissionList = kitsYaml.getStringList("permissions");
		if(permissionList.size() == 0) {
			return null;
		}
		return permissionList.toArray(new String[permissionList.size()]);
	}
	
	public KitsConfig getKitsConfig(String kitsName) throws KitsException {
		return this.getKitsConfig(this.getKitsFile(kitsName));
	}
	
	public KitsConfig getKitsConfig(File kitsFile) throws KitsException {
		return this.getKitsConfig(this.getKitsYaml(kitsFile));
	}
	
	public KitsConfig getKitsConfig(YamlConfiguration kitsYaml) throws KitsException {
		if(kitsYaml == null) {
			throw new KitsException("Kits Yaml is null!");
		}
		
		ArrayList<Object> contents;
		try {
			contents = this.Plugin.Helper.ConfigHelper.readYaml(kitsYaml, new String[] {
											"config.display", //contents.get(0) ItemStack[]
//											"config.showContents", //contents.get(1) boolean
//											"config.showIfDoesntHasPermission" //contents.get(2) boolean
										  });
		} catch (ConfigException e) {
			throw new KitsException("Can not read kits '" + kitsYaml.getName() + "' config!", e);
		}
		
//		if(contents.get(1) == null) {
//			contents.set(1, true);
//		}
		
//		KitsConfig result = new KitsConfig((ItemStack) contents.get(0), (boolean) contents.get(1), (boolean) contents.get(2));
		KitsConfig result = new KitsConfig((ItemStack) contents.get(0), kitsYaml.getBoolean("config.showContents"), kitsYaml.getBoolean("config.showIfDoesntHasPermission"));
		
		return result;
	}
	
	
	
	public int getSlots(KitsObject[] list) throws KitsException {
		return InventoryUtils.getSlots(list.length);
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
