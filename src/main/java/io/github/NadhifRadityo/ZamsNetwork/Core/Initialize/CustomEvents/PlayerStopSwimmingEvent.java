package io.github.NadhifRadityo.ZamsNetwork.Core.Initialize.CustomEvents;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerStopSwimmingEvent extends Event{

    private static final HandlerList handlerList = new HandlerList();
    private PlayerMoveEvent event;
    private Player p;

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList(){
        return handlerList;
    }

    public PlayerStopSwimmingEvent(Player player, PlayerMoveEvent event){
        this.p = player;
        this.event = event;
    }

    public Player getPlayer(){
        return p;
    }
    
    public PlayerMoveEvent getEvent(){
        return event;
    }
}
