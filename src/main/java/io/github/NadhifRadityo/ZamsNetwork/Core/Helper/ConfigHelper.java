package io.github.NadhifRadityo.ZamsNetwork.Core.Helper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.file.YamlConfiguration;

import io.github.NadhifRadityo.ZamsNetwork.Core.Exceptions.FileException;
import io.github.NadhifRadityo.ZamsNetwork.Core.Exceptions.ConfigException;
import io.github.NadhifRadityo.ZamsNetwork.Main.Main;

public class ConfigHelper{
	private Main Plugin;
	
	public void initMain(Main plugins) {
		this.Plugin = plugins;
	}
	
	public <V>void setConfig(String path, List<V> data) {
		this.Plugin.getConfig().set(path, data);
	}
	
	//Create Yaml
	public void createYaml(String dir, Object[][] data) throws ConfigException {
		dir = this.fixedName(dir);
		try {
			this.createYaml(this.Plugin.Helper.FileHelper.getFile(dir), data);
		} catch (FileException e) {
			throw new ConfigException("Can not create yaml!", e);
		}
	}
	public void createYaml(String path, String fileName, Object[][] data) throws ConfigException {
		fileName = this.fixedName(fileName); //check ".yml" things
		try {
			this.createYaml(this.Plugin.Helper.FileHelper.getFile(path, fileName), data);
		} catch (FileException e) {
			throw new ConfigException("Can not create yaml!", e);
		}
	}
	public void createYaml(File file, Object[][] data) throws ConfigException {
		YamlConfiguration Yaml = new YamlConfiguration();
		for(Object[] Group : data) {
			Yaml.set(Group[0].toString(), Group[1]);
		}
		try {
			Yaml.save(file);
		}catch(IOException e) {
			throw new ConfigException("Can not create yaml!", e);
		}
	}
	
	//Read Yaml
	public ArrayList<Object> readYaml(String dir, String[] finds) throws ConfigException {
		dir = this.fixedName(dir);
		try {
			return this.readYaml(this.Plugin.Helper.FileHelper.getFile(dir), finds);
		} catch (FileException e) {
			throw new ConfigException("Can not read yaml!", e);
		}
	}
	public ArrayList<Object> readYaml(String path, String fileName, String[] finds) throws ConfigException {
		fileName = this.fixedName(fileName);
		try {
			return this.readYaml(this.Plugin.Helper.FileHelper.getFile(path, fileName), finds);
		} catch (FileException e) {
			throw new ConfigException("Can not read yaml!", e);
		}
	}
	public ArrayList<Object> readYaml(File file, String[] finds) throws ConfigException {
		YamlConfiguration Contents = null;
		Contents = this.getYaml(file);
		
		ArrayList<Object> result = new ArrayList<Object>();
		for(String find : finds) {
			result.add(Contents.get(find));
		}
		return result;
	}
	
	//Get Yaml
	public YamlConfiguration getYaml(String dir) throws ConfigException {
		dir = this.fixedName(dir);
		try {
			return this.getYaml(this.Plugin.Helper.FileHelper.getFile(dir));
		} catch (FileException e) {
			throw new ConfigException("Can not get yaml!", e);
		}
	}
	public YamlConfiguration getYaml(String path, String fileName) throws ConfigException {
		fileName = this.fixedName(fileName);
		try {
			return this.getYaml(this.Plugin.Helper.FileHelper.getFile(path, fileName));
		} catch (FileException e) {
			throw new ConfigException("Can not get yaml!", e);
		}
	}
	public YamlConfiguration getYaml(File file) {
		YamlConfiguration Contents = null;
		Contents = YamlConfiguration.loadConfiguration(file);
		return Contents;
	}
	
	//Update Yaml
	public void updateYaml(String dir, Object[][] data) throws ConfigException {
		dir = this.fixedName(dir);
		try {
			this.updateYaml(this.Plugin.Helper.FileHelper.getFile(dir), data);
		} catch (ConfigException | FileException e) {
			throw new ConfigException("Can not update yaml!", e);
		}
	}
	public void updateYaml(String path, String fileName, Object[][] data) throws ConfigException {
		fileName = this.fixedName(fileName);
		try {
			this.updateYaml(this.Plugin.Helper.FileHelper.getFile(path, fileName), data);
		} catch (ConfigException | FileException e) {
			throw new ConfigException("Can not update yaml!", e);
		}
	}
	public void updateYaml(File file, Object[][] data) throws ConfigException {
		YamlConfiguration Yaml = YamlConfiguration.loadConfiguration(file);
		
		for(Object[] Group : data) {
			Yaml.set(Group[0].toString(), Group[1]);
		}
		try {
			Yaml.save(file);
		}catch(IOException e) {
			throw new ConfigException("Can not save file!", e);
		}
	}
	
	public Object[][] fixedDataInput(Map<?, ?> inputData) {
		Object[][] result = new Object[inputData.size()][2];
		int i = 0;
		for(Object key : inputData.keySet()) {
			if(inputData.get(key) instanceof Map) {
				result[i][0] = key;
				result[i][1] = this.fixedDataInput((Map<?, ?>) inputData.get(key));
			}else {
				result[i][0] = key;
				result[i][1] = inputData.get(key);
			}
			i++;
		}
		return result;
	}
	
	public String fixedName(String name) {
		try {
			if(name.substring(name.length() - 4) == null) {
				
			}
		} catch(Exception e) {
			name += ".yml";
		}
		if(!name.substring(name.length() - 4).equals(".yml")) {
			name += ".yml";
		}
		return name;
	}
}
