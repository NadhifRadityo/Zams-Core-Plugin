package io.github.NadhifRadityo.ZamsNetwork.Core.Things.Realistic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import io.github.NadhifRadityo.ZamsNetwork.Core.Exceptions.ConfigException;
import io.github.NadhifRadityo.ZamsNetwork.Core.Initialize.initPlugin;
import io.github.NadhifRadityo.ZamsNetwork.Main.Main;

public class BlockExplode implements Listener, initPlugin{
    private Main plugin;
    private boolean isInit;
    
    public List<BlockState> savedStates = new ArrayList<BlockState>();
    private final ArrayList<UUID> FallingSands = new ArrayList<UUID>();;
    private final ArrayList<Location> blockLocationMemory = new ArrayList<Location>();;
    private final HashMap<Location, ItemStack[]> containers = new HashMap<Location, ItemStack[]>();
    private final HashMap<Location, String[]> signs = new HashMap<Location, String[]>();
    private List<Block> blockList = new ArrayList<Block>();
    private YamlConfiguration config;

    @Override
    public boolean initMain(Main plugin) {
        this.plugin = plugin;
        this.isInit = true;
        
        try {
			config = this.plugin.Helper.ConfigHelper.getYaml(this.plugin.Config.getString("Config.BlockExplosion.Config.path"));
		} catch (ConfigException e) {
			e.printStackTrace();
		}
        
        return this.isInit;
    }

    @Override
    public boolean isInit() {
    	return this.isInit;
    }
    
//    public BlockExplode(Main plugin) {
//        this.plugin = plugin;
//        FallingSands = new ArrayList<>();
//        blockLocationMemory = new ArrayList<>();
//        containers = new HashMap<>();
//        signs = new HashMap<>();
//        blockList = new ArrayList<>();
//    }

    @EventHandler(ignoreCancelled = true)
    public void itemSpawn(ItemSpawnEvent e)
    {
        if (blockList.contains(e.getLocation().getBlock())) e.setCancelled(true);
    }

    @SuppressWarnings("deprecation")
	@EventHandler(ignoreCancelled = true)
    public void onBreak(BlockBreakEvent e)
    {
        
        if (!config.getBoolean("block-break-options.block-break-event")
                || isInList("block-break-options.blacklisted-worlds", e.getBlock().getWorld().getName())
                || isBlacklistedMaterial("block-break-options.effected-material-blacklist", e.getBlock())) return;

        int delay = config.getInt("block-break-options.block-regeneration-options.delay");
        boolean dropItems = config.getBoolean("block-break-options.block-drops"),
                restorationMemory = config.getBoolean("block-break-options.block-restoration-memory"),
                containerDrops = config.getBoolean("block-break-options.container-drops"),
                blockRegeneration = config.getBoolean("block-break-options.block-regeneration");
        BlockState blockState = e.getBlock().getState();

        if (restorationMemory)
            if (blockState instanceof InventoryHolder)
            {
                InventoryHolder ih = (InventoryHolder) blockState;
                containers.put(e.getBlock().getLocation(), ih.getInventory().getContents().clone());
                if (!containerDrops) ih.getInventory().clear();
            } else if (blockState instanceof Sign)
            {
                Sign sign = (Sign) blockState;
                signs.put(e.getBlock().getLocation(), sign.getLines());
            }

        if (!dropItems)
        {
            e.getBlock().getLocation().getWorld().playEffect(e.getBlock().getLocation(), Effect.STEP_SOUND, e.getBlock().getType(), e.getBlock().getTypeId());
            e.getBlock().setType(Material.AIR);
        } else e.getBlock().breakNaturally();

        if (!blockLocationMemory.contains(e.getBlock().getLocation()))
            blockLocationMemory.add(e.getBlock().getLocation());
        if (blockRegeneration)
        {
            this.savedStates.add(e.getBlock().getState());
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () ->
            {
                try
                {
                    blockState.update(true, false);
                    blockState.update();

                    if (restorationMemory)
                        if (blockState instanceof InventoryHolder)
                        {
                            InventoryHolder ih = (InventoryHolder) blockState;
                            if (!containers.isEmpty() && containers.containsKey(e.getBlock().getLocation()))
                                ih.getInventory().setContents(containers.get(e.getBlock().getLocation()));
                        } else if (blockState instanceof Sign)
                        {
                            Sign sign = (Sign) blockState;
                            if (!signs.isEmpty() && signs.containsKey(e.getBlock().getLocation()))
                            {
                                int j = 0;
                                for (String line : signs.get(e.getBlock().getLocation()))
                                {
                                    sign.setLine(j, line);
                                    j += 1;
                                }

                                sign.update();
                            }
                        }

                    blockLocationMemory.remove(e.getBlock().getLocation());
                    containers.remove(e.getBlock().getLocation());
                    signs.remove(e.getBlock().getLocation());
                    this.savedStates.remove(blockState);
                } catch (IllegalArgumentException | IndexOutOfBoundsException ignored) {}
            }, delay);
        }
    }
    
    @SuppressWarnings("deprecation")
	@EventHandler(ignoreCancelled = true)
    public void onBlockExplode(BlockExplodeEvent e)
    {
        if (isInList("block-break-options.blacklisted-worlds", e.getBlock().getWorld().getName())) return;
        if (!config.getBoolean("explosive-options.block-damage")) e.blockList().clear();
        else
        {
            int delay = config.getInt("explosive-options.block-regeneration-options.delay"),
                    speed = config.getInt("explosive-options.block-regeneration-options.speed");
            List<Block> blocks = new ArrayList<>(e.blockList());
            for (int i = -1; ++i < blocks.size(); )
            {
                Block b = blocks.get(i);
                BlockState state = b.getState();
                if (isBlacklistedMaterial("explosive-options.effected-material-blacklist", b))
                    continue;

                this.savedStates.add(state);
                boolean dropItems = config.getBoolean("explosive-options.block-drops"),
                        restorationMemory = config.getBoolean("explosive-options.block-restoration-memory"),
                        containerDrops = config.getBoolean("explosive-options.container-drops"),
                        blockPhysics = config.getBoolean("explosive-options.block-physics");

                if (!dropItems)
                {
                    e.setYield(0);
                    blockList.add(b);
                }

                if (b.getType() == Material.TNT)
                {
                    b.setType(Material.AIR);
                    state.setType(Material.AIR);
                    Entity primed = b.getWorld().spawn(b.getLocation().add(0.0D, 1.0D, 0.0D), TNTPrimed.class);
                    ((TNTPrimed) primed).setFuseTicks(80);
                    this.savedStates.remove(state);
                    continue;
                }

                if (restorationMemory)
                    if (state instanceof InventoryHolder)
                    {
                        InventoryHolder ih = (InventoryHolder) state;
                        containers.put(b.getLocation(), ih.getInventory().getContents().clone());
                        if (!containerDrops) ih.getInventory().clear();
                    } else if (b.getState() instanceof Sign)
                    {
                        Sign sign = (Sign) state;
                        signs.put(b.getLocation(), sign.getLines());
                    }

                if (blockPhysics)
                {
                    try
                    {
                        FallingBlock fallingBlock = b.getWorld().spawnFallingBlock(b.getLocation(), b.getType(), b.getData());
                        fallingBlock.setDropItem(true);
                        fallingBlock.setVelocity(new Vector(1, 1, 1));
                        FallingSands.add(fallingBlock.getUniqueId());
                    } catch (IllegalArgumentException ignored) {}
                }

                if (config.getBoolean("explosive-options.block-regeneration"))
                {
                    if (!blockLocationMemory.contains(b.getLocation())) blockLocationMemory.add(b.getLocation());
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () ->
                    {
                        try
                        {
                            state.update(true, false);
                            state.update();
                            b.getLocation().getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, b.getTypeId());

                            if (restorationMemory)
                                if (state instanceof InventoryHolder)
                                {
                                    InventoryHolder ih = (InventoryHolder) state;
                                    if (containers.containsKey(b.getLocation()))
                                    {
                                        ih.getInventory().setContents(containers.get(b.getLocation()));
                                        containers.remove(b.getLocation());
                                    }
                                } else if (state instanceof Sign)
                                {
                                    Sign sign = (Sign) state;
                                    if (signs.containsKey(b.getLocation()))
                                    {
                                        int j = 0;
                                        for (String line : signs.get(b.getLocation()))
                                        {
                                            sign.setLine(j, line);
                                            j += 1;
                                        }

                                        sign.update();
                                        signs.remove(b.getLocation());
                                    }
                                }

                            blockLocationMemory.remove(b.getLocation());
                            this.savedStates.remove(state);
                        } catch (IllegalArgumentException | IndexOutOfBoundsException ignored) {}
                    }, delay);
                    delay += speed;
                }
            }
        }

        blockList.clear();
    }

    @SuppressWarnings("deprecation")
	@EventHandler(ignoreCancelled = true)
    public void onExplodeEntity(EntityExplodeEvent e)
    {
        if (isInList("block-break-options.blacklisted-worlds", e.getLocation().getWorld().getName())) return;
        if (!config.getBoolean("explosive-options.block-damage")) e.blockList().clear();
        else
        {
            int delay = config.getInt("explosive-options.block-regeneration-options.delay"),
                    speed = config.getInt("explosive-options.block-regeneration-options.speed");
            List<Block> blocks = new ArrayList<>(e.blockList());
            for (int i = -1; ++i < blocks.size(); )
            {
                Block b = blocks.get(i);
                BlockState state = b.getState();
                if (isBlacklistedMaterial("explosive-options.effected-material-blacklist", b))
                    continue;

                this.savedStates.add(state);
                boolean dropItems = config.getBoolean("explosive-options.block-drops"),
                        restorationMemory = config.getBoolean("explosive-options.block-restoration-memory"),
                        containerDrops = config.getBoolean("explosive-options.container-drops"),
                        blockPhysics = config.getBoolean("explosive-options.block-physics");

                if (!dropItems)
                {
                    e.setYield(0);
                    blockList.add(b);
                }

                if (b.getType() == Material.TNT)
                {
                    b.setType(Material.AIR);
                    state.setType(Material.AIR);
                    Entity primed = b.getWorld().spawn(b.getLocation().add(0.0D, 1.0D, 0.0D), TNTPrimed.class);
                    ((TNTPrimed) primed).setFuseTicks(80);
                    this.savedStates.remove(state);
                    continue;
                }

                if (restorationMemory)
                    if (state instanceof InventoryHolder)
                    {
                        InventoryHolder ih = (InventoryHolder) state;
                        containers.put(b.getLocation(), ih.getInventory().getContents().clone());
                        if (!containerDrops) ih.getInventory().clear();
                    } else if (b.getState() instanceof Sign)
                    {
                        Sign sign = (Sign) state;
                        signs.put(b.getLocation(), sign.getLines());
                    }

                if (blockPhysics)
                {
                    try
                    {
                        FallingBlock fallingBlock = b.getWorld().spawnFallingBlock(b.getLocation(), b.getType(), b.getData());
                        fallingBlock.setDropItem(false);
                        fallingBlock.setVelocity(new Vector(1, 1, 1));
                        FallingSands.add(fallingBlock.getUniqueId());
                    } catch (IllegalArgumentException ignored) {}
                }

                if (config.getBoolean("explosive-options.block-regeneration"))
                {
                    if (!blockLocationMemory.contains(b.getLocation())) blockLocationMemory.add(b.getLocation());
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () ->
                    {
                        try
                        {
                            state.update(true, false);
                            state.update();
                            b.getLocation().getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, b.getTypeId());

                            if (restorationMemory)
                                if (state instanceof InventoryHolder)
                                {
                                    InventoryHolder ih = (InventoryHolder) state;
                                    if (containers.containsKey(b.getLocation()))
                                    {
                                        ih.getInventory().setContents(containers.get(b.getLocation()));
                                        containers.remove(b.getLocation());
                                    }
                                } else if (state instanceof Sign)
                                {
                                    Sign sign = (Sign) state;
                                    if (signs.containsKey(b.getLocation()))
                                    {
                                        int j = 0;
                                        for (String line : signs.get(b.getLocation()))
                                        {
                                            sign.setLine(j, line);
                                            j += 1;
                                        }

                                        sign.update();
                                        signs.remove(b.getLocation());
                                    }
                                }

                            blockLocationMemory.remove(b.getLocation());
                            this.savedStates.remove(state);
                        } catch (IllegalArgumentException | IndexOutOfBoundsException ignored) {}
                    }, delay);
                    delay += speed;
                }
            }
        }

        blockList.clear();
    }

    @EventHandler(ignoreCancelled = true)
    public void EntityChangeBlockEvent(EntityChangeBlockEvent e)
    {
        if (e.getEntity() instanceof FallingBlock)
        {
            if (FallingSands.contains(e.getEntity().getUniqueId()))
            {
                e.getEntity().getWorld().playEffect(e.getEntity().getLocation(), Effect.STEP_SOUND, e.getBlock().getType());
                e.setCancelled(true);
                FallingSands.remove(e.getEntity().getUniqueId());
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPhysics(BlockPhysicsEvent e)
    {
        if (blockLocationMemory.contains(e.getBlock().getLocation())) e.setCancelled(true);
    }

    private boolean isInList(String configurationPath, String name)
    {
        List<String> list = new ArrayList<>(config.getStringList(configurationPath));
        for (int i = -1; ++i < list.size(); ) if (list.get(i).equalsIgnoreCase(name)) return true;
        return false;
    }

    @SuppressWarnings("deprecation")
	private boolean isBlacklistedMaterial(String configurationPath, Block block)
    {
        List<String> list = new ArrayList<>(config.getStringList(configurationPath));
        for (int i = -1; ++i < list.size(); )
        {
            String line = list.get(i);
            try
            {
                if (line.contains(":"))
                {
                    String[] lineArgs = line.split(":");
                    Material material = Material.getMaterial(lineArgs[0].toUpperCase().replace(" ", "_").replace("-", "_"));
                    short data = (short) Integer.parseInt(lineArgs[1]);
                    if (block.getType() == material && block.getData() == data) return true;
                    continue;
                }

                if (Material.getMaterial(line.toUpperCase().replace(" ", "_").replace("-", "_")) == block.getType())
                    return true;
            } catch (Exception ignored) {}
        }
        return false;
    }
}
