package io.github.NadhifRadityo.ZamsNetwork.Core.Object;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import io.github.NadhifRadityo.ZamsNetwork.Core.Utilization.RegexTester;
import io.github.NadhifRadityo.ZamsNetwork.Main.Main;

public class Input implements Listener, Destroyable {
	private Main Plugin;
	private Title title;
//	private boolean disableChat;
	private String onlyAccept;
	private String invalidMessage = "Masukkan input dengan benar!";
	private responseEventHandler handler;
//	private List<Player> waitingInput = new ArrayList<Player>();
	private Map<Player, Boolean> waitingInput = new HashMap<Player, Boolean>();
	private Map<Player, InputMethod> waitingInputMethod = new HashMap<Player, InputMethod>();
	private InputMethod inputMethod;
	private boolean autoDestroy;
	private boolean isDestroyed;
	
	public Input(String mainTitle, String subTitle, int fadeIn, int duration, int fadeOut, InputMethod method, responseEventHandler handler, Main plugin) {
		this.title = new Title(mainTitle, subTitle, fadeIn, duration, fadeOut);
		this.inputMethod = method;
		this.Plugin = plugin;
		this.handler = handler;
		this.autoDestroy = true;
		this.isDestroyed = false;
		this.Plugin.Helper.InputChatHelper.addWaitingInput(this);
		
		this.Plugin.getServer().getPluginManager().registerEvents(this, this.Plugin);
	}
	public Input(String mainTitle, String subTitle, int fadeIn, int duration, int fadeOut, responseEventHandler handler, Main plugin) {
		this(mainTitle, subTitle, fadeIn, duration, fadeOut, InputMethod.CHAT, handler, plugin);
	}
	public Input(Title title, InputMethod method, responseEventHandler handler, Main plugin) {
		this.title = title;
		this.inputMethod = method;
		this.Plugin = plugin;
		this.handler = handler;
		this.autoDestroy = true;
		this.isDestroyed = false;
		this.Plugin.Helper.InputChatHelper.addWaitingInput(this);

		this.Plugin.getServer().getPluginManager().registerEvents(this, this.Plugin);
	}
	public Input(Title title, responseEventHandler handler, Main plugin) {
		this(title, InputMethod.CHAT, handler, plugin);
	}
	public Input(responseEventHandler handler, Main plugin) {
		this(null, handler, plugin);
	}
	
	public Input setTitle(Title title) {
		this.title = title;
		return this;
	}
	public Input setTitle(String mainTitle, String subTitle, int fadeIn, int duration, int fadeOut) {
		this.setTitle(new Title(mainTitle, subTitle, fadeIn, duration, fadeOut));
		return this;
	}
//	public Input setDisableChat(boolean disableChat) {
//		this.disableChat = disableChat;
//		return this;
//	}
	public void setAutoDestroy(boolean autodestroy) {
		this.autoDestroy = autodestroy;
	}
	
	public String onlyAccept(@Nonnull String regex) {
		RegexTester tester = new RegexTester(regex);
		if(tester.isValid()) {
			this.onlyAccept = regex;
			return null;
		}
		return tester.getError();
	}
	public Input setInvalidMessage(@Nonnull String msg) {
		this.invalidMessage = msg;
		return this;
	}
	
	public Input setInputMethod(@Nonnull InputMethod method) {
		this.inputMethod = method;
		return this;
	}
	
	public Title getTitle() {
		return title;
	}
//	public boolean isDisableChat() {
//		return disableChat;
//	}
	public String getOnlyAccept() {
		return onlyAccept;
	}
	public String getInvalidMessage() {
		return invalidMessage;
	}
	public Map<Player, Boolean> getPlayerWaiting(){
		return waitingInput;
	}
	public boolean isAutoDestroy() {
		return autoDestroy;
	}
	public boolean isDestroyed() {
		return isDestroyed;
	}

	public InputMethod getInputMethod() {
		return inputMethod;
	}
	
	public void send(Player player, boolean getChat, InputMethod method) {
		if(player != null) {
			if(this.title != null) this.title.send(player);
			this.waitingInput.put(player, getChat);
			this.waitingInputMethod.put(player, method != null ? method : this.inputMethod);
			
			if((method != null ? method : this.inputMethod).equals(InputMethod.SIGN)) {
				SignEditor sign = new SignEditor(player.getUniqueId() + "[" + new Date().toString() + "]", new SignEditor.responseEventHandler() {
					
					@Override
					public void onResponse(SignEditor.responseEvent event) {
						onSignChange(event);
					}
				}, this.Plugin);
				sign.send(player);
			}
		}
	}
	public void send(Player player, boolean getChat) {
		this.send(player, false, null);
	}
	public void send(Player player, InputMethod method) {
		this.send(player, false, method);
	}
	public void send(Player player) {
		this.send(player, false, null);
	}
	
	public void onSignChange(SignEditor.responseEvent event) {
		Player player = event.getPlayer();
		if(this.waitingInput.containsKey(player) && this.waitingInputMethod.containsKey(player) && this.waitingInputMethod.get(player).equals(InputMethod.SIGN)) {
			event.getEvent().setCancelled(true);
			String[] lines = event.getLines();
			String allLines = "";
			
			for(String line : lines) {
				allLines += line;
			}
			
			String fixedmsg;
			if(this.onlyAccept != null) {
				fixedmsg = "";
				
				Pattern p = Pattern.compile(this.onlyAccept);
				Matcher m = p.matcher(allLines);
				while (m.find()) {
					fixedmsg += m.group();
				}
				
				if(fixedmsg.equals("")) {
					player.sendMessage(this.invalidMessage);
					
					//reopen sign
					boolean getChat = this.waitingInput.get(player);
					InputMethod input = this.waitingInputMethod.get(player);
					this.waitingInput.remove(player);
					this.waitingInputMethod.remove(player);
					this.send(player, getChat, input);
					
					return;
				}
			}else {
				fixedmsg = allLines;
			}
			
			handler.onResponse(new responseEvent(allLines, fixedmsg, player, this.onlyAccept));
			this.waitingInput.remove(player);
			this.waitingInputMethod.remove(player);
			this.checkAutoDestroy();
		}
	}
	
	@EventHandler
    public void onDisconnect(PlayerQuitEvent event) {
        if (this.waitingInput.containsKey(event.getPlayer())) {
            this.waitingInput.remove(event.getPlayer());
            this.waitingInputMethod.remove(event.getPlayer());
        }
    }
	
	@EventHandler(ignoreCancelled = true)
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		if(this.waitingInput.containsKey(player) && this.waitingInputMethod.containsKey(player) && this.waitingInputMethod.get(player).equals(InputMethod.CHAT)) {
			event.setCancelled(true);
			
			String fixedmsg;
			if(this.onlyAccept != null) {
				fixedmsg = "";
				
				Pattern p = Pattern.compile(this.onlyAccept);
				Matcher m = p.matcher(event.getMessage());
				while (m.find()) {
					fixedmsg += m.group();
				}
				
				if(fixedmsg.equals("")) {
					player.sendMessage(this.invalidMessage);
					return;
				}
			}else {
				fixedmsg = event.getMessage();
			}
			
			handler.onResponse(new responseEvent(event.getMessage(), fixedmsg, player, this.onlyAccept));
			this.waitingInput.remove(player);
			this.waitingInputMethod.remove(player);
			this.checkAutoDestroy();
		}
	}
	
	public void checkAutoDestroy() {
		if(this.waitingInput.isEmpty() && this.autoDestroy) {
			this.destroy();
		}
	}
	
	public void destroy() {
        HandlerList.unregisterAll(this);
		this.Plugin.Helper.InputChatHelper.removeWaitingInput(this);
		this.isDestroyed = true;
		this.handler = null;
		this.invalidMessage = null;
		this.onlyAccept = null;
		this.Plugin = null;
		this.title = null;
		this.waitingInput = null;
	}
	
	public interface responseEventHandler{
		public void onResponse(responseEvent event);
	}
	
	public class responseEvent {
		private String msg;
		private String response;
		private Player player;
		private String regex;
		
		public responseEvent(String msg, String response, Player player, String regex) {
			this.msg = msg;
			this.response = response;
			this.player = player;
			this.regex = regex;
		}
		
		public String getMessage() {
			return msg;
		}
		public String getResponse() {
			return response;
		}
		public Player getPlayer() {
			return player;
		}
		public String getRegex() {
			return regex;
		}
	}
}
