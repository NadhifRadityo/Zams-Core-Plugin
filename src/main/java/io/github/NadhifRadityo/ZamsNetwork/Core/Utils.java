package io.github.NadhifRadityo.ZamsNetwork.Core;

import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class Utils {
	public static String Chat(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}
	public static String getName(ItemStack stack) { // https://bukkit.org/threads/itemnames-get-the-friendly-printable-name-for-an-itemstack.284199/
		return CraftItemStack.asNMSCopy(stack).getName();
	}
	
	public static String replace(String target, Object[] from, Object[] to) {
		int i = 0;
		for(Object change : from) {
			target = target.replaceAll(change.toString(), to[i].toString());
//			target = target.replaceAll(change.toString(), "NGETES");
			i++;
		}
		return target;
	}
	public static String replace(String target, String from, String to) {
//		System.out.println(new Object[] {
//			to
//		}[0]);
		return replace(target, new Object[] {
			from
		}, new Object[] {
			to
		});
	}
	
	@SuppressWarnings({ "rawtypes" })
	public void printMap(Map<?, ?> data) {
		for ( Object key : data.keySet() ) {
			if(data.get(key) instanceof Map) {
				System.out.println("expanding key: '" + key.toString() + "'");
				this.printMap((Map) data.get(key));
			}else {
				System.out.println("key: '"+ key.toString() +"' value: '" + data.get(key) + "'");
			}
		}
	}
}
