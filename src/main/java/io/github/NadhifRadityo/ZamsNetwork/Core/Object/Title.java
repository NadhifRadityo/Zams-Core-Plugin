package io.github.NadhifRadityo.ZamsNetwork.Core.Object;

import java.util.List;

import org.bukkit.entity.Player;

public class Title {
	private String mainTitle;
	private String subTitle;
	private int fadeIn;
	private int duration;
	private int fadeOut;
	
	public Title(String mainTitle, String subTitle, int fadeIn, int duration, int fadeOut) {
		this.mainTitle = mainTitle;
		this.subTitle = subTitle;
		this.fadeIn = fadeIn;
		this.duration = duration;
		this.fadeOut = fadeOut;
	}
	
	public void send(Player player) {
		player.sendTitle(mainTitle, subTitle, fadeIn, duration, fadeOut);
	}
	
	public void send(List<Player> players) {
		for(Player player : players) {
			player.sendTitle(mainTitle, subTitle, fadeIn, duration, fadeOut);
		}
	}
}
