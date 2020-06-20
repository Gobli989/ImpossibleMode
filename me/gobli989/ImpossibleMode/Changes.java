package me.gobli989.ImpossibleMode;

import com.xxmicloxx.NoteBlockAPI.event.SongDestroyingEvent;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.PositionSongPlayer;
import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.io.File;
import java.util.HashMap;

public class Changes implements Listener {

    @EventHandler
    public void onBubbleChange(EntityAirChangeEvent e) {
        if (e.getEntity() instanceof Player) {
            if (e.getAmount() < 298) e.setAmount(e.getAmount() - 2);
            if (e.getAmount() < 0) ((Player) e.getEntity()).damage(((Player) e.getEntity()).getHealth());
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (
                e.getBlock().getType() == Material.IRON_ORE ||
                        e.getBlock().getType() == Material.GOLD_ORE ||
                        e.getBlock().getType() == Material.COAL_ORE ||
                        e.getBlock().getType() == Material.DIAMOND_ORE ||
                        e.getBlock().getType() == Material.EMERALD_ORE ||
                        e.getBlock().getType() == Material.REDSTONE_ORE ||
                        e.getBlock().getType() == Material.LAPIS_ORE
        ) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    e.getBlock().setType(Material.STONE);
                }
            }.runTaskLater(Main.getPlugin(Main.class), 1);
        } else if (e.getBlock().getType() == Material.NETHER_QUARTZ_ORE) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    e.getBlock().setType(Material.NETHERRACK);
                }
            }.runTaskLater(Main.getPlugin(Main.class), 1);
        }
    }

    @EventHandler
    public void onSleep(PlayerBedLeaveEvent e) {
        if (e.getPlayer().getWorld().getTime() <= 1000)
            e.getPlayer().setFoodLevel(0);
    }

    @EventHandler
    public void onWeatherChange(EntityDamageEvent e) {
        if (e.getCause() == EntityDamageEvent.DamageCause.FALL) e.setDamage(e.getDamage() * 2);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        e.getPlayer().setMaximumNoDamageTicks(0);
    }

    @EventHandler
    public void onMobSpawn(EntitySpawnEvent e) {
        if (e.getEntity() instanceof Creeper) {
            Creeper creeper = (Creeper) e.getEntity();
            creeper.setExplosionRadius(15);
            creeper.setMaxFuseTicks(5);
        }

        if (e.getEntity() instanceof Spider) {
            ((Spider) e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1000000, 2, true));
        }

        if (e.getEntity() instanceof Monster) {
            e.getEntity().setSilent(true);
            ((Monster) e.getEntity()).getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(70);
        }
    }

    @EventHandler
    public void onEntityCombust(EntityCombustEvent e) {
        if (e.getEntity() instanceof Monster) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onShoot(ProjectileLaunchEvent e) {
        if (e.getEntity().getShooter() instanceof Skeleton || e.getEntity().getShooter() instanceof Blaze) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (e.getEntity().isOnGround() || e.getEntity().isDead()) {
                        cancel();
                        return;
                    }
                    for (Player player : e.getEntity().getWorld().getNearbyPlayers(e.getEntity().getLocation(), 20)) {
                        e.getEntity().setVelocity(player.getEyeLocation().toVector().subtract(e.getEntity().getLocation().toVector()).normalize().multiply(0.2));
                    }
                }
            }.runTaskTimer(Main.getPlugin(Main.class), 0, 1);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player) {
            if (Math.random() < 0.1) {
                e.getEntity().setVelocity(new Vector(0, 2, 0));
            }
        }
    }

    @EventHandler
    public void onSilverfish(EntitySpawnEvent e) {
        if (e.getEntity() instanceof Silverfish) {
            for (int x = -1; x < 2; x++) {
                for (int y = -1; y < 2; y++) {
                    for (int z = -1; z < 2; z++) {
                        Block block = e.getEntity().getLocation().add(x, y, z).getBlock();

                        if (block.getType() == Material.INFESTED_MOSSY_STONE_BRICKS || block.getType() == Material.INFESTED_CHISELED_STONE_BRICKS || block.getType() == Material.INFESTED_COBBLESTONE || block.getType() == Material.INFESTED_CRACKED_STONE_BRICKS || block.getType() == Material.INFESTED_STONE || block.getType() == Material.INFESTED_STONE_BRICKS) {
                            block.setType(Material.AIR);
                            block.getWorld().spawnEntity(block.getLocation().add(0, 0.5, 0), EntityType.SILVERFISH);
                        }
                    }
                }
            }
        }
    }

//    @EventHandler
//    public void onChestPlace(BlockPlaceEvent e) {
//        if(e.getBlock().getType() == Material.CHEST || e.getBlock().getType() == Material.TRAPPED_CHEST) {
//            final Location loc = e.getBlock().getLocation();
//            new BukkitRunnable() {
//                @Override
//                public void run() {
//                    ((Chest) loc.getBlock()).setLock(UUID.randomUUID().toString());
//                    loc.getBlock().getState().update();
//                }
//            }.runTaskLater(Main.getPlugin(Main.class), 1);
//        }
//    }

    @EventHandler
    public void onSquidAttack(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Squid) {
            e.getEntity().getWorld().getNearbyLivingEntities(e.getEntity().getLocation(), 5, 5, 5).forEach(livingEntity -> livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 120, 1).withIcon(false).withParticles(false)));
        }
    }

    @EventHandler
    public void onWorldSpawn(PlayerChangedWorldEvent e) {
        e.getPlayer().getWorld().setGameRule(GameRule.SPAWN_RADIUS, 100);
    }

    private HashMap<SongPlayer, Player> deaths = new HashMap<>();

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        File file = new File(Main.getPlugin(Main.class).getDataFolder() + "/Astronomia.nbs");
        Song song = NBSDecoder.parse(file);
        PositionSongPlayer psp = new PositionSongPlayer(song);
        psp.setTargetLocation(e.getEntity().getLocation());
        Bukkit.getOnlinePlayers().forEach(psp::addPlayer);
        psp.setDistance(30);
        psp.setAutoDestroy(true);
        psp.setPlaying(true);
        deaths.put(psp, e.getEntity());
    }

    @EventHandler
    public void onDestroy(SongDestroyingEvent e) {
        deaths.remove(e.getSongPlayer());
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        for(SongPlayer p : deaths.keySet()) {
            if(deaths.get(p).getName().equals(e.getPlayer().getName())) { p.destroy(); break; }

        }
    }

    @EventHandler
    public void onWitchPotion(PotionSplashEvent e) {
        if(e.getEntity().getShooter() instanceof Witch) {
            for(LivingEntity ent : e.getAffectedEntities()) {
                e.setIntensity(ent, 3.0);
            }
        }
    }

    @EventHandler
    public void onLightningStrike(LightningStrikeEvent e) {
        e.getWorld().createExplosion(e.getLightning().getLocation(), 5);
    }

    @EventHandler
    public void onFood(PlayerItemConsumeEvent e) {
        e.getPlayer().setFoodLevel(e.getPlayer().getFoodLevel() / 2 );
    }
    private int foodValue(Material m) {
        switch (m) {
            case GOLDEN_APPLE:
            case GOLDEN_CARROT:
                return 24;
            case COOKED_PORKCHOP:
            case COOKED_BEEF:
            case COOKED_MUTTON:
            case COOKED_COD:
            case COOKED_SALMON:
            case SPIDER_EYE:
                return 16;
            case COOKED_CHICKEN:
            case COOKED_RABBIT:
            case RABBIT_STEW:
            case MUSHROOM_STEW:
            case BREAD:
            case CARROT:
            case BAKED_POTATO:
            case BEETROOT:
            case BEETROOT_SOUP:
                return 12;
            case PUMPKIN_PIE:
            case APPLE:
            case BEEF:
            case PORKCHOP:
            case MUTTON:
            case CHICKEN:
            case RABBIT:
            case POISONOUS_POTATO:
            case MELON:
            case POTATO:
            case CHORUS_FRUIT:
                return 6;
            case CAKE:
            case COOKIE:
            case ROTTEN_FLESH:
            case COD:
            case SALMON:
                return 2;
            default:
                return 0;
        }
    }

}
