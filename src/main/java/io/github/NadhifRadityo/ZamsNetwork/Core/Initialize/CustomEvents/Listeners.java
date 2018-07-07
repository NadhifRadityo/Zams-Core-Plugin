package io.github.NadhifRadityo.ZamsNetwork.Core.Initialize.CustomEvents;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.event.Listener;

import io.github.NadhifRadityo.ZamsNetwork.Main.Main;

public class Listeners {
	private Main Plugin;
	private boolean isInit;
	private Map<String, Listener> listeners = new HashMap<String, Listener>();
	
	public boolean Initialize(Main plugins) {
		this.Plugin = plugins;
		this.isInit = true;
		return this.isInit;
	}
	
	public Map<String, Listener> getListeners(){
		return listeners;
	}
	public void addListeners(Map<String, Listener> listeners) {
		this.listeners.putAll(listeners);
	}
	public void addListener(String className, Listener listenerClass) {
		this.listeners.put(className, listenerClass);
	}
	public void setListeners(Map<String, Listener> listeners) {
		this.listeners = listeners;
	}
	public boolean contains(String className, Listener listeners) {
		return this.listeners.containsKey(className) && this.listeners.get(className).equals(listeners);
	}
	
	public void RegisterEvents() {
		if(this.Plugin == null) {
			System.out.println("Class Listeners belum di inisialisasi!");
			System.exit(0);
		}
		
		for(Entry<String, Listener> entry : this.listeners.entrySet()) {
			this.Plugin.getServer().getPluginManager().registerEvents(entry.getValue(), this.Plugin);
		}
	}
}
