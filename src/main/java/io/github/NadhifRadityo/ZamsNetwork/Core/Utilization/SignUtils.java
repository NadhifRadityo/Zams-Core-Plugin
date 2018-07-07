package io.github.NadhifRadityo.ZamsNetwork.Core.Utilization;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import io.github.NadhifRadityo.ZamsNetwork.Core.Object.Reflection;
import io.github.NadhifRadityo.ZamsNetwork.Main.Main;

/**
 * Created by feeps on 7/04/17.
 */
public final class SignUtils {
    public static void openSign(Player p, Block b) {
        try {
            Object world = b.getWorld().getClass().getMethod("getHandle").invoke(b.getWorld());
            Object blockPos = Reflection.PackageType.MINECRAFT_SERVER.getClass("BlockPosition").getConstructor(int.class, int.class, int.class).newInstance(b.getX(), b.getY(), b.getZ());
            Object sign = world.getClass().getMethod("getTileEntity", Reflection.PackageType.MINECRAFT_SERVER.getClass("BlockPosition")).invoke(world, blockPos);
            Object player = p.getClass().getMethod("getHandle").invoke(p);
            player.getClass().getMethod("openSign", Reflection.PackageType.MINECRAFT_SERVER.getClass("TileEntitySign")).invoke(player, sign);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Block setSign(Player player, String metaData) {
        Block b = BlockUtils.findAnAirBlock(player.getLocation(), 10);
        b.setType(Material.SIGN_POST);
        SignUtils.openSign(player, b);
        b.setMetadata(metaData, new FixedMetadataValue(Main.getI(), true));
        return b;
    }
}
