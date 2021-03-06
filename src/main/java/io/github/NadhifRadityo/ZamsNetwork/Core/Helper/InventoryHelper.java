package io.github.NadhifRadityo.ZamsNetwork.Core.Helper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.NadhifRadityo.ZamsNetwork.Core.Exceptions.ConfigException;
import io.github.NadhifRadityo.ZamsNetwork.Core.Exceptions.FileException;
import io.github.NadhifRadityo.ZamsNetwork.Core.Exceptions.InventoryException;
import io.github.NadhifRadityo.ZamsNetwork.Main.Main;

public class InventoryHelper {
	private Main Plugin;
	
	public void initMain(Main plugins) {
		this.Plugin = plugins;
	}
	
	public void save(Player p, Object[] index, String path, String fileName) throws InventoryException {
		if(index == null) {
			index = new Object[]{
				"inventory.armor",
				"inventory.contents"
			};
		}
		Object[][] data = {
				{
					index[0].toString(),
					p.getInventory().getArmorContents()
				},
				{
					index[1].toString(),
					p.getInventory().getContents()
				}
		};
		try {
			this.Plugin.Helper.ConfigHelper.createYaml(path, fileName, data);
		} catch (ConfigException e) {
			throw new InventoryException("Can not save inventory!" + e);
		}
    }
	
	public void updateInventory(ItemStack[][] items, Object[] index, String path, String fileName) throws InventoryException {
		if(index == null) {
			index = new Object[]{
				"inventory.armor",
				"inventory.contents"
			};
		}
		Object[][] data = {
				{
					index[0].toString(),
					items[0]
				},
				{
					index[1].toString(),
					items[1]
				}
		};
		try {
			this.Plugin.Helper.ConfigHelper.updateYaml(path, fileName, data);
		} catch (ConfigException e) {
			throw new InventoryException("Can not update inventory!", e);
		}
	}
	
	public void save(ItemStack[][] items, Object[] index, String path, String fileName) throws InventoryException {
		if(index == null) {
			index = new Object[]{
				"inventory.armor",
				"inventory.contents"
			};
		}
		Object[][] data = {
				{
					index[0].toString(),
					items[0]
				},
				{
					index[1].toString(),
					items[1]
				}
		};
		try {
			this.Plugin.Helper.ConfigHelper.createYaml(path, fileName, data);
		} catch (ConfigException e) {
			throw new InventoryException("Can not save inventory!", e);
		}
	}
	
	public ItemStack[][] restore(String path, String fileName) throws InventoryException{
		try {
			return this.restore(this.Plugin.Helper.FileHelper.getFile(path, fileName));
		} catch (FileException e) {
			throw new InventoryException("can not get file '" + fileName + "' !", e);
		}
	}
	
	public ItemStack[][] restore(File file) throws InventoryException{
		return this.restore(this.Plugin.Helper.ConfigHelper.getYaml(file));
	}
	
    @SuppressWarnings("unchecked")
	public ItemStack[][] restore(YamlConfiguration config) throws InventoryException {
		String[] finds = {
			"inventory.armor",
			"inventory.contents"
		};
		ArrayList<Object> contents;
		try {
			contents = this.Plugin.Helper.ConfigHelper.readYaml(config, finds);
		} catch (ConfigException e) {
			throw new InventoryException("Can not restore inventory!" + e);
		}
        ItemStack[][] result = new ItemStack[2][];
        
    	result[0] = ((List<ItemStack>) contents.get(0)).toArray(new ItemStack[0]);
    	result[1] = ((List<ItemStack>) contents.get(1)).toArray(new ItemStack[0]);
    	return result;
    	
//        ItemStack[] content = ((List<ItemStack>) c.get("inventory.armor")).toArray(new ItemStack[0]);
//        p.getInventory().setArmorContents(content);
//        content = ((List<ItemStack>) c.get("inventory.content")).toArray(new ItemStack[0]);
//        p.getInventory().setContents(content);
    }
    
    public static int countItems(ItemStack[] inv) {
    	int length = 0;
    	for(ItemStack item : inv) { 
			if(item != null)
			length++;
		}
    	return length;
    }
}
