package io.github.NadhifRadityo.ZamsNetwork.Core.Helper;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URISyntaxException;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import io.github.NadhifRadityo.ZamsNetwork.Root;
import io.github.NadhifRadityo.ZamsNetwork.Core.GetClass;
import io.github.NadhifRadityo.ZamsNetwork.Core.Utils;
import io.github.NadhifRadityo.ZamsNetwork.Core.Initialize.initPlugin;
import io.github.NadhifRadityo.ZamsNetwork.Core.Initialize.Commands.GetCommandExecutorClass;
import io.github.NadhifRadityo.ZamsNetwork.Main.Main;

public class Helper {
	private Main Plugin;
	public DataHelper DataHelper = new DataHelper();
	public ConfigHelper ConfigHelper = new ConfigHelper();
	public InventoryHelper InventoryHelper = new InventoryHelper();
	public GUIHelper GUIHelper = new GUIHelper();
	public FileHelper FileHelper = new FileHelper();
	public OSHelper OSHelper = new OSHelper();
	public InputChatHelper InputChatHelper = new InputChatHelper();
	public WindowHelper WindowHelper = new WindowHelper();
	
	public void initMain(Main plugins) {
		this.Plugin = plugins;
		this.ConfigHelper.initMain(this.Plugin);
		this.InventoryHelper.initMain(this.Plugin);
		this.GUIHelper.initMain(this.Plugin);
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
	public initPlugin getInitPluginClass(String ClassName) {
		try {
			Class<?> Clazz = Class.forName(ClassName);
			return (initPlugin) Clazz.newInstance();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Listener getListenerClass(String ClassName) {
		try {
			Class<?> Clazz = Class.forName(ClassName);
			return (Listener) Clazz.newInstance();
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
	public String getCurrentRunningJar() {
		try {
			return new File(Root.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

    public static Class<?> getNMSClass(String clazz) {
        try {
            return Class.forName("net.minecraft.server." + Main.ServerVersion + "." + clazz);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
	
	public static Method getMethod(String name, Class<?> clazz) {
		for (Method m : clazz.getDeclaredMethods()) {
			if (m.getName().equals(name))
			return m;
		}
		return null;
	}
}
