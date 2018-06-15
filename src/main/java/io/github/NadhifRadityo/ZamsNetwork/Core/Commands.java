package io.github.NadhifRadityo.ZamsNetwork.Core;

import java.util.Iterator;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
	
	@SuppressWarnings("rawtypes")
	public boolean CommandsHandler(CommandSender sender, Command cmd, String label, String[] args) {
		boolean result = false;
		Iterator it = this.CommandsList.entrySet().iterator();
	    while (it.hasNext()) {
	    	Map.Entry pair = (Map.Entry)it.next();
			if(pair.getKey().toString().replace("/", "").equals(cmd.getName())) {
				GetCommandExecutorClass commandClass = this.CommandsList.get(pair.getKey().toString());
				result = commandClass.onCommand(sender, cmd, label, args);
			}
		}
		return result;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
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
	@SuppressWarnings("rawtypes")
	public void RegisterEvents() {
		if(this.isInit == false) {
			System.out.println("Class Commands belum di inisialisasi!");
			System.exit(0);
		}
		Iterator it = this.CommandsList.entrySet().iterator();
	    while (it.hasNext()) {
	    	Map.Entry pair = (Map.Entry)it.next();
			this.Plugin.getCommand(pair.getKey().toString().replace("/", "")).setExecutor(this);
		}
	}
	
	public String[] fixedArgs(String[] args) {
//		ArrayList<String> fixed = new ArrayList<String>();
//		boolean quote = false;
//		for(String arg : args) {
//			if(arg.contains("\"")) {
//				String[] splitted = arg.split("");
//				for(String split : splitted) {
//					if(split == "\"") {
//						if(quote == false) {
//							quote = true;
//						}else if(quote == true) {
//							quote = false;
//						}
//					}
//				}
//			}
//		}
		return args;
	}
}
