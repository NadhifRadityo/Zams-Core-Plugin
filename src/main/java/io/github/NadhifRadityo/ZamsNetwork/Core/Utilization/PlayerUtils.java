package io.github.NadhifRadityo.ZamsNetwork.Core.Utilization;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.Vector;

public class PlayerUtils {

	public static double getPlayerMaxHealth(Player player) {
		return getPlayerAttribute(player, Attribute.GENERIC_MAX_HEALTH).getDefaultValue();
	}
	
	public static AttributeInstance getPlayerAttribute(Player player, Attribute attr) {
		return player.getAttribute(attr);
	}
	
    public static boolean playerHasPermission(Player p, String perm){
        /*if(!Config.permsReq){
            return true;
        }else */if(p.hasPermission(perm)){
            return true;
        }else{
            return false;
        }
    }
    
    public static boolean playerHasPermissions(Player p, String[] perms) {
    	if(perms == null) {
    		return true;
    	}
    	
    	for(String perm : perms) {
    		if(!playerHasPermission(p, perm)) {
    			return false;
    		}
    	}
    	return true;
    }

    public static boolean playerIsInCreativeMode(Player p){
        /*if(Config.enabledInCreative){
            return false;
        }else */if(p.getGameMode()== GameMode.CREATIVE){
            return true;
        }else{
            return false;
        }
    }

    /*public static void ncpFix(Player p){
        p.addAttachment(RSMain.getMain(), "nocheatplus.checks", true, Config.noCheatPlusExemptionTimeInTicks);
    }*/
    
    //****************************** Changes by DrkMatr1984 START ******************************
    public static boolean isElytraWeared(Player player) {
        if (player.getInventory().getChestplate() == null) return false;
        if (player.getInventory().getChestplate().getType() != Material.ELYTRA) return false;
        if (player.getInventory().getChestplate().getDurability() >= 431) return false;
        return true;
    }
    
    public static boolean hasElytraStorage(Player player) {
    	PlayerInventory inv = player.getInventory();
    	if(inv.getStorageContents()!=null){
    		for(ItemStack item : inv.getStorageContents()){
    			if(item!=null){
    				if(!item.getType().equals(Material.AIR)){
            			if(item.getType().equals(Material.ELYTRA)){
            				if(item.getDurability() <= 431)
            					return true;
            			}
            		}
    			}     		
        	}
    	}
    	return false;
    }
    //****************************** Changes by DrkMatr1984 END ******************************
	
	public static String getLanguage(Player p){
		return p.getLocale();
	}
	
	public String getCardinalDirection(Player player) throws NullPointerException  {
	    double rotation = (player.getLocation().getYaw() - 90) % 360;
	    if (rotation < 0)
	        rotation += 360.0;
	    if (0 <= rotation && rotation < 67.5)
	        return "NW"; //NorthWest
	    else if (67.5 <= rotation && rotation < 112.5)
	        return "N"; //North
	    else if (112.5 <= rotation && rotation < 157.5)
	        return "NE"; //NorthEeast
	    else if (157.5 <= rotation && rotation < 202.5)
	        return "E"; //East
	    else if (202.5 <= rotation && rotation < 247.5)
	        return "SE"; //SouthEast
	    else if (247.5 <= rotation && rotation < 292.5)
	        return "S"; //South
	    else if (292.5 <= rotation && rotation < 337.5)
	        return "SW"; //SouthWest
	    else if (337.5 <= rotation && rotation < 360.0)
	       return "W"; //West
	    else
	       return null;
	}
	
	/**
    * Iterates through all players and checks for the nearest focused player, (eye location, middle location and feet location of every targeted player for maximum precision)
    *
    * @param player the player to check the target of
    * @param maxRange the maximum range; higher -> take higher precision; lower -> take lower precision (maybe)
    * @param precision sth. between 0.97D and 0.999D; higher -> more precise checking; lower -> less precise // TODO test precision
    * @return the nearest player the given player can see and is focused or null, if no player matches the conditions
    */
    public static Player getTargetedPlayerOfPlayer(final Player player, final double maxRange, final double precision) {
        Validate.notNull(player, "player may not be null");
        Validate.isTrue(maxRange > 0D, "the maximum range has to be greater than 0");
        Validate.isTrue(precision > 0D && precision < 1D, "the precision has to be greater than 0 and smaller than 1");
        final double maxRange2 = maxRange * maxRange;

        final String playerName = player.getName();
        final Location fromLocation = player.getEyeLocation();
        final String playersWorldName = fromLocation.getWorld().getName();
        final Vector playerDirection = fromLocation.getDirection().normalize();
        final Vector playerVectorPos = fromLocation.toVector();

        Player target = null;
        double targetDistance2 = Double.MAX_VALUE;
        // check with target's feed location
        for (final Player somePlayer : Bukkit.getServer().getOnlinePlayers()) {
            if (somePlayer.getName().equals(playerName)) {
                continue;
            }
            final Location somePlayerLocation_middle = getMiddleLocationOfPlayer(somePlayer);
            final Location somePlayerLocation_eye = somePlayer.getEyeLocation();
            final Location somePlayerLocation_feed = somePlayer.getLocation();
            if (!somePlayerLocation_eye.getWorld().getName().equals(playersWorldName)) {
                continue;
            }
            final double newTargetDistance2_middle = somePlayerLocation_middle.distanceSquared(fromLocation);
            final double newTargetDistance2_eye = somePlayerLocation_eye.distanceSquared(fromLocation);
            final double newTargetDistance2_feed = somePlayerLocation_feed.distanceSquared(fromLocation);

            // check angle to target:

            // middle location
            if (newTargetDistance2_middle <= maxRange2) {
                final Vector toTarget_middle = somePlayerLocation_middle.toVector().subtract(playerVectorPos).normalize();
                // check the dotProduct instead of the angle, because it's faster:
                final double dotProduct_middle = toTarget_middle.dot(playerDirection);
                if (dotProduct_middle > precision && player.hasLineOfSight(somePlayer) && (target == null || newTargetDistance2_middle < targetDistance2)) {
                    target = somePlayer;
                    targetDistance2 = newTargetDistance2_middle;
                    continue;
                }
            }
            // eye location
            if (newTargetDistance2_eye <= maxRange2) {
                final Vector toTarget_eye = somePlayerLocation_eye.toVector().subtract(playerVectorPos).normalize();
                final double dotProduct_eye = toTarget_eye.dot(playerDirection);
                if (dotProduct_eye > precision && player.hasLineOfSight(somePlayer) && (target == null || newTargetDistance2_eye < targetDistance2)) {
                    target = somePlayer;
                    targetDistance2 = newTargetDistance2_eye;
                    continue;
                }
            }
            // feed location
            if (newTargetDistance2_feed <= maxRange2) {
                final Vector toTarget_feed = somePlayerLocation_feed.toVector().subtract(playerVectorPos).normalize();
                final double dotProduct_feed = toTarget_feed.dot(playerDirection);
                if (dotProduct_feed > precision && player.hasLineOfSight(somePlayer) && (target == null || newTargetDistance2_feed < targetDistance2)) {
                    target = somePlayer;
                    targetDistance2 = newTargetDistance2_feed;
                }
            }
        }
        return target;
    }
    public static Location getMiddleLocationOfPlayer(final Player player) {
        return player.getLocation().add(0, player.getEyeHeight() / 2, 0);
    }
}
