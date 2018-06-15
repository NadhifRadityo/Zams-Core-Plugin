package io.github.NadhifRadityo.ZamsNetwork.Core.Things;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import io.github.NadhifRadityo.ZamsNetwork.Core.Utils;
import io.github.NadhifRadityo.ZamsNetwork.Core.CustomEvents.CustomEventsListener;
import io.github.NadhifRadityo.ZamsNetwork.Core.Helper.Helper;
import io.github.NadhifRadityo.ZamsNetwork.Main.Main;

public class PlayerJoinQuitMessage implements CustomEventsListener, Listener{
	private Main Plugin;
	private boolean isInit = false;
	
	public boolean initMain(Main plugins) {
		this.Plugin = plugins;
		Bukkit.getPluginManager().registerEvents(this, this.Plugin);
		this.isInit = true;
		return this.isInit;
	}

	@Override
	public boolean isInit() {
		return this.isInit;
	}
	
	@EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
		Player player = event.getPlayer();
		event.setJoinMessage(Utils.Chat("&2[&r&a&l>&r&2]&r &a" + player.getName()));
		if(!player.hasPlayedBefore()) {
			this.sayHi(player);
		}
    }
	
	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent event)
    {
		Player player = event.getPlayer();
		event.setQuitMessage(Utils.Chat("&4[&r&c&l<&r&4]&r &c" + player.getName()));
		if(!player.hasPlayedBefore()) {
			this.sayHi(player);
		}
    }
	
	private void sayHi(Player player){
		Helper.sendChat(player, "Selamat datang di server Zams Network!");
	}
}
