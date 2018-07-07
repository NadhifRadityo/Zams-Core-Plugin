package io.github.NadhifRadityo.ZamsNetwork.Core.Things.Essentials;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import io.github.NadhifRadityo.ZamsNetwork.Core.Utils;
import io.github.NadhifRadityo.ZamsNetwork.Core.Exceptions.ConfigException;
import io.github.NadhifRadityo.ZamsNetwork.Core.Initialize.Commands.GetCommandExecutorClass;
import io.github.NadhifRadityo.ZamsNetwork.Main.Main;

public class Fly implements GetCommandExecutorClass{
	private Main Plugin;
	private boolean isInit;
	private static YamlConfiguration config;

	@Override
	public boolean initMain(Main plugins) {
		this.Plugin = plugins;
		this.isInit = true;
		initConfig();
		return this.isInit;
	}
	
	private void initConfig() {
		try {
			config = this.Plugin.Helper.ConfigHelper.getYaml(this.Plugin.Config.getString("Config.Fly.Config.path"));
		} catch (ConfigException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean isInit() {
		return this.isInit;
	}
	
	private static void sendChat(CommandSender sender, String msg) {
		sender.sendMessage(Utils.Chat(config.getString("prefix") + msg));
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = null;
		if(sender instanceof Player) {
			player = (Player) sender;
		}
		
		if(args.length > 0 && args[0] != null) {
			player = Bukkit.getPlayer(args[0]);
		}

		if(player == null) {
			sendChat(sender, config.getString("playerNotFound"));
			return true;
		}

		if(!player.hasPermission("essentials.fly.player." + player.getName()) || (player.hasPermission("essentials.fly.denied" + player.getName()) && !player.isOp())) {
			sendChat(sender, config.getString("doNotHasPermission"));
			return true;
		}
		
		if(player.getAllowFlight()) {
			player.setAllowFlight(false);
			sendChat(sender, Utils.replace(config.getString("setToNotFlying"), "<Name>", player.getDisplayName()));
			return true;
		}
		player.setAllowFlight(true);
		sendChat(sender, Utils.replace(config.getString("setToFlying"), "<Name>", player.getDisplayName()));
		return true;
	}
}
