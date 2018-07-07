package io.github.NadhifRadityo.ZamsNetwork.Core.Utilization;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * Created by feeps on 3/04/17.
 */
public final class SkinUtils {

    public static ItemStack getCustomSkull(String url) {
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
        if(url.isEmpty())return head;

        //D�code le Base64 en byte
        byte[] decodedValue = Base64.decodeBase64(url);
        //Reprend le String d�cod� et l'applique en UTF8 et replace les mots inutiles par du vide
        String decoded = new String(decodedValue, StandardCharsets.UTF_8).replace("{\"textures\":{\"SKIN\":{\"url\":\"", "").replace("\"}}}", "");

        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", decoded).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));

        Field profileField = null;
        try {
            profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e1) {
            e1.printStackTrace();
        }
        head.setItemMeta(headMeta);
        return head;
    }

}