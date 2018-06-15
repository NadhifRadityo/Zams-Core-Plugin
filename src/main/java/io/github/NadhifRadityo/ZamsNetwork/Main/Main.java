package io.github.NadhifRadityo.ZamsNetwork.Main;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.NadhifRadityo.ZamsNetwork.Core.Commands;
import io.github.NadhifRadityo.ZamsNetwork.Core.Events;
import io.github.NadhifRadityo.ZamsNetwork.Core.Initialize;
import io.github.NadhifRadityo.ZamsNetwork.Core.Exceptions.ConfigException;
import io.github.NadhifRadityo.ZamsNetwork.Core.Exceptions.FileException;
import io.github.NadhifRadityo.ZamsNetwork.Core.Helper.Helper;

//public class Main {
public class Main extends JavaPlugin{
	public FileConfiguration Config = getConfig();
	public Helper Helper = new Helper();
	public Commands Commands = new Commands();
	public Events Events = new Events();
	public Initialize Initialize = new Initialize();
	
	public void Initialize() {
		this.Helper.initMain(this);
		this.addConfigToFile(this.getPluginConfig());
		
		this.Initialize.init(this);
		Map<String, Object[]> Things = new HashMap<String, Object[]>();
		
		/* Format:
		 * Things.put(COMMAND(STRING), new Object[] {
		 *		COMMAND HANDLER(STRING <CLASS PATH>),
		 *		CUSTOM EVENTS(STRING <CLASS PATH>)
		 *	});
		 */
		
		Things.put("/kits", new Object[] {
			"io.github.NadhifRadityo.ZamsNetwork.Core.Things.Kits.Kits", 
			"io.github.NadhifRadityo.ZamsNetwork.Core.Things.Kits.KitsEvents"
		});
		Things.put("/call", new Object[] {
			"io.github.NadhifRadityo.ZamsNetwork.Core.Things.CallClassInGame",
			null
		});
		Things.put(null, new Object[] {
			null,
			"io.github.NadhifRadityo.ZamsNetwork.Core.Things.PlayerJoinQuitMessage"
		});
		Things.put("/resetconfig", new Object[] {
			"io.github.NadhifRadityo.ZamsNetwork.Core.Things.ResetConfig",
			null
		});
//		String[][] Things = {
//				{
//					"/kits",
//					"io.github.NadhifRadityo.ZamsNetwork.Core.Things.Kits.Kits",
//					"io.github.NadhifRadityo.ZamsNetwork.Core.Things.Kits.KitsEvents"
//				},
//				{
//					"/call",
//					"io.github.NadhifRadityo.ZamsNetwork.Core.Things.CallClassInGame"
//				},
//				{
//					"io.github.NadhifRadityo.ZamsNetwork.Core.Things.PlayerJoinQuitMessage"
//				},
//				{
//					"/resetconfig",
//					"io.github.NadhifRadityo.ZamsNetwork.Core.Things.ResetConfig"
//				}
//		};
		this.Initialize.setInit(Things);
		
		this.Initialize.initAll();
	}
	public void onEnable() {
		this.Initialize();
		System.out.println("Zams Core Plugin Enabled");
	}
	public void onDisable() {
		System.out.println("Zams Core Plugin Disable");
	}

	
	
	public Map<String, Object> getPluginConfig() {
		Set<String> Keys = this.Config.getConfigurationSection("Config").getKeys(false);
		
		Map<String, Object> result = new HashMap<String, Object>();
		for(String key : Keys) {
			if(this.Config.getConfigurationSection("Config." + key).getKeys(false).size() != 0) {
				result.put(key, this.Chain(key, "Config"));
			}
		}
		return result;
	}
	
	private Map<String, Object> Chain(String key, String parents) {
		if(parents.split("")[parents.length() - 1] != ".") {
			parents += ".";
		}
		Map<String, Object> result = new HashMap<String, Object>();
		Set<String> Keys = this.Config.getConfigurationSection(parents + key).getKeys(false);
		for(String index : Keys) {
			try {
				if(this.Config.getConfigurationSection(parents + key + "." + index).getKeys(false) != null) {
					result.put(index, this.Chain(index, parents + key));
				}
			}catch(Exception e) {
				result.put(index, this.Config.get(parents + key + "." + index));
			}
		}
		return result;
	}
	
	@SuppressWarnings({ "unchecked" })
	private void addConfigToFile(Map<String, Object> data) {
		for(Object things : data.keySet()) {
			Map<Object, Object> contents = (Map<Object, Object>) data.get(things.toString());
			for(Object keyThings : contents.keySet()) {
				Map<String, Object> settings = (Map<String, Object>) contents.get(keyThings.toString());
				
				String settingsPath = null;
				Map<Object, Object> settingsDefault = null;
				for(Object keySettings : settings.keySet()) {
					if(keySettings.toString().equalsIgnoreCase("path")) {
						settingsPath = settings.get(keySettings.toString()).toString();
					}
					if(keySettings.toString().equalsIgnoreCase("default")) {
						settingsDefault = (Map<Object, Object>) settings.get(keySettings.toString());
					}
				}
				
				if(settingsPath != null && settingsDefault != null) {
					try {
						if(!this.Helper.FileHelper.getFile(settingsPath).exists())
						this.Helper.ConfigHelper.createYaml(settingsPath, this.Helper.ConfigHelper.fixedDataInput(settingsDefault));
					} catch (ConfigException | FileException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void resetConfig(Map<String, Object> data) {
		for(Object things : data.keySet()) {
			Map<Object, Object> contents = (Map<Object, Object>) data.get(things.toString());
			for(Object keyThings : contents.keySet()) {
				System.out.println("Mereset: '" + things + "." + keyThings + "'");
				Map<String, Object> settings = (Map<String, Object>) contents.get(keyThings.toString());
				
				String settingsPath = null;
				for(Object keySettings : settings.keySet()) {
					if(keySettings.toString().equalsIgnoreCase("path")) {
						settingsPath = settings.get(keySettings.toString()).toString();
					}
				}
				
				if(settingsPath != null) {
					try {
						this.Helper.FileHelper.getFile(settingsPath).delete();
					} catch (FileException e) {
						e.printStackTrace();
					}
				}
			}
		}
		this.addConfigToFile(this.getPluginConfig());
	}
	
	
	
	public static void main(String args[]) throws FileException {
		System.out.println("Start Testings...!");
//		Helper Helper = new Helper();
//		File curDir = new File(".");
//        Helper.FileHelper.aw(curDir);
//		Helper helper = new Helper();
		
//		Main Plugin = new Main();
//		System.out.println("'" + Plugin.Helper.FileHelper.fixedName("co*<>\"|?:m1") + "'");
	}
}
