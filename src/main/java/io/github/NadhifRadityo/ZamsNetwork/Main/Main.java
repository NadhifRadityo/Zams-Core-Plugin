package io.github.NadhifRadityo.ZamsNetwork.Main;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.NadhifRadityo.ZamsNetwork.Core.Exceptions.ConfigException;
import io.github.NadhifRadityo.ZamsNetwork.Core.Exceptions.FileException;
import io.github.NadhifRadityo.ZamsNetwork.Core.Helper.Helper;
import io.github.NadhifRadityo.ZamsNetwork.Core.Initialize.Init;
import io.github.NadhifRadityo.ZamsNetwork.Core.Initialize.InitList;
import io.github.NadhifRadityo.ZamsNetwork.Core.Initialize.Initialize;
import io.github.NadhifRadityo.ZamsNetwork.Core.Initialize.Commands.CommandAttribute;
import io.github.NadhifRadityo.ZamsNetwork.Core.Initialize.Commands.Commands;
import io.github.NadhifRadityo.ZamsNetwork.Core.Initialize.CustomEvents.Listeners;
import io.github.NadhifRadityo.ZamsNetwork.Core.OutOfGame.Things.UserInterface.LoadingUserInterface;
import io.github.NadhifRadityo.ZamsNetwork.Core.OutOfGame.Things.UserInterface.Console.ConsoleUserInterface;
import io.github.NadhifRadityo.ZamsNetwork.Core.Utilization.JarUtils;

//public class Main {
public class Main extends JavaPlugin{
	public static Main instance;
	public static String ServerVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
	public static String NMSVersion = "net.minecraft.server." + ServerVersion + ".";
	public static String CBVersion = "org.bukkit.craftbukkit." + ServerVersion + ".";
	
	public FileConfiguration Config = getConfig();
	public Helper Helper = new Helper();
	public InitList InitList = new InitList();
	public Commands Commands = new Commands();
	public Listeners Listeners = new Listeners();
	public Initialize Initialize = new Initialize();
	public ClassLoader ClassLoader = getClass().getClassLoader();
	public LoadingUserInterface LoadingUserInterface = new LoadingUserInterface("User Interface", 455, 256, this.Helper.WindowHelper);
	public ConsoleUserInterface Console;
	
	private void loadClasses() {
		try {
			final File[] libs = new File[] {
					new File(getDataFolder(), "lib/GoogleGuava.jar"),
					new File(getDataFolder(), "lib/JCommon.jar"),
					new File(getDataFolder(), "lib/JFreeChart.jar"),
					new File(getDataFolder(), "lib/MigLayout.jar"),
			};
			for (final File lib : libs) {
				if (!lib.exists()) {
					JarUtils.extractFromJar(lib.getName(), lib.getAbsolutePath());
				}
			}
			for (final File lib : libs) {
				if (!lib.exists()) {
					getLogger().warning("There was a critical error loading My plugin! Could not find lib: " + lib.getName());
					Bukkit.getServer().getPluginManager().disablePlugin(this);
					return;
				}
				addClassPath(JarUtils.getJarUrl(lib));
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
	
	public void Initialize() {
		List<String> toConsole = new ArrayList<String>();
		toConsole.add("Server Log");
		toConsole.add("Chat");
		toConsole.add("Command");
		Console = new ConsoleUserInterface("Console", 455, 256, toConsole, this.Helper.WindowHelper);
		Console.setToConsoleOutput("Server Log");
		
		this.LoadingUserInterface.showWindow();
		Console.show();
//		Console.setToConsoleOutput();
		
		this.dontRead.add("enabled");
		
		this.Helper.initMain(this);
		this.addConfigToFile(this.getPluginConfig());
		
		this.Initialize.init(this);
		
		/* Format:
		 * Things.put(COMMAND(STRING), new Object[] {
		 *		COMMAND HANDLER(STRING <CLASS PATH>),
		 *		CUSTOM EVENTS(STRING <CLASS PATH>)
		 *	});
		 */
		
//		Things.put("/kits", new Object[] {
//			"io.github.NadhifRadityo.ZamsNetwork.Core.Things.Kits.Kits",
//			new String[] {
//					"io.github.NadhifRadityo.ZamsNetwork.Core.Things.Kits.Kits"
//			}
//		});
//		Things.put("/call", new Object[] {
//			"io.github.NadhifRadityo.ZamsNetwork.Core.Things.CallClassInGame"
//		});
//		Things.put(null, new Object[] {
//			"io.github.NadhifRadityo.ZamsNetwork.Core.Things.PlayerJoinQuitMessage"
//		});
//		Things.put("/resetconfig", new Object[] {
//			"io.github.NadhifRadityo.ZamsNetwork.Core.Things.ResetConfig"
//		});
//		Things.put("/destroygui", new Object[] {
//			"io.github.NadhifRadityo.ZamsNetwork.Core.Things.DestroyAllGui"
//		});
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
//		this.Initialize.setInit(Things);
		
		this.LoadingUserInterface.setLoadingTextMessage("Memulai inisialisasi command...");
		
		CommandAttribute kitsCommand = new CommandAttribute();
		kitsCommand.addAlliases("kit");
		this.Initialize.addInit(new Init("/kits", "io.github.NadhifRadityo.ZamsNetwork.Core.Things.Kits.Kits", new String[] {"io.github.NadhifRadityo.ZamsNetwork.Core.Things.Kits.Kits"}, "Kits").setCommandAttribute(kitsCommand));
		this.Initialize.addInit(new Init("/call", "io.github.NadhifRadityo.ZamsNetwork.Core.Things.CallClassInGame"));
		this.Initialize.addInit(new Init(null, "io.github.NadhifRadityo.ZamsNetwork.Core.Things.PlayerJoinQuitMessage", new String[] {"io.github.NadhifRadityo.ZamsNetwork.Core.Things.PlayerJoinQuitMessage"}));
		this.Initialize.addInit(new Init("/resetconfig", "io.github.NadhifRadityo.ZamsNetwork.Core.Things.ResetConfig"));
		this.Initialize.addInit(new Init("/destroygui", "io.github.NadhifRadityo.ZamsNetwork.Core.Things.DestroyAllGui"));
		this.Initialize.addInit(new Init("/test", "io.github.NadhifRadityo.ZamsNetwork.Core.Things.Testing"));
//		this.Initialize.addInit(new Init("/prone", "io.github.NadhifRadityo.ZamsNetwork.Core.Things.Reality.Prone", new String[] {"io.github.NadhifRadityo.ZamsNetwork.Core.Things.Reality.Prone"}));
		this.Initialize.addInit(new Init("/swimming", "io.github.NadhifRadityo.ZamsNetwork.Core.Things.Realistic.Swimming", new String[] {"io.github.NadhifRadityo.ZamsNetwork.Core.Things.Realistic.Swimming"}));
		
		//Essentials

		this.Initialize.addInit(new Init("/fly", "io.github.NadhifRadityo.ZamsNetwork.Core.Things.Essentials.Fly", "Fly"));
		this.Initialize.addInit(new Init("/heal", "io.github.NadhifRadityo.ZamsNetwork.Core.Things.Essentials.Heal", "Heal"));
		this.Initialize.addInit(new Init("/glow", "io.github.NadhifRadityo.ZamsNetwork.Core.Things.Essentials.Glowing", "Glow"));

		this.Initialize.addInit(new Init(null, "io.github.NadhifRadityo.ZamsNetwork.Core.Things.Realistic.BlockExplode", new String[] {"io.github.NadhifRadityo.ZamsNetwork.Core.Things.Realistic.BlockExplode"}, "BlockExplosion"));

		this.Initialize.initAll();
		this.LoadingUserInterface.setLoadingTextMessage("Selesai inisialisasi command...");
		instance = this;
	}
	public void onEnable() {
		this.loadClasses();
		this.Initialize();
		System.out.println("Zams Core Plugin Enabled");
		System.out.println(ServerVersion);
	}
	public void onDisable() {
		this.Helper.WindowHelper.closeAllWindows();
		System.out.println("Zams Core Plugin Disable");
	}

	public static Main getI() {
		return instance;
	}
	
	private void addClassPath(final URL url) throws IOException {
        final URLClassLoader sysloader = (URLClassLoader) java.lang.ClassLoader.getSystemClassLoader();
        final Class<URLClassLoader> sysclass = URLClassLoader.class;
        try {
            final Method method = sysclass.getDeclaredMethod("addURL", new Class[] { URL.class });
            method.setAccessible(true);
            method.invoke(sysloader, new Object[] { url });
        } catch (final Throwable t) {
            t.printStackTrace();
            throw new IOException("Error adding " + url + " to system classloader");
        }
    }
	
	public Map<String, Object> getPluginConfig() {
		Set<String> Keys = getConfig().getConfigurationSection("Config").getKeys(false);
		
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
	
	private List<String> dontRead = new ArrayList<String>();
	
	@SuppressWarnings({ "unchecked" })
	private void addConfigToFile(Map<String, Object> data) {
		for(Object things : data.keySet()) {
			Map<Object, Object> contents = (Map<Object, Object>) data.get(things.toString());
			for(Object keyThings : contents.keySet()) {
				if(!this.dontRead.contains(keyThings.toString())) {
				
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
								this.Helper.ConfigHelper.createYaml(settingsPath, settingsDefault);
						} catch (ConfigException | FileException e) {
							e.printStackTrace();
						}
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
				if(!this.dontRead.contains(keyThings.toString())) {
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
