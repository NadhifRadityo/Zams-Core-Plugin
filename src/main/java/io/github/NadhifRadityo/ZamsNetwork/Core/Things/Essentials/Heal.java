package io.github.NadhifRadityo.ZamsNetwork.Core.Things.Essentials;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import io.github.NadhifRadityo.ZamsNetwork.Core.Utils;
import io.github.NadhifRadityo.ZamsNetwork.Core.Exceptions.ConfigException;
import io.github.NadhifRadityo.ZamsNetwork.Core.Initialize.Commands.GetCommandExecutorClass;
import io.github.NadhifRadityo.ZamsNetwork.Core.Utilization.PlayerUtils;
import io.github.NadhifRadityo.ZamsNetwork.Main.Main;

public class Heal implements GetCommandExecutorClass {
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
			config = this.Plugin.Helper.ConfigHelper.getYaml(this.Plugin.Config.getString("Config.Heal.Config.path"));
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
		
		if(!sender.hasPermission("essentials.heal.player." + player.getName()) || (player.hasPermission("essentials.heal.denied" + player.getName()) && !player.isOp())) {
			sendChat(sender, config.getString("doNotHasPermission"));
			return true;
		}
		
		player.setHealth(PlayerUtils.getPlayerMaxHealth(player));
		sendChat(sender, Utils.replace(config.getString("successHeal"), "<Name>", player.getDisplayName()));
		return true;
	}
}
