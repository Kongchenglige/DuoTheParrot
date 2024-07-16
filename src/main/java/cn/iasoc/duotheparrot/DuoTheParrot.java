package cn.iasoc.duotheparrot;

import org.bukkit.plugin.java.JavaPlugin;

public final class DuoTheParrot extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.saveDefaultConfig();

        new CommandListener(this);

        getLogger().info("DuoTheParrot has been enabled!");
        getServer().getPluginManager().registerEvents(new MobSpawnListener(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("DuoTheParrot has been disabled!");
    }
}
