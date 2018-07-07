package io.github.NadhifRadityo.ZamsNetwork.Core.Utilization;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.stream.IntStream;

import javax.annotation.Nonnull;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.NadhifRadityo.ZamsNetwork.Core.Utils;

public final class ItemUtils {
	
	public static boolean isSign(Material mat) {
		return mat.equals(Material.SIGN) || mat.equals(Material.SIGN_POST) || mat.equals(Material.WALL_SIGN);
	}
	public static String hideText(ItemStack item, String text) {
		String hide = hideText(text);
		ItemMeta meta = item.getItemMeta();
		String fixedName = meta.getDisplayName() != null ? meta.getDisplayName() : Utils.getName(item);
		
		return hide + "§r" + fixedName;
	}
	public static String revealText(ItemStack item) {
		if(item.getItemMeta().getDisplayName() != null) {
			String name = item.getItemMeta().getDisplayName();
			String text = name.split("§r")[0];
			
			return revealText(text);
		}
		return null;
	}
	
	/**
     * Hides text in color codes
     * Left side: change text in anvil
     *
     * @param text The text to hide
     * @return The hidden text
     */
    @Nonnull
    public static String hideText(@Nonnull String text) {
        Objects.requireNonNull(text, "text can not be null!");

        StringBuilder output = new StringBuilder();

        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        String hex = Hex.encodeHexString(bytes);

        for (char c : hex.toCharArray()) {
            output.append(ChatColor.COLOR_CHAR).append(c);
        }

        return output.toString();
    }

    /**
     * Reveals the text hidden in color codes
     *
     * @param text The hidden text
     * @throws IllegalArgumentException if an error occurred while decoding.
     * @return The revealed text
     */
    @Nonnull
    public static String revealText(@Nonnull String text) {
        Objects.requireNonNull(text, "text can not be null!");

        if (text.isEmpty()) {
            return text;
        }

        char[] chars = text.toCharArray();

        char[] hexChars = new char[chars.length / 2];

        IntStream.range(0, chars.length)
                .filter(value -> value % 2 != 0)
                .forEach(value -> hexChars[value / 2] = chars[value]);

        try {
            return new String(Hex.decodeHex(hexChars), StandardCharsets.UTF_8);
        } catch (DecoderException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Couldn't decode text", e);
        }
    }
}