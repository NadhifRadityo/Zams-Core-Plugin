package io.github.NadhifRadityo.ZamsNetwork.Core.Things;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import io.github.NadhifRadityo.ZamsNetwork.Core.Initialize.initPlugin;
import io.github.NadhifRadityo.ZamsNetwork.Core.Object.Input;
import io.github.NadhifRadityo.ZamsNetwork.Core.Utilization.TimeUtils;
import io.github.NadhifRadityo.ZamsNetwork.Main.Main;

public class Chat implements Listener, initPlugin{
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
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		this.Plugin.Console.add("Chat", TimeUtils.getTime() + " [" + event.getPlayer().getName() + "] : " + event.getMessage());
		
		event.setCancelled(true);
		List<Input> inputs = this.Plugin.Helper.InputChatHelper.getAllWaitingInput();
		List<Player> dontReceive = new ArrayList<Player>();
		
		for(Input input : inputs) {
			for(Entry<Player, Boolean> entry : input.getPlayerWaiting().entrySet()) {
				if(entry.getValue() == false) {
					dontReceive.add(entry.getKey());
				}
			}
		}
		
		for(Player player : Bukkit.getOnlinePlayers()) {
			if(!dontReceive.contains(player)) {
				player.sendMessage("<"+event.getPlayer().getName()+"> " + event.getMessage());
			}
		}
	}
}
