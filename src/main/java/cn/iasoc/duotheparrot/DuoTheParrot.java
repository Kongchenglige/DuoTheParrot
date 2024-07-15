package cn.iasoc.duotheparrot;

import org.bukkit.plugin.java.JavaPlugin;

public final class DuoTheParrot extends JavaPlugin {

    private CommandListener commands;
    @Override
    public void onEnable() {
        // Plugin startup logic
        this.saveDefaultConfig();

        this.commands = new CommandListener(this);

        getLogger().info("DuoTheParrot has been enabled!");
        getServer().getPluginManager().registerEvents(new MobSpawnListener(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("DuoTheParrot has been disabled!");
    }
}
