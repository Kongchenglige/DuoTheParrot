package cn.iasoc.duotheparrot;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

public class CommandListener implements CommandExecutor {
    private final JavaPlugin plugin;
    private final Random random = new Random();
    private TabCompletion tabCompletion;

    public CommandListener(JavaPlugin plugin) {
        this.plugin = plugin;
        this.tabCompletion = new TabCompletion(plugin);
        Objects.requireNonNull(plugin.getCommand("dtp")).setExecutor(this);
        Objects.requireNonNull(plugin.getCommand("duotheparrot")).setExecutor(this);
        Objects.requireNonNull(plugin.getCommand("dtp")).setTabCompleter(tabCompletion);
        Objects.requireNonNull(plugin.getCommand("duotheparrot")).setTabCompleter(tabCompletion);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("dtp") || label.equalsIgnoreCase("duotheparrot")) {
            if (args.length == 0) {
                sender.sendMessage(org.bukkit.ChatColor.GREEN + "[DTP] DuoTheParrot v1.0.0\n" +
                        "----指令----\n" +
                        "/" + label + " reload - 重載配置文件\n" +
                        "/" + label + " random - 生成隨機數\n" +
                        "----Information----\n" +
                        "Author: Lige Kongcheng"
                );
                return true;
            }
            switch (args[0]) {
                case "random":

                    if (args.length < 2 || args.length > 4) {
                        sender.sendMessage(org.bukkit.ChatColor.RED + "[DTP]使用方法: /" + label + " random <最大數> <最小數> <數字數量>");
                        return true;
                    }
                    try {
                        int start; // The minimum number
                        int count; // The number of random numbers
                        int end;   // The maximum number

                        switch (args.length) {
                            case 2:
                                start = 0;
                                count = 1;
                                end = Integer.parseInt(args[1]);
                                break;
                            case 3:
                                count = 1;
                                end = Integer.parseInt(args[1]);
                                start = Integer.parseInt(args[2]);
                                break;

                            case 4:
                                end = Integer.parseInt(args[1]);
                                start = Integer.parseInt(args[2]);
                                count = Integer.parseInt(args[3]);
                                if(count > 50) {
                                    sender.sendMessage(org.bukkit.ChatColor.RED + "[DTP]過大的數字數量, 已自動縮減為50.");
                                    count = 50;
                                }
                                break;
                            default:
                                start = 0;
                                count = 1;
                                end = Integer.parseInt(args[1]);
                                break;
                        }

                        if (start > end) {
                            sender.sendMessage(org.bukkit.ChatColor.RED + "[DTP]最小數必須小於最大數.");
                            return true;
                        }

                        int[] randomNumbers = new int[count];

                        for (int i = 0; i < randomNumbers.length; i++) {
                            randomNumbers[i] = random.nextInt(end - start + 1) + start;
                        }

                        String formattedString = Arrays.stream(randomNumbers)
                                .mapToObj(String::valueOf)
                                .collect(Collectors.joining(", "));

                        String message = ChatColor.GREEN + "[DTP] " + ChatColor.GOLD + sender.getName() + " 擲出了點數 " + ChatColor.AQUA + formattedString + ChatColor.GOLD + " !";
                        plugin.getServer().broadcastMessage(message);
                    } catch (NumberFormatException e) {
                        sender.sendMessage(org.bukkit.ChatColor.RED + "[DTP]請確保您輸入的參數皆爲數字.");
                    }

                    break;
                case "reload":

                    if (sender.hasPermission("duotheparrot.reload")) {
                        plugin.reloadConfig();
                        sender.sendMessage(org.bukkit.ChatColor.GREEN + "[DTP]配置已重載.");
                    } else {
                        sender.sendMessage(org.bukkit.ChatColor.RED + "[DTP]您沒有權限來這麽做.");
                    }

                    break;
                default:

                    sender.sendMessage(org.bukkit.ChatColor.RED + "[DTP]未知的指令.");

                    break;
            }
            return true;
        }
        return false;
    }
}