//https://bukkit.org/threads/icon-menu.108342/

package io.github.NadhifRadityo.ZamsNetwork.Core.Object;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.NadhifRadityo.ZamsNetwork.Core.Utils;
import io.github.NadhifRadityo.ZamsNetwork.Core.Helper.GUIHelper;
import io.github.NadhifRadityo.ZamsNetwork.Main.Main;

public class GUI implements Listener, Destroyable {
    private String name;
    private int size;
    private boolean canPutInGui;
    private OptionClickEventHandler handler;
    private Main plugin;
    private GUIDecor decoration;
    private boolean hasDestroyed;
    private boolean autoDestroy;
   
    private String[] optionNames;
    private ItemStack[] optionIcons;
    private List<Integer> unstealable = new ArrayList<Integer>();
    private Map<Player, Inventory> openedGui = new HashMap<Player, Inventory>();
    private List<Integer> startEndDecor = new ArrayList<Integer>();
    private List<ItemStack> customRandomDecor = new ArrayList<ItemStack>();
   
    public GUI(String name, int size, boolean canPutInGui, boolean autoDestroy, OptionClickEventHandler handler, Main plugin) {
        this.name = name;
        this.size = size;
        this.canPutInGui = canPutInGui;
        this.handler = handler;
        this.plugin = plugin;
        this.optionNames = new String[size];
        this.optionIcons = new ItemStack[size];
        this.hasDestroyed = false;
        this.autoDestroy = autoDestroy;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    public GUI(String name, int size, OptionClickEventHandler handler, Main plugin) {
    	this(name, size, true, true, handler, plugin);
    }
    
    public GUI(String name, int size, boolean canPutInGui, OptionClickEventHandler handler, Main plugin){ 
    	this(name, size, canPutInGui, true, handler, plugin);
    }
   
    public GUI setOption(int position, ItemStack icon, String name, boolean unsteal, String... info) {
    	if(icon != null) {
	    	name = icon.getItemMeta() != null ? icon.getItemMeta().getDisplayName() != null ? icon.getItemMeta().getDisplayName() : Utils.getName(icon) : Utils.getName(icon);
	        optionNames[position] = name;
	        optionIcons[position] = setItemNameAndLore(icon, name, info);
	        if(unsteal) {
	        	unstealable.add(position);
	        }
    	}
        return this;
    }
    public GUI setOption(int position, ItemStack icon, String name, String... info) {
    	return this.setOption(position, icon, name, false, info);
    }
    
    public GUI setOption(int position, ItemStack icon, boolean unsteal) {
    	if(icon != null) {
	        optionNames[position] = icon.getItemMeta() != null ? icon.getItemMeta().getDisplayName() != null ? icon.getItemMeta().getDisplayName() : Utils.getName(icon) : Utils.getName(icon);
	        optionIcons[position] = icon;
	        if(unsteal) {
	        	unstealable.add(position);
	        }
    	}
        return this;
    }
    public GUI setOption(int position, ItemStack icon) {
    	return this.setOption(position, icon, false);
    }
    
    public GUI setContents(ItemStack[] items, boolean unsteal, int starts) {
    	int index = starts;
    	for(ItemStack item : items) {
    		if(item != null) {
	    		setOption(index, item, unsteal);
	    		index++;
    		}
    	}
    	return this;
    }
    public GUI setContents(List<ItemStack> items, boolean unsteal, int starts) {
    	setContents(items.toArray(new ItemStack[items.size()]), unsteal, starts);
    	return this;
    }
    
    public GUI setCanPutInGui(boolean flag) {
    	this.canPutInGui = flag;
    	return this;
    }
    
    public GUI setDecoration(GUIDecor decor, int start, int end) {
    	this.decoration = decor;
    	this.startEndDecor.add(start);
    	this.startEndDecor.add(end);
    	return this;
    }
    
    public GUI addCustomDecoration(ItemStack item) {
    	this.customRandomDecor.add(item);
    	return this;
    }
    
    public GUI addCustomDecoration(ItemStack[] items) {
    	for(ItemStack item : items) {
    		addCustomDecoration(item);
    	}
    	return this;
    }
    
    public GUI addCustomDecoration(List<ItemStack> items) {
    	this.customRandomDecor.addAll(items);
    	return this;
    }
    
    public GUI setCustomDecoration(List<ItemStack> items) {
    	this.customRandomDecor = items;
    	return this;
    }
    
    public GUI setCustomDecoration(ItemStack[] items) {
    	this.customRandomDecor = new ArrayList<ItemStack>();
    	for(ItemStack item : items) {
    		this.customRandomDecor.add(item);
    	}
    	return this;
    }
    
    public String getName() {
    	return name;
    }
    public int getSize() {
    	return size;
    }
    public boolean areCanPutInGui() {
    	return canPutInGui;
    }
    public GUI.OptionClickEventHandler getHandler() {
    	return handler;
    }
    public String[] getItemsName() {
    	return optionNames;
    }
    public ItemStack[] getItems() {
    	return optionIcons;
    }
    
    public GUIDecor getDecoration() {
    	return decoration;
    }
    
    public List<Integer> getStartEndDecor(){
    	return startEndDecor;
    }
    
    public List<ItemStack> getCustomDecoration(){
    	return customRandomDecor;
    }
    
    public int getNullIndex() {
    	int old = 0;
    	for(int i = 0; i < optionIcons.length; i++) {
    		if(optionIcons[old] == null) {
    			return old;
    		}
    		old++;
    	}
    	return 0;
    }
    
    private void initDecor() {
		if(this.decoration == null || (this.startEndDecor.size() < 0) || (this.startEndDecor.size() % 2 != 0)) {// check if factor of 2
			return;
		}
		
		Map<Integer, Integer> list = new HashMap<Integer, Integer>();
		boolean flag = false;
		int start = 0;
		for(int startEnd : this.startEndDecor) {
			if(flag == false) {
				start = startEnd;
				flag = true;
			}else if(flag == true){
				flag = false;
				list.put(start, startEnd);
			}
		}
		
    	switch(this.decoration) {
    		case RANDOM_SAME_TYPE:
    			ItemStack decor = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) new Random().nextInt(16));
    			ItemMeta decormeta = decor.getItemMeta();
    			decormeta.setDisplayName(" ");
    			decor.setItemMeta(decormeta);
    			
    			for(Entry<Integer, Integer> pair : list.entrySet()) {
					for(int i = pair.getKey(); i < pair.getValue(); i++) {
						if(this.optionIcons[i] == null) {
							this.setOption(i, decor, true);
						}
					}
    			}
    			break;
    		case RANDOM_DIFFERENT_TYPE:
    			for(Entry<Integer, Integer> pair : list.entrySet()) {
					for(int i = pair.getKey(); i < pair.getValue(); i++) {
						if(this.optionIcons[i] == null) {
							ItemStack decor1 = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) new Random().nextInt(16));
			    			ItemMeta decormeta1 = decor1.getItemMeta();
			    			decormeta1.setDisplayName(" ");
			    			decor1.setItemMeta(decormeta1);
			    			
							this.setOption(i, decor1, true);
						}
					}
    			}
    			break;
    		case CUSTOM:
    			for(Entry<Integer, Integer> pair : list.entrySet()) {
					for(int i = pair.getKey(); i < pair.getValue(); i++) {
						if(this.optionIcons[i] == null) {
							this.setOption(i, this.customRandomDecor.get(new Random().nextInt(this.customRandomDecor.size())), true);
						}
					}
    			}
    			break;
    		case DEFAULT:
    			return;
    	}
    }
   
	public void open(Player player) {
		initDecor();
        Inventory inventory = GUIHelper.createGUI(size, name);
        for (int i = 0; i < optionIcons.length; i++) {
            if (optionIcons[i] != null) {
                GUIHelper.addItem(inventory, optionIcons[i], i);
            }
        }
        player.openInventory(inventory);
        GUIHelper.show(player, inventory);
        this.openedGui.put(player, inventory);
        this.plugin.Helper.GUIHelper.addOpenGui(player, this);
    }
    
    public void setUnstealable(int pos, boolean flag) {
    	if(flag == true) {
    		if(!unstealable.contains(pos)) {
    			unstealable.add(pos);
    			return;
    		}
    	}else {
    		if(unstealable.contains(pos)) {
    			//https://stackoverflow.com/questions/4534146/properly-removing-an-integer-from-a-listinteger
    			unstealable.remove(new Integer(pos));
    			return;
    		}
    	}
    }
    
    public void setUnstealable(int start, int end, boolean flag) {
    	if(start < 0) {
    		start = 0;
    	}
    	if(end > optionIcons.length - 1) {
    		end = optionIcons.length - 1;
    	}
    	for(int i = start; i <= end; i++) {
    		setUnstealable(i, flag);
    	}
    }
    
    public List<Integer> getUnstealable(){
    	return unstealable;
    }
   
    public void destroy() {
        HandlerList.unregisterAll(this);
        handler = null;
        plugin = null;
        optionNames = null;
        optionIcons = null;
        hasDestroyed = true;
    }
    
    public boolean isDestroyed() {
    	return hasDestroyed;
    }
    
    private boolean isClickedTop(int rawSlot) {
    	return rawSlot < size;
    }
    
    private boolean isClickedBottom(int rawSlot) {
    	return rawSlot >= size;
    }
    
    @EventHandler
    public void onDisconnect(PlayerQuitEvent event) {
        if (this.openedGui.containsKey(event.getPlayer())) {
            this.openedGui.remove(event.getPlayer());
        }
    }
    
    @EventHandler(priority=EventPriority.MONITOR)
    void onInventoryClick(InventoryClickEvent event) {
    	Player player = (Player) event.getWhoClicked();
    	int slot = event.getRawSlot();
        if (this.plugin.Helper.GUIHelper.isInventoryOpen(player, this.openedGui.get(player))) {
        	if(event.getAction().equals(InventoryAction.NOTHING) || event.getAction().equals(InventoryAction.UNKNOWN)) {
        		event.setCancelled(true);
        		event.setResult(Result.DENY);
        		return;
        	}
        	
        	if(unstealable.contains(slot)) {
        		event.setCancelled(true);
//        		event.setResult(Result.DENY);
        	}

//        	player.sendMessage("Cursor: " + Utils.getName(event.getCursor()));
//        	player.sendMessage("Current Item: " + Utils.getName(event.getCurrentItem()));
        	if(isClickedTop(slot) && optionIcons[slot] != null && optionIcons[slot].equals(event.getCurrentItem())) {
        		OptionClickEvent e = new GUI.OptionClickEvent(player, optionIcons[slot], slot, optionNames[slot], event, this);
        		handler.onOptionClick(e);
        		
        		this.autoDestroy = e.isAutoDestroy();
        		if(e.isCancelled()) {
        			event.setCancelled(true);
            		event.setResult(Result.DENY);
        		}
        		if (e.willClose() && plugin != null) {
        			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
        				public void run() {
        					player.closeInventory();
        				}
        			}, 1);
        		}
        		if (e.willDestroy()) {
        			destroy();
        		}
        		
        	}else if(isClickedBottom(slot)) {
        		if(this.canPutInGui == false) {
        			event.setCancelled(true);
        			return;
        		}
        	}
        }
    }
    

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		GUI gui = this.plugin.Helper.GUIHelper.getGui().get(player);
		if (this.plugin.Helper.GUIHelper.isGuiOpen(player, gui) && (this.openedGui.containsKey(player) ? this.openedGui.get(player).equals(event.getInventory()) : false)) {
			this.plugin.Helper.GUIHelper.removeOpenGui(player, gui);
			this.openedGui.remove(player);
			this.checkAutoDestroy();
		}
	}
	
	@Override
	public void checkAutoDestroy() {
		if(this.openedGui.isEmpty() && this.autoDestroy == true){
			this.destroy();
		}
	}
	
	@Override
	public boolean isAutoDestroy() {
		return this.autoDestroy;
	}
	
	@Override
	public void setAutoDestroy(boolean autoDestroy) {
		this.autoDestroy = autoDestroy;
	}
    
    public interface OptionClickEventHandler {
        public void onOptionClick(OptionClickEvent event);       
    }
    
    public class OptionClickEvent {
    	private InventoryClickEvent event;
        private Player player;
        private ItemStack item;
        private String name;
        private int position;
        private GUI gui;
        private boolean close;
        private boolean destroy;
        private boolean cancelled;
        private boolean autoDestroy;
       
        public OptionClickEvent(Player player, ItemStack item, int position, String name, InventoryClickEvent event, GUI gui) {
        	this.event = event;
            this.player = player;
            this.item = item;
            this.name = name;
            this.position = position;
            this.gui = gui;
            this.close = false;
            this.destroy = false;
            this.cancelled = false;
            this.autoDestroy = true;
        }
       
        public InventoryClickEvent getEvent() {
        	return event;
        }
        
        public Player getPlayer() {
            return player;
        }
        
        public ItemStack getItem() {
        	return item;
        }

        public String getName() {
            return name;
        }
        
        public boolean isAutoDestroy() {
        	return autoDestroy;
        }
        
        public String getFixedName() {
        	if(this.getName() == null || this.getName() == "") {
        		return Utils.getName(item);
        	}
        	return getName();
        }
        
        public int getPosition() {
            return position;
        }
        
        public GUI getGUI() {
        	return gui;
        }
        
        public boolean isCancelled() {
        	return this.cancelled;
        }
       
        public boolean willClose() {
            return close;
        }
       
        public boolean willDestroy() {
            return destroy;
        }
        
        public void setCancelled(boolean cancelled) {
        	this.cancelled = cancelled;
        }
       
        public void setWillClose(boolean close) {
            this.close = close;
        }
       
        public void setWillDestroy(boolean destroy) {
            this.destroy = destroy;
        }
        
        public void setAutoDestroy(boolean autodestroy) {
        	this.autoDestroy = autodestroy;
        }
    }
   
    private ItemStack setItemNameAndLore(ItemStack item, String name, String[] lore) {
        ItemMeta im = item.getItemMeta();
            im.setDisplayName(Utils.Chat(name));
            im.setLore(Arrays.asList(lore));
        item.setItemMeta(im);
        return item;
    }
}
