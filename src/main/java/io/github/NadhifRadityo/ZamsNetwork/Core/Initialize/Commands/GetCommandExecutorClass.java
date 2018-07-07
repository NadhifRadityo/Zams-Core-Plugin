package io.github.NadhifRadityo.ZamsNetwork.Core.Initialize.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import io.github.NadhifRadityo.ZamsNetwork.Core.Initialize.initPlugin;

public interface GetCommandExecutorClass extends initPlugin {
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args);
}
