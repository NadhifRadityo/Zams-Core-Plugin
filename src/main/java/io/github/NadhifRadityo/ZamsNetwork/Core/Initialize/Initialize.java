package io.github.NadhifRadityo.ZamsNetwork.Core.Initialize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Listener;

import io.github.NadhifRadityo.ZamsNetwork.Core.Initialize.Commands.CommandAttribute;
import io.github.NadhifRadityo.ZamsNetwork.Core.Initialize.Commands.GetCommandExecutorClass;
import io.github.NadhifRadityo.ZamsNetwork.Main.Main;

public class Initialize {
	private Main Plugin;
	boolean isInit = false;
	private List<Init> initList = new ArrayList<Init>();
	
	public void init(Main plugins) {
		this.Plugin = plugins;
		this.isInit = true;
		this.Plugin.Commands.Initialize(this.Plugin);
	}
	
	public void addInit(Init init) {
		if(!this.initList.contains(init)) {
			this.initList.add(init);
		}
	}
	
	public void removeInit(Init init) {
		if(!this.initList.contains(init)) {
			this.initList.remove(init);
		}
	}
	
	public void removeInit(int index) {
		if(this.initList.get(index) != null) {
			this.initList.remove(index);
		}
	}
	
	public void setInit(List<Init> inits) {
		this.initList = inits;
	}
	
	public void initAll() {
		if(this.initList.isEmpty()) {
			return;
		}
		
		List<initPlugin> InitList = new ArrayList<initPlugin>();
		Map<String, GetCommandExecutorClass> CommandList = new HashMap<String, GetCommandExecutorClass>();
		Map<String, CommandAttribute> CommandAttribute = new HashMap<String, CommandAttribute>();
		Map<String, Listener> ListenerList = new HashMap<String, Listener>();
		
		for(Init init : this.initList) {
			if(init != null) {
				String command = init.getCommand();
				String mainClass = init.getMainClassName();
				String[] listenerClasses = init.getListeners();
				String configName = init.getConfig();
				
				ConfigurationSection config = configName != null ? this.Plugin.Config.getConfigurationSection("Config." + configName) : null;
				Set<String> index = configName != null ? config.getKeys(false) : null;
				
				if(configName == null || config.getBoolean("enabled") == true || !index.contains("enabled")) {
					if(command != null) {
						GetCommandExecutorClass getCommandClass = this.Plugin.Helper.getCommandExecutorClass(mainClass);
						if(!getCommandClass.initMain(this.Plugin) || !getCommandClass.isInit()) {
							this.Plugin.Helper.Throw("Class: '" + getCommandClass.getClass().getName() + "' Belum di Init! IsInit: " + getCommandClass.isInit());
						}
						
						CommandList.put(command, getCommandClass);
						CommandAttribute.put(command, init.getCommandAttribute());
					} else {
						initPlugin getInitClass = this.Plugin.Helper.getInitPluginClass(mainClass);
						if(!getInitClass.initMain(this.Plugin) || !getInitClass.isInit()) {
							this.Plugin.Helper.Throw("Class: '" + getInitClass.getClass().getName() + "' Belum di Init! IsInit: " + getInitClass.isInit());
						}
						
						InitList.add(getInitClass);
					}
					
					if(listenerClasses != null) {
						for(String listener : listenerClasses) {
							if(listener.equals(mainClass)) {
								if(CommandList.get(command) != null && CommandList.get(command) instanceof Listener) {
									ListenerList.put(listener.toString(), (Listener) CommandList.get(command));
								}else if(InitList.get(InitList.size() - 1) != null && InitList.get(InitList.size() - 1) instanceof Listener) {
									ListenerList.put(listener.toString(), (Listener) InitList.get(InitList.size() - 1));
								}else {
									Listener listen = this.Plugin.Helper.getListenerClass(listener.toString());
									ListenerList.put(listener.toString(), listen);
								}
							} else {
								Listener listen = this.Plugin.Helper.getListenerClass(listener.toString());
								ListenerList.put(listener.toString(), listen);
							}
						}
					}
				} else if(config.getBoolean("enable") == false) {
					if(command != null) {
						CommandList.put(command, new Disabled());
					}
				}
			}
		}
		
		this.Plugin.InitList.setInitList(InitList);

		this.Plugin.Commands.Initialize(this.Plugin);
		this.Plugin.Commands.setCommands(CommandList);
		this.Plugin.Commands.RegisterEvents(CommandAttribute);
		
		this.Plugin.Listeners.Initialize(this.Plugin);
		this.Plugin.Listeners.setListeners(ListenerList);
		this.Plugin.Listeners.RegisterEvents();
	}
	
//	public void initAll() {
//		Map<String, GetCommandExecutorClass> CommandList = new HashMap<String, GetCommandExecutorClass>();
//		Map<String, Listener> ListenerList = new HashMap<String, Listener>();
//		
//	    for(Entry<String, Object[]> pair : this.initList.entrySet()) {
//			String command = pair.getKey() != null ? pair.getKey().toString() : null;
//			Object[] contents = pair.getValue();
//			int length = contents.length;
//			
//			String CommandClass = length > 0 ? contents[0] != null ? contents[0].toString() : null : null;
//			String[] Listeners = length > 1 ? contents[1] != null ? (String[]) contents[1] : null : null;
//			
//			if(CommandClass != null) {
//				GetCommandExecutorClass getCommandClass = this.Plugin.Helper.getCommandExecutorClass(CommandClass);
//				if(!getCommandClass.initMain(this.Plugin) || !getCommandClass.isInit()) {
//					this.Plugin.Helper.Throw("Class: '" + getCommandClass.getClass().getName() + "' Belum di Init! IsInit: " + getCommandClass.isInit());
//				}
//				
//				CommandList.put(command, getCommandClass);
//			}
//			
//			if(Listeners != null) {
//				for(String listener : Listeners) {
//					Listener listen = this.Plugin.Helper.getListenerClass(listener.toString());
//					this.Plugin.getServer().getPluginManager().registerEvents(listen, this.Plugin);
//					ListenerList.put(listener.toString(), listen);
//				}
//			}
//		}
//		
//		this.Plugin.Commands.Initialize(this.Plugin);
//		this.Plugin.Commands.setCommands(CommandList);
//		this.Plugin.Commands.RegisterEvents();
//		
//		this.Plugin.ListenerList.setListeners(ListenerList);
//	}
	
	public Map<String, Object> getAllClasses(){
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<initPlugin> initList = this.Plugin.InitList.getInitList();
		for(initPlugin init : initList) {
			result.put(init.getClass().getName(), init.getClass());
		}
		
		Map<String, GetCommandExecutorClass> commandsList = this.Plugin.Commands.getCommandsList();
		for(Entry<String, GetCommandExecutorClass> entry : commandsList.entrySet()) {
			result.put(entry.getValue().getClass().getName(), entry.getValue());
		}
		
		Map<String, Listener> listenerList = this.Plugin.Listeners.getListeners();
		for(Entry<String, Listener> entry : listenerList.entrySet()) {
			result.put(entry.getValue().getClass().getName(), entry.getValue());
		}
		
		return result;
	}
}
