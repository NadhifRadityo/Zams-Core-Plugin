package io.github.NadhifRadityo.ZamsNetwork.Core.Helper;

import org.bukkit.entity.Player;

import io.github.NadhifRadityo.ZamsNetwork.Core.GetClass;
import io.github.NadhifRadityo.ZamsNetwork.Core.GetCommandExecutorClass;
import io.github.NadhifRadityo.ZamsNetwork.Core.Utils;
import io.github.NadhifRadityo.ZamsNetwork.Core.CustomEvents.GetEventsHandlerClass;
import io.github.NadhifRadityo.ZamsNetwork.Main.Main;

public class Helper {
	private Main Plugin;
	public DataHelper DataHelper = new DataHelper();
	public ConfigHelper ConfigHelper = new ConfigHelper();
	public InventoryHelper InventoryHelper = new InventoryHelper();
	public GUIHelper GUIHelper = new GUIHelper();
	public FileHelper FileHelper = new FileHelper();
	public OSHelper OSHelper = new OSHelper();
	
	public void initMain(Main plugins) {
		this.Plugin = plugins;
		this.ConfigHelper.initMain(this.Plugin);
		this.InventoryHelper.initMain(this.Plugin);
	}
	
	public void Throw(String Error) {
		System.out.println(Error);
		System.exit(0);
	}
	public static void sendChat(Player player, String text) {
		player.sendMessage(Utils.Chat(text));
	}
	public GetCommandExecutorClass getCommandExecutorClass(String ClassName) {
		try {
			Class<?> Clazz = Class.forName(ClassName);
			return (GetCommandExecutorClass) Clazz.newInstance();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	public GetEventsHandlerClass getEventsClass(String ClassName) {
		try {
			Class<?> Clazz = Class.forName(ClassName);
			return (GetEventsHandlerClass) Clazz.newInstance();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	public GetClass getClass(String ClassName) {
		try {
			Class<?> Clazz = Class.forName(ClassName);
			return (GetClass) Clazz.newInstance();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
}
