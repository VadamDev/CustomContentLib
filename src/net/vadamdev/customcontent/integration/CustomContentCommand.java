package net.vadamdev.customcontent.integration;

import net.vadamdev.customcontent.lib.ItemRegistry;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

/**
 * @author VadamDev
 * @since 04.01.2021
 */
public class CustomContentCommand extends Command {
    public CustomContentCommand() {
        super("customcontent");
        setAliases(Arrays.asList("customcontentlib"));
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;

            if(args.length == 2) {
                if(args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("get")) {
                    ItemStack item = ItemRegistry.getCustomItemAsItemStack(args[1]);

                    if(item != null) player.getInventory().addItem(item);
                    else player.sendMessage("§c" + args[1] + " doesn't exist !");
                }
            }if(args.length == 3) {
                if(args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("get")) {
                    if(Bukkit.getPlayer(args[2]) == null) {
                        ItemStack item = ItemRegistry.getCustomItemAsItemStack(args[1]);
                        if(item == null) {
                            player.sendMessage("§c" + args[1] + " doesn't exist !");
                            return true;
                        }

                        try {
                            item.setAmount(Integer.parseInt(args[2]));
                        }catch(Exception ignored) {
                            //TODO: Argument 2 is not an Integer
                            return true;
                        }

                        player.getInventory().addItem(item);
                    }else {
                        Player target = Bukkit.getPlayer(args[2]);

                        ItemStack item = ItemRegistry.getCustomItemAsItemStack(args[1]);

                        if(item != null) target.getInventory().addItem(item);
                        else player.sendMessage("§c" + args[1] + " doesn't exist !");
                    }
                }
            }if(args.length == 4) {
                if(args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("get")) {
                    if(Bukkit.getPlayer(args[2]) != null) {
                        ItemStack item = ItemRegistry.getCustomItemAsItemStack(args[1]);
                        if (item == null) {
                            player.sendMessage("§c" + args[1] + " doesn't exist !");
                            return true;
                        }

                        try {
                            item.setAmount(Integer.parseInt(args[3]));
                        } catch (Exception ignored) {
                            //TODO: Argument 3 is not an Integer
                            return true;
                        }

                        Bukkit.getPlayer(args[2]).getInventory().addItem(item);
                    }
                }else {
                    //TODO: Help message
                }
            }
        }else {
            if(args.length == 3) {
                if(args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("get")) {
                    if(Bukkit.getPlayer(args[2]) != null) {
                        Player target = Bukkit.getPlayer(args[2]);

                        ItemStack item = ItemRegistry.getCustomItemAsItemStack(args[1]);

                        if(item != null) target.getInventory().addItem(item);
                        else sender.sendMessage("§c" + args[1] + " doesn't exist !");
                    }else {
                        //TODO: Player is null
                    }
                }
            }else if(args.length == 4) {
                if(args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("get")) {
                    if(Bukkit.getPlayer(args[2]) != null) {
                        ItemStack item = ItemRegistry.getCustomItemAsItemStack(args[1]);
                        if(item == null) {
                            sender.sendMessage("§c" + args[1] + " doesn't exist !");
                            return true;
                        }

                        try {
                            item.setAmount(Integer.parseInt(args[3]));
                        }catch(Exception ignored) {
                            //TODO: Argument 3 is not an Integer
                            return true;
                        }

                        Bukkit.getPlayer(args[2]).getInventory().addItem(item);
                    }else {
                        //TODO: Player is null
                    }
                }
            }
        }

        return true;
    }
}
