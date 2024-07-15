package cn.iasoc.duotheparrot;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Random;

public class MobSpawnListener implements Listener {
    private JavaPlugin plugin;
    private Random random = new Random();
    private List<String> messages;
    private List<String> DamagedMessages;

    public MobSpawnListener(JavaPlugin plugin) {
        this.plugin = plugin;
        this.messages = plugin.getConfig().getStringList("duoMessages");
        this.DamagedMessages = plugin.getConfig().getStringList("duoDamagedMessages");
    }

    private boolean isDuoParrot(Parrot entity) {
        return entity instanceof Parrot && entity.getVariant() == Parrot.Variant.GREEN && "§a§lDuo".equals(entity.getCustomName());
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (event.getEntityType() == EntityType.PARROT) {
            Parrot parrot = (Parrot) event.getEntity();
            if (parrot.getVariant() == Parrot.Variant.GREEN) {
                parrot.setCustomName("§a§lDuo");
                parrot.setMaxHealth(20.0);
                parrot.setHealth(20.0);
                parrot.setCustomNameVisible(true);
                plugin.getLogger().info("Duo has been spawn at" + event.getEntity().getLocation().toString() + " !");
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Parrot) {
            Parrot parrot = (Parrot) event.getEntity();
            if (isDuoParrot(parrot) && event.getDamager() instanceof Player) {
                Player player = (Player) event.getDamager();
                parrot.setHealth(parrot.getHealth() + event.getDamage());
                parrot.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 60, 0));
                parrot.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 10, 2));
                // Example fight back logic: Damage the player
                player.damage(event.getDamage() * 2); // Damages the player
                if (!DamagedMessages.isEmpty()) {
                    // Send a random message to the player
                    String randomMessage = DamagedMessages.get(random.nextInt(DamagedMessages.size()));
                    player.sendMessage(parrot.getCustomName() + ": "+ randomMessage);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof Parrot) {
            Parrot parrot = (Parrot) event.getRightClicked();
            if (isDuoParrot(parrot) && event.getHand() == org.bukkit.inventory.EquipmentSlot.HAND) {
                Player player = event.getPlayer();
                // Send a message to the player
                if (!messages.isEmpty()) {
                    // Send a random message to the player
                    String randomMessage = messages.get(random.nextInt(messages.size()));
                    player.sendMessage(parrot.getCustomName() + ": "+ randomMessage);
                }
            }
        }
    }
}