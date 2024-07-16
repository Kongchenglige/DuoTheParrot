package cn.iasoc.duotheparrot;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TabCompletion implements TabCompleter {
    private final JavaPlugin plugin;

    public TabCompletion(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            String partialCommand = args[0].toLowerCase();
            Map<String, Map<String, Object>> commands = plugin.getDescription().getCommands();
            for (String command : commands.keySet()) {
                if (command.toLowerCase().startsWith(partialCommand)) {
                    Object permission = commands.get(command).get("permission");
                    if (permission == null || sender.hasPermission(permission.toString())) {
                        completions.add(command);
                    }
                }
            }
        }
        return completions;
    }
}