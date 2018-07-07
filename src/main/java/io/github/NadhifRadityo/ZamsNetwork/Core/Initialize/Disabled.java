package io.github.NadhifRadityo.ZamsNetwork.Core.Initialize;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.NadhifRadityo.ZamsNetwork.Core.Initialize.Commands.GetCommandExecutorClass;
import io.github.NadhifRadityo.ZamsNetwork.Main.Main;

public class Disabled implements GetCommandExecutorClass{
	@SuppressWarnings("unused")
	private Main Plugin;
	private boolean isInit;

	@Override
	public boolean initMain(Main plugins) {
		this.Plugin = plugins;
		this.isInit = true;
		return this.isInit;
	}

	@Override
	public boolean isInit() {
		return this.isInit;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			player.sendMessage("Command ini di nonaktifkan!");
		}else {
			System.out.println("Command ini di nonaktifkan!");
		}
		return true;
	}
	
}
