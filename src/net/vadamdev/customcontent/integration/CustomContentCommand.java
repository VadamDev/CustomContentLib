package net.vadamdev.customcontent.integration;

import net.vadamdev.customcontent.lib.ItemRegistry;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author VadamDev
 */
public class CustomContentCommand extends Command {
    public CustomContentCommand() {
        super("customcontent");
        setAliases(Collections.singletonList("customcontentlib"));
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;

            if(!player.hasPermission("customcontentlib.admin")) return false;

            if(args.length == 1) {
                StringBuilder stringBuilder = new StringBuilder("§3CustomContentLib §f» §bHere is the list of registered custom items: ");
                ItemRegistry.getCustomItems().forEach((registryName, itemStack) -> stringBuilder.append(registryName + ", "));
                player.sendMessage(stringBuilder.toString());
            }else if(args.length == 2) {
                if(args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("get")) {
                    ItemStack item = ItemRegistry.getCustomItemAsItemStack(args[1]);

                    if(item != null) player.getInventory().addItem(item);
                    else player.sendMessage("§3CustomContentLib §f» §c" + args[1] + " doesn't exist !");
                }else player.sendMessage("§3CustomContentLib §f» §cInvalid arguments !");
            }if(args.length == 3) {
                if(args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("get")) {
                    if(Bukkit.getPlayer(args[2]) == null) {
                        ItemStack item = ItemRegistry.getCustomItemAsItemStack(args[1]);
                        if(item == null) {
                            player.sendMessage("§3CustomContentLib §f» §c" + args[1] + " doesn't exist !");
                            return true;
                        }

                        try {
                            item.setAmount(Integer.parseInt(args[2]));
                        }catch(Exception ignored) {
                            player.sendMessage("§3CustomContentLib §f» §c" + args[2] + " is not a number !");
                            return true;
                        }

                        player.getInventory().addItem(item);
                    }else {
                        Player target = Bukkit.getPlayer(args[2]);

                        ItemStack item = ItemRegistry.getCustomItemAsItemStack(args[1]);

                        if(item != null) target.getInventory().addItem(item);
                        else player.sendMessage("§3CustomContentLib §f» §c" + args[1] + " doesn't exist !");
                    }
                }else player.sendMessage("§3CustomContentLib §f» §cInvalid arguments !");
            }if(args.length == 4) {
                if(args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("get")) {
                    if(Bukkit.getPlayer(args[2]) != null) {
                        ItemStack item = ItemRegistry.getCustomItemAsItemStack(args[1]);
                        if (item == null) {
                            player.sendMessage("§3CustomContentLib §f» §c" + args[1] + " doesn't exist !");
                            return true;
                        }

                        try {
                            item.setAmount(Integer.parseInt(args[3]));
                        } catch (Exception ignored) {
                            player.sendMessage("§3CustomContentLib §f» §c" + args[3] + " is not a number !");
                            return true;
                        }

                        Bukkit.getPlayer(args[2]).getInventory().addItem(item);
                    }
                }else player.sendMessage("§3CustomContentLib §f» §cInvalid arguments !");
            }else player.sendMessage("§3CustomContentLib §f» §cInvalid arguments !");
        }else {
            if(args.length == 3) {
                if(args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("get")) {
                    if(Bukkit.getPlayer(args[2]) != null) {
                        Player target = Bukkit.getPlayer(args[2]);

                        ItemStack item = ItemRegistry.getCustomItemAsItemStack(args[1]);

                        if(item != null) target.getInventory().addItem(item);
                        else sender.sendMessage("§3CustomContentLib §f» §c" + args[1] + " doesn't exist !");
                    }else sender.sendMessage("§3CustomContentLib §f» §cThis player is invalid !");
                }
            }else if(args.length == 4) {
                if(args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("get")) {
                    if(Bukkit.getPlayer(args[2]) != null) {
                        ItemStack item = ItemRegistry.getCustomItemAsItemStack(args[1]);
                        if(item == null) {
                            sender.sendMessage("§3CustomContentLib §f» §c" + args[1] + " doesn't exist !");
                            return true;
                        }

                        try {
                            item.setAmount(Integer.parseInt(args[3]));
                        }catch(Exception ignored) {
                            sender.sendMessage("§3CustomContentLib §f» §c" + args[3] + " is not a number !");
                            return true;
                        }

                        Bukkit.getPlayer(args[2]).getInventory().addItem(item);
                    }else sender.sendMessage("§3CustomContentLib §f» §cThis player is invalid !");
                }
            }

            return true;
        }

        return false;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        if(args.length == 1) {
            return Stream.of("give", "get").filter(s -> s.startsWith(args[0])).collect(Collectors.toList());
        }else if(args.length == 2 && (args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("get"))) {
            return ItemRegistry.getCustomItems().keySet().parallelStream().filter(item -> item.startsWith(args[1])).collect(Collectors.toList());
        }

        return super.tabComplete(sender, alias, args);
    }
}
