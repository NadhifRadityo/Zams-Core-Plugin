package io.github.NadhifRadityo.ZamsNetwork.Core.Things;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import io.github.NadhifRadityo.ZamsNetwork.Core.Initialize.Commands.GetCommandExecutorClass;
import io.github.NadhifRadityo.ZamsNetwork.Main.Main;

public class DestroyAllGui implements CommandExecutor, GetCommandExecutorClass{

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
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] arg) {
		this.Plugin.Helper.GUIHelper.destroyAllGui();
		return true;
	}

}
