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

public class Glowing implements GetCommandExecutorClass {
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
			config = this.Plugin.Helper.ConfigHelper.getYaml(this.Plugin.Config.getString("Config.Glow.Config.path"));
		} catch (ConfigException e) {
			e.printStackTrace();
		}
	}
	
	private static void sendChat(CommandSender sender, String msg) {
		sender.sendMessage(Utils.Chat(config.getString("prefix") + msg));
	}

	@Override
	public boolean isInit() {
		return this.isInit;
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
		
		if(!sender.hasPermission("essentials.glow.player." + player.getName()) || (player.hasPermission("essentials.glow.denied" + player.getName()) && !player.isOp())) {
			sendChat(sender, config.getString("doNotHasPermission"));
			return true;
		}
		
		if(player.isGlowing()) {
			player.setGlowing(false);
			sendChat(sender, Utils.replace(config.getString("setToNotGlowing"), "<Name>", player.getDisplayName()));
			return true;
		}
		player.setGlowing(true);
		sendChat(sender, Utils.replace(config.getString("setToGlowing"), "<Name>", player.getDisplayName()));
		return true;
	}
}
