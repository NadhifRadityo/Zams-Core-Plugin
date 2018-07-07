package io.github.NadhifRadityo.ZamsNetwork.Core.Object;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import io.github.NadhifRadityo.ZamsNetwork.Core.Utilization.ItemUtils;
import io.github.NadhifRadityo.ZamsNetwork.Core.Utilization.SignUtils;
import io.github.NadhifRadityo.ZamsNetwork.Main.Main;

public class SignEditor implements Listener, Destroyable {

    private Main Plugin;
    private List<Player> inEdit = new ArrayList<Player>();
    private List<Location> tempSign = new ArrayList<Location>();
    private String Subject;
    private responseEventHandler handler;
    private boolean isDestroyed;
    private boolean autoDestroy;

    public SignEditor(@Nonnull String sbj, responseEventHandler handler, Main plugin) {
    	this.Plugin = plugin;
    	this.Subject = sbj;
    	this.handler = handler;
    	this.isDestroyed = false;
    	this.autoDestroy = true;
    	
        this.Plugin.getServer().getPluginManager().registerEvents(this, this.Plugin);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
    	if(ItemUtils.isSign(event.getBlock().getType()) && tempSign.contains(event.getBlock().getLocation())) {
    		event.setCancelled(true);
    	}
    }
    
    @EventHandler
    public void onDisconnect(PlayerQuitEvent event) {
        if (inEdit.contains(event.getPlayer())) {
            inEdit.remove(event.getPlayer());
        }
        if (tempSign.contains(event.getPlayer().getLocation().getBlock().getLocation())) {
        	tempSign.remove(event.getPlayer().getLocation().getBlock().getLocation());
        	event.getPlayer().getLocation().getBlock().setType(Material.AIR);
        }
    }

	@EventHandler(ignoreCancelled = true)
    public void onSignChange(SignChangeEvent event) {
        if (inEdit.contains(event.getPlayer()) && tempSign.contains(event.getBlock().getLocation()) && event.getBlock().hasMetadata(this.Subject)) {
            inEdit.remove(event.getPlayer());
            tempSign.remove(event.getBlock().getLocation());
        	this.handler.onResponse(new responseEvent(event.getPlayer(), event.getLines(), event));
        	event.getBlock().setType(Material.AIR);
        }
        this.checkAutoDestroy();
    }
	
	public boolean isDestroyed() {
		return this.isDestroyed;
	}
	
	public boolean isAutoDestroy() {
		return this.autoDestroy;
	}

	public void setAutoDestroy(boolean autoDestroy) {
		this.autoDestroy = autoDestroy;
	}
	
	public void checkAutoDestroy() {
		if(this.inEdit.isEmpty() && this.autoDestroy) {
			this.destroy();
		}
	}
	public void destroy() {
        HandlerList.unregisterAll(this);
        this.handler = null;
        this.inEdit = null;
        this.Plugin = null;
        this.Subject = null;
        this.isDestroyed = true;
	}

    public void open(Player player) {
    	this.tempSign.add(SignUtils.setSign(player, this.Subject).getLocation());
        inEdit.add(player);
    }
    
    public void send(Player player) {
    	this.open(player);
    }

    public interface responseEventHandler{
    	public void onResponse(responseEvent event);
    }
    
    public class responseEvent {
		private String[] lines;
		private SignChangeEvent event;
		private Player player;
		
		public responseEvent(Player player, String[] lines, SignChangeEvent event) {
			this.player = player;
			this.lines = lines;
			this.event = event;
		}
		
		public Player getPlayer() {
			return player;
		}
		public String[] getLines() {
			return lines;
		}
		public SignChangeEvent getEvent() {
			return event;
		}
	}
}
