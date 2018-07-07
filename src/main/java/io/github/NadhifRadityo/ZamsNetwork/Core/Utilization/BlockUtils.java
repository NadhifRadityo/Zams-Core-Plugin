package io.github.NadhifRadityo.ZamsNetwork.Core.Utilization;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class BlockUtils {
	public static Block findAnAirBlock(Location loc, int radius) {
//		Location fixedLoc = loc;
		if(loc.getBlock().getType().equals(Material.AIR)) {
			return loc.getBlock();
		}
		
		loc.add(-((int) radius / 2), -((int) radius / 2), -((int) radius / 2));
		for(int x = 0; x < radius; x++) {
			for(int y = 0; y < radius; y++) {
				for(int z = 0; z < radius; z++) {
					if(loc.getBlock().getType().equals(Material.AIR)) {
						return loc.getBlock();
					}
//					loc.getBlock().setType(Material.GLASS);
					loc.add(0, 0, 1);
				}
				loc.subtract(0, 0, radius);
				loc.add(0, 1, 0);
			}
			loc.subtract(0, radius, 0);
			loc.add(1, 0, 0);
		}
		return null;
	}
}
