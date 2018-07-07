package io.github.NadhifRadityo.ZamsNetwork.Core.Things.Realistic;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerStatisticIncrementEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import io.github.NadhifRadityo.ZamsNetwork.Core.Exceptions.ConfigException;
import io.github.NadhifRadityo.ZamsNetwork.Core.Initialize.Commands.GetCommandExecutorClass;
import io.github.NadhifRadityo.ZamsNetwork.Core.Initialize.CustomEvents.PlayerStartSwimmingEvent;
import io.github.NadhifRadityo.ZamsNetwork.Core.Initialize.CustomEvents.PlayerStopSwimmingEvent;
import io.github.NadhifRadityo.ZamsNetwork.Core.Utilization.PlayerUtils;
import io.github.NadhifRadityo.ZamsNetwork.Main.Main;

public class Swimming implements Listener, GetCommandExecutorClass {
	private Main Plugin;
	private boolean isInit;
	private static YamlConfiguration config;

	@Override
	public boolean initMain(Main plugins) {
		this.Plugin = plugins;
		this.isInit = true;
		initConfig();
		return this.isInit;
	}
	
	private void initConfig() {
		try {
			config = this.Plugin.Helper.ConfigHelper.getYaml(this.Plugin.Config.getString("Config.Swimming.Config.path"));
		} catch (ConfigException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean isInit() {
		return this.isInit;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length > 0 && args[0] != null) {
			Player player = null;
			if(args.length > 1 && args[1] != null) {
				player = this.Plugin.getServer().getPlayer(args[1]);
			}else {
				if(sender instanceof Player) {
					player = (Player) sender;
				}else {
					sender.sendMessage("masukkan nama player!");
					return true;
				}
			}
			
			if(args[0].equalsIgnoreCase("disable")) {
				if(!player.hasMetadata("swimmingDisabled")) {
					FixedMetadataValue m = new FixedMetadataValue(Plugin, null);
		            player.setMetadata("swimmingDisabled", m);
		            sender.sendMessage("Membuat player " + player.getDisplayName() + " tidak dapat berenang lagi!");
				}else {
					player.removeMetadata("swimmingDisabled", Plugin);
		            sender.sendMessage("Membuat player " + player.getDisplayName() + " dapat berenang lagi!");
				}
			}
		}
		return true;
	}
	
	@EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event){
        Player p = event.getPlayer();
        ItemStack elytra = p.getInventory().getChestplate();
        
        if(playerCanSwim(p)){
            if(event.getTo().getY()<=event.getFrom().getY() || config.getBoolean("enableSwimmingUp")){
                //Only start swimming animation if the user did not disable it
                if(!p.hasMetadata("swimmingDisabled") && PlayerUtils.playerHasPermission(p, "relistic.swimming")){
                	p.setGliding(true);
                    boost(p);
                    
                	startSwimming(p, event);
                }

                //EXPERMIMENTAL fix to prevent elytra from loosing durability while swimming
                if(!config.getBoolean("durabilityLoss") && elytra!=null && elytra.getType()==Material.ELYTRA && !elytra.getEnchantments().containsKey(Enchantment.DURABILITY)){
                    ItemMeta meta = elytra.getItemMeta();
                    meta.addEnchant(Enchantment.DURABILITY, 100, true);
                    elytra.setItemMeta(meta);
                }

            }
        }else{
            //EXPERMIMENTAL fix to prevent elytra from loosing durability while swimming
            if(!config.getBoolean("durabilityLoss") && elytra!=null && elytra.getType()==Material.ELYTRA && elytra.getEnchantmentLevel(Enchantment.DURABILITY)==100){
                ItemMeta meta = elytra.getItemMeta();
                meta.removeEnchant(Enchantment.DURABILITY);
                elytra.setItemMeta(meta);
            }
            stopSwimming(p, event);
        }
    }

	public void startSwimming(Player p, PlayerMoveEvent event) {
		if(!p.hasMetadata("swimming")){
			
            startStaminaSystem(p);
            FixedMetadataValue m = new FixedMetadataValue(Plugin, null);
            p.setMetadata("swimming", m);
            
            PlayerStartSwimmingEvent callEvent = new PlayerStartSwimmingEvent(p, event);
            Bukkit.getServer().getPluginManager().callEvent(callEvent);
        }
	}
	
	public void stopSwimming(Player p, PlayerMoveEvent event) {
        if(p.hasMetadata("swimming")) {
            p.removeMetadata("swimming", Plugin);
            
            PlayerStopSwimmingEvent callEvent = new PlayerStopSwimmingEvent(p, event);
            Bukkit.getServer().getPluginManager().callEvent(callEvent);
        }
	}
	
    @EventHandler
    public void onEntityToggleGlideEvent(EntityToggleGlideEvent event){
        if(event.getEntity() instanceof Player){
            Player p = (Player) event.getEntity();
            if(playerCanSwim(p) && !p.hasMetadata("swimmingDisabled")){
                event.setCancelled(true);
            }
        }
    }


    //EXPERMIMENTAL fix to prevent elytra from loosing durability while swimming
    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event){
        try{
            if(event.getCurrentItem().getType()==Material.ELYTRA && event.getInventory().getHolder() instanceof Player){
                ItemStack elytra = event.getCurrentItem();
                if(!config.getBoolean("durabilityLoss") && elytra!=null && elytra.getType()==Material.ELYTRA && elytra.getEnchantmentLevel(Enchantment.DURABILITY)==100){
                    ItemMeta meta = elytra.getItemMeta();
                    meta.removeEnchant(Enchantment.DURABILITY);
                    elytra.setItemMeta(meta);
                }
            }
        }catch(NullPointerException ignored){

        }
    }

    public boolean playerCanSwim(Player p){
        if(p.getLocation().getBlock().getType()==Material.STATIONARY_WATER && p.getLocation().subtract(0, config.getInt("minWaterDepth"), 0).getBlock().getType()==Material.STATIONARY_WATER && p.getVehicle()==null && !PlayerUtils.playerIsInCreativeMode(p) && !p.isFlying()){

            //TODO make configurable
            if(!isInWaterElevator(p)){
                return true;
            }else{
                return false;
            }

        }else{
            return false;
        }
    }

    public void boost(Player p){
        if(PlayerUtils.playerHasPermission(p, "realistic.swimming.boost") && config.getBoolean("enableBoost") && p.isSprinting() && (p.getLocation().getDirection().getY()<-0.1 || !config.getBoolean("ehmCompatibility"))){
            p.setVelocity(p.getLocation().getDirection().multiply(config.getInt("sprintSpeed")));
        }
    }

    public void startStaminaSystem(Player p){
        if(!PlayerUtils.playerHasPermission(p, "relistic.swimming.stamina") || !config.getBoolean("staminaSystem")){

            //Debug
            //p.sendMessage("Starting stamina system...");

            //****************************** Changes by DrkMatr1984 START ******************************
//            BukkitRunnable stamina = new Stamina(plugin, p, this);
//            stamina.runTaskTimer(plugin, 0, Config.staminaUpdateDelay);
            //****************************** Changes by DrkMatr1984 END ******************************
        }
    }

    public static boolean isInWaterElevator(Player p){

        if(!config.getBoolean("disableSwimInWaterfall")){
            return false;
        }

        //TODO make configurable
        int width = config.getInt("maxWaterfallDiameter");

        if(p.getLocation().add(width, 0, 0).getBlock().getType() != Material.STATIONARY_WATER
                && p.getLocation().add(-width, 0, 0).getBlock().getType() != Material.STATIONARY_WATER
                && p.getLocation().add(0, 0, width).getBlock().getType() != Material.STATIONARY_WATER
                && p.getLocation().add(0, 0, -width).getBlock().getType() != Material.STATIONARY_WATER){
            return true;
        }else {
            return false;
        }
    }

    @EventHandler
    public void onStatisticIncrement(PlayerStatisticIncrementEvent event){
        //Don't increment elytra statistic if the player is swimming.
        if(event.getStatistic() == Statistic.AVIATE_ONE_CM && event.getPlayer().hasMetadata("swimming")){
            event.setCancelled(true);
        }
    }

    //Block rocket-boost while swimming
    @EventHandler
    public void blockRocketBoost(PlayerInteractEvent event){
        if(event.hasItem() && event.getItem().getType() == Material.FIREWORK && event.getPlayer().hasMetadata("swimming")){
            event.setCancelled(true);
        }
    }
}
