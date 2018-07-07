package io.github.NadhifRadityo.ZamsNetwork.Core.Initialize.Commands;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.NadhifRadityo.ZamsNetwork.Core.Utilization.TimeUtils;
import io.github.NadhifRadityo.ZamsNetwork.Main.Main;

public class Commands implements CommandExecutor{
	private Main Plugin;
	private boolean isInit = false;
	private Map<String, GetCommandExecutorClass> CommandsList;

	public void Initialize(Main Plugins) {
		this.Plugin = Plugins;
		this.isInit = true;
	}
	
	public void setCommands(Map<String, GetCommandExecutorClass> commands) {
		this.CommandsList = commands;
	}
	public void addCommands(Map<String, GetCommandExecutorClass> commands) {
		this.CommandsList.putAll(commands);
	}
	public void addCommands(String commands, GetCommandExecutorClass commandsClass) {
		this.CommandsList.put(commands, commandsClass);
	}
	public boolean contains(String commands, GetCommandExecutorClass commandsClass) {
		return this.CommandsList.containsKey(commands) && this.CommandsList.get(commands).equals(commandsClass);
	}
	public Map<String, GetCommandExecutorClass> getCommandsList(){
		return this.CommandsList;
	}
	
	public boolean CommandsHandler(CommandSender sender, Command cmd, String label, String[] args) {
		boolean result = false;
		for(Entry<String, GetCommandExecutorClass> pair : this.CommandsList.entrySet()) {
			if(pair.getKey() != null && pair.getValue() != null) {
				if(pair.getKey().toString().replace("/", "").equals(cmd.getName())) {
					GetCommandExecutorClass commandClass = this.CommandsList.get(pair.getKey().toString());
					result = commandClass.onCommand(sender, cmd, label, args);
				}
			}
		}
		return result;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		this.Plugin.Console.add("Command", TimeUtils.getTime() + " [" + sender.getName() + "] : /" + cmd.getName() + " " + Arrays.toString(args));
		
		if(this.isInit == false) {
			System.out.println("Class Commands belum di inisialisasi!");
			System.exit(0);
		}
		
		Player player = null;
		boolean isConsole = false;
		boolean isPlayer = false;
		
		if(sender instanceof Player) {
			player = (Player) sender;
			isConsole = true;
		} else {
			isPlayer = true;
		}
		if(isConsole == false && isPlayer == false || isConsole == true && isPlayer == true) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Who are you!"));
		}
		
		return this.CommandsHandler(sender, cmd, label, args);
	}
	
	public void RegisterEvents(Map<String, CommandAttribute> commandsAttribute) {
		if(this.isInit == false) {
			System.out.println("Class Commands belum di inisialisasi!");
			System.exit(0);
		}
		
		Field f;
		CommandMap cmap = null;
		try {
			f = this.Plugin.getServer().getClass().getDeclaredField("commandMap");
	        f.setAccessible(true);
	        cmap = (CommandMap)f.get(Bukkit.getServer());
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(Entry<String, GetCommandExecutorClass> pair : this.CommandsList.entrySet()) {
			if(pair.getKey() != null && pair.getValue() != null) {
//				this.Plugin.getCommand(pair.getKey().toString().replace("/", "")).setExecutor(this);
				
				CommandAttribute attr = commandsAttribute.get(pair.getKey());
				
				CommandsHandler cmd;
				if(attr != null) {
					cmd = new CommandsHandler(pair.getKey().toString().replace("/", ""), attr.getUsage(), attr.getDescription(), attr.getPermission(), attr.getPermissionMessage(), attr.getAlliases(), attr.getTabComplete());
				}else {
					cmd = new CommandsHandler(pair.getKey().toString().replace("/", ""));
				}
				cmap.register("", cmd);
		        cmd.setExecutor(this);
			}
		}
	}
	
	public String[] fixedArgs(String[] args) {
//		List<String> finalArgs = new ArrayList<String>();
//		
//		StringBuilder sb = new StringBuilder();
//		for (int i = 0; i < args.length; i++){
//			sb.append(args[i]).append(" ");
//		}
//		String allArgs = sb.toString().trim();
//		
//		int quoteIndex = -1;
//		int fixedIndex = 0;
//		for(int i = 0; i < args.length; i++) {
//			if(args[i].startsWith("\"")) {
//				if(quoteIndex < 0) {
//					quoteIndex = fixedIndex;
//				}else if(quoteIndex > 0){
//					quoteIndex = -1;
//					finalArgs.add(allArgs.substring(quoteIndex, fixedIndex));
//				}
//			}
//			fixedIndex += args[i].length() + 1; //+1 for space
//		}
//		System.out.println(finalArgs);
//		return finalArgs.toArray(new String[finalArgs.size()]);
		return args;
	}
}
