package io.github.NadhifRadityo.ZamsNetwork.Core;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import io.github.NadhifRadityo.ZamsNetwork.Main.Main;

public interface GetCommandExecutorClass {
	public boolean initMain(Main plugins);
	public boolean isInit();
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args);
}
