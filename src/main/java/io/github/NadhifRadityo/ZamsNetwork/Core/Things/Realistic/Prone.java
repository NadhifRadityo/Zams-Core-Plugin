package io.github.NadhifRadityo.ZamsNetwork.Core.Things.Realistic;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;

import io.github.NadhifRadityo.ZamsNetwork.Core.Initialize.Commands.GetCommandExecutorClass;
import io.github.NadhifRadityo.ZamsNetwork.Main.Main;

public class Prone implements GetCommandExecutorClass, Listener {
	private Main Plugin;
	private boolean isInit;

	@Override
	public boolean initMain(Main plugins) {
		this.Plugin = plugins;
		this.isInit = this.Plugin != null ? true : false;
		return this.isInit;
	}

	@Override
	public boolean isInit() {
		return this.isInit;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
//		if(sender instanceof Player) {
//			Player player = (Player) sender;
//			if(player.hasPermission("reality.prone")) {
//				if(player.isGliding()) {
//					if(player.hasMetadata("falling")) {
//						player.removeMetadata("falling", Plugin);
//						player.sendMessage("removed!");
//					}
//					player.setGliding(false);
//					return true;
//				}
//				player.setGliding(true);
//				FixedMetadataValue m = new FixedMetadataValue(Plugin, null);
//				player.setMetadata("falling", m);
//				player.setVelocity(new Vector(player.getLocation().getDirection().getX()* 0.1, 1*-1, player.getLocation().getDirection().getZ()*0.1));
//			}else {
//				player.sendMessage(Utils.Chat("&cKamu tidak memiliki ijin!"));
//			}
//		}else {
//			System.out.println("Hanya bisa untuk player!");
//		}
		return true;
	}
}
