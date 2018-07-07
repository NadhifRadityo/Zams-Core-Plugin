package io.github.NadhifRadityo.ZamsNetwork.Core.Initialize;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import io.github.NadhifRadityo.ZamsNetwork.Main.Main;

public class ConfigInit {
	public Main Plugin;
	public void initMain(Main plugin) {
		this.Plugin = plugin;
	}
	
	public void addConfig() {
		Set<String> Keys = this.Plugin.Config.getConfigurationSection("Config").getKeys(false);
		
		Map<String, Object> result = new HashMap<String, Object>();
		for(String key : Keys) {
			if(this.Plugin.Config.getConfigurationSection("Config." + key).getKeys(false).size() != 0) {
				System.out.println("has child!");
				result.put(key, this.Chain("Config", key));
			}
		}
		this.printMap(result);
	}
	
	private Map<String, Object> Chain(String key, String parents) {
		if(parents.split("")[parents.length()] != ".") {
			parents += ".";
		}
		
		Map<String, Object> result = new HashMap<String, Object>();
		Set<String> Keys = this.Plugin.Config.getConfigurationSection(parents + key).getKeys(false);
		for(String index : Keys) {
			if(this.Plugin.Config.getConfigurationSection(parents + key + "." + index).getKeys(false).size() != 0) {
				System.out.println("has child!");
				result.put(index, this.Chain(index, parents + key));
			}else {
				result.put(index, this.Plugin.Config.get(parents + key + "." + index));
			}
		}
		return result;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void printMap(Map<String, Object> data) {
		for ( String key : data.keySet() ) {
			if(data.get(key) instanceof Map) {
				this.printMap((Map) data.get(key));
			}else {
				System.out.println(key);
			}
		}
	}
	
//	public void initDefault(Object[][] settings) {
//		for(Object[] Group : settings) {
//			System.out.println(Group[1].toString());
//			this.Plugin.Config.addDefault(Group[0].toString(), Group[1].toString());
//			
//			try {
//				if(!this.Plugin.Helper.FileHelper.getFile(Group[1].toString()).exists()) {
//					if(this.Plugin.Helper.FileHelper.getFile(this.Plugin.Config.getString("DefaultConfig")).exists()) {
//						ArrayList<Object> data = this.Plugin.Helper.ConfigHelper.readYaml(this.Plugin.Config.getString("DefaultConfig"), new String[] {
//								"*"
//						});
//						Object[] toObject = data.toArray(new Object[data.size()]);
//						for(Object obj : toObject) {
//							System.out.println(obj);
//						}
//						
//						this.Plugin.Helper.ConfigHelper.createYaml(Group[1].toString(), data);
//					}
//				}
//			} catch (IllegalFileNameException | YamlFileException e) {
//				e.printStackTrace();
//			}
//		}
//	}
}
