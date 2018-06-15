package io.github.NadhifRadityo.ZamsNetwork.Core.Things;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import io.github.NadhifRadityo.ZamsNetwork.Core.GetClass;
import io.github.NadhifRadityo.ZamsNetwork.Core.GetCommandExecutorClass;
import io.github.NadhifRadityo.ZamsNetwork.Main.Main;

public class CallClassInGame implements CommandExecutor, GetCommandExecutorClass, GetClass{
	private Main Plugin;
	private boolean isInit = false;
	
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
		if(!this.Plugin.Helper.DataHelper.isEmptyArrayString(args)) {
			GetClass Clazz = this.Plugin.Helper.getClass(args[0]);
			Clazz.AccessMethod();
		}

		return true;
	}

	@Override
	public void AccessMethod() {
		System.out.println("What the F.. orm!");
	}
}
